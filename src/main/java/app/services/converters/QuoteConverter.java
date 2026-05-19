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
        int customerId = quote.getCustomerId();
        double quotePrice = quote.getQuotePrice();
        int carportId = quote.getCarportId();
        int salesRepId = quote.getSalesRepId();

        return new QuoteResponseDTO(quoteId, customerId, quotePrice, carportId, salesRepId);
    }
    // TODO: Flyt mapper til service
    public Quote convertQuoteDTOtoEntity(QuoteRequestDTO quoteRequestDTO) throws DatabaseException {
        int customerId = quoteRequestDTO.getCustomerId();
        int carportId = quoteRequestDTO.getCarportId();
        double discount = quoteRequestDTO.getDiscount();
        int salesRepId = quoteRequestDTO.getSalesRepId();
        /* fjern carport price og quote price her */

        double carportPrice = carportMapper.getCarportById(carportId).getPrice();
        double quotePrice = DiscountCalculator.calculateDiscount(carportPrice, discount);

        return new Quote(quotePrice, carportId, customerId, salesRepId);
    }
}

