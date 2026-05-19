package app.services;

import app.dto.requestDTO.InquiryRequestDTO;
import app.dto.responseDTO.CustomerResponseDTO;
import app.dto.responseDTO.InquiryResponseDTO;
import app.dto.responseDTO.carports.CarportResponseDTO;
import app.entities.Carport;
import app.entities.Customer;
import app.entities.Inquiry;
import app.exceptions.DatabaseException;
import app.persistence.CarportMapper;
import app.persistence.CustomerMapper;
import app.persistence.InquiryMapper;
import app.services.converters.CarportConverter;
import app.services.converters.InquiryConverter;
import app.services.converters.UserConverter;

import java.util.ArrayList;
import java.util.List;

public class InquiryService {
    private InquiryMapper inquiryMapper;
    private InquiryConverter inquiryConverter;
    private CustomerMapper customerMapper;
    private UserConverter userConverter;
    private CarportService carportService;

    public InquiryService(InquiryMapper inquiryMapper, CarportService carportService, CustomerMapper customerMapper) {
        this.inquiryMapper = inquiryMapper;
        this.inquiryConverter = new InquiryConverter();
        this.customerMapper = customerMapper;
        this.userConverter = new UserConverter();
        this.carportService = carportService;
    }

    public void createInquiry(InquiryRequestDTO inquiryRequestDTO) throws DatabaseException {
        Inquiry inquiry = inquiryConverter.convertInquiryDtoToEntity(inquiryRequestDTO);
        inquiryMapper.createInquiry(inquiry);
    }


    public InquiryResponseDTO getInquiry(int inquiryId) throws DatabaseException {
        Inquiry inquiry = inquiryMapper.getInquiryById(inquiryId);
        InquiryResponseDTO inquiryResponseDTO = inquiryConverter.convertInquiryToDto(inquiry);

        Customer customer = customerMapper.getCustomerById(inquiry.getCustomerId());

        CustomerResponseDTO customerResponseDTO = userConverter.convertCustomerToDto(customer);
        CarportResponseDTO carportResponseDTO = carportService.getCarport(inquiry.getCarportId());

        inquiryResponseDTO.setCustomerResponseDTO(customerResponseDTO);
        inquiryResponseDTO.setCarportRespondDto(carportResponseDTO);

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
