package startup.repository.implementations;

import startup.config.DatabaseConnection;
import startup.domain.entities.Labor;
import startup.domain.entities.Project;
import startup.domain.enums.ComponentType;
import startup.domain.enums.LaborType;
import startup.domain.enums.ProductivityLevelType;
import startup.domain.enums.RateTaxType;
import startup.repository.interfaces.CrudInterface;

import java.sql.*;
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
        String query = "INSERT INTO labors (project_id, name, tax, cost, transport_cost, type, work_hours, productivity_level) " +
                "VALUES (?, ?, ?::tax_rate_type, ?, ?, ?::labor_type, ?, ?::productivity_level_type) RETURNING id";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            setStatementParameters(preparedStatement, labor);
            try (ResultSet generatedKeys = preparedStatement.executeQuery()) {
                if (generatedKeys.next()) {
                    labor.setId(generatedKeys.getLong(1));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error saving labor: " + e.getMessage());
            e.printStackTrace();
        }
        return labor;
    }

    @Override
    public Optional<Labor> findById(Long id) {
        String query = "SELECT * FROM labors WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(createLaborFromResultSet(resultSet));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error finding labor by id: " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public List<Labor> findAll() {
        List<Labor> labors = new ArrayList<>();
        String query = "SELECT * FROM labors";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                labors.add(createLaborFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            System.out.println("Error finding all labors: " + e.getMessage());
        }
        return labors;
    }

    @Override
    public Labor update(Labor labor) {
        String sql = "UPDATE labors SET name = ?, tax = ?::tax_rate_type, cost = ?, transport_cost = ?, type = ?::labor_type, work_hours = ?, productivity_level = ?::productivity_level_type WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            setStatementParameters(preparedStatement, labor);
            preparedStatement.setLong(8, labor.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error updating labor: " + e.getMessage());
        }
        return labor;
    }

    @Override
    public boolean delete(Long id) {
        String query = "DELETE FROM labors WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, id);
            int result = preparedStatement.executeUpdate();
            return result == 1;
        } catch (SQLException e) {
            System.out.println("Error deleting labor: " + e.getMessage());
        }
        return false;
    }

    private void setStatementParameters(PreparedStatement preparedStatement, Labor labor) throws SQLException {
        preparedStatement.setLong(1, labor.getProject() != null ? labor.getProject().getId() : 0);
        preparedStatement.setString(2, labor.getComponentName());
        preparedStatement.setString(3, labor.getTaxType().name().toLowerCase());
        preparedStatement.setDouble(4, labor.getCost());
        preparedStatement.setDouble(5, labor.getTransportCost());
        preparedStatement.setString(6, labor.getLaborType().name().toLowerCase());
        preparedStatement.setDouble(7, labor.getWorkHours());
        preparedStatement.setString(8, labor.getProductivityLevel().name().toLowerCase());
    }

    private Labor createLaborFromResultSet(ResultSet resultSet) throws SQLException {
        Long laborId = resultSet.getLong("id");
        String name = resultSet.getString("name");
        RateTaxType rateTaxType = RateTaxType.valueOf(resultSet.getString("tax").toUpperCase());
        double cost = resultSet.getDouble("cost");
        double transportCost = resultSet.getDouble("transport_cost");
        LaborType laborType = LaborType.valueOf(resultSet.getString("labor_type").toUpperCase());
        double workHours = resultSet.getDouble("work_hours");
        ProductivityLevelType productivityLevel = ProductivityLevelType.valueOf(resultSet.getString("productivity_level").toUpperCase());
        Long projectId = resultSet.getLong("project_id");

        ProjectRepository projectRepository = new ProjectRepository();
        Project project = projectRepository.findById(projectId).orElse(null);

        Labor labor = new Labor(laborId, name, transportCost, laborType, workHours, productivityLevel, project);
        labor.setTaxType(rateTaxType);
        labor.setCost(cost);
        return labor;
    }
}