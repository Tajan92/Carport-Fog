package app.services;

import app.dto.requestDTO.InquiryRequestDTO;
import app.dto.responseDTO.CustomerResponseDTO;
import app.dto.responseDTO.InquiryResponseDTO;
import app.entities.Inquiry;
import app.exceptions.DatabaseException;
import app.persistence.CarportMapper;
import app.persistence.CustomerMapper;
import app.persistence.InquiryMapper;
import app.services.converters.InquiryConverter;

import java.util.ArrayList;
import java.util.List;

public class InquiryService {

    private CarportMapper carportMapper;
    private CustomerMapper customerMapper;
    private InquiryMapper inquiryMapper;

    private InquiryConverter inquiryConverter;

    public InquiryService(CarportMapper carportMapper, CustomerMapper customerMapper, InquiryMapper inquiryMapper, InquiryConverter inquiryConverter) {
        this.carportMapper = carportMapper;
        this.customerMapper = customerMapper;
        this.inquiryMapper = inquiryMapper;
        this.inquiryConverter = new InquiryConverter();
    }

    public void createInquiry(InquiryRequestDTO inquiryRequestDTO) throws DatabaseException {
        Inquiry inquiry = inquiryConverter.convertInquiryDtoToEntity(inquiryRequestDTO);
        inquiryMapper.createInquiry(inquiry);
    }


    public InquiryResponseDTO getInquiry(int inquiryId) throws DatabaseException {
        Inquiry inquiry = inquiryMapper.getInquiryById(inquiryId);
        return inquiryConverter.convertInquiryToDto(inquiry);
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
