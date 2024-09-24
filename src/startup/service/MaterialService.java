package startup.service;

import startup.domain.entities.Material;
import startup.exceptions.MaterialNotFoundException;
import startup.repository.implementations.MaterialRepository;
import startup.utils.ValidationUtils;

import java.util.List;
import java.util.Optional;

public class MaterialService {
    private MaterialRepository materialRepository;

    public MaterialService(MaterialRepository materialRepository) {
        this.materialRepository = new MaterialRepository();
    }

    public Material saveMaterial(Material material) {
        validateMaterial(material);
        return materialRepository.save(material);
    }

    public Material findMaterialById(Long id) {
        Optional<Material> material = materialRepository.findById(id);
        return material.orElseThrow(() -> new MaterialNotFoundException("No material found with id: " + id));
    }

    public List<Material> findAllMaterials() {
        return materialRepository.findAll();
    }

    public Material updateMaterial(Material material) {
        validateMaterial(material);
        if (!materialRepository.findById(material.getId()).isPresent()) {
            throw new MaterialNotFoundException("No material found with id: " + material.getId());
        }
        return materialRepository.update(material);
    }

    public boolean deleteMaterial(Long id) {
        if (!materialRepository.findById(id).isPresent()) {
            throw new MaterialNotFoundException("No material found with id: " + id);
        }
        return materialRepository.delete(id);
    }

    private void validateMaterial(Material material) {
        material.setUnitPrice(ValidationUtils.validateUnitPrice(material.getUnitPrice()));
        material.setQuantity(ValidationUtils.validateQuantity(material.getQuantity()));
        material.setQualityCoefficient(ValidationUtils.validateQualityCoefficient(material.getQualityCoefficient().name()));
    }
}
