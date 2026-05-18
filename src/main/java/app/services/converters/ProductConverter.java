package app.services.converters;

import app.dto.responseDTO.ProductResponseDTO;
import app.entities.Product;

public class ProductConverter {

    public ProductResponseDTO convertProductToDTO(Product product){
       double length = product.getLength();
       String unit = product.getUnit();
       String productGroup = product.getProductGroup();
       String description = product.getDescription();
       return new ProductResponseDTO(length, unit, productGroup, description);
    }
}
