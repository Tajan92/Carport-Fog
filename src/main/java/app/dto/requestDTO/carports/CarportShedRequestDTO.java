package app.dto.requestDTO.carports;
import lombok.Getter;

@Getter
public class CarportShedRequestDTO extends CarportRequestDTO {
    private ShedRequestDTO shedRequestDTO;

    public CarportShedRequestDTO(double width, double height, double length, double price, RoofRequestDTO roofRequestDTO, ShedRequestDTO shedRequestDTO) {
        super(width, height, length, price, roofRequestDTO);
        this.shedRequestDTO = shedRequestDTO;
    }
}
