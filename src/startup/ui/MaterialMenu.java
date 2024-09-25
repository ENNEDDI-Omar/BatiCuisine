package startup.ui;

import startup.domain.entities.Material;
import startup.domain.entities.Project;
import startup.domain.enums.ComponentType;
import startup.domain.enums.QualityCoefficientType;
import startup.exceptions.MaterialNotFoundException;
import startup.service.MaterialService;
import startup.utils.ValidationUtils;

import java.util.List;
import java.util.Scanner;

public class MaterialMenu {
    private MaterialService materialService;
    private Scanner scanner;

    public MaterialMenu(Scanner scanner) {
        this.materialService = new MaterialService();
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
                    addNewMaterial(null);  // Passing null as we're not in a project context
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

    public Material addNewMaterial(Project project) {
        try {
            System.out.println("Enter new material details:");
            System.out.print("Name: ");
            String name = scanner.nextLine();
            System.out.print("Unit Price: ");
            double unitPrice = Double.parseDouble(scanner.nextLine());
            System.out.print("Quantity: ");
            double quantity = Double.parseDouble(scanner.nextLine());

            QualityCoefficientType qualityCoefficient = QualityCoefficientType.STANDARD; // Valeur par défaut
            boolean validInput = false;
            while (!validInput) {
                System.out.print("Quality Coefficient (STANDARD/PREMIUM): ");
                String qualityStr = scanner.nextLine().toUpperCase();
                try {
                    qualityCoefficient = QualityCoefficientType.valueOf(qualityStr);
                    validInput = true;
                    System.out.println(qualityCoefficient);
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid quality coefficient. Using STANDARD.");
                    validInput = true; // Utiliser la valeur par défaut et continuer
                }
            }

            System.out.print("Transport Cost: ");
            double transportCost = Double.parseDouble(scanner.nextLine());

            Material newMaterial = new Material(0L, name, transportCost, unitPrice, quantity, qualityCoefficient, project);
            System.out.println("Material to be saved: " + newMaterial);

            // Sauvegarder le matériel
            Material savedMaterial = materialService.saveMaterial(newMaterial);
            System.out.println("Material saved: " + savedMaterial);
            return savedMaterial;
        } catch (Exception e) {
            System.out.println("Failed to add new material: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }




    public Material findExistingMaterial() {
        System.out.print("Enter Material ID: ");
        String idInput = scanner.nextLine();
        Long materialId;
        try {
            materialId = ValidationUtils.validateId(idInput);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return null;
        }

        try {
            Material material = materialService.findMaterialById(materialId);
            System.out.println("Material found: " + material);
            return material;
        } catch (MaterialNotFoundException e) {
            System.out.println(e.getMessage());
            return null;
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
            return;
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
                return;
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
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
                return;
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
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
                return;
            }
        }

        System.out.println("Current quality coefficient: " + material.getQualityCoefficient());
        System.out.print("New quality coefficient (STANDARD/PREMIUM, leave blank to not change): ");
        String qualityStr = scanner.nextLine();
//        if (!qualityStr.isEmpty()) {
//            try {
////                QualityCoefficientType qualityCoefficient = ValidationUtils.validateQualityCoefficient(qualityStr);
////                material.setQualityCoefficient(qualityCoefficient);
//            } catch (IllegalArgumentException e) {
//                System.out.println(e.getMessage());
//                return;
//            }
//        }

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