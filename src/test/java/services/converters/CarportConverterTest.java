package services.converters;

import app.dto.requestDTO.RoofRequestDTO;
import app.dto.requestDTO.carports.CarportNoShedRequestDTO;
import app.dto.requestDTO.carports.CarportRequestDTO;
import app.dto.responseDTO.carports.CarportResponseDTO;
import app.entities.Carport;
import app.exceptions.DatabaseException;
import app.persistence.CarportMapper;
import app.services.converters.CarportConverter;
import org.junit.jupiter.api.Test;
import persistence.MapperTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CarportConverterTest extends MapperTest {
    private CarportConverter carportConverter;
    private CarportMapper carportMapper;

    @Test
    void convertCarportDTOToEntityTest() throws DatabaseException {
        RoofRequestDTO roofRequestDTO = new RoofRequestDTO(15, "Betontagsten", "Rejsning");
        CarportRequestDTO expectedCarport = new CarportNoShedRequestDTO(3.00, 2.08, 5.00, 24999.00, roofRequestDTO);

        Carport actualCarport = carportConverter.covertCarportDTOToEntity(expectedCarport);

        assertEquals(expectedCarport.getWidth() + expectedCarport.getHeight() + expectedCarport.getLength() + expectedCarport.getPrice(),
                actualCarport.getWidth() + actualCarport.getHeight() + actualCarport.getLength() + actualCarport.getPrice());

    }


    @Test
    void convertCarportNoShedEntityToDTOTest() {
    }

    @Test
    void convertCarportShedEntityToDTOTest() {
    }
}




