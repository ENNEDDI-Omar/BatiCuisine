package startup.service;

import startup.domain.entities.Client;
import startup.repository.implementations.ClientRepository;
import startup.utils.ValidationUtils;

import java.util.List;
import java.util.Optional;

public class ClientService {
    private final ClientRepository clientRepository;

    public ClientService() {
        this.clientRepository =new ClientRepository();
    }

    public Client save(Client client) {
        ValidationUtils.clientValidation(client);  // Valide les informations du client
        return clientRepository.save(client);
    }

    public Optional<Client> findById(Long id) {
        return clientRepository.findById(id);
    }

    public Client update(Client client) {
        ValidationUtils.clientValidation(client);  // Valide les informations du client
        return clientRepository.update(client);
    }

    public boolean delete(Long id) {
        return clientRepository.delete(id);
    }

    public List<Client> findAll() {
        return clientRepository.findAll();
    }

    public Optional<Client> findByName(String name) {
        return clientRepository.findByName(name);
    }
}
