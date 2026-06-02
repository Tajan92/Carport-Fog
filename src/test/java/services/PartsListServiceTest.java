package services;

import app.dto.requestDTO.RoofRequestDTO;
import app.dto.requestDTO.ShedRequestDTO;
import app.dto.requestDTO.carports.CarportRequestDTO;
import app.dto.requestDTO.carports.CarportShedRequestDTO;
import app.dto.responseDTO.PartsListResponseDTO;
import app.entities.ProductsPartsListEntry;
import app.exceptions.CalculatorException;
import app.exceptions.DatabaseException;
import app.services.ServiceFactory;
import org.thymeleaf.TemplateEngine;
import persistence.MapperTest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PartsListServiceTest extends MapperTest {
    TemplateEngine templateEngine = new TemplateEngine();
    ServiceFactory serviceFactory = new ServiceFactory(templateEngine);

    @Test
    public void createPartsListIdTest() throws DatabaseException {
        int expectedId = 7;

        int actualId = serviceFactory.getPartsListService().createPartsListId();

        //6 entries in db so next should be 7
        assertEquals(expectedId, actualId);
    }

    @Test
    public void createProductsPartsListEntriesTest() throws CalculatorException, DatabaseException {
        ShedRequestDTO shedRequestDTO = new ShedRequestDTO(400, 350, "Beklædning: 9x1220x2440mm Krydsfiner m/spor (Svalehale)", true);
        RoofRequestDTO roofRequestDTO = new RoofRequestDTO(25, "Eternittag B6 - sortblå", "Højt tag");
        CarportRequestDTO carportRequestDTO = new CarportShedRequestDTO(600, 230, 650, roofRequestDTO, shedRequestDTO);

        List<ProductsPartsListEntry> entries = serviceFactory.getPartsListService().createProductsPartsListEntries(carportRequestDTO);

        //Check the returned list isn't null and empty
        assertNotNull(entries);
        assertFalse(entries.isEmpty());
    }

    @Test
    public void getPartsListTest() throws DatabaseException, CalculatorException {
        int existingCarportId = 1;
        int existingPartsListId = 1;

        PartsListResponseDTO response = serviceFactory.getPartsListService().getPartsList(existingCarportId);

        //Check the returned DTO contains right id and list of entries exists
        assertEquals(existingPartsListId, response.getPartsListId());
        assertNotNull(response.getPartsListEntries());
    }
}
