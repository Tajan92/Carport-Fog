package app.dto.responseDTO.carports;
import app.dto.responseDTO.RoofResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
@Setter
public abstract class CarportResponseDTO {
    private int carportId;
    private double width;
    private double height;
    private double length;
    private double price;
    RoofResponseDTO roofResponseDTO;

    public CarportResponseDTO(int carportId, double width, double height, double length, double price) {
        this.carportId = carportId;
        this.width = width;
        this.height = height;
        this.length = length;
        this.price = price;
    }
}
