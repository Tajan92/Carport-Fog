package app.services;
import app.dto.requestDTO.carports.CarportRequestDTO;
import app.dto.requestDTO.carports.CarportShedRequestDTO;
import app.dto.responseDTO.PartsListResponseDTO;
import app.dto.responseDTO.ProductsPartsListEntryResponseDTO;
import app.entities.*;
import app.exceptions.CalculatorException;
import app.exceptions.DatabaseException;
import app.persistence.*;
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
    private CarportMapper carportMapper;
    ProductMapper productMapper;
    private CarportConverter carportConverter;
    private ShedConverter shedConverter;
    private RoofConverter roofConverter;
    PartsListConverter partsListConverter;
    PartsListCalculator partsListCalculator = new PartsListCalculator();
    CarportService carportService;

    public int createPartsListId() throws DatabaseException {
        return partsListMapper.createPartListId();
    }

    public void createProductsPartsListEntries(CarportRequestDTO carportRequestDTO) throws DatabaseException, CalculatorException {
        List<Product> products = productMapper.getAllProducts();
        Carport carport = carportConverter.covertCarportDTOToEntity(carportRequestDTO);
        Roof roof = roofConverter.convertRoofDTOtoEntity(carportRequestDTO.getRoofRequestDTO());
        Shed shed;

        if (carportRequestDTO instanceof CarportShedRequestDTO withShed){
            shed = shedConverter.convertShedDTOtoEntity(withShed.getShedRequestDTO());
        }else {
            shed = null;
        }

        List<ProductsPartsListEntry> allEntries = partsListCalculator.createProductsPartsList(carport, shed, roof, products);
        for (ProductsPartsListEntry entry : allEntries) {
            partsListMapper.createProductPartsList(entry.getProduct().getProductId(), carport.getPartsListId(), entry.getQuantity());
        }
    }

    public PartsListResponseDTO getPartsList(int carportId) throws DatabaseException, CalculatorException {
        List<Product> allProducts = productMapper.getAllProducts();
        Carport carport = carportMapper.getCarportById(carportId);
        Roof roof = roofMapper.getRoofById(carport.getRoofId());
        Shed shed;

        if (carport.getShedId() != null){
            shed = shedMapper.getShedById(carport.getShedId());
        } else {
            shed = null;
        }

        List<ProductsPartsListEntry> partsListEntries = partsListCalculator.createProductsPartsList(carport, shed, roof, allProducts);
        List<ProductsPartsListEntryResponseDTO> productsPartsListEntriesWithDescription = partsListConverter.convertProductsPartsListToDTO(partsListEntries);

        return new PartsListResponseDTO(carport.getPartsListId(), productsPartsListEntriesWithDescription);
    }

}
