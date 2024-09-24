package startup.ui;

import startup.domain.entities.Material;
import startup.domain.enums.QualityCoefficientType;
import startup.exceptions.MaterialNotFoundException;
import startup.service.MaterialService;
import startup.utils.ValidationUtils;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class MaterialMenu {
    private MaterialService materialService;
    private Scanner scanner;

    public MaterialMenu(Scanner scanner, MaterialService materialService) {
        this.materialService = materialService;
        this.scanner = scanner;
    }

    public void displayMaterialMenu() {
        boolean isRunning = true;
        while (isRunning) {
            System.out.println("\n--- Material Menu ---");
            System.out.println("1. Add New Material");
            System.out.println("2. View All Materials");
            System.out.println("3. Update Material");
            System.out.println("4. Delete Material");
            System.out.println("5. Find Material by ID");
            System.out.println("6. Return to Main Menu");
            System.out.print("Enter your choice: ");

            String input = scanner.nextLine();
            int choice = ValidationUtils.validateMenusChoices(input);
            if (choice == -1) {
                continue;
            }

            switch (choice) {
                case 1:
                    addNewMaterial();
                    break;
                case 2:
                    viewAllMaterials();
                    break;
                case 3:
                    updateMaterial();
                    break;
                case 4:
                    deleteMaterial();
                    break;
                case 5:
                    findMaterialById();
                    break;
                case 6:
                    isRunning = false;
                    System.out.println("Returning to Main Menu...");
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a valid number.");
            }
        }
    }

    private void addNewMaterial() {
        System.out.println("Enter new material details:");
        System.out.print("Name: ");
        String name = scanner.nextLine();
        try {
            name = ValidationUtils.validateName(name);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return;
        }

        System.out.print("Unit Price: ");
        String unitPriceStr = scanner.nextLine();
        double unitPrice;
        try {
            unitPrice = Double.parseDouble(unitPriceStr);
            unitPrice = ValidationUtils.validateUnitPrice(unitPrice); // Valider le prix unitaire
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return;
        }

        System.out.print("Quantity: ");
        String quantityStr = scanner.nextLine();
        double quantity;
        try {
            quantity = Double.parseDouble(quantityStr);
            quantity = ValidationUtils.validateQuantity(quantity); // Valider la quantité
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return; // Sortie précoce si la validation échoue
        }

        System.out.print("Quality Coefficient (STANDARD/PREMIUM): ");
        String qualityStr = scanner.nextLine();
        QualityCoefficientType qualityCoefficient;
        try {
            qualityCoefficient = ValidationUtils.validateQualityCoefficient(qualityStr); // Valider le coefficient de qualité
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return; // Sortie précoce si la validation échoue
        }

        // Création de l'objet Material
        Material newMaterial = new Material();
        newMaterial.setComponentName(name);
        newMaterial.setUnitPrice(unitPrice);
        newMaterial.setQuantity(quantity);
        newMaterial.setQualityCoefficient(qualityCoefficient);

        // Sauvegarde du matériel via le service
        try {
            materialService.saveMaterial(newMaterial);
            System.out.println("New material added successfully.");
        } catch (Exception e) {
            System.out.println("Failed to add new material: " + e.getMessage());
        }
    }


    private void viewAllMaterials() {
        System.out.println("\n--- View All Materials ---");
        List<Material> materials = materialService.findAllMaterials();
        for (Material material : materials) {
            System.out.println(material);
        }
    }

    private void updateMaterial() {
        System.out.print("Enter the material ID to update: ");
        String idInput = scanner.nextLine();
        Long materialId;
        try {
            materialId = ValidationUtils.validateId(idInput);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return; // Early exit if ID validation fails
        }

        Material material;
        try {
            material = materialService.findMaterialById(materialId);
        } catch (MaterialNotFoundException e) {
            System.out.println(e.getMessage());
            return;
        }

        System.out.println("Current name: " + material.getComponentName());
        System.out.print("New name (leave blank to not change): ");
        String name = scanner.nextLine();
        if (!name.isEmpty()) {
            try {
                name = ValidationUtils.validateName(name);
                material.setComponentName(name);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
                return; // Early exit if validation fails
            }
        }

        System.out.println("Current unit price: " + material.getUnitPrice());
        System.out.print("New unit price (leave blank to not change): ");
        String unitPriceStr = scanner.nextLine();
        if (!unitPriceStr.isEmpty()) {
            try {
                double unitPrice = Double.parseDouble(unitPriceStr);
                unitPrice = ValidationUtils.validateUnitPrice(unitPrice);
                material.setUnitPrice(unitPrice);
            } catch ( IllegalArgumentException e) {
                System.out.println(e.getMessage());
                return; // Early exit if validation fails
            }
        }

        System.out.println("Current quantity: " + material.getQuantity());
        System.out.print("New quantity (leave blank to not change): ");
        String quantityStr = scanner.nextLine();
        if (!quantityStr.isEmpty()) {
            try {
                double quantity = Double.parseDouble(quantityStr);
                quantity = ValidationUtils.validateQuantity(quantity);
                material.setQuantity(quantity);
            } catch ( IllegalArgumentException e) {
                System.out.println(e.getMessage());
                return; // Early exit if validation fails
            }
        }

        System.out.println("Current quality coefficient: " + material.getQualityCoefficient());
        System.out.print("New quality coefficient (STANDARD/PREMIUM, leave blank to not change): ");
        String qualityStr = scanner.nextLine();
        if (!qualityStr.isEmpty()) {
            try {
                QualityCoefficientType qualityCoefficient = ValidationUtils.validateQualityCoefficient(qualityStr);
                material.setQualityCoefficient(qualityCoefficient);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
                return; // Early exit if validation fails
            }
        }

        // Update the material via the service
        try {
            materialService.updateMaterial(material);
            System.out.println("Material updated successfully.");
        } catch (Exception e) {
            System.out.println("Failed to update material: " + e.getMessage());
        }
    }




    private void deleteMaterial() {
        System.out.println("\n--- Delete Material ---");
        System.out.print("Enter Material ID to delete: ");
        long id = Long.parseLong(scanner.nextLine());
        if (materialService.deleteMaterial(id)) {
            System.out.println("Material deleted successfully!");
        } else {
            System.out.println("Error deleting material.");
        }
    }

    private void findMaterialById() {
        System.out.println("\n--- Find Material by ID ---");
        System.out.print("Enter Material ID: ");
        long id = Long.parseLong(scanner.nextLine());
        try {
            Material material = materialService.findMaterialById(id);
            System.out.println(material);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
