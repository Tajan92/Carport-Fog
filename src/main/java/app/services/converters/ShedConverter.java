package app.services.converters;

import app.dto.requestDTO.ShedRequestDTO;
import app.dto.responseDTO.ShedResponseDTO;
import app.entities.Shed;

public class ShedConverter {

    public ShedResponseDTO convertShedToDto(Shed shed) {
        int shedId = shed.getShedId();
        Double width = shed.getWidth();
        Double length = shed.getLength();
        String siding = shed.getSiding();
        Boolean floor = shed.isFloor();

        return new ShedResponseDTO(shedId, width, length, siding, floor);
    }

    public Shed convertShedDTOtoEntity(ShedRequestDTO shedRequestDTO) {
        double width = shedRequestDTO.getWidth();
        double length = shedRequestDTO.getLength();
        String siding = shedRequestDTO.getSiding();
        boolean floor = shedRequestDTO.isFloor();

        return new Shed(width, length, siding, floor);
    }

    public ShedRequestDTO convertShedResponseToRequestDTO(ShedResponseDTO shedResponseDTO) {
        double width = shedResponseDTO.getWidth();
        double length = shedResponseDTO.getLength();
        String siding = shedResponseDTO.getSiding();
        boolean floor = shedResponseDTO.isFloor();

        return new ShedRequestDTO(width, length, siding, floor);
    }
}
