package app.dto.requestDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor

public class InquiryRequestDTO {
    private int customerId;
    private String remark;
    private int carportId;
}
