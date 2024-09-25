package startup.repository.implementations;

import startup.config.DatabaseConnection;
import startup.domain.entities.Labor;
import startup.domain.entities.Project;
import startup.domain.enums.ComponentType;
import startup.domain.enums.LaborType;
import startup.domain.enums.ProductivityLevelType;
import startup.repository.interfaces.CrudInterface;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LaborRepository implements CrudInterface<Labor> {
    private Connection connection;

    public LaborRepository() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public Labor save(Labor labor) {
        String query = "INSERT INTO labor (project_id, name, tax, cost, transport_cost, type, work_hours, productivity_level) VALUES (?, ?, ?::tax_rate_type, ?, ?, ?::labor_type, ?, ?::productivity_level_type) RETURNING id";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, labor.getProject().getId());
            preparedStatement.setString(2, labor.getComponentName());
            preparedStatement.setString(3, labor.getTaxType().name());
            preparedStatement.setDouble(4, labor.getCost());
            preparedStatement.setDouble(5, labor.getTransportCost());
            preparedStatement.setString(6, labor.getLaborType().name());
            preparedStatement.setDouble(7, labor.getWorkHours());
            preparedStatement.setString(8, labor.getProductivityLevel().name());

            try (ResultSet generatedKeys = preparedStatement.executeQuery()) {
                if (generatedKeys.next()) {
                    labor.setId(generatedKeys.getLong(1));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error saving labor: " + e.getMessage());
        }
        return labor;
    }

    @Override
    public Optional<Labor> findById(Long id) {
        String query = "SELECT * FROM labor WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(mapToLabor(resultSet));
            }
        } catch (SQLException e) {
            System.out.println("Error finding labor by id: " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public List<Labor> findAll() {
        List<Labor> labors = new ArrayList<>();
        String query = "SELECT * FROM labor";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                labors.add(mapToLabor(resultSet));
            }
        } catch (SQLException e) {
            System.out.println("Error finding all labors: " + e.getMessage());
        }
        return labors;
    }

    @Override
    public Labor update(Labor labor) {
        String sql = "UPDATE labor SET name = ?, tax = ?::tax_rate_type, cost = ?, transport_cost = ?, type = ?::labor_type, work_hours = ?, productivity_level = ?::productivity_level_type WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, labor.getComponentName());
            preparedStatement.setString(2, labor.getTaxType().name());
            preparedStatement.setDouble(3, labor.getCost());
            preparedStatement.setDouble(4, labor.getTransportCost());
            preparedStatement.setString(5, labor.getLaborType().name());
            preparedStatement.setDouble(6, labor.getWorkHours());
            preparedStatement.setString(7, labor.getProductivityLevel().name());
            preparedStatement.setLong(8, labor.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error updating labor: " + e.getMessage());
        }
        return labor;
    }

    @Override
    public boolean delete(Long id) {
        String query = "DELETE FROM labor WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, id);
            int result = preparedStatement.executeUpdate();
            return result == 1;
        } catch (SQLException e) {
            System.out.println("Error deleting labor: " + e.getMessage());
        }
        return false;
    }

    private Labor mapToLabor(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getLong("id");
        String name = resultSet.getString("name");
        LaborType type = LaborType.valueOf(resultSet.getString("type"));
        double workHours = resultSet.getDouble("work_hours");
        ProductivityLevelType productivityLevel = ProductivityLevelType.valueOf(resultSet.getString("productivity_level"));
        double transportCost = resultSet.getDouble("transport_cost");
        double cost = resultSet.getDouble("cost");
        Long projectId = resultSet.getLong("project_id");


        ProjectRepository projectRepository = new ProjectRepository(new ClientRepository());
        Project project = projectRepository.findById(projectId).orElse(null);

        Labor labor = new Labor(id, ComponentType.LABOR, name, transportCost, type, workHours, productivityLevel, project);
        labor.setCost(cost);
        return labor;
    }
}
