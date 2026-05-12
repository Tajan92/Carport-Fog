package app.persistence;

import app.entities.*;
import app.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerMapper {

    private ConnectionPool connectionPool;

    public CustomerMapper(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    public void createCustomer(String firstName, String lastName, String email, String password, String phoneNumber, String address, String zipCode) throws DatabaseException {

        String sql = "insert into customers(email,password,first_name,last_name,address,zip_code,phone_number,city) values (?,?,?,?,?,?,?)";
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, firstName);
            preparedStatement.setString(4, lastName);
            preparedStatement.setString(5, address);
            preparedStatement.setString(6, zipCode);
            preparedStatement.setString(7, phoneNumber);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected != 1) {
                throw new DatabaseException("An error occurred trying to insert values in the customers table");
            }
        } catch (SQLException e) {
            String errorMessage = "Fejl ved oprettelse af bruger, prøv at genindlæs siden eller kontakt os, hvis problemet stadig er der";
            throw new DatabaseException(errorMessage, e.getMessage());
        }
    }


    public Customer getCustomerById(int customerId) throws DatabaseException {

        String sql = "select c.email,c.password,c.first_name,c.last_name,c.address,c.zip_code,c.phone_number,z.city" +
                " from customers c" +
                " join zip_codes z using (zip_code) " +
                " where customer_id = ?";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, customerId);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String email = resultSet.getString("email");
                String password = resultSet.getString("password");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String address = resultSet.getString("address");
                String zipCode = resultSet.getString("zip_code");
                String phoneNumber = resultSet.getString("phone_number");
                String city = resultSet.getString("city");

                return new Customer(customerId, email, password, firstName, lastName, address, zipCode, phoneNumber, city);
            } else {
                throw new DatabaseException("An error occurred, when trying to get customer by provided id: " + customerId);
            }
        } catch (SQLException e) {
            String message = "Fejl ved at hente customer";
            throw new DatabaseException(message, e.getMessage());
        }
    }
}

