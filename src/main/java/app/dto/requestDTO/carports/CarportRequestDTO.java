package app.dto.requestDTO.carports;
import app.dto.requestDTO.RoofRequestDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public abstract class CarportRequestDTO {
    private double width;
    private double height;
    private double length;
    private RoofRequestDTO roofRequestDTO;
}
