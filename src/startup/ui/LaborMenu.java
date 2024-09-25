package startup.ui;

import startup.domain.entities.Labor;
import startup.domain.entities.Project;
import startup.domain.enums.LaborType;
import startup.domain.enums.ProductivityLevelType;
import startup.service.LaborService;
import startup.utils.ValidationUtils;

import java.util.Optional;
import java.util.Scanner;

public class LaborMenu {
    private final Scanner scanner;
    private final LaborService laborService;

    public LaborMenu(Scanner scanner) {
        this.scanner = scanner;
        this.laborService = new LaborService();
    }

    // Main menu for labor management
    public void displayLaborMenu() {
        boolean isRunning = true;
        while (isRunning) {
            System.out.println("\n--- Labor Menu ---");
            System.out.println("1. Add new labor");
            System.out.println("2. Update existing labor");
            System.out.println("3. Delete labor");
            System.out.println("4. Find labor by ID");
            System.out.println("5. Return to Main Menu");
            System.out.print("Enter your choice: ");

            String input = scanner.nextLine();
            int choice = ValidationUtils.validateMenusChoices(input);
            if (choice == -1) {
                continue;
            }

            switch (choice) {
                case 1:
                    addNewLabor();  // Calls the overloaded method with no Project argument
                    break;
                case 2:
                    updateLabor();
                    break;
                case 3:
                    deleteLabor();
                    break;
                case 4:
                    findLaborById();
                    break;
                case 5:
                    isRunning = false;
                    System.out.println("Returning to Main Menu...");
                    break;
                default:
                    System.out.println("Invalid choice. Please enter 1, 2, 3, 4, or 5.");
            }
        }
    }

    // Overloaded method that doesn't require a Project
    public Labor addNewLabor() {
        // Calls the other addNewLabor method with null for the project
        return addNewLabor(null);
    }

    // Main method to add new labor, requires a Project
    public Labor addNewLabor(Project project) {
        try {
            System.out.println("Enter new labor details:");

            System.out.print("Component Name: ");
            String componentName = scanner.nextLine();

            // Validate and input Work Hours
            double workHours = 0;
            boolean validInput = false;
            while (!validInput) {
                System.out.print("Work Hours: ");
                String workHoursStr = scanner.nextLine();
                try {
                    workHours = Double.parseDouble(workHoursStr);
                    validInput = true;
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input for Work Hours. Please enter a numeric value.");
                }
            }

            // Validate and input Labor Type
            LaborType laborType = null;
            validInput = false;
            while (!validInput) {
                System.out.print("Labor Type (WORKER/SPECIALIST): ");
                String laborTypeStr = scanner.nextLine().toUpperCase();
                try {
                    laborType = LaborType.valueOf(laborTypeStr);
                    validInput = true;
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid input for Labor Type. Please enter WORKER or SPECIALIST.");
                }
            }

            // Validate and input Productivity Level
            ProductivityLevelType productivityLevel = null;
            validInput = false;
            while (!validInput) {
                System.out.print("Productivity Level (STANDARD/HIGH): ");
                String productivityLevelStr = scanner.nextLine().toUpperCase();
                try {
                    productivityLevel = ProductivityLevelType.valueOf(productivityLevelStr);
                    validInput = true;
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid input for Productivity Level. Please enter STANDARD or HIGH.");
                }
            }

            // Input Transport Cost
            double transportCost = 0;
            validInput = false;
            while (!validInput) {
                System.out.print("Transport Cost: ");
                String transportCostStr = scanner.nextLine();
                try {
                    transportCost = Double.parseDouble(transportCostStr);
                    validInput = true;
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input for Transport Cost. Please enter a numeric value.");
                }
            }

            // Create new Labor object
            Labor newLabor = new Labor(0L, componentName, transportCost, laborType, workHours, productivityLevel, project);
            System.out.println("Labor to be saved: " + newLabor);

            // Save the labor
            Labor savedLabor = laborService.save(newLabor);
            System.out.println("Labor saved: " + savedLabor);

            return savedLabor;
        } catch (Exception e) {
            System.out.println("Failed to add new labor: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    private void updateLabor() {
        System.out.print("Enter the labor ID to update: ");
        String idInput = scanner.nextLine();
        Long laborId = ValidationUtils.validateId(idInput);

        Optional<Labor> laborOptional = laborService.findLaborById(laborId);
        if (!laborOptional.isPresent()) {
            System.out.println("Labor not found with ID: " + laborId);
            return;
        }

        Labor labor = laborOptional.get();
        System.out.print("New component name (leave blank to not change): ");
        String name = scanner.nextLine();
        if (!name.isEmpty()) {
            labor.setComponentName(name);
        }

        System.out.print("New work hours (leave blank to not change): ");
        String hoursInput = scanner.nextLine();
        if (!hoursInput.isEmpty()) {
            double hours = Double.parseDouble(hoursInput);
            hours = ValidationUtils.validateWorkHours(hours);
            labor.setWorkHours(hours);
        }

        laborService.update(labor);
        System.out.println("Labor updated successfully.");
    }

    private void deleteLabor() {
        System.out.print("Enter the labor ID to delete: ");
        String idInput = scanner.nextLine();
        Long laborId = ValidationUtils.validateId(idInput);

        boolean deleted = laborService.delete(laborId);
        if (deleted) {
            System.out.println("Labor deleted successfully.");
        } else {
            System.out.println("Failed to delete labor.");
        }
    }

    private void findLaborById() {
        System.out.print("Enter the labor ID: ");
        String idInput = scanner.nextLine();
        Long laborId = ValidationUtils.validateId(idInput);

        Optional<Labor> labor = laborService.findLaborById(laborId);
        if (labor.isPresent()) {
            System.out.println("Labor found: " + labor.get());
        } else {
            System.out.println("No labor found with ID: " + laborId);
        }
    }
}
