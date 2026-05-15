package app.dto.responseDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor

public class RoofResponseDTO {
    private int roofId;
    private double roofSlope;
    private String roofMaterial;
    private String roofType;
}
