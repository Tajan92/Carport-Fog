package app.services.converters;

import app.dto.requestDTO.QuoteRequestDTO;
import app.dto.responseDTO.QuoteResponseDTO;
import app.entities.Quote;
import app.services.utils.DiscountCalculator;

public class QuoteConverter {

    public QuoteResponseDTO convertQuoteToDto(Quote quote) {

        int quoteId = quote.getQuoteId();
        double quotePrice = quote.getQuotePrice();

        return new QuoteResponseDTO(quoteId, quotePrice);
    }

    public Quote convertQuoteDTOtoEntity(QuoteRequestDTO quoteRequestDTO) {
        double carportPrice = quoteRequestDTO.getCarportRequestDTO().getPrice;
        double discount = quoteRequestDTO.getDiscount();

        double quotePrice = DiscountCalculator.calculateDiscount(carportPrice, discount);

        return new Quote(quotePrice);
    }
}
