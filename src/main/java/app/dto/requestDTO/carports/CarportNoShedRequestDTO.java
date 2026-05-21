package app.dto.requestDTO.carports;
import app.dto.requestDTO.RoofRequestDTO;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
public class CarportNoShedRequestDTO extends CarportRequestDTO{

    public CarportNoShedRequestDTO(double width, double height, double length, RoofRequestDTO roofRequestDTO) {
        super(width, height, length, roofRequestDTO);
    }
}
