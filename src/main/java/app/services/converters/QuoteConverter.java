package app.services.converters;

import app.dto.requestDTO.QuoteRequestDTO;
import app.dto.responseDTO.QuoteResponseDTO;
import app.entities.Quote;
import app.exceptions.DatabaseException;
import app.persistence.CarportMapper;
import app.services.utils.DiscountCalculator;

public class QuoteConverter {
    CarportMapper carportMapper = new CarportMapper();

    public QuoteResponseDTO convertQuoteToDto(Quote quote) {
        int quoteId = quote.getQuoteId();
        double quotePrice = quote.getQuotePrice();

        return new QuoteResponseDTO(quoteId, quotePrice);
    }
    public Quote convertQuoteDTOtoEntity(QuoteRequestDTO quoteRequestDTO) throws DatabaseException {
        int customerId = quoteRequestDTO.getCustomerId();
        int carportId = quoteRequestDTO.getCarportId();
        double discount = quoteRequestDTO.getDiscount();
        int salesRepId = quoteRequestDTO.getSalesRepId();

        return new Quote(carportId, customerId, salesRepId);
    }
}

