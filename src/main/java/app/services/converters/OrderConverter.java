package app.services.converters;
import app.dto.requestDTO.OrderRequestDTO;
import app.dto.responseDTO.OrderResponseDTO;
import app.entities.Order;

public class OrderConverter {

    public OrderResponseDTO convertOrderToDto(Order order) {
        int orderId = order.getOrderId();
        double orderPrice = order.getOrderPrice();

        return new OrderResponseDTO(orderId, orderPrice);
    }

    public Order convertOrderToEntity(OrderRequestDTO orderRequestDTO) {
        int customerId = orderRequestDTO.getCustomerId();
        int salesRepId = orderRequestDTO.getSalesRepId();
        int carportId = orderRequestDTO.getCarportId();
        double orderPrice = orderRequestDTO.getOrderPrice();
        int partsListId = orderRequestDTO.getPartsListId();
        double discount = orderRequestDTO.getOrderDiscount();
        return new Order(customerId, salesRepId, carportId, orderPrice, partsListId,discount);
    }
}
