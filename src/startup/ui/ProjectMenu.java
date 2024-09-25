package startup.ui;

import startup.domain.entities.Project;
import startup.domain.enums.ProjectStatusType;
import startup.service.ProjectService;
import startup.utils.ValidationUtils;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class ProjectMenu {
    private final Scanner scanner;
    private final ProjectService projectService;

    public ProjectMenu(Scanner scanner, ProjectService projectService) {
        this.scanner = scanner;
        this.projectService = projectService;
    }

    public void displayProjectMenu() {
        boolean isRunning = true;
        while (isRunning) {
            System.out.println("\n--- Project Menu ---");
            System.out.println("1. Add a new project");
            System.out.println("2. View all projects");
            System.out.println("3. Search for a project by ID");
            System.out.println("4. Update an existing project");
            System.out.println("5. Delete a project");
            System.out.println("6. Return to Main Menu");
            System.out.print("Enter your choice: ");

            int choice = ValidationUtils.validateMenusChoices(scanner.nextLine());
            switch (choice) {
                case 1:
                    addNewProject();
                    break;
                case 2:
                    viewAllProjects();
                    break;
                case 3:
                    searchProjectById();
                    break;
                case 4:
                    updateExistingProject();
                    break;
                case 5:
                    deleteProject();
                    break;
                case 6:
                    isRunning = false;
                    System.out.println("Returning to Main Menu...");
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 6.");
            }
        }
    }

    private void addNewProject() {
        System.out.println("Enter project details:");
        System.out.print("Project Name: ");
        String name = scanner.nextLine();
        System.out.print("Surface: ");
        double surface = Double.parseDouble(scanner.nextLine());  // Ensure proper validation
        // Additional fields can be prompted here

        Project project = new Project(null, name, surface, null); // Adjust constructor usage
        projectService.save(project);
        System.out.println("Project added successfully.");
    }

    private void viewAllProjects() {
        List<Project> projects = projectService.findAllProjects();
        projects.forEach(System.out::println);
    }

    private void searchProjectById() {
        System.out.print("Enter Project ID: ");
        Long id = Long.parseLong(scanner.nextLine());  // Assurez-vous de valider correctement l'entrée

        Optional<Project> projectOptional = projectService.findProjectById(id);
        if (projectOptional.isPresent()) {
            System.out.println("Project found: " + projectOptional.get());
        } else {
            System.out.println("Project not found with ID: " + id);
        }
    }

    private void updateExistingProject() {
        System.out.print("Enter the Project ID to update: ");
        String idInput = scanner.nextLine();
        Long projectId;
        try {
            projectId = ValidationUtils.validateId(idInput);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return;
        }

        Optional<Project> projectOptional = projectService.findProjectById(projectId);
        if (!projectOptional.isPresent()) {
            System.out.println("Project not found with ID: " + projectId);
            return;
        }

        Project project = projectOptional.get();

        System.out.println("Current project name: " + project.getProjectName());
        System.out.print("New name (leave blank to not change): ");
        String name = scanner.nextLine();
        if (!name.isEmpty()) {
            try {
                name = ValidationUtils.validateProjectName(name);
                project.setProjectName(name);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
                return; // Sortie anticipée si la validation échoue
            }
        }

        System.out.println("Current surface: " + project.getSurface());
        System.out.print("New surface (leave blank to not change): ");
        String surfaceStr = scanner.nextLine();
        if (!surfaceStr.isEmpty()) {
            try {
                double surface = Double.parseDouble(surfaceStr);
                surface = ValidationUtils.validateSurface(surface);
                project.setSurface(surface);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
                return;
            }
        }

        try {
            projectService.update(project);
            System.out.println("Project updated successfully.");
        } catch (Exception e) {
            System.out.println("Failed to update project: " + e.getMessage());
        }
    }


    private void deleteProject() {
        System.out.print("Enter Project ID to delete: ");
        Long id = Long.parseLong(scanner.nextLine());
        boolean deleted = projectService.delete(id);
        if (deleted) {
            System.out.println("Project deleted successfully.");
        } else {
            System.out.println("Failed to delete project.");
        }
    }
}
