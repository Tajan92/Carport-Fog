package app.dto.responseDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter

public class QuoteResponseDTO {
    private int quoteId;
    private double quotePrice;
    private CarportResponseDTO carportResponseDTO;
    private CustomerResponseDTO customerResponseDTO;
    private SalesRepResponseDTO salesRepResponseDTO;

    public QuoteResponseDTO(int quoteId, double price) {
        this.quoteId = quoteId;
        this.quotePrice = price;
    }
}
