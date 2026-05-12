package app.persistence;
import app.entities.*;
import app.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderMapper {
    //create, read

    private ConnectionPool connectionPool;

    public OrderMapper() {
        this.connectionPool = ConnectionPool.getInstance();
    }

    public void createOrder(Order order) throws DatabaseException {

        String sql = "insert into orders (customer_id, sales_rep_id, carport_id, order_price) values (?,?,?,?)";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, order.getCustomerId());
            preparedStatement.setInt(2, order.getSalesRepId());
            preparedStatement.setInt(3, order.getCarportId());
            preparedStatement.setDouble(4, order.getOrderPrice());

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected != 1) {
                throw new DatabaseException("An error occurred trying to create an order in the database");
            }
        } catch (SQLException e) {
            String errorMessage = "Fejl ved oprettelse af ordre, prøv at genindlæs siden eller kontakt os, hvis problemet stadig er der";
            throw new DatabaseException(errorMessage, e.getMessage());
        }
    }


    public Order getOrderById(int orderId) throws DatabaseException {

        String sql = "select o.*, c.parts_list_id from orders o join carports c using (carport_id) where order_id = ?";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, orderId);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                //Order
                int customer_id = resultSet.getInt("customer_id");
                int salesRepId = resultSet.getInt("sales_rep_id");
                int carportId = resultSet.getInt("carport_id");
                double orderPrice = resultSet.getDouble("order_price");
                int partsListId = resultSet.getInt("parts_list_id");

                //Carport


                //Customer

                //SalesRep
                return new Order(orderId, customer_id, salesRepId, carportId, orderPrice, partsListId);
            } else
            {
                throw new DatabaseException("An error occurred, when trying to get order by provided id: " + orderId);
            }
        } catch (SQLException e) {
            String message = "Fejl ved at hente carport";
            throw new DatabaseException(message, e.getMessage());
        }
    }


}
