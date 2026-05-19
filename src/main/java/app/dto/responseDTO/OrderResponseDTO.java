package app.dto.responseDTO;

import app.dto.responseDTO.carports.CarportResponseDTO;
import app.persistence.SalesRepMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter

public class OrderResponseDTO {
    private int orderId;
    private double orderPrice;
    private CustomerResponseDTO customerResponseDTO;
    private SalesRepResponseDTO salesRepResponseDTO;
    private CarportResponseDTO carportResponseDTO;
    private PartsListResponseDTO partsListResponseDTO;


    public OrderResponseDTO(int orderId, double orderPrice) {
        this.orderId = orderId;
        this.orderPrice = orderPrice;
    }
}
