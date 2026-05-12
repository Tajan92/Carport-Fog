package app.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter

public class Product {
    private int productId;
    private double costPrice;
    private double retailPrice;
    private double length;
    private String unit;
    private String productGroup;
    private String description;
    private double quantity;

    private String placementDescription; //Kun i DTO?
}
