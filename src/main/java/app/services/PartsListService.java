package app.services;

import app.dto.responseDTO.ProductsPartsListEntryResponseDTO;
import app.entities.Carport;
import app.entities.ProductsPartsListEntry;
import app.entities.Product;
import app.exceptions.DatabaseException;
import app.persistence.PartsListMapper;
import app.persistence.RoofMapper;
import app.persistence.ShedMapper;
import app.services.converters.CarportConverter;
import app.services.converters.PartsListConverter;
import app.services.converters.RoofConverter;
import app.services.converters.ShedConverter;
import app.services.utils.PartsListUtil;

import java.util.List;



public class PartsListService {
    private PartsListMapper partsListMapper;
    private ShedMapper shedMapper;
    private RoofMapper roofMapper;
    private CarportConverter carportConverter;
    private ShedConverter shedConverter;
    private RoofConverter roofConverter;
    PartsListConverter partsListConverter;

    public int createPartsListId() throws DatabaseException {
        return partsListMapper.createPartListId();
    }

    public void createProductsPartsListEntries(Carport carport) throws DatabaseException {
        List<ProductsPartsListEntry> allEntries = PartsListUtil.createProductsPartsList(carport);
        for (ProductsPartsListEntry entry : allEntries) {
            partsListMapper.createProductPartsList(entry.getProduct().getProductId(), carport.getPartsListId(), entry.getQuantity());
        }
    }

    public List<ProductsPartsListEntryResponseDTO> getProductsPartsListEntries(Carport carport) throws DatabaseException {
        String placementDescription = Placementfinder.getDescription(carport);
        List<ProductsPartsListEntryResponseDTO> productsPartsListEntriesWithDescription = partsListConverter.convertProductsPartsListToDTO(carport);

        for (ProductsPartsListEntryResponseDTO productsPartsListEntryResponseDTO : productsPartsListEntriesWithDescription) {
            productsPartsListEntryResponseDTO.setPlacementDescription(placementDescription);
        }

        return productsPartsListEntriesWithDescription;
    }

}
