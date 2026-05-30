package app.services;
import app.dto.requestDTO.QuoteRequestDTO;
import app.dto.responseDTO.CustomerResponseDTO;
import app.dto.responseDTO.QuoteResponseDTO;
import app.dto.responseDTO.SalesRepResponseDTO;
import app.dto.responseDTO.*;
import app.dto.responseDTO.carports.CarportResponseDTO;
import app.entities.*;
import app.exceptions.CalculatorException;
import app.exceptions.DatabaseException;
import app.persistence.*;
import app.services.converters.*;
import app.services.utils.PartsListCalculator;
import app.services.utils.PriceCalculator;

import java.sql.SQLException;
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
    private ProductMapper productMapper;
    private UserService userService;
    PartsListCalculator partsListCalculator;


    public QuoteService(QuoteMapper quoteMapper, CarportService carportService, CustomerMapper customerMapper, SalesRepMapper salesRepMapper, CarportMapper carportMapper, UserService userService, ProductMapper productMapper) {
        this.userService = userService;
        this.quoteMapper = quoteMapper;
        this.quoteConverter = new QuoteConverter();
        this.userConverter = new UserConverter();
        this.carportService = carportService;
        this.customerMapper = customerMapper;
        this.salesRepMapper = salesRepMapper;
        this.carportMapper = carportMapper;
        this.partsListCalculator = new PartsListCalculator();
        this.productMapper = productMapper;
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
        quote.setQuoteDiscount(quoteRequestDTO.getQuoteDiscount());

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

        double serviceFee = PriceCalculator.calculateServiceFee(quote.getQuotePrice());
        double totalPrice = PriceCalculator.getRevenue(quoteResponseDTO.getPrice(), quoteResponseDTO.getDiscount(), serviceFee);

        /* Set the Missing variables */
        quoteResponseDTO.setTotalPrice(totalPrice);

        /* Set the new DTOS´s */
        quoteResponseDTO.setCustomerResponseDTO(customerResponseDTO);
        quoteResponseDTO.setSalesRepResponseDTO(salesRepResponseDTO);
        quoteResponseDTO.setCarportResponseDTO(carportResponseDTO);

        return quoteResponseDTO;
    }

    public QuoteAdminResponseDTO getQuoteAdmin(int quoteId) throws DatabaseException {
        Quote quote = quoteMapper.getQuoteById(quoteId);
        QuoteAdminResponseDTO quoteAdminResponseDTO = (QuoteAdminResponseDTO) quoteConverter.convertQuoteToDto(quote);
        List<Product> products = productMapper.getAllProducts();

        /* Instantiate DTO´s that cannot be instantiated in the converter */
        Customer customer = customerMapper.getCustomerById(quote.getCustomerId());
        SalesRep salesRep = salesRepMapper.getSalesRepById(quote.getSalesRepId());

        CustomerResponseDTO customerResponseDTO = userConverter.convertCustomerToDto(customer);
        SalesRepResponseDTO salesRepResponseDTO = userConverter.convertSalesRepToDto(salesRep);
        CarportResponseDTO carportResponseDTO = carportService.getCarport(quote.getCarportId());

        double calculatedCostPrice = 0;
        for (Product product : products) {
            calculatedCostPrice += product.getCostPrice();
        }

        double serviceFee = PriceCalculator.calculateServiceFee(quote.getQuotePrice());
        double totalPrice = PriceCalculator.getRevenue(quote.getQuotePrice(), quote.getQuoteDiscount(), serviceFee);
        double costPrice = calculatedCostPrice;

        /* Set the Missing variables */
        quoteAdminResponseDTO.setTotalPrice(totalPrice);
        quoteAdminResponseDTO.setServiceFee(serviceFee);
        quoteAdminResponseDTO.setCostPrice(costPrice);

        /* Set the new DTOS´s */
        quoteAdminResponseDTO.setCustomerResponseDTO(customerResponseDTO);
        quoteAdminResponseDTO.setSalesRepResponseDTO(salesRepResponseDTO);
        quoteAdminResponseDTO.setCarportResponseDTO(carportResponseDTO);

        return quoteAdminResponseDTO;
    }

    public List<QuoteAdminResponseDTO> getAllQuotesAdmin() throws DatabaseException {
        List<Quote> allQuotes = quoteMapper.getAllQuotes();
        List<QuoteAdminResponseDTO> responseDTOS = new ArrayList<>();
        List<Product> products = productMapper.getAllProducts();

        for (Quote quote : allQuotes) {
            int quoteId = quote.getQuoteId();
            CarportResponseDTO carportResponseDTO = carportService.getCarport(quote.getCarportId());
            CustomerResponseDTO customerResponseDTO = userService.getCustomer(quote.getCustomerId());
            SalesRepResponseDTO salesRepResponseDTO = userService.getSalesRep(quote.getSalesRepId());

            double calculatedCostPrice = 0;
            for (Product product : products) {
                calculatedCostPrice += product.getCostPrice();
            }

            boolean isPayed = quote.isPayed();
            double retailPrice = quote.getQuotePrice();
            double discount = quote.getQuoteDiscount();
            double serviceFee = PriceCalculator.calculateServiceFee(retailPrice);
            double totalPrice = PriceCalculator.getRevenue(retailPrice, discount, serviceFee);
            double costPrice = calculatedCostPrice;

            responseDTOS.add(new QuoteAdminResponseDTO(quoteId, retailPrice, discount, totalPrice, costPrice, serviceFee, isPayed, customerResponseDTO, carportResponseDTO, salesRepResponseDTO));
        }
        return responseDTOS;
    }

    public List<QuoteResponseDTO> getAllQuotesByCustomerId(int customerId) throws DatabaseException {
        List<Quote> quotes = quoteMapper.getAllQuotesByCustomerId(customerId);
        List<QuoteResponseDTO> quoteResponseDTOS = new ArrayList<>();

        for (Quote quote : quotes) {
            int quoteId = quote.getQuoteId();
            CarportResponseDTO carportResponseDTO = carportService.getCarport(quote.getCarportId());
            CustomerResponseDTO customerResponseDTO = userService.getCustomer(quote.getCustomerId());
            SalesRepResponseDTO salesRepResponseDTO = userService.getSalesRep(quote.getSalesRepId());

            boolean isPayed = quote.isPayed();
            double retailPrice = quote.getQuotePrice();
            double discount = quote.getQuoteDiscount();
            double serviceFee = PriceCalculator.calculateServiceFee(retailPrice);
            double totalPrice = PriceCalculator.getRevenue(retailPrice, discount, serviceFee);

            quoteResponseDTOS.add(new QuoteResponseDTO(quoteId, retailPrice, discount, totalPrice, isPayed, customerResponseDTO, carportResponseDTO, salesRepResponseDTO));
        }
        return quoteResponseDTOS;
    }

    public void updateQuoteStatus(int quoteId) throws SQLException, DatabaseException {
        quoteMapper.updateQuoteToPayed(quoteId);
    }

    public void deleteQuote(int quoteId) throws DatabaseException {
        quoteMapper.deleteQuoteById(quoteId);
    }
}
