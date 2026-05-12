package app.persistence;

import app.entities.Carport;
import app.entities.Quote;
import app.entities.Roof;
import app.entities.Shed;
import app.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class QuoteMapper {
    private ConnectionPool connectionPool;

    public QuoteMapper() {
        this.connectionPool = ConnectionPool.getInstance();
    }
    public void createQuote(int quotePrice, int carportId, int customerId, int salesRepId ) throws DatabaseException {

        String sql = "insert into quotes (quote_price, carport_id, customer_id, sales_rep_id) values (?,?,?,?)";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, quotePrice);
            preparedStatement.setInt(2, carportId);
            preparedStatement.setInt(3, customerId);
            preparedStatement.setInt(4, salesRepId);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected != 1) {
                throw new DatabaseException("An error occurred trying to insert values in the quotes table");
            }
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
            if (resultSet.next()){
                int quotePrice = resultSet.getInt("quote_price");
                int carportId = resultSet.getInt("carport_id");
                int customerId = resultSet.getInt("customer_id");
                int salesRepId = resultSet.getInt("sales_rep_id");

                return new Quote(quoteId,quotePrice,carportId,customerId,salesRepId);
            } else
            {
                throw new DatabaseException("An error occurred, when trying to get quote by provided id: " + quoteId);
            }
        } catch (SQLException e) {
            String message = "Fejl ved at hente tilbud";
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
        }catch (SQLException e) {
            String errorMessage = "Fejl ved forsøg på at fjerne tilbud";
            throw new DatabaseException(errorMessage, e.getMessage());
        }
    }
}
