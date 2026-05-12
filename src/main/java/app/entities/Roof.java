package app.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter

public class Roof {
    private double roofSlope;
    private String roofMaterial;
    private String roofType;
}
