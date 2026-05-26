package services;
import app.dto.requestDTO.OrderRequestDTO;
import app.dto.responseDTO.OrderResponseDTO;
import app.exceptions.CalculatorException;
import app.exceptions.DatabaseException;
import app.services.ServiceFactory;
import org.junit.jupiter.api.Test;
import persistence.MapperTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class OrderServiceTest extends MapperTest {
    ServiceFactory serviceFactory = new ServiceFactory();

    @Test
    public void createOrderTest() throws DatabaseException {
        OrderRequestDTO request = new OrderRequestDTO(1, 1, 3, 25000.00, 3);

        //Creating order should not give exception if successful
        assertDoesNotThrow(() -> serviceFactory.getOrderService().createOrder(request));
    }

    @Test
    public void getOrderTest() throws CalculatorException, DatabaseException {
        int existingCustomerId = 1;
        int existingSalesRepId = 1;
        int existingCarportId = 1;
        double existingOrderPrice = 23500.00;

        OrderResponseDTO response = serviceFactory.getOrderService().getOrder(1);

        //Verify that the attatched DTO's are on the response
        assertNotNull(response.getCustomerResponseDTO());
        assertNotNull(response.getCarportResponseDTO());
        assertNotNull(response.getSalesRepResponseDTO());

        //Check the id's and price is correct from db
        assertEquals(existingCustomerId, response.getCustomerResponseDTO().getId());
        assertEquals(existingCarportId, response.getCarportResponseDTO().getCarportId());
        assertEquals(existingSalesRepId, response.getSalesRepResponseDTO().getId());
        assertEquals(existingOrderPrice, response.getOrderPrice());
    }

    @Test
    public void getAllOrdersTest() throws CalculatorException, DatabaseException {
        List<OrderResponseDTO> orders = serviceFactory.getOrderService().getAllOrders();
        double firstOrderPrice = 23500.00;

        //The list shouldn't be null or empty
        assertNotNull(orders);
        assertFalse(orders.isEmpty());

        OrderResponseDTO firstOrder = orders.get(0);

        //Check if other DTO's are attached
        assertNotNull(firstOrder.getCustomerResponseDTO());
        assertNotNull(firstOrder.getSalesRepResponseDTO());
        assertNotNull(firstOrder.getCarportResponseDTO());
        assertNotNull(firstOrder.getPartsListResponseDTO());

        //Check if the first order has correct id = 1 and the price is correct
        assertEquals(1, firstOrder.getOrderId());
        assertEquals(firstOrderPrice, firstOrder.getOrderPrice());
    }

    @Test
    public void getAllOrdersByCustomerIdTest() throws CalculatorException, DatabaseException {
        int customerId = 1;
        int amountOfOrdersByCustomer = 3;

        List<OrderResponseDTO> orders = serviceFactory.getOrderService().getAllOrdersByCustomerId(customerId);

        //The list shouldn't be null or empty
        assertNotNull(orders);
        assertFalse(orders.isEmpty());

        OrderResponseDTO firstOrder = orders.get(0);

        //Check if other DTO's are attached
        assertNotNull(firstOrder.getCustomerResponseDTO());
        assertNotNull(firstOrder.getSalesRepResponseDTO());
        assertNotNull(firstOrder.getCarportResponseDTO());
        assertNotNull(firstOrder.getPartsListResponseDTO());

        //Check if the first order has correct id = 1 and the size is 3
        assertEquals(1, firstOrder.getOrderId());
        assertEquals(amountOfOrdersByCustomer, orders.size());
    }
}
