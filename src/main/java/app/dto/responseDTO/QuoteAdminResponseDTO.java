package app.dto.responseDTO;

import app.dto.responseDTO.carports.CarportResponseDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class QuoteAdminResponseDTO extends QuoteResponseDTO {
    private double serviceFee;
    private double costPrice;

    public QuoteAdminResponseDTO(int quoteId, double retailPrice, double discount, double totalPrice, double costPrice, double serviceFee, CustomerResponseDTO customerResponseDTO, CarportResponseDTO carportResponseDTO, SalesRepResponseDTO salesRepResponseDTO) {
        super(quoteId, retailPrice, discount, totalPrice, customerResponseDTO, carportResponseDTO, salesRepResponseDTO);
        this.costPrice = costPrice;
        this.serviceFee = serviceFee;
    }

    public QuoteAdminResponseDTO(int quoteId, double retailPrice, double discount, double totalPrice, double costPrice, double serviceFee) {
        super(quoteId, retailPrice, discount, totalPrice);
        this.costPrice = costPrice;
        this.serviceFee = serviceFee;
    }
}
