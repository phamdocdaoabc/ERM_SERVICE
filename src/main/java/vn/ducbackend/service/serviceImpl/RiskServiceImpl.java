package vn.ducbackend.service.serviceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.ducbackend.domain.dto.*;
import vn.ducbackend.domain.entity.*;
import vn.ducbackend.exception.customException.DuplicateException;
import vn.ducbackend.exception.customException.NotFoundException;
import vn.ducbackend.mapper.RiskCauseTrackingMapper;
import vn.ducbackend.mapper.RiskMapper;
import vn.ducbackend.repository.*;
import vn.ducbackend.service.RiskService;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RiskServiceImpl implements RiskService {
    private final RiskRepository riskRepository;
    private final RiskMapper riskMapper;
    private final FileRiskRepository fileRiskRepository;
    private final RiskAttributeLineRepository riskAttributeLineRepository;
    private final RiskCauseLineRepository riskCauseLineRepository;
    private final RiskAttributeLineValueRepository riskAttributeLineValueRepository;
    private final RiskCauseTrackingRepository riskCauseTrackingRepository;
    private final RiskCauseTrackingMapper riskCauseTrackingMapper;

    @Value("${employee.auth.token}")
    private String token;

    @Value("${employee.auth.domain}")
    private String domain;

    @Override
    @Transactional
    public Long create(RiskDTO riskDTO) {
        // Check code trùng
        if (riskRepository.existsRisksByCodeOrName(riskDTO.getName(), riskDTO.getCode())) {
            throw new DuplicateException("Code [" + riskDTO.getCode() + "] or Name [" + riskDTO.getName() + "] already exists");
        }
        // Step 1
        Risk risk = riskMapper.toRisk(riskDTO);
        risk.setSystemId(riskDTO.getSystem().getId());
        risk.setRiskTypeId(riskDTO.getRiskType().getId());
        risk.setSystemId(riskDTO.getRiskCategory().getId());
        risk.setSystemId(riskDTO.getRecorder().getId());
        risk.setSystemId(riskDTO.getTag().getId());
        riskRepository.save(risk);

        // Sau khi frontend gọi api uploadfile trả ra url
        if(riskDTO.getAttachments() != null && !riskDTO.getAttachments().isEmpty()){
            List<FileRisk> fileList = new ArrayList<>();
            for(FileResponse file : riskDTO.getAttachments()){
                FileRisk fileRisk = new FileRisk();
                fileRisk.setRiskId(risk.getId());
                fileRisk.setFile(file.getUrl());
                fileList.add(fileRisk);
            }
            fileRiskRepository.saveAll(fileList);
        }

        if (riskDTO.getRiskAttributeLines() != null && !riskDTO.getRiskAttributeLines().isEmpty()) {
            List<RiskAttributeLine> riskAttributeLines = new ArrayList<>();
            for(RiskAttributeLineDTO riskAttributeLineDTO : riskDTO.getRiskAttributeLines()){
                RiskAttributeLine riskAttributeLine = new RiskAttributeLine();
                riskAttributeLine.setRiskId(risk.getId());
                riskAttributeLine.setAttributesId(riskAttributeLineDTO.getAttributeRiskResponse().getId());
                riskAttributeLines.add(riskAttributeLine);
            }
            List<RiskAttributeLine> save1 = riskAttributeLineRepository.saveAll(riskAttributeLines);

            List<RiskAttributeLineValue> riskAttributeLineValues = new ArrayList<>();
            for (int i = 0; i < save1.size(); i++) {
                RiskAttributeLineDTO riskAttributeLineDTO = riskDTO.getRiskAttributeLines().get(i);
                if(riskAttributeLineDTO.getRiskAttributeLineValues() != null && !riskAttributeLineDTO.getRiskAttributeLineValues().isEmpty()){
                    for(RiskAttributeLineValueDTO riskAttributeLineValueDTO : riskAttributeLineDTO.getRiskAttributeLineValues()){
                        RiskAttributeLineValue riskAttributeLineValue = new RiskAttributeLineValue();
                        riskAttributeLineValue.setRiskAttributeLineId(riskAttributeLineDTO.getId());
                        riskAttributeLineValue.setAttributeValueRiskId(riskAttributeLineValueDTO.getId());
                        // if display = textbox => value
                        if(riskAttributeLineValueDTO.getValue() != null && !riskAttributeLineValueDTO.getValue().isBlank()){
                            riskAttributeLineValue.setTextValues(riskAttributeLineValueDTO.getValue());
                        }
                        riskAttributeLineValues.add(riskAttributeLineValue);
                    }
                }
            }
            riskAttributeLineValueRepository.saveAll(riskAttributeLineValues);
        }
        // Step 2:
        if(riskDTO.getRiskCauseLines() != null && !riskDTO.getRiskCauseLines().isEmpty()){
            for(RiskCauseLineDTO riskCauseLineDTO : riskDTO.getRiskCauseLines()){
                if(riskCauseLineDTO.getCauseTracking().getId() == null){
                    RiskCauseTracking riskCauseTracking = riskCauseTrackingMapper.toRiskCauseTracking(riskCauseLineDTO.getCauseTracking());
                    riskCauseTracking.setCauseId(riskCauseLineDTO.getCauseTracking().getCause().getId());
                    riskCauseTracking.setCauseCategoryId(riskCauseLineDTO.getCauseTracking().getCauseCategory().getId());
                    if(riskCauseLineDTO.getCauseTracking().getRepeatCount() == null){
                        riskCauseTracking.setRepeatCount(1L);
                    }
                    riskCauseTrackingRepository.save(riskCauseTracking);
                    RiskCauseLine riskCauseLine = new RiskCauseLine();
                    riskCauseLine.setRiskId(risk.getId());
                    riskCauseLine.setCauseTrackingId(riskCauseTracking.getId());
                    riskCauseLineRepository.save(riskCauseLine);
                }else{
                    RiskCauseTracking riskCauseTracking = riskCauseTrackingRepository.findById(riskCauseLineDTO.getCauseTracking().getId())
                            .orElseThrow(() -> new NotFoundException("RiskCauseTracking not found"));

                    riskCauseTracking.setRepeatCount(riskCauseTracking.getRepeatCount() + 1);
                    riskCauseTrackingRepository.save(riskCauseTracking);
                }
            }
        }
        return risk.getId();
    }
}
