package persistence;

import app.entities.Carport;
import app.entities.Roof;
import app.exceptions.DatabaseException;
import app.persistence.RoofMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RoofMapperTest extends MapperTest {


    @Test
    public void createRoofTest() throws DatabaseException {
        RoofMapper roofMapper = new RoofMapper();
        int sizeBefore = roofMapper.getAllRoofs().size();
        Roof roof = new Roof(1.9, "Betontagsten", "Fladt");
        roofMapper.createRoof(roof);
        int sizeAfter = roofMapper.getAllRoofs().size();

        //Expect size to increase by 1
        assertEquals(sizeBefore + 1, sizeAfter);

    }


    @Test
    public void getRoofByID() throws DatabaseException {

        RoofMapper roofMapper = new RoofMapper();

        Roof expectedRoof = new Roof(1,15,"Betontagsten","Rejsning");

        Roof actualRoof = roofMapper.getRoofById(1);

        assertEquals(expectedRoof,actualRoof);
    }

    @Test
    public void getAllRoofsTest() throws DatabaseException {
        RoofMapper roofMapper = new RoofMapper();
        int size = roofMapper.getAllRoofs().size();

        // There is 4 roofs
        assertEquals(4, size);
    }

    @Test
    public void deleteRoofByIdTest() throws DatabaseException {
        RoofMapper roofMapper = new RoofMapper();
        int beforeSize = roofMapper.getAllRoofs().size();
        roofMapper.deleteRoofById(1);
        int afterSize = roofMapper.getAllRoofs().size();

        // size should be beforeSize -1
        assertEquals(beforeSize-1, afterSize);
    }
}
