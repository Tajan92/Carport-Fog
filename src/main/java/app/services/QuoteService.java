package app.services;

import app.dto.requestDTO.QuoteRequestDTO;
import app.dto.responseDTO.CustomerResponseDTO;
import app.dto.responseDTO.QuoteResponseDTO;
import app.dto.responseDTO.SalesRepResponseDTO;
import app.entities.Quote;
import app.exceptions.DatabaseException;
import app.persistence.CarportMapper;
import app.persistence.CustomerMapper;
import app.persistence.QuoteMapper;
import app.persistence.SalesRepMapper;
import app.services.converters.QuoteConverter;

public class QuoteService {

    private QuoteMapper quoteMapper;
    private CarportMapper carportMapper;
    private CustomerMapper customerMapper;
    private SalesRepMapper salesRepMapper;
    private QuoteConverter quoteConverter;

    public QuoteService(QuoteMapper quoteMapper, CarportMapper carportMapper, CustomerMapper customerMapper, SalesRepMapper salesRepMapper) {
        this.quoteMapper = quoteMapper;
        this.carportMapper = carportMapper;
        this.customerMapper = customerMapper;
        this.salesRepMapper = salesRepMapper;
        this.quoteConverter = new QuoteConverter();
    }

    public void createQuote(QuoteRequestDTO quoteRequestDTO, int carportId, int customerId, int salesRepId) throws DatabaseException {

        Quote quote = quoteConverter.convertQuoteDTOtoEntity(quoteRequestDTO);
        quote.setCarportId(carportId);
        quote.setCustomerId(customerId);
        quote.setSalesRepId(salesRepId);

        quoteMapper.createQuote(quote);
    }

    public QuoteResponseDTO getQuote(int quoteId, CarportResponseDTO carportResponseDTO, CustomerResponseDTO customerResponseDTO, SalesRepResponseDTO salesRepResponseDTO) throws DatabaseException {

        Quote quote = quoteMapper.getQuoteById(quoteId);

        QuoteResponseDTO quoteResponseDTO = quoteConverter.convertQuoteToDto(quote);
        quoteResponseDTO.setCarportResponseDTO(carportResponseDTO);
        quoteResponseDTO.setCustomerResponseDTO(customerResponseDTO);
        quoteResponseDTO.setSalesRepResponseDTO(salesRepResponseDTO);

        return quoteConverter.convertQuoteToDto(quote);
    }
}
