package app.dto.responseDTO;

import app.dto.responseDTO.carports.CarportResponseDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class OrderAdminResponseDTO extends OrderResponseDTO {
    double serviceFee;
    private double costPrice;

    public OrderAdminResponseDTO(int orderId, double retailPrice, double discount, double totalPrice, double costPrice, double serviceFee, CustomerResponseDTO customerResponseDTO, SalesRepResponseDTO salesRepResponseDTO, CarportResponseDTO carportResponseDTO, PartsListResponseDTO partsListResponseDTO) {
        super(orderId, retailPrice, discount, totalPrice, customerResponseDTO, salesRepResponseDTO, carportResponseDTO, partsListResponseDTO);
        this.costPrice = costPrice;
        this.serviceFee = serviceFee;
    }
}
