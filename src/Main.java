import startup.config.DatabaseConnection;
import startup.repository.implementations.ClientRepository;
import startup.repository.implementations.LaborRepository;
import startup.repository.implementations.MaterialRepository;
import startup.service.ClientService;
import startup.service.LaborService;
import startup.service.MaterialService;
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

        ClientService clientService = new ClientService(clientRepository);
        MaterialService materialService = new MaterialService(materialRepository);
        LaborService laborService = new LaborService(laborRepository);

        MainMenu mainMenu = new MainMenu(clientService, materialService, laborService);
        mainMenu.displayMainMenu();
    }
}