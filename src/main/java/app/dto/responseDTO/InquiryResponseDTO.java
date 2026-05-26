package app.dto.responseDTO;
import app.dto.responseDTO.carports.CarportResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor

public class InquiryResponseDTO {
    private int inquiryId;
    private String remark;
    private double costPrice;
    private double retailPrice;
    private double serviceFee;
    private CarportResponseDTO CarportResponseDTO;
    private CustomerResponseDTO customerResponseDTO;

    public InquiryResponseDTO(String remark, int inquiryId) {
        this.remark = remark;
        this.inquiryId = inquiryId;
    }

    public InquiryResponseDTO(int inquiryId, String remark, CarportResponseDTO carportResponseDTO, CustomerResponseDTO customerResponseDTO) {
        this.inquiryId = inquiryId;
        this.remark = remark;
        CarportResponseDTO = carportResponseDTO;
        this.customerResponseDTO = customerResponseDTO;
    }
}
