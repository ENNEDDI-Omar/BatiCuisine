package startup.ui;

import startup.domain.entities.Labor;
import startup.service.LaborService;
import startup.utils.ValidationUtils;

import java.util.Optional;
import java.util.Scanner;

public class LaborMenu {
    private final Scanner scanner;
    private final LaborService laborService;

    public LaborMenu(Scanner scanner, LaborService laborService) {
        this.scanner = scanner;
        this.laborService = laborService;
    }

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
                    addNewLabor();
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

    private void addNewLabor() {
        try {
            System.out.println("Enter new labor details:");
            System.out.print("Component Name: ");
            String componentName = scanner.nextLine();
            System.out.print("Work Hours: ");
            double workHours = Double.parseDouble(scanner.nextLine());
            workHours = ValidationUtils.validateWorkHours(workHours);

            Labor newLabor = new Labor();
            newLabor.setComponentName(componentName);
            newLabor.setWorkHours(workHours);

            laborService.save(newLabor);
            System.out.println("New labor added successfully.");
        } catch (Exception e) {
            System.out.println("Error adding new labor: " + e.getMessage());
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
