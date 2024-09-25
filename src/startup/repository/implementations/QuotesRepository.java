package startup.repository.implementations;

import startup.domain.entities.Quotes;
import startup.repository.interfaces.QuotesInterface;

import java.util.List;
import java.util.Optional;

public class QuotesRepository implements QuotesInterface {
    @Override
    public Quotes save(Quotes entity) {
        return null;
    }

    @Override
    public Optional<Quotes> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<Quotes> findAll() {
        return List.of();
    }

    @Override
    public Quotes update(Quotes entity) {
        return null;
    }

    @Override
    public boolean delete(Long id) {
        return false;
    }
}
