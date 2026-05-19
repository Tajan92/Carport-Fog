package app.services;

import app.dto.requestDTO.QuoteRequestDTO;
import app.dto.responseDTO.CustomerResponseDTO;
import app.dto.responseDTO.InquiryResponseDTO;
import app.dto.responseDTO.QuoteResponseDTO;
import app.dto.responseDTO.SalesRepResponseDTO;
import app.entities.Inquiry;
import app.entities.Quote;
import app.exceptions.DatabaseException;
import app.persistence.CarportMapper;
import app.persistence.CustomerMapper;
import app.persistence.QuoteMapper;
import app.persistence.SalesRepMapper;
import app.services.converters.QuoteConverter;

import java.util.ArrayList;
import java.util.List;

public class QuoteService {

    private QuoteMapper quoteMapper;
    private QuoteConverter quoteConverter;

    public QuoteService(QuoteMapper quoteMapper) {
        this.quoteMapper = quoteMapper;
        this.quoteConverter = new QuoteConverter();
    }

    public void createQuote(QuoteRequestDTO quoteRequestDTO) throws DatabaseException {

        Quote quote = quoteConverter.convertQuoteDTOtoEntity(quoteRequestDTO);
        quote.setCarportId(quoteRequestDTO.getCarportId());
        quote.setCustomerId(quoteRequestDTO.getCustomerId());
        quote.setSalesRepId(quoteRequestDTO.getSalesRepId());

        /* Mapper fra converter kobles på her */

        quoteMapper.createQuote(quote);
    }

    public QuoteResponseDTO getQuote(int quoteId) throws DatabaseException {
        Quote quote = quoteMapper.getQuoteById(quoteId);

        return quoteConverter.convertQuoteToDto(quote);
    }

    public List<QuoteResponseDTO> getAllQuotes() throws DatabaseException {
        List<Quote> allQuotes = quoteMapper.getAllQuotes();
        List<QuoteResponseDTO> responseDTOS = new ArrayList<>();
        for (Quote allQuote : allQuotes) {
            responseDTOS.add(quoteConverter.convertQuoteToDto(allQuote));
        }
        return responseDTOS;
    }

    public void deleteQuote(int quoteId) throws DatabaseException {
        quoteMapper.deleteQuoteById(quoteId);
    }
}
