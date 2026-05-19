package app.services.converters;

import app.dto.requestDTO.InquiryRequestDTO;
import app.dto.requestDTO.RoofRequestDTO;
import app.dto.responseDTO.InquiryResponseDTO;
import app.dto.responseDTO.RoofResponseDTO;
import app.entities.Inquiry;
import app.entities.Roof;

public class InquiryConverter {

    public InquiryResponseDTO convertInquiryToDto(Inquiry inquiry) {
        int inquiryId = inquiry.getInquiryId();
        String remark = inquiry.getRemark();

        return new InquiryResponseDTO(remark, inquiryId);
    }

    public Inquiry convertInquiryDtoToEntity(InquiryRequestDTO inquiryRequestDTO) {
        String remark = inquiryRequestDTO.getRemark();

        return new Inquiry(remark);
    }
}
