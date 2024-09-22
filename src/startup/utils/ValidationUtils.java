package startup.utils;

import startup.domain.entities.Client;
import startup.exceptions.ClientNotFoundException;

public class ValidationUtils
{
    // Méthode pour valider les entrées des menus et retourner un choix valide ou -1 si non valide
    public static int validateMenusChoices(String input) {
        try
        {
            return Integer.parseInt(input);

        } catch (NumberFormatException e)
        {
            System.out.println("Invalid input. Please enter a number.");
            return -1;
        }
    }

    //Methode pour questioner les entrées Client
    public static void clientValidation(Client client)
    {
        if (client.getName() == null || client.getName().trim().isEmpty())
        {
            throw new ClientNotFoundException("Client cannot be empty");

        }if (client.getAddress() == null || client.getAddress().trim().isEmpty())
    {
        throw new ClientNotFoundException("Address cannot be empty");

    }if (client.getPhone() == null || client.getPhone().trim().isEmpty())
    {
        throw new ClientNotFoundException("Phone cannot be empty");

    }if (client.getPhone().length() > 20)
    {
        throw new ClientNotFoundException("Phone cannot be longer than 20 characters");
    }
    }
}
