package startup.ui;

import startup.service.ClientService;
import startup.service.LaborService;
import startup.service.MaterialService;
import startup.service.ProjectService;
import startup.utils.ValidationUtils;

import java.util.Scanner;

public class MainMenu {
    private final Scanner scanner;
    private final ClientService clientService;
    private final MaterialService materialService;
    private final LaborService laborService;
    private final ProjectService projectService;



    public MainMenu(ClientService clientService, MaterialService materialService, LaborService laborService, ProjectService projectService)
    {
        this.scanner = new Scanner(System.in);
        this.clientService = clientService;
        this.materialService = materialService;
        this.laborService = laborService;
        this.projectService = projectService;
    }



   public void displayMainMenu()
   {
       ClientMenu clientMenu = new ClientMenu(scanner, clientService);
       MaterialMenu materialMenu = new MaterialMenu(scanner, materialService);
       LaborMenu laborMenu = new LaborMenu(scanner, laborService);
       ProjectMenu projectMenu = new ProjectMenu(scanner, projectService);
       boolean isRunning = true;

     try {
         while (isRunning)
         {
             System.out.println("\n--- Welcome to Bati Cuisine Application ---");
             System.out.println("\n--- Main Menu ---");
             System.out.println("1. Manage Clients");
             System.out.println("2. Manage projects");
             System.out.println("3. Calculate the cost of a project");
             System.out.println("4. Manage Material");
             System.out.println("5. Manage Labor");
             System.out.println("6. Manage Quotes");
             System.out.println("7. Quit the application");
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
                     clientMenu.displayClientMenu();
                     break;
                 case 2:
                     projectMenu.displayProjectMenu();
                     break;
                 case 3:
                     //calculationsMenu.displayCalculationsMenu();
                     break;
                 case 4:
                     materialMenu.displayMaterialMenu();
                     break;
                 case 5:
                     laborMenu.displayLaborMenu();
                     break;
                 case 6:
                     //quotes;
                     break;
                 case 7:
                     isRunning = false;
                     System.out.println("Exiting Bati Cuisine Application... Thank You for your Visite.");
                     break;
                 default:
                     System.out.println("Invalid choice. Please enter 1, 2, 3, 4, 5, 6, or 7.");
             }
         }
     }finally {
         scanner.close();
     }

   }
}
