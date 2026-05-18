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
    private int customerId;
    private String remark;
    private int carportId;
}
