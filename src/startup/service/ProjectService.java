package startup.service;

import startup.domain.entities.Project;
import startup.exceptions.ProjectNotFoundException;
import startup.repository.implementations.ProjectRepository;
import startup.utils.ValidationUtils;

import java.util.List;
import java.util.Optional;

public class ProjectService {
    private ProjectRepository projectRepository;

    public ProjectService() {
        this.projectRepository =new ProjectRepository();
    }


    public Project save(Project project) {
        if (project == null) {
            throw new IllegalArgumentException("Cannot save null project.");
        }
        validateProject(project);
        return projectRepository.save(project);
    }


    public Optional<Project> findProjectById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Project ID must not be null.");
        }
        return projectRepository.findById(id);
    }

    public List<Project> findAllProjects() {
        return projectRepository.findAll();
    }


    public Project update(Project project) {
        if (project == null || project.getId() == null) {
            throw new IllegalArgumentException("Project or Project ID must not be null.");
        }
        validateProject(project);
        return projectRepository.update(project);
    }

    public boolean delete(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Project ID must not be null.");
        }
        return projectRepository.delete(id);
    }


    private void validateProject(Project project) {
        try {
            ValidationUtils.validateProjectName(project.getProjectName());
            ValidationUtils.validateSurface(project.getSurface());
            ValidationUtils.validateTotalCost(project.getTotalCost());
        } catch (IllegalArgumentException e) {
            throw new ProjectNotFoundException("Validation failed: " + e.getMessage());
        }
    }
}
