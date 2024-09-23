package startup.ui;

import startup.domain.entities.Client;
import startup.exceptions.ClientNotFoundException;
import startup.service.ClientService;
import startup.utils.ValidationUtils;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class ClientMenu {
    private final Scanner scanner;
    private final ClientService clientService;

    public ClientMenu(Scanner scanner, ClientService clientService) {
        this.scanner = scanner;
        this.clientService = clientService;
    }
 //Client Menu
    public void displayClientMenu() {
        boolean isRunning = true;
        while (isRunning) {
            System.out.println("\n--- Client Menu ---");
            System.out.println("1. Search for an existing client");
            System.out.println("2. Add a new client");
            System.out.println("3. Update an existing client");
            System.out.println("4. Delete an existing client");
            System.out.println("5. Return to Main Menu");
            System.out.print("Enter your choice: ");

            String input = scanner.nextLine();
            int choice = ValidationUtils.validateMenusChoices(input);
            if (choice == -1) {
                continue;
            }

            switch (choice) {
                case 1:
                    displaySearchForClientMenu();
                    break;
                case 2:
                    addNewClient();
                    break;
                case 3:
                    //updateAnExistingClient();
                    break;
                case 4:
                    //deleteAnExistingClient();
                    break;
                case 5:
                    isRunning = false;
                    System.out.println("Returning to Main Menu...");
                    break;
                default:
                    System.out.println("Invalid choice. Please enter 1, 2, 3, 4 or 5.");
            }
        }
    }

    //Searching by id or by Name for Client Menu
     public void displaySearchForClientMenu()
     {
         boolean isRunning = true;
         while (isRunning) {
             System.out.println("\n--- Client Search Menu ---");
             System.out.println("1. Search by Name");
             System.out.println("2. Search by Id");
             System.out.println("3. Return to the Client Menu");
             System.out.print("Enter your choice: ");

             String input = scanner.nextLine();
             int choice = ValidationUtils.validateMenusChoices(input);
             if (choice == -1) {
                 continue;
             }

             switch (choice) {
                 case 1:
                     searchClientByName();
                     break;
                 case 2:
                     searchClientById();
                     break;
                 case 3:
                     isRunning = false;
                     System.out.println("Returning to Client Menu...");
                     break;
                 default:
                     System.out.println("Invalid choice. Please enter 1, 2, or 3.");
             }
         }
     }

    // Méthode pour rechercher un client par ID
    private void searchClientById() {
        System.out.print("Enter the client ID to search for: ");
        String idInput = scanner.nextLine();
        try {
            Long clientId = ValidationUtils.validateId(idInput);
            Client client = clientService.findById(clientId).orElseThrow(() -> new ClientNotFoundException("Client not found with ID: " + clientId));
            System.out.println("Client found: " + client);
            if (promptForContinuation()) {
                createProjectForClient(client);
            }
        } catch (IllegalArgumentException | ClientNotFoundException e) {
            System.out.println(e.getMessage());
            handleClientNotFound();
        }
    }

    // Méthode pour rechercher un client par Nom
    private void searchClientByName() {
        System.out.print("Enter the client name to search for: ");
        String name = scanner.nextLine();
        try {
            String validatedName = ValidationUtils.validateName(name);
            Optional<Client> clientOptional = clientService.findByName(validatedName);
            if (clientOptional.isPresent()) {
                System.out.println("Client found: " + clientOptional.get());
                if (promptForContinuation()) {
                    createProjectForClient(clientOptional.get());
                }
            } else {
                System.out.println("No client found with the name: " + validatedName);
                handleClientNotFound();
            }
        } catch (ClientNotFoundException e) {
            System.out.println(e.getMessage());
            handleClientNotFound();
        }
    }



    //Methode for adding New Client
      private void addNewClient()
      {
          try {
              System.out.println("Enter new client details:");
              System.out.print("Name: ");
              String name = scanner.nextLine();
              System.out.print("Address: ");
              String address = scanner.nextLine();
              System.out.print("Phone: ");
              String phone = scanner.nextLine();
              boolean isProfessional = getProfessionalStatus();

              Client newClient = new Client(null, name, address, phone, isProfessional);
              ValidationUtils.clientValidation(newClient);
              if (confirmClientDetails(newClient)) {
                  clientService.save(newClient);
                  System.out.println("New client added successfully.");
              } else {
                  System.out.println("Client addition canceled. Client details aren't Correct");
              }

              System.out.println("New client added successfully.");
          } catch (Exception e) {
              System.out.println("Error adding new client: " + e.getMessage());
              e.printStackTrace();
          }
      }

    //Methode for updating a Client
    private void updateAnExistingClient() {
        System.out.print("Enter the client ID to update: ");
        String idInput = scanner.nextLine();
        Long clientId = ValidationUtils.validateId(idInput);
        if (clientId == null) {
            System.out.println("Invalid ID. Please enter a numeric ID.");
            return;
        }

        try {
            Client client = clientService.findById(clientId).orElseThrow(() -> new ClientNotFoundException("Client not found"));

            System.out.print("New name (leave blank to not change): ");
            String name = scanner.nextLine();
            if (!name.isEmpty()) {
                client.setName(ValidationUtils.validateName(name));
            }
            System.out.print("New address (leave blank to not change): ");
            String address = scanner.nextLine();
            if (!address.isEmpty()) {
                client.setAddress(ValidationUtils.validateAddress(address));
            }
            System.out.print("New phone (leave blank to not change): ");
            String phone = scanner.nextLine();
            if (!phone.isEmpty()) {
                client.setPhone(ValidationUtils.validatePhone(phone));
            }
            if (confirmUpdate(client)) {
                clientService.update(client);
                System.out.println("Client updated successfully.");
            } else {
                System.out.println("Update canceled by user.");
            }
        } catch (ClientNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("An error occurred during client update: " + e.getMessage());
            e.printStackTrace();
        }
    }

    //Methode for deliting a Client
    private void deleteAnExistingClient() {
        System.out.print("Enter the client ID to delete: ");
        String idInput = scanner.nextLine();
        try {
            Long clientId = ValidationUtils.validateId(idInput);  // Valide l'ID et lance une exception si non valide
            if (clientService.delete(clientId)) {
                System.out.println("Client deleted successfully.");
            } else {
                System.out.println("Failed to delete client.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }


///////////////////////////////////////////////Infos Confirmations Methodes://////////////////////////////////////////////

    private boolean getProfessionalStatus() {
        while (true) {
            System.out.print("Is the client a professional (y/n)? ");
            String isProfessionalInput = scanner.nextLine().trim().toLowerCase();
            if ("y".equals(isProfessionalInput)) {
                return true;
            } else if ("n".equals(isProfessionalInput)) {
                return false;
            } else {
                System.out.println("Invalid input. Please enter 'y' for yes or 'n' for no.");
            }
        }
    }

    private boolean confirmClientDetails(Client client) {
        System.out.println("\nPlease confirm the client details:");
        System.out.println("Name: " + client.getName());
        System.out.println("Address: " + client.getAddress());
        System.out.println("Phone: " + client.getPhone());
        System.out.println("Professional: " + (client.isProfessional() ? "Yes" : "No"));
        System.out.print("Is this information correct? (y/n): ");
        String confirmation = scanner.nextLine().trim().toLowerCase();
        return "y".equals(confirmation);
    }

    private boolean confirmUpdate(Client client) {
        System.out.println("\nPlease review the updated client details:");
        System.out.println("Name: " + client.getName());
        System.out.println("Address: " + client.getAddress());
        System.out.println("Phone: " + client.getPhone());
        System.out.print("Confirm the changes (y/n): ");
        String confirmation = scanner.nextLine().trim().toLowerCase();
        return "y".equals(confirmation);
    }

//////////////////////////////Asking prompt to search methodes: ///////////////////////////////

    private boolean promptForContinuation() {
        System.out.print("Do you wish to continue with this client? (y/n): ");
        String response = scanner.nextLine().trim().toLowerCase();
        return "y".equals(response);
    }

    private void createProjectForClient(Client client) {
        System.out.println("Proceeding to create a project for: " + client.getName());

    }

    private void handleClientNotFound() {
        System.out.println("Would you like to retry or create a new client? (retry/new): ");
        String response = scanner.nextLine().trim().toLowerCase();
        if ("retry".equals(response)) {
            displayClientMenu();
        } else if ("new".equals(response)) {
            addNewClient(); 
        }
    }
}
