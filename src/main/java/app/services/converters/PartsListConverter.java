package app.services.converters;
import app.dto.responseDTO.ProductsPartsListEntryResponseDTO;
import app.entities.Carport;
import app.entities.Product;
import app.entities.ProductsPartsListEntry;
import app.exceptions.DatabaseException;
import app.persistence.PartsListMapper;

import java.util.ArrayList;
import java.util.List;

public class PartsListConverter {

    public List<ProductsPartsListEntryResponseDTO> convertProductsPartsListToDTO(List<ProductsPartsListEntry> productsPartsListEntries){
        List<ProductsPartsListEntryResponseDTO> responseDTOS = new ArrayList<>();

        for (ProductsPartsListEntry productsPartsListEntry : productsPartsListEntries) {
            Product product = productsPartsListEntry.getProduct();
            double quantity = productsPartsListEntry.getQuantity();
            String placementDescription = productsPartsListEntry.getPlacementDescription();
            responseDTOS.add(new ProductsPartsListEntryResponseDTO(product, placementDescription, quantity));
        }
        return responseDTOS;
    }
}
