package persistence;

import app.entities.Carport;
import app.exceptions.DatabaseException;
import app.persistence.CarportMapper;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PartsListMapperTest extends MapperTest{
    @Test
    void createCarportTest() throws DatabaseException {
        CarportMapper carportMapper = new CarportMapper();



    }

    @Test
    void getCarportByIdTest() throws DatabaseException {
        CarportMapper carportMapper = new CarportMapper();
        Carport expectedCarport = new Carport(1, 3.00, 2.08, 5.00, 24999.00, 1, 0, 0);

        Carport actualCarport = carportMapper.getCarportById(1, 1, 0, 0);

        assertEquals(expectedCarport.getCarportId(), actualCarport.getCarportId());
    }

    @Test
    void updateCarportByIdTest() throws DatabaseException {
        CarportMapper carportMapper = new CarportMapper();
        Carport expectedCarport = new Carport(1, 4.00, 2.50, 5.00, 23000, 1, 0, 0);

        carportMapper.updateCarportById(expectedCarport, 1);
        Carport actualCarport = carportMapper.getCarportById(1, 1, 0, 0);

        assertEquals(expectedCarport.getCarportId(), actualCarport.getCarportId());
    }

    @Test
    void removeCarportByIdTest() throws DatabaseException {
        CarportMapper carportMapper = new CarportMapper();
        carportMapper.removeCarportById(1);
        int size = carportMapper.getAllCarports().size();

        assertEquals(5,size);
    }

    @Test
    void getAllCarportTest() throws DatabaseException {
        CarportMapper carportMapper = new CarportMapper();
        int size = carportMapper.getAllCarports().size();

        assertEquals(6,size);
    }
}
