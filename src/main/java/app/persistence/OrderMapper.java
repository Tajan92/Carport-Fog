package app.persistence;
import app.entities.*;
import app.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderMapper {
    //create, read

    private ConnectionPool connectionPool;

    public OrderMapper() {
        this.connectionPool = ConnectionPool.getInstance();
    }

    public int createOrder(Order order) throws DatabaseException {

        String sql = "insert into orders (customer_id, sales_rep_id, carport_id, order_price) values (?,?,?,?)";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1, order.getCustomerId());
            preparedStatement.setInt(2, order.getSalesRepId());
            preparedStatement.setInt(3, order.getCarportId());
            preparedStatement.setDouble(4, order.getOrderPrice());

            int rowsAffected = preparedStatement.executeUpdate();
            int orderId = 0;
            if (rowsAffected != 1) {
                throw new DatabaseException("An error occurred trying to create an order in the database");
            }
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                orderId = resultSet.getInt("order_id");
            }
            return orderId;
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
                int customer_id = resultSet.getInt("customer_id");
                int salesRepId = resultSet.getInt("sales_rep_id");
                int carportId = resultSet.getInt("carport_id");
                double orderPrice = resultSet.getDouble("order_price");
                int partsListId = resultSet.getInt("parts_list_id");

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

    public List<Order> getAllOrders() throws DatabaseException {
        List<Order> orders = new ArrayList<>();
        String sql = """
                select o.order_id, o.customer_id, o.sales_rep_id, o.carport_id, o.order_price, pl.parts_list_id from orders o
                left join customers using(customer_id)
                left join sales_reps using(sales_rep_id)
                left join carports using(carport_id)
                left join parts_lists pl using (parts_list_id)
                """;

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int orderId = resultSet.getInt("order_id");
                int customerId = resultSet.getInt("customer_id");
                int salesRepId = resultSet.getInt("sales_rep_id");
                int carportId = resultSet.getInt("carport_id");
                double orderPrice = resultSet.getDouble("order_price");
                int partsListId = resultSet.getInt("parts_list_id");

                orders.add(new Order(orderId, customerId, salesRepId, carportId, orderPrice, partsListId));
            }
            return orders;
        } catch (SQLException e) {
            String message = "Fejl ved at hente alle ordrer";
            throw new DatabaseException(message, e.getMessage());
        }
    }

    public List<Order> getAllOrdersByCustomerId(int customerId) throws DatabaseException {
        List<Order> orders = new ArrayList<>();
        String sql = """
                select o.order_id, o.customer_id, o.sales_rep_id, o.carport_id, o.order_price, pl.parts_list_id from orders o
                left join customers using(customer_id)
                left join sales_reps using(sales_rep_id)
                left join carports using(carport_id)
                left join parts_lists pl using (parts_list_id)
                where customer_id = ?
                """;

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, customerId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int orderId = resultSet.getInt("order_id");
                int salesRepId = resultSet.getInt("sales_rep_id");
                int carportId = resultSet.getInt("carport_id");
                double orderPrice = resultSet.getDouble("order_price");
                int partsListId = resultSet.getInt("parts_list_id");

                orders.add(new Order(orderId, customerId, salesRepId, carportId, orderPrice, partsListId));
            }
            return orders;
        } catch (SQLException e) {
            String message = "Fejl ved at hente alle ordrer for bruger id:" + customerId;
            throw new DatabaseException(message, e.getMessage());
        }
    }

    public void updateOrder(Order order) throws DatabaseException {

        String sql = "update orders set order_price = ? where order_id = ?";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setDouble(1, order.getOrderPrice());
            preparedStatement.setInt(2, order.getOrderId());

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected != 1) {
                throw new DatabaseException("Could not update order");
            }
        } catch (SQLException e) {
            String errorMessage = "Fejl ved opdatering af ordre, prøv at genindlæs siden eller kontakt os, hvis problemet stadig er der";
            throw new DatabaseException(errorMessage, e.getMessage());
        }
    }

    public void removeOrderById(int orderId) throws DatabaseException {
        String sql = "delete from orders where order_id = ?";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, orderId);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected != 1) {
                throw new DatabaseException("An error occurred trying to remove an order from the database");
            }
        } catch (SQLException e) {
            String errorMessage = "Fejl ved forsøg på at fjerne ordre";
            throw new DatabaseException(errorMessage, e.getMessage());
        }
    }


}
