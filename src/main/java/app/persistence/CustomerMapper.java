package app.persistence;

import app.entities.*;
import app.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerMapper {

    private ConnectionPool connectionPool;

    public CustomerMapper() {
        this.connectionPool = ConnectionPool.getInstance();
    }

    public int createCustomer(Customer customer) throws DatabaseException {

        String sql = "insert into customers(email,password,first_name,last_name,address,zip_code,phone_number) values (?,?,?,?,?,?,?)";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, customer.getEmail());
            preparedStatement.setString(2, customer.getPassword());
            preparedStatement.setString(3, customer.getFirstName());
            preparedStatement.setString(4, customer.getLastName());
            preparedStatement.setString(5, customer.getAddress());
            preparedStatement.setString(6, customer.getZipCode());
            preparedStatement.setString(7, customer.getPhoneNumber());

            int customerId = 0;
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected != 1) {
                throw new DatabaseException("An error occurred trying to insert values in the customers table");
            }
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                customerId = resultSet.getInt("customer_id");
            }
            return customerId;
        } catch (SQLException e) {
            String errorMessage = "Fejl ved oprettelse af bruger, prøv at genindlæs siden eller kontakt os, hvis problemet stadig er der";
            throw new DatabaseException(errorMessage, e.getMessage());
        }
    }

    public Customer getCustomerById(int customerId) throws DatabaseException {

        String sql = "select c.email,c.password,c.first_name,c.last_name,c.address,c.zip_code,c.phone_number,z.town" +
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
                String town = resultSet.getString("town");

                return new Customer(customerId,firstName, lastName, email, password, phoneNumber, address, zipCode, town);
            } else {
                throw new DatabaseException("An error occurred, when trying to get customer by provided id: " + customerId);
            }
        } catch (SQLException e) {
            String message = "Fejl ved at hente customer";
            throw new DatabaseException(message, e.getMessage());
        }
    }

    public List<Customer> getAllCustomers() throws DatabaseException {
        List<Customer> customers = new ArrayList<>();
        String sql = "select * from customers join zip_codes using (zip_code)";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int userId = resultSet.getInt("customer_id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String email = resultSet.getString("email");
                String password = resultSet.getString("password");
                String phoneNumber = resultSet.getString("phone_number");
                String address = resultSet.getString("address");
                String zipCode = resultSet.getString("zip_code");
                String town = resultSet.getString("town");

                customers.add(new Customer(userId, firstName, lastName, email, password, phoneNumber, address, zipCode, town));
            }
            return customers;
        } catch (SQLException e) {
            String message = "Fejl ved at hente alle brugere";
            throw new DatabaseException(message, e.getMessage());
        }
    }
}

