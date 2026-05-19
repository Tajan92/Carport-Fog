package app.dto.responseDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter

public class QuoteResponseDTO {
    private int quoteId;
    private int customerId;
    private double quotePrice;
    private int carportId;
    private int salesRepId;
}
