package app.dto.requestDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor

public class RoofRequestDTO {
    private double roofSlope;
    private String roofMaterial;
    private String roofType;
}


