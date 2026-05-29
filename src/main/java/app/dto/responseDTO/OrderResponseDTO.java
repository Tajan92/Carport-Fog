package app.dto.responseDTO;

import app.dto.responseDTO.carports.CarportResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter

public class OrderResponseDTO {
    private int orderId;
    private double price;
    private double discount;
    private double totalPrice;
    private CustomerResponseDTO customerResponseDTO;
    private SalesRepResponseDTO salesRepResponseDTO;
    private CarportResponseDTO carportResponseDTO;
    private PartsListResponseDTO partsListResponseDTO;


    public OrderResponseDTO(int orderId, double retailPrice) {
        this.orderId = orderId;
        this.price = retailPrice;
    }
}
