package app.dto.requestDTO;

import app.dto.requestDTO.carports.CarportRequestDTO;
import app.dto.requestDTO.users.CustomerRequestDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor

public class InquiryRequestDTO {
    private String remark;
    private CustomerRequestDTO customerRequestDTO;
    private CarportRequestDTO carportRequestDTO;
}
