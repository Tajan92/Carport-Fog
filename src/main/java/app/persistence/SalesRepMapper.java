package app.persistence;

import app.entities.Customer;
import app.entities.SalesRep;
import app.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SalesRepMapper {

    private ConnectionPool connectionPool;

    public SalesRepMapper(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    public SalesRep getSalesRepById(int salesRepId) throws DatabaseException {

        String sql = "select email,password,first_name,last_name,phone_number from sales_rep where sales_rep_id = ?";

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
}
