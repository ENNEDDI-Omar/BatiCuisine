package startup.ui;

import startup.utils.ValidationUtils;

import java.util.Scanner;

public class MainMenu {
    private final Scanner scanner;



    public MainMenu()
    {
        this.scanner = new Scanner(System.in);
    }



   public void displayMainMenu()
   {
       ClientMenu clientMenu = new ClientMenu(scanner);
       boolean isRunning = true;

     try {
         while (isRunning)
         {
             System.out.println("\n--- Welcome to Bati Cuisine Application ---");
             System.out.println("\n--- Main Menu ---");
             System.out.println("1. Create a new project");
             System.out.println("2. Display existing projects");
             System.out.println("3. Calculate the cost of a project");
             System.out.println("4. Quit");
             System.out.print("Enter your choice: ");

             String input = scanner.nextLine();
             int choice = ValidationUtils.validateMenusChoices(input);
             if (choice == -1)
             {
                 continue;
             }
             switch (choice)
             {
                 case 1:
                     //clientMenu.displayClientMenu();
                     break;
                 case 2:
                     //projectMenu.displayProjectMenu();
                     break;
                 case 3:
                     //calculationsMenu.displayCalculationsMenu();
                     break;
                 case 4:
                     isRunning = false;
                     System.out.println("Exiting Bati Cuisine Application... Thank You for your Visite.");
                     break;
                 default:
                     System.out.println("Invalid choice. Please enter 1, 2, 3, or 4.");
             }
         }
     }finally {
         scanner.close();
     }

   }
}
