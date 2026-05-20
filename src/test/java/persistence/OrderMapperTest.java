package persistence;

import app.entities.Order;
import app.exceptions.DatabaseException;
import app.persistence.OrderMapper;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class OrderMapperTest extends MapperTest {
    OrderMapper orderMapper = new OrderMapper();

    @Test
    void createOrderTest() throws DatabaseException {

        try (Connection connection = connectionPool.getConnection()) {
            try (Statement statement = connection.createStatement()) {
                statement.execute("INSERT INTO test.sheds (shed_width, shed_length, siding, floor) VALUES " +
                        "(2.40, 3.00, 'Trykimp. bræddebeklædning', true)");

                statement.execute("INSERT INTO test.roofs (roof_slope, roof_material, roof_type) VALUES " +
                        "(15, 'Betontagsten', 'Rejsning')");

                statement.execute("INSERT INTO test.addons (shed_id, roof_id) VALUES" +
                        "(5, 5)");

                statement.execute("INSERT INTO test.parts_lists DEFAULT VALUES");

                statement.execute("INSERT INTO test.carports (carport_width, carport_height, carport_length, addon_id, price, parts_list_id) VALUES" +
                        "(3.00, 2.08, 5.00, 1, 24999.00, 7)");

                Order order = new Order(1, 1, 7, 24999.00, 7);

                //5th order made in the system so should be 5
                int orderId = orderMapper.createOrder(order);

                assertEquals(5, orderId);

            }
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
    }


    @Test
    void getOrderByIdTest() throws DatabaseException {
        Order actualOrder = new Order(1, 1, 1, 1, 23500.0, 1);

        Order expectedOrder = orderMapper.getOrderById(1);

        assertEquals(actualOrder, expectedOrder);
    }

    @Test
    void getAllOrdersTest() throws DatabaseException {

        int size = orderMapper.getAllOrders().size();

        //4 orders created in our test code
        assertEquals(4, size);
    }

    @Test
    void removeOrderByIdTest() throws DatabaseException {

        orderMapper.removeOrderById(4);

        int size = orderMapper.getAllOrders().size();

        //Removed one so new size should be 3
        assertEquals(3, size);

    }

    @Test
    void updateOrderTest() throws DatabaseException{
        OrderMapper orderMapper = new OrderMapper();
        Order expectedOrder = new Order(1, 1, 1, 1, 25000.00, 1);
        orderMapper.updateOrder(expectedOrder);

        Order actualOrder = orderMapper.getOrderById(1);

        assertEquals(expectedOrder, actualOrder);
    }
}

