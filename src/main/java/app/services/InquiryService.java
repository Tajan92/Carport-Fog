package app.services;
import app.dto.requestDTO.InquiryRequestDTO;
import app.dto.requestDTO.carports.CarportRequestDTO;
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
    private PartsListCalculator partsListCalculator;
    private PartsListService partsListService;

    public InquiryService(InquiryMapper inquiryMapper, CarportService carportService, CustomerMapper customerMapper, CarportMapper carportMapper, RoofMapper roofMapper, ShedMapper shedMapper, ProductMapper productMapper, UserService userService, PartsListService partsListService) {
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
        this.partsListCalculator = new PartsListCalculator();
        this.partsListService = partsListService;
    }

    public void createInquiry(InquiryRequestDTO inquiryRequestDTO) throws DatabaseException {
        Inquiry inquiry = inquiryConverter.convertInquiryDtoToEntity(inquiryRequestDTO);
        inquiryMapper.createInquiry(inquiry);
    }

    public InquiryResponseDTO getInquiry(int inquiryId) throws DatabaseException, CalculatorException {
        Inquiry inquiry = inquiryMapper.getInquiryById(inquiryId);

        InquiryResponseDTO inquiryResponseDTO = inquiryConverter.convertInquiryToDto(inquiry);
        CarportResponseDTO carportResponseDTO = carportService.getCarport(inquiry.getCarportId());
        CustomerResponseDTO customerResponseDTO = userService.getCustomer(inquiry.getCustomerId());

        Carport carport = carportMapper.getCarportById(inquiry.getInquiryId());
        Roof roof = roofMapper.getRoofById(carport.getRoofId());
        Shed shed;
        if (carport.getShedId()!=null) {
            shed = shedMapper.getShedById(carport.getShedId());
        }else{
            shed = null;
        }
        List<Product> products = productMapper.getAllProducts();

        List<ProductsPartsListEntry> productsPartsListEntries = partsListCalculator.createProductsPartsList(carport, shed, roof, products);

        double costPrice = PriceCalculator.calculateInquiryCostPrice(productsPartsListEntries);
        double retailPrice = PriceCalculator.calculateRetailPrice(productsPartsListEntries);
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

    public List<InquiryResponseDTO> getAllInquiries() throws DatabaseException, CalculatorException {
        List<Inquiry> allInquiries = inquiryMapper.getAllInquiries();
        List<InquiryResponseDTO> responseDTOS = new ArrayList<>();

        for (Inquiry allInquiry : allInquiries) {
            CarportResponseDTO carportResponseDTO = carportService.getCarport(allInquiry.getCarportId());
            CustomerResponseDTO customerResponseDTO = userService.getCustomer(allInquiry.getCustomerId());
            CarportRequestDTO carportRequestDTO = carportService.convertCarportResponseToRequest(carportResponseDTO);
            List<ProductsPartsListEntry> entries = partsListService.createProductsPartsListEntries(carportRequestDTO);

            double costPrice = PriceCalculator.calculateInquiryCostPrice(entries);
            double retailPrice = PriceCalculator.calculateRetailPrice(entries);
            double serviceFee = PriceCalculator.calculateServiceFee(entries);

            InquiryResponseDTO response = inquiryConverter.convertInquiryToDto(allInquiry);
            response.setCostPrice(costPrice);
            response.setServiceFee(serviceFee);
            response.setRetailPrice(retailPrice);
            response.setCustomerResponseDTO(customerResponseDTO);
            response.setCarportResponseDTO(carportResponseDTO);
            responseDTOS.add(response);
        }
        return responseDTOS;
    }

    public List<InquiryResponseDTO> getAllInquiriesByCustomerId(int customerId) throws DatabaseException {
        List<Inquiry> allInquiries = inquiryMapper.getAllInquiriesByCustomerId(customerId);
        List<InquiryResponseDTO> responseDTOS = new ArrayList<>();
        for (Inquiry inquiry : allInquiries) {
            InquiryResponseDTO inquiryResponseDTO = inquiryConverter.convertInquiryToDto(inquiry);
            CarportResponseDTO carportResponseDTO = carportService.getCarport(inquiry.getCarportId());
            CustomerResponseDTO customerResponseDTO = userService.getCustomer(inquiry.getCustomerId());

            inquiryResponseDTO.setCustomerResponseDTO(customerResponseDTO);
            inquiryResponseDTO.setCarportResponseDTO(carportResponseDTO);
            responseDTOS.add(inquiryResponseDTO);
        }
        return responseDTOS;
    }

    public void deleteInquiry(int inquiryId) throws DatabaseException {
        inquiryMapper.deleteInquiryById(inquiryId);
    }
}
