package app.dto.responseDTO;
import app.entities.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter

public class ProductsPartsListEntryResponseDTO {
    private Product product;
    private String placementDescription;
    private double quantity;


}

