package app.dto.responseDTO.carports;
import app.dto.responseDTO.RoofResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public abstract class CarportResponseDTO {
    private int carportId;
    private double width;
    private double height;
    private double length;
    private double price;
    RoofResponseDTO roofResponseDTO;
}
