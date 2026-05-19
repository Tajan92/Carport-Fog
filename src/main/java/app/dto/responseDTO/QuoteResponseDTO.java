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
    private double quotePrice;
    private CustomerResponseDTO customerResponseDTO;
    private CarportResponseDTO carportResponseDTO;
    private SalesRepResponseDTO salesRepResponseDTO;


    public QuoteResponseDTO(int quoteId, double quotePrice) {
        this.quoteId = quoteId;
        this.quotePrice = quotePrice;
    }
}


