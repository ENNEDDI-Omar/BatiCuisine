package startup.ui;

import startup.domain.entities.Project;
import startup.domain.entities.Quotes;
import startup.service.ProjectService;
import startup.service.QuotesService;
import startup.utils.ValidationUtils;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class QuotesMenu {
    private final QuotesService quotesService;
    private final ProjectService projectService;
    private final Scanner scanner;

    public QuotesMenu(Scanner scanner) {
        this.quotesService = new QuotesService();
        this.projectService = new ProjectService();
        this.scanner = scanner;
    }

    public void displayQuotesMenu() {
        boolean running = true;
        while (running) {
            System.out.println("\n--- Quotes Menu ---");
            System.out.println("1. Generate Quote by Project ID");
            System.out.println("2. List all Quotes");
            System.out.println("3. Return to Main Menu");

            System.out.print("Enter your choice: ");
            int choice = ValidationUtils.validateMenusChoices(scanner.nextLine());

            switch (choice) {
                case 1:
                    generateQuoteByProjectId();
                    break;
                case 2:
                    listAllQuotes();
                    break;
                case 3:
                    running = false;
                    System.out.println("Returning to Main Menu...");
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 3.");
            }
        }
    }

    // Generates a quote by Project ID and saves it.
    private void generateQuoteByProjectId() {
        System.out.println("Enter the Project ID:");
        long projectId = ValidationUtils.validateId(scanner.nextLine());

        // Retrieve the project using ProjectService
        Optional<Project> projectOptional = projectService.findProjectById(projectId);
        if (projectOptional.isPresent()) {
            Project project = projectOptional.get();

            // Calculate the estimated amount (using the project's total cost and profit margin)
            Quotes quote = new Quotes(null, project);
            System.out.println("Calculated estimated amount for quote: " + quote.getEstimatedAmount());

            // Save the generated quote
            Quotes savedQuote = quotesService.saveQuote(quote);
            System.out.println("Generated Quote: " + savedQuote);
        } else {
            System.out.println("Project not found with ID: " + projectId);
        }
    }

    // Lists all quotes
    private void listAllQuotes() {
        List<Quotes> quotes = quotesService.findAllQuotes();
        if (quotes.isEmpty()) {
            System.out.println("No quotes found.");
        } else {
            quotes.forEach(System.out::println);
        }
    }
}
