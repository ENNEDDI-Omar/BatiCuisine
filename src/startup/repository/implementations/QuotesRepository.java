package startup.repository.implementations;

import startup.config.DatabaseConnection;
import startup.domain.entities.Quotes;
import startup.domain.enums.QuotesStatusType;
import startup.repository.interfaces.QuotesInterface;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class QuotesRepository implements QuotesInterface {
    private Connection connection;

    public QuotesRepository() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public Quotes save(Quotes quote) {
        String query = "INSERT INTO quotes (project_id, estimated_amount, issue_date, expiration_date, quote_status) VALUES (?, ?, ?, ?, ?::quote_status_type) RETURNING id";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, quote.getProject().getId());
            preparedStatement.setDouble(2, quote.getEstimatedAmount());
            preparedStatement.setDate(3, java.sql.Date.valueOf(quote.getIssueDate()));
            preparedStatement.setDate(4, java.sql.Date.valueOf(quote.getExpirationDate()));
            preparedStatement.setString(5, quote.getStatus().name().toLowerCase());

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                quote.setId(resultSet.getLong(1));
            }
        } catch (SQLException e) {
            System.out.println("Error saving quote: " + e.getMessage());
        }
        return quote;
    }


    @Override
    public Optional<Quotes> findById(Long id) {
        String query = "SELECT * FROM quotes WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Quotes quote = new Quotes();
                quote.setId(resultSet.getLong("id"));
                quote.setEstimatedAmount(resultSet.getDouble("estimated_amount"));
                quote.setIssueDate(resultSet.getDate("issue_date").toLocalDate());
                quote.setExpirationDate(resultSet.getDate("expiration_date").toLocalDate());
                quote.setStatus(QuotesStatusType.valueOf(resultSet.getString("quote_status").toUpperCase()));
                return Optional.of(quote);
            }
        } catch (SQLException e) {
            System.out.println("Error finding quote by id: " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public List<Quotes> findAll() {
        List<Quotes> quotes = new ArrayList<>();
        String query = "SELECT * FROM quotes";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                Quotes quote = new Quotes();
                quote.setId(resultSet.getLong("id"));
                quote.setEstimatedAmount(resultSet.getDouble("estimated_amount"));
                quote.setIssueDate(resultSet.getDate("issue_date").toLocalDate());
                quote.setExpirationDate(resultSet.getDate("expiration_date").toLocalDate());
                quote.setStatus(QuotesStatusType.valueOf(resultSet.getString("quote_status")));
                quotes.add(quote);
            }
        } catch (SQLException e) {
            System.out.println("Error finding all quotes: " + e.getMessage());
        }
        return quotes;
    }

    @Override
    public Quotes update(Quotes quote) {
        String sql = "UPDATE quotes SET project_id = ?, estimated_amount = ?, issue_date = ?, expiration_date = ?, quote_status = ?::quote_status_type WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, quote.getProject().getId());
            preparedStatement.setDouble(2, quote.getEstimatedAmount());
            preparedStatement.setDate(3, java.sql.Date.valueOf(quote.getIssueDate()));
            preparedStatement.setDate(4, java.sql.Date.valueOf(quote.getExpirationDate()));
            preparedStatement.setString(5, quote.getStatus().name());
            preparedStatement.setLong(6, quote.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e)
        {
            System.out.println("Error updating quote: " + e.getMessage());
        }
        return quote;
    }

    @Override
    public boolean delete(Long id) {
        String query = "DELETE FROM quotes WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, id);
            int result = preparedStatement.executeUpdate();
            return result == 1;
        } catch (SQLException e) {
            System.out.println("Error deleting quote: " + e.getMessage());
        }
        return false;
    }
}
