package app.dto.responseDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor

public class InquiryResponseDTO {
    private int inquiryId;
    private int customerId;
    private String remark;
    private int carportId;
}
