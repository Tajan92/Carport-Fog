package app.dto.responseDTO.carports;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CarportNoShedResponseDTO extends CarportResponseDTO{

    public CarportNoShedResponseDTO(int carportId, double width, double height, double length, double price) {
        super(carportId, width, height, length, price);
    }


}
