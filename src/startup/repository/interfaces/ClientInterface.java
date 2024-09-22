package startup.repository.interfaces;

import startup.domain.entities.Client;

import java.util.Optional;

public interface ClientInterface extends CrudInterface<Client>
{
    Optional<Client> findByName(String name);
}
