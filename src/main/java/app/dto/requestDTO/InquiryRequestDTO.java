package app.dto.requestDTO;

import app.dto.requestDTO.carports.CarportRequestDTO;
import app.dto.requestDTO.users.CustomerRequestDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor

public class InquiryRequestDTO {
    private int customerId;
    private String remark;
    private int carportId;
}
