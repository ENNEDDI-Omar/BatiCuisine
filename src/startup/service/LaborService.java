package startup.service;

import startup.domain.entities.Labor;
import startup.exceptions.LaborNotFoundException;
import startup.repository.implementations.LaborRepository;
import startup.utils.ValidationUtils;

import java.util.List;
import java.util.Optional;

public class LaborService {
    private final LaborRepository laborRepository;

    public LaborService(LaborRepository laborRepository) {
        this.laborRepository = laborRepository;
    }

    // Save a new labor
    public Labor save(Labor labor) {
        if (labor == null) {
            throw new IllegalArgumentException("Cannot save null labor.");
        }
        // Additional validations can be added here if needed
        return laborRepository.save(labor);
    }

    public Optional<Labor> findLaborById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Labor ID must not be null.");
        }
        return laborRepository.findById(id);
    }

    public List<Labor> findAllLabors() {
        return laborRepository.findAll();
    }


    public Labor update(Labor labor) {
        if (labor == null || labor.getId() == null) {
            throw new IllegalArgumentException("Labor or Labor ID must not be null.");
        }
        validateLabor(labor);  // Perform validation before updating
        return laborRepository.update(labor);
    }


    public boolean delete(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Labor ID must not be null.");
        }
        return laborRepository.delete(id);
    }


    private void validateLabor(Labor labor) {
        try {
            ValidationUtils.validateName(labor.getComponentName());
            ValidationUtils.validateWorkHours(labor.getWorkHours());
        } catch (IllegalArgumentException e) {
            throw new LaborNotFoundException("Validation failed: " + e.getMessage());
        }
    }
}
