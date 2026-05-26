package app.services;
import app.dto.requestDTO.InquiryRequestDTO;
import app.dto.responseDTO.CustomerResponseDTO;
import app.dto.responseDTO.InquiryResponseDTO;
import app.dto.responseDTO.carports.CarportResponseDTO;
import app.entities.*;
import app.exceptions.CalculatorException;
import app.exceptions.DatabaseException;
import app.persistence.*;
import app.services.converters.InquiryConverter;
import app.services.converters.UserConverter;
import app.services.utils.PartsListCalculator;
import app.services.utils.PriceCalculator;

import java.util.ArrayList;
import java.util.List;

public class InquiryService {
    private InquiryMapper inquiryMapper;
    private InquiryConverter inquiryConverter;
    private CustomerMapper customerMapper;
    private UserConverter userConverter;
    private CarportService carportService;
    private CarportMapper carportMapper;
    private RoofMapper roofMapper;
    private ShedMapper shedMapper;
    private ProductMapper productMapper;
    private UserService userService;

    public InquiryService(InquiryMapper inquiryMapper, CarportService carportService, CustomerMapper customerMapper, CarportMapper carportMapper, RoofMapper roofMapper, ShedMapper shedMapper, ProductMapper productMapper, UserService userService) {
        this.inquiryMapper = inquiryMapper;
        this.inquiryConverter = new InquiryConverter();
        this.customerMapper = customerMapper;
        this.userConverter = new UserConverter();
        this.carportService = carportService;
        this.carportMapper = carportMapper;
        this.roofMapper = roofMapper;
        this.shedMapper = shedMapper;
        this.productMapper = productMapper;
        this.userService = userService;
    }

    public void createInquiry(InquiryRequestDTO inquiryRequestDTO) throws DatabaseException {
        Inquiry inquiry = inquiryConverter.convertInquiryDtoToEntity(inquiryRequestDTO);
        inquiryMapper.createInquiry(inquiry);
    }

    public InquiryResponseDTO getInquiry(int inquiryId) throws DatabaseException, CalculatorException {
        Inquiry inquiry = inquiryMapper.getInquiryById(inquiryId);
        PartsListCalculator partsListCalculator = new PartsListCalculator();


        InquiryResponseDTO inquiryResponseDTO = inquiryConverter.convertInquiryToDto(inquiry);
        CarportResponseDTO carportResponseDTO = carportService.getCarport(inquiry.getCarportId());
        CustomerResponseDTO customerResponseDTO = userService.getCustomer(inquiry.getCustomerId());

        Carport carport = carportMapper.getCarportById(inquiry.getInquiryId());
        Roof roof = roofMapper.getRoofById(carport.getRoofId());
        Shed shed = shedMapper.getShedById(carport.getShedId());
        List<Product> products = productMapper.getAllProducts();

        List<ProductsPartsListEntry> productsPartsListEntries = partsListCalculator.createProductsPartsList(carport, shed, roof, products);

        double costPrice = PriceCalculator.calculateInquiryCostPrice(productsPartsListEntries);
        double retailPrice = PriceCalculator.calculateInquiryRetailPrice(productsPartsListEntries);
        double serviceFee = PriceCalculator.calculateServiceFee(productsPartsListEntries);

        inquiryResponseDTO.setCostPrice(costPrice);
        inquiryResponseDTO.setRetailPrice(retailPrice);
        inquiryResponseDTO.setServiceFee(serviceFee);
        /* Instantiate variables that cannot be instantiated in the converter */
        Customer customer = customerMapper.getCustomerById(inquiry.getCustomerId());

        /* Set the new DTO´s */
        inquiryResponseDTO.setCustomerResponseDTO(customerResponseDTO);
        inquiryResponseDTO.setCarportResponseDTO(carportResponseDTO);

        return inquiryResponseDTO;
    }

    public List<InquiryResponseDTO> getAllInquiries() throws DatabaseException {
        List<Inquiry> allInquiries = inquiryMapper.getAllInquiries();
        List<InquiryResponseDTO> responseDTOS = new ArrayList<>();
        for (Inquiry allInquiry : allInquiries) {
            responseDTOS.add(inquiryConverter.convertInquiryToDto(allInquiry));
        }
        return responseDTOS;
    }

    public void deleteInquiry(int inquiryId) throws DatabaseException {
        inquiryMapper.deleteInquiryById(inquiryId);
    }
}
