package app.dto.responseDTO;
import app.entities.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter

public class ProductsPartsListEntryResponseDTO {
    private int productsPartsListEntryId;
    private Product product;
    private String placementDescription;
    private double quantity;

    public ProductsPartsListEntryResponseDTO(int productsPartsListEntryId, Product product, double quantity) {
        this.productsPartsListEntryId = productsPartsListEntryId;
        this.product = product;
        this.quantity = quantity;
    }
}

