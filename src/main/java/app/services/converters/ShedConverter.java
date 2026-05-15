package app.services.converters;

import app.dto.requestDTO.RoofRequestDTO;
import app.dto.requestDTO.ShedRequestDTO;
import app.dto.responseDTO.RoofResponseDTO;
import app.dto.responseDTO.ShedResponseDTO;
import app.entities.Roof;
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
        Double width = shedRequestDTO.getWidth();
        Double length = shedRequestDTO.getLength();
        String siding = shedRequestDTO.getSiding();
        Boolean floor = shedRequestDTO.isFloor();

        return new Shed(width, length, siding, floor);
    }
}
