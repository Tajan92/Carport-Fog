package app.services;

import app.dto.responseDTO.ProductsPartsListEntryResponseDTO;
import app.entities.*;
import app.exceptions.DatabaseException;
import app.persistence.PartsListMapper;
import app.persistence.ProductMapper;
import app.persistence.RoofMapper;
import app.persistence.ShedMapper;
import app.services.converters.CarportConverter;
import app.services.converters.PartsListConverter;
import app.services.converters.RoofConverter;
import app.services.converters.ShedConverter;
import app.services.utils.PartsListCalculator;

import java.util.List;


public class PartsListService {
    private PartsListMapper partsListMapper;
    private ShedMapper shedMapper;
    private RoofMapper roofMapper;
    ProductMapper productMapper;
    private CarportConverter carportConverter;
    private ShedConverter shedConverter;
    private RoofConverter roofConverter;
    PartsListConverter partsListConverter;
    PartsListCalculator partsListUtil = new PartsListCalculator();

    public int createPartsListId() throws DatabaseException {
        return partsListMapper.createPartListId();
    }

    public void createProductsPartsListEntries(Carport carport, Shed shed, Roof roof) throws DatabaseException {
        List<Product> products = productMapper.getAllProducts();
        List<ProductsPartsListEntry> allEntries = partsListUtil.createProductsPartsList(carport, shed, roof, products);
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
