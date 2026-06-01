package app.dto.responseDTO.carports;
import app.dto.responseDTO.ShedResponseDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CarportShedResponseDTO extends CarportResponseDTO{
    ShedResponseDTO shedResponseDTO;

    public CarportShedResponseDTO(int carportId, double width, double height, double length, double price) {
        super(carportId, width, height, length, price);
    }
}
