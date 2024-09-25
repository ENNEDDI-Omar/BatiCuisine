import startup.config.DatabaseConnection;
import startup.repository.implementations.*;
import startup.service.*;
import startup.ui.ClientMenu;
import startup.ui.MainMenu;

import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.

        ClientRepository clientRepository = new ClientRepository();
        MaterialRepository materialRepository = new MaterialRepository();
        LaborRepository laborRepository = new LaborRepository();
        ProjectRepository projectRepository = new ProjectRepository();
        QuotesRepository quotesRepository = new QuotesRepository();

        ClientService clientService = new ClientService();
        MaterialService materialService = new MaterialService();
        LaborService laborService = new LaborService();
        ProjectService projectService = new ProjectService();
        QuotesService quotesService = new QuotesService();
        Scanner scanner = new Scanner(System.in);
        MainMenu mainMenu = new MainMenu(scanner,clientService, materialService, laborService, projectService, quotesService);
        mainMenu.displayMainMenu();
    }
}