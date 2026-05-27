package services;

import app.exceptions.DatabaseException;
import app.services.ServiceFactory;
import persistence.MapperTest;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PartsListServiceTest extends MapperTest {
    ServiceFactory serviceFactory = new ServiceFactory();

    @Test
    public void createPartsListIdTest() throws DatabaseException {
        int expectedId = 7;

        int actualId = serviceFactory.getPartsListService().createPartsListId();

        //6 entries in db so next should be 7
        assertEquals(expectedId, actualId);
    }

    @Test
    public void createProductsPartsListEntriesTest(){

    }
}
