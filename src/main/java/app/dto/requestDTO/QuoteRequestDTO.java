package app.dto.requestDTO;


import app.dto.requestDTO.carports.CarportRequestDTO;
import app.dto.requestDTO.users.CustomerRequestDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter

public class QuoteRequestDTO {
    private double discount;
    private CarportRequestDTO carportRequestDTO;
    private CustomerRequestDTO customerRequestDTO;
    private SalesRepRequestDTO salesRepRequestDTO;
}
