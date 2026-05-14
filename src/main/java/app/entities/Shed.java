package app.entities;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor
@Getter
@EqualsAndHashCode

public class Shed {
    private int shedId;
    private double width;
    private double length;
    private String siding;
    private boolean floor;

    public Shed(double width, double length, String siding, boolean floor) {
        this.width = width;
        this.length = length;
        this.siding = siding;
        this.floor = floor;
    }
}
