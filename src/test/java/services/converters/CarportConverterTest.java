package services.converters;

import app.dto.requestDTO.RoofRequestDTO;
import app.dto.requestDTO.carports.CarportNoShedRequestDTO;
import app.dto.requestDTO.carports.CarportRequestDTO;
import app.dto.responseDTO.carports.CarportResponseDTO;
import app.dto.responseDTO.carports.CarportShedResponseDTO;
import app.entities.Carport;
import app.entities.Shed;
import app.exceptions.DatabaseException;
import app.persistence.CarportMapper;
import app.services.converters.CarportConverter;
import org.junit.jupiter.api.Test;
import persistence.MapperTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CarportConverterTest extends MapperTest {
    private CarportConverter carportConverter = new CarportConverter();
    private CarportMapper carportMapper = new CarportMapper();
    RoofRequestDTO roofRequestDTO = new RoofRequestDTO(15, "Betontagsten", "Rejsning");

    @Test
    void convertCarportDTOToEntityTest() throws DatabaseException {

        CarportRequestDTO expectedCarport = new CarportNoShedRequestDTO(3.00, 2.08, 5.00, 24999.00, roofRequestDTO);

        Carport actualCarport = carportConverter.covertCarportDTOToEntity(expectedCarport);

        assertEquals(expectedCarport.getWidth() + expectedCarport.getHeight() + expectedCarport.getLength() + expectedCarport.getPrice(),
                actualCarport.getWidth() + actualCarport.getHeight() + actualCarport.getLength() + actualCarport.getPrice());

    }


    @Test
    void convertCarportNoShedEntityToDTOTest() throws DatabaseException {

        //The third carport in db has no shedid in addons table
        Carport carport = carportMapper.getCarportById(3);
        CarportResponseDTO carportResponseDTO = carportConverter.convertCarportNoShedEntityToDTO(carport);

        assertEquals(carport.getWidth() + carport.getHeight() + carport.getLength() + carport.getPrice(),
                carportResponseDTO.getWidth() + carportResponseDTO.getHeight() + carportResponseDTO.getLength() + carportResponseDTO.getPrice());

    }

    @Test
    void convertCarportShedEntityToDTOTest() throws DatabaseException {
        //First carport in db has a shedid
        Carport carport = carportMapper.getCarportById(1);
        CarportShedResponseDTO carportResponseDTO = carportConverter.convertCarportShedEntityToDTO(carport);

        //Data from test database shed id 1
        Shed shed = new Shed(2.40, 3.00, "Trykimp. bræddebeklædning", true);

        assertEquals(carport.getWidth() + carport.getHeight() + carport.getLength() + carport.getPrice(),
                carportResponseDTO.getWidth() + carportResponseDTO.getHeight() + carportResponseDTO.getLength() + carportResponseDTO.getPrice());

        //Cannot check if the shed is equals, until testing the service layer, since adding a shed happens in service instead of converters now
//        assertEquals(shed.getWidth() + shed.getLength() + shed.getSiding(), carportResponseDTO.getShedResponseDTO().getWidth()
//                + carportResponseDTO.getShedResponseDTO().getLength() + carportResponseDTO.getShedResponseDTO().getSiding());
  }
}




