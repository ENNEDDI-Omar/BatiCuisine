package startup.repository.implementations;

import startup.domain.entities.Project;
import startup.repository.interfaces.ProjectInterface;

import java.util.List;
import java.util.Optional;

public class ProjectRepository implements ProjectInterface {
    @Override
    public Project save(Project entity) {
        return null;
    }

    @Override
    public Optional<Project> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<Project> findAll() {
        return List.of();
    }

    @Override
    public Project update(Project entity) {
        return null;
    }

    @Override
    public boolean delete(Long id) {
        return false;
    }
}
