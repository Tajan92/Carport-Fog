package app.entities;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor

public class Carport {
    private int carportId;
    private double width;
    private double height;
    private double length;
    private double price;
    private int partsListId;
    private Integer shedId;
    private int roofId;

    public Carport(double width, double height, double length, double price, int partsListId, Integer shedId, int roofId) {
        this.width = width;
        this.height = height;
        this.length = length;
        this.price = price;
        this.partsListId = partsListId;
        this.shedId = shedId;
        this.roofId = roofId;
    }

    public Carport(double width, double height, double length, double price) {
        this.width = width;
        this.height = height;
        this.length = length;
        this.price = price;
    }

    public Carport(double width, double height, double length) {
        this.width = width;
        this.height = height;
        this.length = length;
    }
}
