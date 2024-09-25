package startup.exceptions;

import startup.domain.entities.Client;
import startup.repository.implementations.ProjectRepository;

public class ClientNotFoundException extends RuntimeException {
    public ClientNotFoundException(String message) {
        super(message);
    }

}
