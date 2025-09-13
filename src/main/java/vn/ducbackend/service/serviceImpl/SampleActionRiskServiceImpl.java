package vn.ducbackend.service.serviceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.ducbackend.client.DepartmentManagerClient;
import vn.ducbackend.domain.dto.*;
import vn.ducbackend.domain.dto.SampleActionRisk.*;
import vn.ducbackend.domain.entity.*;
import vn.ducbackend.exception.customException.DuplicateException;
import vn.ducbackend.exception.customException.NotFoundException;
import vn.ducbackend.mapper.*;
import vn.ducbackend.repository.*;
import vn.ducbackend.service.SampleActionRiskService;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SampleActionRiskServiceImpl implements SampleActionRiskService {
    private final SampleActionRiskRepository sampleActionRiskRepository;
    private final SampleActionRiskMapper sampleActionRiskMapper;
    private final SampleActionRiskMapMapper sampleActionRiskMapMapper;
    private final RiskTypeMapper riskTypeMapper;
    private final RiskTypeRepository riskTypeRepository;
    private final CauseCategoryMapper causeCategoryMapper;
    private final CauseCategoryRepository causeCategoryRepository;
    private final SampleActionRiskMapRepository sampleActionRiskMapRepository;
    private final PrecautionRepository precautionRepository;
    private final PrecautionMapper precautionMapper;
    private final DepartmentManagerClient departmentManagerClient;

    @Value("${department.auth.token}")
    private String token;

    @Value("${department.auth.tenantId}")
    private String tenantId;

    @Override
    @Transactional
    public Long create(SampleActionRiskRequest request) {
        // Check code trùng
        if (sampleActionRiskRepository.existsSampleActionRiskByCodeOrName(request.getName(), request.getCode())) {
            throw new DuplicateException("Code [" + request.getCode() + "] or Name [" + request.getName() + "] already exists");
        }
        SampleActionRisk sampleActionRisk = sampleActionRiskMapper.toSampleActionRisk(request);
        sampleActionRiskRepository.save(sampleActionRisk);
        if(request.getSampleActionMaps() != null && !request.getSampleActionMaps().isEmpty()){
            List<SampleActionRiskMap> sampleActionRiskMapList = new ArrayList<>();
            for(SampleActionRiskMapRequest link : request.getSampleActionMaps()){
                SampleActionRiskMap sampleActionRiskMap = sampleActionRiskMapMapper.toSampleActionRisk(link);
                sampleActionRiskMap.setSampleActionId(sampleActionRisk.getId());
                sampleActionRiskMapList.add(sampleActionRiskMap);
            }
            sampleActionRiskMapRepository.saveAll(sampleActionRiskMapList);
        }
        return sampleActionRisk.getId();
    }

    @Override
    public Page<SampleActionRiskResponse> getListSampleAction(Pageable pageable, Set<Long> ids) {
        Page<SampleActionRisk> sampleActionRisks;
        if (ids != null && !ids.isEmpty()) {
            sampleActionRisks = sampleActionRiskRepository.findByIdIn(ids, pageable);
        } else {
            sampleActionRisks = sampleActionRiskRepository.findAll(pageable);
        }
        return sampleActionRisks.map(sampleAction -> {
            // map sang DTO
            SampleActionRiskResponse dto = sampleActionRiskMapper.toSampleActionRiskResponse(sampleAction);
            RiskType riskType = riskTypeRepository.findById(sampleAction.getRiskTypeId())
                    .orElseThrow(() -> new NotFoundException("RiskTypes not found with id :"+sampleAction.getRiskTypeId()));
            dto.setRiskType(riskTypeMapper.toLinkDTO(riskType));
            CauseCategory causeCategory = causeCategoryRepository.findById(sampleAction.getCauseCategoryId())
                    .orElseThrow(() -> new NotFoundException("CauseCategory not found with id :"+sampleAction.getCauseCategoryId()));
            dto.setCauseCategory(causeCategoryMapper.toLinkDTO(causeCategory));
            return dto;
        });
    }

    @Override
    public SampleActionRiskDetailResponse getSampleAction(Long id) {
        SampleActionRisk sampleActionRisk = sampleActionRiskRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("SampleAction not found with id :" + id));

        // Map sang DTO gốc
        SampleActionRiskDetailResponse dto = sampleActionRiskMapper.toDetailDTO(sampleActionRisk);

        // RiskType
        RiskType riskType = riskTypeRepository.findById(sampleActionRisk.getRiskTypeId())
                .orElseThrow(() -> new NotFoundException("RiskTypes not found with id :" + sampleActionRisk.getRiskTypeId()));
        dto.setRiskType(riskTypeMapper.toLinkDTO(riskType));

        // CauseCategory
        CauseCategory causeCategory = causeCategoryRepository.findById(sampleActionRisk.getCauseCategoryId())
                .orElseThrow(() -> new NotFoundException("CauseCategory not found with id :" + sampleActionRisk.getCauseCategoryId()));
        dto.setCauseCategory(causeCategoryMapper.toLinkDTO(causeCategory));

        // Lấy danh sách map
        List<SampleActionRiskMap> sampleActionRiskMapList = sampleActionRiskMapRepository.findBySampleActionId(id);

        // === Tối ưu N+1 ===
        // 1. Lấy toàn bộ precautionId
        List<Long> precautionIds = sampleActionRiskMapList.stream()
                .map(SampleActionRiskMap::getPrecautionId)
                .filter(Objects::nonNull)
                .toList();

        Map<Long, Precaution> precautionMap = precautionRepository.findAllById(precautionIds)
                .stream().collect(Collectors.toMap(Precaution::getId, p -> p));

        // 2. Lấy toàn bộ departmentId
        List<Long> departmentIds = sampleActionRiskMapList.stream()
                .map(SampleActionRiskMap::getDepartmentId)
                .filter(Objects::nonNull)
                .toList();
        Map<Long, BasicInfoDTO> departmentMap = departmentManagerClient.getAllDepartment(
                departmentIds, token, tenantId)
                .getData().getContent()
                .stream()
                .collect(Collectors.toMap(BasicInfoDTO::getId, d -> d));

        // Map sang response
        List<SampleActionRiskMapResponse> sampleActionMaps = new ArrayList<>();
        for (SampleActionRiskMap sarm : sampleActionRiskMapList) {
            SampleActionRiskMapResponse response = sampleActionRiskMapMapper.toActionRiskMapResponse(sarm);

            // Precaution
            if (sarm.getPrecautionId() != null) {
                Precaution precaution = precautionMap.get(sarm.getPrecautionId());
                if (precaution == null) {
                    throw new NotFoundException("Precaution not found with id :" + sarm.getPrecautionId());
                }
                response.setPrecautionId(precautionMapper.toLinkDTO(precaution));
            }

            // Department
            if (sarm.getDepartmentId() != null) {
                BasicInfoDTO department = departmentMap.get(sarm.getDepartmentId());
                if (department == null) {
                    throw new NotFoundException("Department not found with id :" + sarm.getDepartmentId());
                }
                response.setDepartmentId(department);
            }

            sampleActionMaps.add(response);
        }

        dto.setSampleActionMaps(sampleActionMaps);
        return dto;
    }


    @Override
    @Transactional
    public void delete(Long id) {
        // Kiểm tra tồn tại
        if (!sampleActionRiskRepository.existsById(id)) {
            throw new NotFoundException("sampleActionRisk not found with id: " + id);
        }
        // Xóa mapping trước
        sampleActionRiskMapRepository.deleteBySampleActionId(id);

        // Xóa CauseCategory
        sampleActionRiskRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Long update(SampleActionRiskRequest request) {
        // 1. Kiểm tra tồn tại
        SampleActionRisk existing = sampleActionRiskRepository.findById(request.getId())
                .orElseThrow(() -> new NotFoundException("SampleActionRisk not found with id: " + request.getId()));

        // 2. Check code trùng (nếu code có thể update)
        if (sampleActionRiskRepository.existsByCodeAndIdNot(request.getCode(), request.getId())) {
            throw new DuplicateException("Code [" + request.getCode() + "] already exists");
        }

        sampleActionRiskMapper.toSampleActionRisk(request);
        sampleActionRiskRepository.save(existing);

        // 4. Update mapping SampleActionRiskMap
        // Cách đơn giản nhất: Xóa hết rồi insert lại (tránh rắc rối update từng record)
        sampleActionRiskMapRepository.deleteBySampleActionId(request.getId());

        if (request.getSampleActionMaps() != null && !request.getSampleActionMaps().isEmpty()) {
            List<SampleActionRiskMap> newLinks = request.getSampleActionMaps().stream()
                    .map(linkReq -> {
                        SampleActionRiskMap map = sampleActionRiskMapMapper.toSampleActionRisk(linkReq);
                        map.setSampleActionId(existing.getId());
                        return map;
                    })
                    .toList();
            sampleActionRiskMapRepository.saveAll(newLinks);
        }

        return existing.getId();
    }
}
