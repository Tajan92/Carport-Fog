package persistence;

import app.entities.Shed;
import app.exceptions.DatabaseException;
import app.persistence.ShedMapper;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ShedMapperTest extends MapperTest{
    ShedMapper shedMapper = new ShedMapper();

    @Test
    void createShedTest() throws DatabaseException {
        Shed shed = new Shed(7.0, 8.0, "Trykimp. bræddebeklædning", true);

        int shedId = shedMapper.createShed(shed);

        //5th shed created so id should match that
        assertEquals(5, shedId);
    }

    @Test
    void getShedById() throws DatabaseException {
        Shed expectedShed = new Shed(2.40, 3.00, "Trykimp. bræddebeklædning", true);

        Shed actualShed = shedMapper.getShedById(1);

        assertEquals(expectedShed, actualShed);
    }

    @Test
    void deleteShedById() throws DatabaseException {
        shedMapper.removeShedById(3);

        int size = shedMapper.getAllSheds().size();

        //Deleted one from total of 4 sheds
        assertEquals(3, size);

    }
}
