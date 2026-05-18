package app.services.converters;
import app.dto.requestDTO.OrderRequestDTO;
import app.dto.responseDTO.OrderResponseDTO;
import app.entities.Order;

public class OrderConverter {

    public OrderResponseDTO convertOrderToDto(Order order) {
        int orderId = order.getOrderId();
        int customerId = order.getCustomerId();
        int salesRepId = order.getSalesRepId();
        int carportId = order.getCarportId();
        double orderPrice = order.getOrderPrice();
        int partsListId = order.getPartsListId();
        return new OrderResponseDTO(orderId, customerId, salesRepId, carportId, orderPrice, partsListId);
    }

    public Order convertOrderToEntity(OrderRequestDTO orderRequestDTO) {
        int customerId = orderRequestDTO.getCustomerId();
        int salesRepId = orderRequestDTO.getSalesRepId();
        int carportId = orderRequestDTO.getCarportId();
        double orderPrice = orderRequestDTO.getOrderPrice();
        int partsListId = orderRequestDTO.getPartsListId();
        return new Order(customerId,salesRepId, carportId, orderPrice, partsListId);
    }
}
