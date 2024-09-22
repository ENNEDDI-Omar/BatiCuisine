package startup.repository.implementations;

import startup.domain.entities.Client;
import startup.repository.interfaces.ClientInterface;

import java.util.List;
import java.util.Optional;

public class ClientRepository implements ClientInterface
{
    @Override
    public Optional<Client> findByName(String name) {
        return Optional.empty();
    }

    @Override
    public Client save(Client entity) {
        return null;
    }

    @Override
    public Optional<Client> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<Client> findAll() {
        return List.of();
    }

    @Override
    public Client update(Client entity) {
        return null;
    }

    @Override
    public boolean delete(Long id) {
        return false;
    }
}
