package startup.ui;

import startup.utils.ValidationUtils;

import java.util.Scanner;

public class ClientMenu {
    private final Scanner scanner;

    public ClientMenu(Scanner scanner) {
        this.scanner = scanner;
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

    //Methode to Search for a Client by id
    private void searchClientById()
    {
    }

    //Methode to Search for a Client by Name
    private void searchClientByName()
    {
    }

    //Methode for adding New Client
      private void addNewClient()
      {
        System.out.println("Enter new client details:");
        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.print("Address: ");
        String address = scanner.nextLine();
        System.out.print("Phone: ");
        String phone = scanner.nextLine();
        // Logique pour ajouter un nouveau client dans la base de données
        System.out.println("Adding new client: " + name);
        // Implémentez l'ajout ici
    }

    //Methode for updating a Client
    private void updateAnExistingClient()
    {
    }

    //Methode for deliting a Client
    private void deleteAnExistingClient()
    {

    }

}
