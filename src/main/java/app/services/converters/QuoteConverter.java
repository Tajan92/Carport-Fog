package app.services.converters;

import app.dto.requestDTO.QuoteRequestDTO;
import app.dto.responseDTO.QuoteResponseDTO;
import app.entities.Quote;
import app.exceptions.DatabaseException;

public class QuoteConverter {

    public QuoteResponseDTO convertQuoteToDto(Quote quote) {
        int quoteId = quote.getQuoteId();
        double quotePrice = quote.getQuotePrice();

        return new QuoteResponseDTO(quoteId, quotePrice);
    }

    public Quote convertQuoteDTOtoEntity(QuoteRequestDTO quoteRequestDTO) throws DatabaseException {
        int customerId = quoteRequestDTO.getCustomerId();
        int carportId = quoteRequestDTO.getCarportId();
        double quotePrice = quoteRequestDTO.getQuotePrice();
        int salesRepId = quoteRequestDTO.getSalesRepId();

        return new Quote(quotePrice, carportId, customerId, salesRepId);
    }
}

