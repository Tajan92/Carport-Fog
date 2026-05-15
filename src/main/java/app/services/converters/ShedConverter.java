package app.services.converters;

import app.dto.requestDTO.RoofRequestDTO;
import app.dto.requestDTO.ShedRequestDTO;
import app.dto.responseDTO.RoofResponseDTO;
import app.dto.responseDTO.ShedResponseDTO;
import app.entities.Roof;
import app.entities.Shed;

public class ShedConverter {


    public ShedResponseDTO convertShedToDto(Shed shed) {

        return new ShedResponseDTO();
    }


    public Shed convertShedDTOtoEntity(ShedRequestDTO shedRequestDTO) {


        return new Shed();
    }

}
