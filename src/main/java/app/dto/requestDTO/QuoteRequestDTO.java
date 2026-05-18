package app.dto.requestDTO;


import app.dto.requestDTO.carports.CarportNoShedRequestDTO;
import app.dto.requestDTO.users.CustomerRequestDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter

public class QuoteRequestDTO {
    private double discount;
    private CarportNoShedRequestDTO carportRequestDTO;
    private CustomerRequestDTO customerRequestDTO;
    private SalesRepRequestDTO salesRepRequestDTO;
}
