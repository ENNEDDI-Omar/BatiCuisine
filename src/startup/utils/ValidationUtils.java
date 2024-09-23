package startup.utils;

import startup.domain.entities.Client;
import startup.exceptions.ClientNotFoundException;
import startup.exceptions.ProjectNotFoundException;

import java.util.Optional;

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
            throw new ClientNotFoundException("Client Name cannot be empty");

        }if (client.getAddress() == null || client.getAddress().trim().isEmpty())
        {
        throw new ClientNotFoundException(" Client Address cannot be empty");

        }if (client.getPhone() == null || client.getPhone().trim().isEmpty())
        {
        throw new ClientNotFoundException(" Client Phone number cannot be empty");

        }if (client.getPhone().length() > 20)
        {
        throw new ClientNotFoundException("Phone number cannot be longer than 20 characters");
        }
    }

    public static Long validateId(String input) throws IllegalArgumentException {
        if (input == null || input.trim().isEmpty()) {
            throw new IllegalArgumentException("Input cannot be null or empty. Please enter a valid numeric ID.");
        }
        try {
            return Long.parseLong(input.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid ID. Please enter a numeric ID.");
        }
    }


    public static Optional<Long> optionalValidateId(String input) {
        try {
            return Optional.of(Long.parseLong(input));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    public static String validateName(String name) throws ClientNotFoundException {
        if (name == null || name.trim().isEmpty()) {
            throw new ClientNotFoundException("Client name cannot be empty");
        }
        return name.trim();
    }

    public static String validateAddress(String address) throws ClientNotFoundException {
        if (address == null || address.trim().isEmpty()) {
            throw new ClientNotFoundException("Address cannot be empty");
        }
        return address.trim();
    }

    public static String validatePhone(String phone) throws ClientNotFoundException {
        if (phone == null || phone.trim().isEmpty() || phone.length() > 20) {
            throw new ClientNotFoundException("Phone number is invalid or too long");
        }
        return phone.trim();
    }

    // Valide le nom du projet
    public static String validateProjectName(String projectName) throws ProjectNotFoundException {
        if (projectName == null || projectName.trim().isEmpty()) {
            throw new ProjectNotFoundException("Project name cannot be empty.");
        }
        return projectName.trim();
    }

    // Valide la surface du projet
    public static double validateSurface(double surface) throws ProjectNotFoundException {
        if (surface <= 0) {
            throw new ProjectNotFoundException("Surface must be greater than zero.");
        }
        return surface;
    }

    // Valide le coût total du projet
    public static double validateTotalCost(double totalCost) throws ProjectNotFoundException {
        if (totalCost < 0) {
            throw new ProjectNotFoundException("Total cost cannot be negative.");
        }
        return totalCost;
    }
}
