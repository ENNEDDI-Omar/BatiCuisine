package startup.utils;

import startup.domain.entities.Client;
import startup.domain.enums.QualityCoefficientType;
import startup.exceptions.ClientNotFoundException;
import startup.exceptions.ProjectNotFoundException;

import java.time.LocalDate;
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

    public static boolean validateIsProfessional(String input) {
        if ("yes".equalsIgnoreCase(input) || "y".equalsIgnoreCase(input)) {
            return true;
        } else if ("no".equalsIgnoreCase(input) || "n".equalsIgnoreCase(input)) {
            return false;
        } else {
            throw new IllegalArgumentException("Invalid input for professional status. Enter 'yes' or 'no'.");
        }
    }

    public static double validateUnitPrice(double unitPrice) {
        if (unitPrice < 0) {
            throw new IllegalArgumentException("Unit price cannot be negative.");
        }
        return unitPrice;
    }

    public static double validateQuantity(double quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative.");
        }
        return quantity;
    }

    public static QualityCoefficientType validateQualityCoefficient(String qualityCoefficient) {
        try {
            return QualityCoefficientType.valueOf(qualityCoefficient.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid quality coefficient provided.");
        }
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

    public static double validateWorkHours(double workHours) {
        if (workHours < 0) {
            throw new IllegalArgumentException("Work hours cannot be negative.");
        }
        return workHours;
    }

    public static double validateEstimatedAmount(double amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Estimated amount cannot be negative.");
        }
        return amount;
    }

    // Validation pour les dates d'émission pour s'assurer qu'elles ne sont pas postérieures à la date d'expiration
    public static LocalDate validateIssueDate(LocalDate issueDate, LocalDate expirationDate) {
        if (issueDate != null && expirationDate != null && issueDate.isAfter(expirationDate)) {
            throw new IllegalArgumentException("Issue date cannot be after expiration date.");
        }
        return issueDate;
    }

    // Validation pour les dates d'expiration pour s'assurer qu'elles ne sont pas antérieures à la date d'émission
    public static LocalDate validateExpirationDate(LocalDate expirationDate, LocalDate issueDate) {
        if (expirationDate != null && issueDate != null && expirationDate.isBefore(issueDate)) {
            throw new IllegalArgumentException("Expiration date cannot be before issue date.");
        }
        return expirationDate;
    }

}
