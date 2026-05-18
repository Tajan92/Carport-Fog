package app.services.converters;

import app.dto.requestDTO.OrderRequestDTO;
import app.dto.responseDTO.OrderResponseDTO;
import app.dto.responseDTO.ProductsPartsListEntryResponseDTO;
import app.entities.Carport;
import app.entities.Order;
import app.entities.Product;
import app.entities.ProductsPartsListEntry;
import app.exceptions.DatabaseException;
import app.persistence.PartsListMapper;

import java.util.ArrayList;
import java.util.List;

public class PartsListConverter {
    PartsListMapper partsListMapper = new PartsListMapper();

    public List<ProductsPartsListEntryResponseDTO> convertProductsPartsListToDTO(Carport carport) throws DatabaseException {

        List<ProductsPartsListEntryResponseDTO> responseDTOS = new ArrayList<>();

        List<ProductsPartsListEntry> productsPartsListEntries = partsListMapper.getPartsListById(carport.getPartsListId());

        for (ProductsPartsListEntry productsPartsListEntry : productsPartsListEntries) {
            int productsPartsListEntryId = productsPartsListEntry.getProductsPartsListEntryId();
            Product product = productsPartsListEntry.getProduct();

            double quantity = productsPartsListEntry.getQuantity();

            responseDTOS.add(new ProductsPartsListEntryResponseDTO(productsPartsListEntryId, product, quantity));
        }
        return responseDTOS;
    }
}
