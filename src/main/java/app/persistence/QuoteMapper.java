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

        String sql = "insert into quotes (quote_price, carport_id, customer_id, sales_rep_id, quote_discount, is_payed) values (?,?,?,?,?,?)";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setDouble(1, quote.getQuotePrice());
            preparedStatement.setInt(2, quote.getCarportId());
            preparedStatement.setInt(3, quote.getCustomerId());
            preparedStatement.setInt(4, quote.getSalesRepId());
            preparedStatement.setDouble(5, quote.getQuoteDiscount());
            preparedStatement.setBoolean(6, false);

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

        String sql = "select quote_price, carport_id, customer_id, sales_rep_id, quote_discount, is_payed from quotes where quote_id = ?";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, quoteId);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int quotePrice = resultSet.getInt("quote_price");
                int carportId = resultSet.getInt("carport_id");
                int customerId = resultSet.getInt("customer_id");
                int salesRepId = resultSet.getInt("sales_rep_id");
                int quoteDiscount = resultSet.getInt("quote_discount");
                boolean isPayed = resultSet.getBoolean("is_payed");

                return new Quote(quoteId, quotePrice, carportId, customerId, salesRepId, quoteDiscount, isPayed);
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
                select quote.quote_id, quote.quote_price, quote.quote_discount, quote.is_payed, carport.carport_id, customer.customer_id, sales.sales_rep_id from quotes quote\s
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
                double quoteDiscount = resultSet.getDouble("quote_discount");
                boolean isPayed = resultSet.getBoolean("is_payed");

                quotes.add(new Quote(quoteId, quotePrice, carportId, customerId, salesRepId, quoteDiscount, isPayed));
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
        select quote.quote_id, quote.quote_price, quote.quote_discount, quote.is_payed, carport.carport_id, customer.customer_id, sales.sales_rep_id 
        from quotes quote
        left join carports carport using(carport_id)
        left join customers customer using(customer_id)
        left join sales_reps sales using(sales_rep_id) 
        where customer_id = ?
        order by quote.quote_id asc;
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
            double quoteDiscount = resultSet.getDouble("quote_discount");
            boolean isPayed = resultSet.getBoolean("is_payed");

            quotes.add(new Quote(quoteId, quotePrice, carportId, customerId, salesRepId, quoteDiscount, isPayed));
        }
        return quotes;
        } catch (SQLException e) {
            String message = "Fejl ved at hente alle kundens tilbud";
            throw new DatabaseException(message, e.getMessage());
        }
    }

    public void updateQuoteToPayed(int quoteId) throws SQLException, DatabaseException {
        String sql = "update quotes set is_payed = true where quote_id = ?";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, quoteId);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected != 1) {
                throw new DatabaseException("Could not update quote");
            }
        } catch (SQLException | DatabaseException e) {
            String errorMessage = "Fejl ved opdatering af tilbud, prøv at genindlæs siden eller kontakt os, hvis problemet stadig er der";
            throw new DatabaseException(errorMessage, e.getMessage());
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
