package app.entities;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor

public class Carport {
    private int carportId;
    private double width;
    private double length;
    private double price;
    private PartsList partsList;
    private Shed shed;
    private Roof roof;
}
