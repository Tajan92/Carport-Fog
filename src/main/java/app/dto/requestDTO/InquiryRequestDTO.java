package app.dto.requestDTO;

import app.dto.requestDTO.carports.CarportNoShedRequestDTO;
import app.dto.requestDTO.users.CustomerRequestDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor

public class InquiryRequestDTO {
    private String remark;
    private CustomerRequestDTO customerRequestDTO;
    private CarportNoShedRequestDTO carportRequestDTO;
}
