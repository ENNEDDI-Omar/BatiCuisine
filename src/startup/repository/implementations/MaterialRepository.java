package startup.repository.implementations;

import startup.config.DatabaseConnection;
import startup.domain.entities.Material;
import startup.domain.entities.Project;
import startup.domain.enums.ComponentType;
import startup.domain.enums.QualityCoefficientType;
import startup.domain.enums.RateTaxType;
import startup.repository.interfaces.CrudInterface;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MaterialRepository implements CrudInterface<Material> {
    private Connection connection;

    public MaterialRepository() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public Material save(Material material) {
        String query = "INSERT INTO materials (project_id, name, tax, cost, transport_cost, unit_price, quantity, quality_coefficient) VALUES (?, ?, ?::tax_rate_type, ?, ?, ?, ?, ?::quality_coefficient_type) RETURNING id";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            setStatementParameters(preparedStatement, material);
            try (ResultSet generatedKeys = preparedStatement.executeQuery()) {
                if (generatedKeys.next()) {
                    material.setId(generatedKeys.getLong(1));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error saving material: " + e.getMessage());
            e.printStackTrace();
        }
        return material;
    }

    @Override
    public Optional<Material> findById(Long id) {
        String query = "SELECT * FROM materials WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(createMaterialFromResultSet(resultSet));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error finding material by id: " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public List<Material> findAll() {
        List<Material> materials = new ArrayList<>();
        String query = "SELECT * FROM materials";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                materials.add(createMaterialFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            System.out.println("Error finding all materials: " + e.getMessage());
        }
        return materials;
    }

    @Override
    public Material update(Material material) {
        String sql = "UPDATE materials SET name = ?, tax = ?::tax_rate_type, cost = ?, transport_cost = ?, unit_price = ?, quantity = ?, quality_coefficient = ?::quality_coefficient_type WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            setStatementParameters(preparedStatement, material);
            preparedStatement.setLong(8, material.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error updating material: " + e.getMessage());
        }
        return material;
    }

    @Override
    public boolean delete(Long id) {
        String query = "DELETE FROM materials WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, id);
            int result = preparedStatement.executeUpdate();
            return result == 1;
        } catch (SQLException e) {
            System.out.println("Error deleting material: " + e.getMessage());
        }
        return false;
    }

    private void setStatementParameters(PreparedStatement preparedStatement, Material material) throws SQLException {
        if (material.getProject() != null && material.getProject().getId() != null) {
            preparedStatement.setLong(1, material.getProject().getId());
        } else {
            preparedStatement.setNull(1, Types.BIGINT);
        }
        preparedStatement.setString(2, material.getComponentName());
        preparedStatement.setString(3, RateTaxType.MATERIAL_TAX_ONLY.name().toLowerCase());
        preparedStatement.setDouble(4, material.getCost());
        preparedStatement.setDouble(5, material.getTransportCost());
        preparedStatement.setDouble(6, material.getUnitPrice());
        preparedStatement.setDouble(7, material.getQuantity());
        preparedStatement.setString(8, material.getQualityCoefficient().name().toLowerCase());
    }

    private Material createMaterialFromResultSet(ResultSet resultSet) throws SQLException {
        Long materialId = resultSet.getLong("id");
        String name = resultSet.getString("name");
        double unitPrice = resultSet.getDouble("unit_price");
        double quantity = resultSet.getDouble("quantity");
        QualityCoefficientType qualityCoefficient = QualityCoefficientType.valueOf(resultSet.getString("quality_coefficient").toUpperCase());
        double transportCost = resultSet.getDouble("transport_cost");
        Long projectId = resultSet.getLong("project_id");

        ProjectRepository projectRepository = new ProjectRepository();
        Project project = projectRepository.findById(projectId).orElse(null);

        return new Material(materialId, name, transportCost, unitPrice, quantity, qualityCoefficient, project);
    }
}