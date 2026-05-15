package app.dto.requestDTO.carports;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class CarportRequestDTO {
    private double width;
    private double height;
    private double length;
    private double price;
    private RoofRequestDTO roofRequestDTO;
}
