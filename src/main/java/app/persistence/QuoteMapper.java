package app.persistence;

import app.entities.*;
import app.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class QuoteMapper {
    private ConnectionPool connectionPool;

    public QuoteMapper() {
        this.connectionPool = ConnectionPool.getInstance();
    }

    public int createQuote(Quote quote) throws DatabaseException {

        String sql = "insert into quotes (quote_price, carport_id, customer_id, sales_rep_id) values (?,?,?,?)";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setDouble(1, quote.getQuotePrice());
            preparedStatement.setInt(2, quote.getCarportId());
            preparedStatement.setInt(3, quote.getCustomerId());
            preparedStatement.setInt(4, quote.getSalesRepId());
            int quoteId = 0;
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected != 1) {
                throw new DatabaseException("An error occurred trying to insert values in the quotes table");
            }
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                quoteId = resultSet.getInt("quote_id");
            }
            return quoteId;
        } catch (SQLException e) {
            String errorMessage = "Fejl ved oprettelse af tilbud, prøv igen";
            throw new DatabaseException(errorMessage, e.getMessage());
        }
    }

    public Quote getQuoteById(int quoteId) throws DatabaseException {

        String sql = "select quote_price, carport_id, customer_id, sales_rep_id from quotes where carport_id = ?";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, quoteId);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int quotePrice = resultSet.getInt("quote_price");
                int carportId = resultSet.getInt("carport_id");
                int customerId = resultSet.getInt("customer_id");
                int salesRepId = resultSet.getInt("sales_rep_id");

                return new Quote(quoteId, quotePrice, carportId, customerId, salesRepId);
            } else {
                throw new DatabaseException("An error occurred, when trying to get quote by provided id: " + quoteId);
            }
        } catch (SQLException e) {
            String message = "Fejl ved at hente tilbud";
            throw new DatabaseException(message, e.getMessage());
        }
    }

    public List<Quote> getAllQuotes() throws DatabaseException {
        List<Quote> quotes = new ArrayList<>();
        String sql = """
                select quote.quote_id, quote.quote_price, carport.carport_id, customer.customer_id, sales.sales_rep_id from quotes quote\s
                left join carports carport using(carport_id)\s
                left join customers customer using(customer_id)\s
                left join sales_reps sales using(sales_rep_id)
                """;

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int quoteId = resultSet.getInt("quote_id");
                double quotePrice = resultSet.getInt("quote_price");
                int carportId = resultSet.getInt("carport_id");
                int customerId = resultSet.getInt("customer_id");
                int salesRepId = resultSet.getInt("sales_rep_id");

                quotes.add(new Quote(quoteId, quotePrice, carportId, customerId, salesRepId));
            }
            return quotes;
        } catch (SQLException e) {
            String message = "Fejl ved at hente alle tilbud";
            throw new DatabaseException(message, e.getMessage());
        }
    }

    public List<Quote> getAllQuotesByCustomerId(int customerId) throws DatabaseException {
        List<Quote> quotes = new ArrayList<>();
        String sql = """
                select quote.quote_id, quote.quote_price, carport.carport_id, customer.customer_id, sales.sales_rep_id from quotes quote\s
                left join carports carport using(carport_id)\s
                left join customers customer using(customer_id)\s
                left join sales_reps sales using(sales_rep_id) where customer_id = ?
                """;
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, customerId);

            ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            int quoteId = resultSet.getInt("quote_id");
            double quotePrice = resultSet.getInt("quote_price");
            int carportId = resultSet.getInt("carport_id");
            int salesRepId = resultSet.getInt("sales_rep_id");

            quotes.add(new Quote(quoteId, quotePrice, carportId, customerId, salesRepId));
        }
        return quotes;
        } catch (SQLException e) {
            String message = "Fejl ved at hente alle kundens tilbud";
            throw new DatabaseException(message, e.getMessage());
        }
    }

    public void deleteQuoteById(int quoteId) throws DatabaseException {
        String sql = "delete from quotes where quote_id = ?";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, quoteId);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected != 1) {
                throw new DatabaseException("An error occurred trying to remove a quote from DB");
            }
        } catch (SQLException e) {
            String errorMessage = "Fejl ved forsøg på at fjerne tilbud";
            throw new DatabaseException(errorMessage, e.getMessage());
        }
    }
}
