package startup.repository.implementations;

import startup.config.DatabaseConnection;
import startup.domain.entities.Material;
import startup.domain.entities.Project;
import startup.domain.enums.ComponentType;
import startup.domain.enums.QualityCoefficientType;
import startup.domain.enums.RateTaxType;
import startup.repository.interfaces.CrudInterface;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
        String query = "INSERT INTO materials (project_id, name, tax, cost, transport_cost, unit_price, quantity, quality_coefficient, component_type) VALUES (?, ?, ?::tax_rate_type, ?, ?, ?, ?, ?::quality_coefficient_type, ?::component_type) RETURNING id";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, material.getProject().getId());
            preparedStatement.setString(2, material.getComponentName());
            preparedStatement.setString(3, material.getTaxType().name());
            preparedStatement.setDouble(4, material.getCost());
            preparedStatement.setDouble(5, material.getTransportCost());
            preparedStatement.setDouble(6, material.getUnitPrice());
            preparedStatement.setDouble(7, material.getQuantity());
            preparedStatement.setString(8, material.getQualityCoefficient().name());
            preparedStatement.setString(9, material.getComponentType().name());

            try (ResultSet generatedKeys = preparedStatement.executeQuery()) {
                if (generatedKeys.next()) {
                    Long id = generatedKeys.getLong(1);
                    material.setId(id);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error saving material: " + e.getMessage());
        }
        return material;
    }

    @Override
    public Optional<Material> findById(Long id) {
        String query = "SELECT m.*, c.component_type FROM materials m JOIN components c ON m.id = c.id WHERE m.id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(mapToMaterial(resultSet));
            }
        } catch (SQLException e) {
            System.out.println("Error finding material by id: " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public List<Material> findAll() {
        List<Material> materials = new ArrayList<>();
        String query = "SELECT m.*, c.component_type FROM materials m JOIN components c ON m.id = c.id";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                materials.add(mapToMaterial(resultSet));
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
            preparedStatement.setString(1, material.getComponentName());
            preparedStatement.setString(2, material.getTaxType().name());
            preparedStatement.setDouble(3, material.getCost());
            preparedStatement.setDouble(4, material.getTransportCost());
            preparedStatement.setDouble(5, material.getUnitPrice());
            preparedStatement.setDouble(6, material.getQuantity());
            preparedStatement.setString(7, material.getQualityCoefficient().name());
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

    private Material mapToMaterial(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getLong("id");
        String name = resultSet.getString("name");
        double unitPrice = resultSet.getDouble("unit_price");
        double quantity = resultSet.getDouble("quantity");
        QualityCoefficientType qualityCoefficient = QualityCoefficientType.valueOf(resultSet.getString("quality_coefficient"));
        RateTaxType rateTaxType = RateTaxType.valueOf(resultSet.getString("tax"));
        double transportCost = resultSet.getDouble("transport_cost");
        double cost = resultSet.getDouble("cost");
        ComponentType componentType = ComponentType.valueOf(resultSet.getString("component_type"));
        Long projectId = resultSet.getLong("project_id");


        // Autres champs du projet...
        ProjectRepository projectRepository = new ProjectRepository(new ClientRepository());
        Project project = projectRepository.findById(projectId).orElse(null);

        // Assumption: project must be fetched or set elsewhere if needed
        Material material = new Material(id, componentType, name, transportCost, unitPrice, quantity, qualityCoefficient, project);
        material.setTaxType(rateTaxType);
        material.setCost(cost);
        return material;
    }

}
