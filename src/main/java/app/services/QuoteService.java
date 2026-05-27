package app.services;

import app.dto.requestDTO.QuoteRequestDTO;
import app.dto.responseDTO.CustomerResponseDTO;
import app.dto.responseDTO.PartsListResponseDTO;
import app.dto.responseDTO.QuoteResponseDTO;
import app.dto.responseDTO.SalesRepResponseDTO;
import app.dto.responseDTO.carports.CarportResponseDTO;
import app.entities.Customer;
import app.entities.Quote;
import app.entities.SalesRep;
import app.exceptions.DatabaseException;
import app.persistence.CarportMapper;
import app.persistence.CustomerMapper;
import app.persistence.QuoteMapper;
import app.persistence.SalesRepMapper;
import app.services.converters.QuoteConverter;
import app.services.converters.UserConverter;
import app.services.utils.PriceCalculator;

import java.util.ArrayList;
import java.util.List;

public class QuoteService {

    private QuoteMapper quoteMapper;
    private QuoteConverter quoteConverter;
    private UserConverter userConverter;
    private CarportService carportService;
    private CustomerMapper customerMapper;
    private SalesRepMapper salesRepMapper;
    private CarportMapper carportMapper;
    private UserService userService;


    public QuoteService(QuoteMapper quoteMapper, CarportService carportService, CustomerMapper customerMapper, SalesRepMapper salesRepMapper, CarportMapper carportMapper, UserService userService) {
        this.userService = userService;
        this.quoteMapper = quoteMapper;
        this.quoteConverter = new QuoteConverter();
        this.userConverter = new UserConverter();
        this.carportService = carportService;
        this.customerMapper = customerMapper;
        this.salesRepMapper = salesRepMapper;
        this.carportMapper = carportMapper;
    }

    public void createQuote(QuoteRequestDTO quoteRequestDTO) throws DatabaseException {

        /* Instantiate variables that cannot be instantiated in the converter */
        double carportPrice = carportMapper.getCarportById(quoteRequestDTO.getCarportId()).getPrice();
        double quotePrice = PriceCalculator.calculateDiscount(carportPrice, quoteRequestDTO.getQuotePrice());

        /* Set the new variables */
        Quote quote = quoteConverter.convertQuoteDTOtoEntity(quoteRequestDTO);
        quote.setCarportId(quoteRequestDTO.getCarportId());
        quote.setCustomerId(quoteRequestDTO.getCustomerId());
        quote.setSalesRepId(quoteRequestDTO.getSalesRepId());
        quote.setQuotePrice(quotePrice);

        quoteMapper.createQuote(quote);
    }

    public QuoteResponseDTO getQuote(int quoteId) throws DatabaseException {
        Quote quote = quoteMapper.getQuoteById(quoteId);
        QuoteResponseDTO quoteResponseDTO = quoteConverter.convertQuoteToDto(quote);

        /* Instantiate DTO´s that cannot be instantiated in the converter */
        Customer customer = customerMapper.getCustomerById(quote.getCustomerId());
        SalesRep salesRep = salesRepMapper.getSalesRepById(quote.getSalesRepId());

        CustomerResponseDTO customerResponseDTO = userConverter.convertCustomerToDto(customer);
        SalesRepResponseDTO salesRepResponseDTO = userConverter.convertSalesRepToDto(salesRep);
        CarportResponseDTO carportResponseDTO = carportService.getCarport(quote.getCarportId());

        /* Set the new DTOS´s */
        quoteResponseDTO.setCustomerResponseDTO(customerResponseDTO);
        quoteResponseDTO.setSalesRepResponseDTO(salesRepResponseDTO);
        quoteResponseDTO.setCarportResponseDTO(carportResponseDTO);

        return quoteResponseDTO;
    }

    public List<QuoteResponseDTO> getAllQuotes() throws DatabaseException {
        List<Quote> allQuotes = quoteMapper.getAllQuotes();
        List<QuoteResponseDTO> responseDTOS = new ArrayList<>();
        for (Quote quote : allQuotes) {
            int quoteId = quote.getQuoteId();
            double quotePrice = quote.getQuotePrice();
            CarportResponseDTO carportResponseDTO = carportService.getCarport(quote.getCarportId());
            CustomerResponseDTO customerResponseDTO = userService.getCustomer(quote.getCustomerId());
            SalesRepResponseDTO salesRepResponseDTO = userService.getSalesRep(quote.getSalesRepId());
            responseDTOS.add(new QuoteResponseDTO(quoteId, quotePrice, customerResponseDTO, carportResponseDTO, salesRepResponseDTO));
        }
        return responseDTOS;
    }

    public List<QuoteResponseDTO> getAllQuotesByCustomerId(int customerId) throws DatabaseException{
        List<Quote> quotes = quoteMapper.getAllQuotesByCustomerId(customerId);
        List<QuoteResponseDTO> quoteResponseDTOS = new ArrayList<>();
        for (Quote quote : quotes) {
            int quoteId = quote.getQuoteId();
            double quotePrice = quote.getQuotePrice();
            CarportResponseDTO carportResponseDTO = carportService.getCarport(quote.getCarportId());
            CustomerResponseDTO customerResponseDTO = userService.getCustomer(quote.getCustomerId());
            SalesRepResponseDTO salesRepResponseDTO = userService.getSalesRep(quote.getSalesRepId());
            quoteResponseDTOS.add(new QuoteResponseDTO(quoteId, quotePrice, customerResponseDTO, carportResponseDTO, salesRepResponseDTO));
        }
        return quoteResponseDTOS;
    }

    public void deleteQuote(int quoteId) throws DatabaseException {
        quoteMapper.deleteQuoteById(quoteId);
    }
}
