package services;
import app.dto.requestDTO.RoofRequestDTO;
import app.dto.requestDTO.ShedRequestDTO;
import app.dto.requestDTO.carports.CarportNoShedRequestDTO;
import app.dto.requestDTO.carports.CarportRequestDTO;
import app.dto.requestDTO.carports.CarportShedRequestDTO;
import app.dto.responseDTO.carports.CarportNoShedResponseDTO;
import app.dto.responseDTO.carports.CarportResponseDTO;
import app.dto.responseDTO.carports.CarportShedResponseDTO;
import app.exceptions.CalculatorException;
import app.exceptions.DatabaseException;
import app.services.ServiceFactory;
import org.junit.jupiter.api.Test;
import org.thymeleaf.TemplateEngine;
import persistence.MapperTest;

import static org.junit.jupiter.api.Assertions.*;

public class CarportServiceTest extends MapperTest {
    TemplateEngine templateEngine = new TemplateEngine();
    private ServiceFactory serviceFactory = new ServiceFactory(templateEngine);
    ShedRequestDTO shedRequestDTO = new ShedRequestDTO(400, 350, "Birketræ", true);
    RoofRequestDTO roofRequestDTO = new RoofRequestDTO(25, "Eternittag B6 - sortblå", "Højt tag");
    CarportRequestDTO carportRequestDTO = new CarportShedRequestDTO(600, 230, 650, roofRequestDTO, shedRequestDTO);

    @Test
    public void createCarportTest() throws CalculatorException, DatabaseException {
        int actualCarportId = serviceFactory.getCarportService().createCarport(carportRequestDTO);

        //We are creating the 7th entry in db
        assertEquals(7, actualCarportId);
    }

    @Test
    public void getCarportTestWithShed() throws DatabaseException {
        int carportIdWithShed = 1;

        CarportResponseDTO response = serviceFactory.getCarportService().getCarport(carportIdWithShed);

        //Exists in db so should never be null
        assertNotNull(response);

        //Should be instance of carportShedDTO
        assertInstanceOf(CarportShedResponseDTO.class, response);

        CarportShedResponseDTO shedResponseCarport = (CarportShedResponseDTO) response;

        //Check both roof and shed dto's contains data
        assertNotNull(shedResponseCarport.getShedResponseDTO());
        assertNotNull(shedResponseCarport.getRoofResponseDTO());
    }

    @Test
    public void getCarportTestNoShed() throws DatabaseException {
        int carportIdNoShed = 3;

        CarportResponseDTO response = serviceFactory.getCarportService().getCarport(3);

        //Exists in db so should never be null
        assertNotNull(response);

        //Should be instance of carportNoShedDTO
        assertInstanceOf(CarportNoShedResponseDTO.class, response);

        CarportNoShedResponseDTO noShedResponseDTO = (CarportNoShedResponseDTO) response;

        //Roof should not be null
        assertNotNull(noShedResponseDTO.getRoofResponseDTO());
    }

    @Test
    public void updateCarportTest() throws CalculatorException, DatabaseException {
        //First add the global carport to db
        int carportId = serviceFactory.getCarportService().createCarport(carportRequestDTO);

        //Create carport with new dimensions
        int expectedNewWidth = 550;
        int expectedNewLength = 600;
        CarportRequestDTO carportNewDimensions = new CarportShedRequestDTO(
                expectedNewWidth, 230, expectedNewLength, roofRequestDTO, shedRequestDTO);

        serviceFactory.getCarportService().updateCarport(carportId, carportNewDimensions);

        //Get updated carport from db
        CarportResponseDTO updatedCarport = serviceFactory.getCarportService().getCarport(carportId);

        //Check the updated carport has new dimensions
        assertEquals(expectedNewWidth, updatedCarport.getWidth());
        assertEquals(expectedNewLength, updatedCarport.getLength());
    }

    @Test
    public void deleteCarportTestNoShed() throws DatabaseException {
        CarportResponseDTO carportResponseDTO = serviceFactory.getCarportService().getCarport(3);

        //Checking that it is not null before deleting
        assertNotNull(carportResponseDTO);

        //Deleting carport id 3, which contains no shed
        serviceFactory.getCarportService().deleteCarport(3);

        //Should get an exception since no longer exists
        assertThrows(Exception.class, () -> serviceFactory.getCarportService().getCarport(3));
    }

    @Test
    public void deleteCarportTestWithShed() throws DatabaseException {
        CarportResponseDTO carportResponseDTO = serviceFactory.getCarportService().getCarport(1);

        //Checking that it is not null before deleting
        assertNotNull(carportResponseDTO);

        //Deleting carport id 3, which contains no shed
        serviceFactory.getCarportService().deleteCarport(1);

        //Should get an exception since no longer exists
        assertThrows(Exception.class, () -> serviceFactory.getCarportService().getCarport(1));
    }

    @Test
    public void convertCarportResponseToRequestNoShedTest() throws DatabaseException {
        //Dimensions from db
        double cpWidth = 450;
        double cpLength = 780;
        //Roof
        double roofSlope = 25;
        String roofMaterial = "Eternittag B6 - sortblå";
        String roofType = "Højt tag";

        CarportResponseDTO actual = serviceFactory.getCarportService().getCarport(3);
        //Double check response has the correct dimensions
        assertEquals(cpWidth, actual.getWidth());
        assertEquals(cpLength, actual.getLength());

        CarportRequestDTO expected = serviceFactory.getCarportService().convertCarportResponseToRequest(actual);

        //Should be instance of noshed carport
        assertInstanceOf(CarportNoShedRequestDTO.class, expected);
        RoofRequestDTO roofExpected = expected.getRoofRequestDTO();

        //Roof
        assertEquals(roofSlope, roofExpected.getRoofSlope());
        assertEquals(roofMaterial, roofExpected.getRoofMaterial());
        assertEquals(roofType, roofExpected.getRoofType());

        //Carport
        assertEquals(cpWidth, expected.getWidth());
        assertEquals(cpLength, expected.getLength());

    }

    @Test
    public void convertCarportResponseToRequestWithShedTest() throws DatabaseException {
        //Dimensions from db
        double cpWidth = 300;
        double cpLength = 500;
        //Roof
        double roofSlope = 15;
        String roofMaterial = "Betontagsten - sort";
        String roofType = "Højt tag";
        //Shed
        double sWidth = 240;
        double sLength = 300;
        String sSiding = "Beklædning: 19x100mm Profilbrædt (1 på 2 beklædning)";
        boolean sFloor = true;

        CarportResponseDTO actual = serviceFactory.getCarportService().getCarport(1);
        //Double check response has the correct dimensions
        assertEquals(cpWidth, actual.getWidth());
        assertEquals(cpLength, actual.getLength());

        CarportRequestDTO expectedTemp = serviceFactory.getCarportService().convertCarportResponseToRequest(actual);

        //Should be instance of shedcarport
        assertInstanceOf(CarportShedRequestDTO.class, expectedTemp);

        //Cast it to get shedrequest
        CarportShedRequestDTO expected = (CarportShedRequestDTO) expectedTemp;

        RoofRequestDTO roofExpected = expectedTemp.getRoofRequestDTO();
        ShedRequestDTO shedExpected = expected.getShedRequestDTO();

        //Check if same dimensions
        assertEquals(cpWidth, expected.getWidth());
        assertEquals(cpLength, expected.getLength());

        //Roof
        assertEquals(roofSlope, roofExpected.getRoofSlope());
        assertEquals(roofMaterial, roofExpected.getRoofMaterial());
        assertEquals(roofType, roofExpected.getRoofType());

        //Shed
        assertEquals(sWidth, shedExpected.getWidth());
        assertEquals(sLength, shedExpected.getLength());
        assertEquals(sSiding, shedExpected.getSiding());
        assertEquals(sFloor, shedExpected.isFloor());
    }
}

