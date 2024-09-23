package startup.exceptions;

public class LaborNotFoundException extends RuntimeException {
    public LaborNotFoundException(String message) {
        super(message);
    }
}
