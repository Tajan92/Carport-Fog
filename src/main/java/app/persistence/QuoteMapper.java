package app.persistence;

import app.entities.Carport;
import app.entities.Roof;
import app.entities.Shed;
import app.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class QuoteMapper {
    //create, read, delete
    private ConnectionPool connectionPool;

    public QuoteMapper() {
        this.connectionPool = ConnectionPool.getInstance();
    }
    public void createProductPartsList(int quotePrice, int carportId, int customerId, int salesRepId ) throws DatabaseException {

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

        public Carport getCarportById(int carportId, PartsList partsList, Shed shed, Roof roof) throws DatabaseException {

            String sql = "select carport_width, carport_height, carport_length, price from carports where carport_id = ?";

            try (Connection connection = connectionPool.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

                preparedStatement.setInt(1, carportId);

                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()){
                    double width = resultSet.getDouble("carport_width");
                    double height = resultSet.getDouble("carport_height");
                    double length = resultSet.getDouble("carport_length");
                    double price = resultSet.getDouble("price");

                    return new Carport(carportId,width,height,length,price, partsList, shed, roof);
                } else
                {
                    throw new DatabaseException("An error occurred, when trying to get carport by provided id: " + carportId);
                }
            } catch (SQLException e) {
                String message = "Fejl ved at hente carport";
                throw new DatabaseException(message, e.getMessage());
            }
        }
    }
}
