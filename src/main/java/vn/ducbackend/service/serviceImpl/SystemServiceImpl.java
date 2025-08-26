package vn.ducbackend.service.serviceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.ducbackend.domain.dto.SystemRequest;
import vn.ducbackend.domain.dto.SystemResponse;
import vn.ducbackend.domain.entity.Systems;
import vn.ducbackend.mapper.SystemMapper;
import vn.ducbackend.repository.SystemRepository;
import vn.ducbackend.service.SystemService;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SystemServiceImpl implements SystemService {
    private final SystemRepository systemRepository;
    private final SystemMapper systemMapper;

    @Override
    public SystemResponse createSystem(SystemRequest systemRequest) {
        Systems systems = systemMapper.toSystem(systemRequest);
        systems = systemRepository.save(systems);
        return systemMapper.toSystemResponse(systems);
    }

    @Override
    public SystemResponse getSystem(Long id) {
        Systems systems =  systemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("System not found with id: " + id));
        return systemMapper.toSystemResponse(systems);
    }

    @Override
    public List<SystemResponse> getAllSystem() {
        var systems = systemRepository.findAll();
        return systems.stream().map(systemMapper::toSystemResponse).toList();
    }

    @Override
    public void deleteSystem(Long id) {
        systemRepository.deleteById(id);
    }

    @Override
    public SystemResponse updateSystem(SystemResponse systemResponse) {
        Systems systems = systemRepository.findById(systemResponse.getId())
                .orElseThrow(() -> new RuntimeException("System not found with id: " + systemResponse.getId()));
        systemMapper.updateSystemFromDto(systemResponse, systems);
        systems = systemRepository.save(systems);
        return systemMapper.toSystemResponse(systems);
    }
}