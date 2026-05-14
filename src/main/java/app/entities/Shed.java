package app.entities;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor
@Getter
@EqualsAndHashCode

public class Shed {
    private double width;
    private double length;
    private String siding;
    private boolean floor;
}
