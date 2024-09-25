package startup.ui;

import startup.domain.entities.Client;
import startup.domain.entities.Labor;
import startup.domain.entities.Material;
import startup.domain.entities.Project;
import startup.service.ProjectService;
import startup.utils.ValidationUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class ProjectMenu {
    private final Scanner scanner;
    private final ProjectService projectService;
    private final ClientMenu clientMenu;
    private final MaterialMenu materialMenu;
    private final LaborMenu laborMenu;

    public ProjectMenu(Scanner scanner) {
        this.scanner = scanner;
        this.projectService = new ProjectService();
        this.materialMenu = new MaterialMenu(scanner);
        this.laborMenu = new LaborMenu(scanner);
        this.clientMenu = new ClientMenu(scanner);
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

    private List<Material> addMaterialsToProject(Project project) {
        List<Material> materials = new ArrayList<>();
        boolean addingMaterials = true;

        while (addingMaterials) {
            System.out.println("\nAdd material to project:");
            System.out.println("1. Add existing material");
            System.out.println("2. Add new material");
            System.out.println("3. Finish adding materials");
            System.out.print("Enter your choice: ");

            int choice = ValidationUtils.validateMenusChoices(scanner.nextLine());
            switch (choice) {
                case 1:
                    Material existingMaterial = materialMenu.findExistingMaterial();
                    if (existingMaterial != null) {
                        materials.add(existingMaterial);
                    }
                    break;
                case 2:
                    // Pass the project when adding a new material
                    Material newMaterial = materialMenu.addNewMaterial(project);
                    if (newMaterial != null) {
                        materials.add(newMaterial);
                    }
                    break;
                case 3:
                    addingMaterials = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }

        return materials;
    }



    public void addNewProject() {
        Client client = findClientForProject();
        if (client == null) {
            System.out.println("Project creation cancelled.");
            return;
        }

        System.out.println("Enter project details:");
        System.out.print("Project Name: ");
        String name = scanner.nextLine();

        System.out.print("Surface: ");
        double surface = scanner.nextDouble();
        scanner.nextLine();

        // Create the project and associate it with the client
        Project project = new Project(null, name, surface, client);
        project = projectService.save(project);  // Save the project to get an ID

        addMaterialsToProject(project);
        addLaborToProject(project);

        // After adding components, recalculate and update the total cost
        project.setTotalCost(project.calculateTotalCost());

        // Now, update the project with the correct total cost in the database
        projectService.update(project);

        System.out.println("Project added successfully for client: " + client.getName());
        System.out.println("Total Project Cost: " + project.getTotalCost());
    }


    private Client findClientForProject() {
        while (true) {
            System.out.println("\n--- Find Client for Project ---");
            System.out.println("1. Search client by name");
            System.out.println("2. Search client by ID");
            System.out.println("3. Cancel project creation");
            System.out.print("Enter your choice: ");

            int choice = ValidationUtils.validateMenusChoices(scanner.nextLine());
            switch (choice) {
                case 1:
                    return clientMenu.searchClientByName();
                case 2:
                    return clientMenu.searchClientById();
                case 3:
                    return null;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void viewAllProjects() {
        List<Project> projects = projectService.findAllProjects();
        if (projects.isEmpty()) {
            System.out.println("No projects found.");
        } else {
            projects.forEach(System.out::println);
        }
    }

    private void searchProjectById() {
        System.out.print("Enter Project ID: ");
        Long id = Long.parseLong(scanner.nextLine());  // Ensure to validate this input properly

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


//    private Client findClientForProject() {
//        while (true) {
//            System.out.println("\n--- Find Client for Project ---");
//            System.out.println("1. Search client by name");
//            System.out.println("2. Search client by ID");
//            System.out.println("3. Cancel project creation");
//            System.out.print("Enter your choice: ");
//
//            int choice = ValidationUtils.validateMenusChoices(scanner.nextLine());
//            switch (choice) {
//                case 1:
//                    return new ClientMenu(scanner).searchClientByName();
//                case 2:
//                    return new ClientMenu(scanner).searchClientById();
//                case 3:
//                    return null;
//                default:
//                    System.out.println("Invalid choice. Please try again.");
//            }
//        }
//    }




    private List<Labor> addLaborToProject(Project project) {
        List<Labor> labors = new ArrayList<>();
        boolean addingLabor = true;

        while (addingLabor) {
            System.out.println("\nAdd labor to project:");
            System.out.println("1. Add new labor");
            System.out.println("2. Finish adding labor");
            System.out.print("Enter your choice: ");

            int choice = ValidationUtils.validateMenusChoices(scanner.nextLine());
            switch (choice) {
                case 1:
                    Labor newLabor = laborMenu.addNewLabor(project);
                    if (newLabor != null) {
                        labors.add(newLabor);
                    }
                    break;
                case 2:
                    addingLabor = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }

        return labors;
    }


    private List<Material> addMaterialsToProject() {
        List<Material> materials = new ArrayList<>();
        boolean addingMaterials = true;

        while (addingMaterials) {
            System.out.println("\nAdd material to project:");
            System.out.println("1. Add existing material");
            System.out.println("2. Add new material");
            System.out.println("3. Finish adding materials");
            System.out.print("Enter your choice: ");

            int choice = ValidationUtils.validateMenusChoices(scanner.nextLine());
            switch (choice) {
                case 1:
                    Material existingMaterial = materialMenu.findExistingMaterial();
                    if (existingMaterial != null) {
                        materials.add(existingMaterial);
                    }
                    break;
                case 2:
                    Material newMaterial = materialMenu.addNewMaterial(null);
                    if (newMaterial != null) {
                        materials.add(newMaterial);
                    }
                    break;
                case 3:
                    addingMaterials = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }

        return materials;
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
