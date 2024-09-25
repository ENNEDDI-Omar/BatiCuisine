package startup.repository.implementations;

import startup.config.DatabaseConnection;
import startup.domain.entities.Client;
import startup.domain.entities.Project;
import startup.domain.enums.ProfitMarginType;
import startup.domain.enums.ProjectStatusType;
import startup.repository.interfaces.CrudInterface;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProjectRepository implements CrudInterface<Project> {
    private Connection connection;
    private ClientRepository clientRepository;

    public ProjectRepository() {
        this.connection = DatabaseConnection.getInstance().getConnection();
        this.clientRepository = new ClientRepository();
    }

    @Override
    public Project save(Project project) {
        String query = "INSERT INTO projects (project_name, surface, profit_margin, total_cost, project_status, client_id) VALUES (?, ?, ?::profit_margin_type, ?, ?::project_status_type, ?) RETURNING id";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, project.getProjectName());
            preparedStatement.setDouble(2, project.getSurface());
            preparedStatement.setString(3, project.getProfitMargin().name().toLowerCase());
            preparedStatement.setDouble(4, project.getTotalCost());
            preparedStatement.setString(5, project.getStatus().name().toLowerCase());
            preparedStatement.setLong(6, project.getClient().getId());

            try (ResultSet generatedKeys = preparedStatement.executeQuery()) {
                if (generatedKeys.next()) {
                    project.setId(generatedKeys.getLong(1));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error saving project: " + e.getMessage());
        }
        return project;
    }

    @Override
    public Optional<Project> findById(Long id) {
        String query = "SELECT * FROM projects WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Long projectId = resultSet.getLong("id");
                String projectName = resultSet.getString("project_name");
                double surface = resultSet.getDouble("surface");
                ProfitMarginType profitMargin = ProfitMarginType.valueOf(resultSet.getString("profit_margin").toUpperCase());
                double totalCost = resultSet.getDouble("total_cost");
                ProjectStatusType projectStatus = ProjectStatusType.valueOf(resultSet.getString("project_status").toUpperCase());
                Long clientId = resultSet.getLong("client_id");

                // Fetch the client from the client repository
                Client client = clientRepository.findById(clientId)
                        .orElseThrow(() -> new RuntimeException("Client not found for ID: " + clientId));

                Project project = new Project(projectId, projectName, surface, client);
                project.setProfitMargin(profitMargin);
                project.setTotalCost(totalCost);
                project.setStatus(projectStatus);
                return Optional.of(project);
            }
        } catch (SQLException e) {
            System.out.println("Error finding project by id: " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public List<Project> findAll() {
        List<Project> projects = new ArrayList<>();
        String query = "SELECT * FROM projects";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                Long projectId = resultSet.getLong("id");
                String projectName = resultSet.getString("project_name");
                double surface = resultSet.getDouble("surface");

                // Convert database value to uppercase before mapping to Java enum
                ProfitMarginType profitMargin = ProfitMarginType.valueOf(resultSet.getString("profit_margin").toUpperCase());
                double totalCost = resultSet.getDouble("total_cost");

                // Convert database value to uppercase before mapping to Java enum
                ProjectStatusType projectStatus = ProjectStatusType.valueOf(resultSet.getString("project_status").toUpperCase());
                Long clientId = resultSet.getLong("client_id");

                Client client = clientRepository.findById(clientId)
                        .orElseThrow(() -> new RuntimeException("Client not found for ID: " + clientId));

                Project project = new Project(projectId, projectName, surface, client);
                project.setProfitMargin(profitMargin);
                project.setTotalCost(totalCost);
                project.setStatus(projectStatus);
                projects.add(project);
            }
        } catch (SQLException e) {
            System.out.println("Error finding all projects: " + e.getMessage());
        }
        return projects;
    }


    @Override
    public Project update(Project project) {
        String sql = "UPDATE projects SET project_name = ?, surface = ?, profit_margin = ?::profit_margin_type, total_cost = ?, project_status = ?::project_status_type, client_id = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, project.getProjectName());
            preparedStatement.setDouble(2, project.getSurface());
            preparedStatement.setString(3, project.getProfitMargin().name().toLowerCase());
            preparedStatement.setDouble(4, project.getTotalCost());
            preparedStatement.setString(5, project.getStatus().name().toLowerCase());
            preparedStatement.setLong(6, project.getClient().getId());
            preparedStatement.setLong(7, project.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error updating project: " + e.getMessage());
        }
        return project;
    }

    @Override
    public boolean delete(Long id) {
        String query = "DELETE FROM projects WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, id);
            int result = preparedStatement.executeUpdate();
            return result == 1;
        } catch (SQLException e) {
            System.out.println("Error deleting project: " + e.getMessage());
        }
        return false;
    }
}
