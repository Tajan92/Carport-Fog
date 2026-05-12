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

//    private String placementDescription; Kun i DTO?


    public Product(int productId, double costPrice, double retailPrice, double length, String unit, String productGroup, String description) {
        this.productId = productId;
        this.costPrice = costPrice;
        this.retailPrice = retailPrice;
        this.length = length;
        this.unit = unit;
        this.productGroup = productGroup;
        this.description = description;
    }
}
