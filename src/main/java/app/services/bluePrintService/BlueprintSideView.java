package app.services.bluePrintService;

import app.dto.responseDTO.carports.CarportResponseDTO;
import app.services.utils.Svg;

public class BlueprintSideView {
    Svg svg;
    CarportResponseDTO carportResponseDTO;

    public BlueprintSideView(CarportResponseDTO carportResponseDTO) {
        this.svg = new Svg();
        this.carportResponseDTO = carportResponseDTO;
    }
}
