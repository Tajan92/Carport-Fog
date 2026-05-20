package app.entities;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter

public class ProductsPartsListEntry {
    private Product product;
    private double quantity;
    private String placementDescription;

    public ProductsPartsListEntry(Product product, double quantity) {
        this.product = product;
        this.quantity = quantity;
    }
}
