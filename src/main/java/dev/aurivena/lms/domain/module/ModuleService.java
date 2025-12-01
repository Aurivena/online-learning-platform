package dev.aurivena.lms.domain.module;

import dev.aurivena.lms.domain.module.dto.CreateModuleRequest;
import dev.aurivena.lms.domain.module.dto.ModuleResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class ModuleService {
    private final ModuleRepository moduleRepository;
    private final ModuleMapper moduleMapper;

    @Transactional
    public ModuleResponse create(CreateModuleRequest request) {
        return moduleMapper.toResponse(moduleRepository.save(moduleMapper.toEntity(request)));
    }


    @Transactional ModuleResponse findById(Long id) {
        Module module = moduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Module not found"));
        return moduleMapper.toResponse(module);
    }
}
