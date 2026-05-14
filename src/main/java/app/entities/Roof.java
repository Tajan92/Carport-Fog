package app.entities;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor
@Getter
@EqualsAndHashCode

public class Roof {
    private int roofId;
    private double roofSlope;
    private String roofMaterial;
    private String roofType;

    public Roof(double roofSlope, String roofMaterial, String roofType) {
        this.roofSlope = roofSlope;
        this.roofMaterial = roofMaterial;
        this.roofType = roofType;
    }
}
