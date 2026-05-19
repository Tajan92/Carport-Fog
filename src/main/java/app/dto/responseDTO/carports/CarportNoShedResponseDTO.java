package app.dto.responseDTO.carports;
import app.dto.responseDTO.RoofResponseDTO;
import lombok.Getter;

@Getter
public class CarportNoShedResponseDTO extends CarportResponseDTO{

    public CarportNoShedResponseDTO(int carportId, double width, double height, double length, double price, RoofResponseDTO roofResponseDTO) {
        super(carportId, width, height, length, price, roofResponseDTO);
    }
}
