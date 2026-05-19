package app.services.utils;

import app.dto.requestDTO.InquiryRequestDTO;
import app.dto.requestDTO.carports.CarportRequestDTO;
import app.dto.responseDTO.CustomerResponseDTO;
import app.dto.responseDTO.InquiryResponseDTO;
import app.entities.Inquiry;
import app.exceptions.DatabaseException;
import app.persistence.CarportMapper;
import app.persistence.CustomerMapper;
import app.persistence.InquiryMapper;
import app.services.converters.InquiryConverter;

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

    public void createInquiry(InquiryRequestDTO inquiryRequestDTO, int customerId, int carportId) throws DatabaseException {
        Inquiry inquiry = inquiryConverter.convertInquiryDtoToEntity(inquiryRequestDTO);

        inquiry.setCustomerId(customerId);
        inquiry.setCarportId(carportId);

        inquiryMapper.createInquiry(inquiry);
    }


    public InquiryResponseDTO getInquiry(int inquiryId, CarportRespondDTO carportRespondDTO, CustomerResponseDTO customerResponseDTO) throws DatabaseException {

        Inquiry inquiry = inquiryMapper.getInquiryById(inquiryId);

        InquiryResponseDTO inquiryResponseDTO = inquiryConverter.convertInquiryToDto(inquiry);
        inquiryResponseDTO.setCarportRespondDto(carportRespondDTO);
        inquiryResponseDTO.setCustomerResponseDTO(customerResponseDTO);

        return inquiryConverter.convertInquiryToDto(inquiry);
    }

}
