package app.persistence;

import app.entities.Customer;
import app.entities.SalesRep;
import app.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SalesRepMapper {

    private ConnectionPool connectionPool;

    public SalesRepMapper() {
        this.connectionPool = ConnectionPool.getInstance();
    }

    public SalesRep getSalesRepById(int salesRepId) throws DatabaseException {

        String sql = "select email,password,first_name,last_name,phone_number from sales_reps where sales_rep_id = ?";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, salesRepId);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String email = resultSet.getString("email");
                String password = resultSet.getString("password");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String phoneNumber = resultSet.getString("phone_number");

                return new SalesRep(salesRepId, email, password, firstName, lastName, phoneNumber);
            } else {
                throw new DatabaseException("An error occurred, when trying to get sales rep by provided id: " + salesRepId);
            }
        } catch (SQLException e) {
            String message = "Fejl ved at hente salesRep";
            throw new DatabaseException(message, e.getMessage());
        }
    }

    public List<SalesRep> getAllSalesReps() throws DatabaseException {
        List<SalesRep> salesReps = new ArrayList<>();
        String sql = "select * from sales_reps";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int salesRepId= resultSet.getInt("sales_rep_id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String email = resultSet.getString("email");
                String password = resultSet.getString("password");
                String phoneNumber = resultSet.getString("phone_number");

                salesReps.add(new SalesRep(salesRepId, firstName, lastName, email, password, phoneNumber));
            }
            return salesReps;
        } catch (SQLException e) {
            String message = "Fejl ved at hente alle sales reps";
            throw new DatabaseException(message, e.getMessage());
        }
    }
}
