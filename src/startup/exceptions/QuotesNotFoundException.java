package startup.exceptions;

public class QuotesNotFoundException extends RuntimeException {
    public QuotesNotFoundException(String message) {
        super(message);
    }
}
