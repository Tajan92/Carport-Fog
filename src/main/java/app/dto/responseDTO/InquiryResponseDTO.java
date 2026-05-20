package app.dto.responseDTO;
import app.dto.responseDTO.carports.CarportResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor

public class InquiryResponseDTO {
    private String remark;
    private int inquiryId;
    private CarportResponseDTO CarportRespondDto;
    private CustomerResponseDTO customerResponseDTO;


    public InquiryResponseDTO(String remark, int inquiryId) {
        this.remark = remark;
        this.inquiryId = inquiryId;
    }
}
