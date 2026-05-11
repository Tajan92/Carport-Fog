package app.entities;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter

public class Shed {
    private double width;
    private double length;
    private String siding;
    private boolean floor;
}
