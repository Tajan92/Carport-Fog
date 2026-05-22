package app.dto.requestDTO.carports;
import app.dto.requestDTO.RoofRequestDTO;
import app.dto.requestDTO.ShedRequestDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CarportShedRequestDTO extends CarportRequestDTO {
    private ShedRequestDTO shedRequestDTO;

    public CarportShedRequestDTO(double width, double height, double length, RoofRequestDTO roofRequestDTO, ShedRequestDTO shedRequestDTO) {
        super(width, height, length, roofRequestDTO);
        this.shedRequestDTO = shedRequestDTO;
    }
}
