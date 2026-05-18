package app.entities;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter

public class ProductsPartsListEntry {
    private int productsPartsListEntryId;
    private Product product;
    private double quantity;
}
