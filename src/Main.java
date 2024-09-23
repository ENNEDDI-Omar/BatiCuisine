import startup.config.DatabaseConnection;
import startup.repository.implementations.ClientRepository;
import startup.service.ClientService;
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

        ClientService clientService = new ClientService(clientRepository);

        MainMenu mainMenu = new MainMenu(clientService);
        mainMenu.displayMainMenu();
    }
}