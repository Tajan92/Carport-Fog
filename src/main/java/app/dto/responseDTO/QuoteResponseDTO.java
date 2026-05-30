package app.dto.responseDTO;

import app.dto.responseDTO.carports.CarportResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter

public class QuoteResponseDTO {
    private int quoteId;
    private double price;
    private double discount;
    private double retailPrice;
    private boolean isPayed;
    private CustomerResponseDTO customerResponseDTO;
    private CarportResponseDTO carportResponseDTO;
    private SalesRepResponseDTO salesRepResponseDTO;

    public QuoteResponseDTO(int quoteId, double retailPrice, double discountPrice, boolean isPayed) {
        this.quoteId = quoteId;
        this.price = retailPrice;
        this.discount = discountPrice;
        this.isPayed = isPayed;
    }
}


