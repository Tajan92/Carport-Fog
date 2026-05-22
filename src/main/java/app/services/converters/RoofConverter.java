package app.services.converters;

import app.dto.requestDTO.RoofRequestDTO;
import app.dto.responseDTO.CustomerResponseDTO;
import app.dto.responseDTO.RoofResponseDTO;
import app.entities.Customer;
import app.entities.Roof;

public class RoofConverter {

    public RoofResponseDTO convertRoofToDto(Roof roof) {
        int roofId = roof.getRoofId();
        double roofSlope = roof.getRoofSlope();
        String roofMaterial = roof.getRoofMaterial();
        String roofType = roof.getRoofType();

        return new RoofResponseDTO(roofId, roofSlope, roofMaterial, roofType);
    }


    public Roof convertRoofDTOtoEntity(RoofRequestDTO roofRequestDTO) {
        double roofSlope = roofRequestDTO.getRoofSlope();
        String roofMaterial = roofRequestDTO.getRoofMaterial();
        String roofType = roofRequestDTO.getRoofType();

        return new Roof(roofSlope, roofMaterial, roofType);
    }

    public RoofRequestDTO convertRoofResponseToRequestDTO(RoofResponseDTO roofResponseDTO){
        double roofSlope = roofResponseDTO.getRoofSlope();
        String roofMaterial = roofResponseDTO.getRoofMaterial();
        String roofType = roofResponseDTO.getRoofType();

        return new RoofRequestDTO(roofSlope, roofMaterial, roofType);
    }


}
