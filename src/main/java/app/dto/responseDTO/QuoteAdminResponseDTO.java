package app.dto.responseDTO;

import app.dto.responseDTO.carports.CarportResponseDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class QuoteAdminResponseDTO extends QuoteResponseDTO {
    private double serviceFee;
    private double costPrice;

    public QuoteAdminResponseDTO(int quoteId, double retailPrice, double discount, double totalPrice, double costPrice, double serviceFee, boolean isPayed, CustomerResponseDTO customerResponseDTO, CarportResponseDTO carportResponseDTO, SalesRepResponseDTO salesRepResponseDTO) {
        super(quoteId, retailPrice, discount, totalPrice, isPayed, customerResponseDTO, carportResponseDTO, salesRepResponseDTO);
        this.costPrice = costPrice;
        this.serviceFee = serviceFee;
    }

    public QuoteAdminResponseDTO(int quoteId, double retailPrice, double discountPrice, boolean isPayed) {
        super(quoteId, retailPrice, discountPrice, isPayed);
    }
}
