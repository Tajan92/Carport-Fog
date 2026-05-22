package app.dto.requestDTO.carports;
import app.dto.requestDTO.RoofRequestDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public abstract class CarportRequestDTO {
    private double width;
    private double height;
    private double length;
    private RoofRequestDTO roofRequestDTO;
}
