package persistence;
import app.entities.PartsList;
import app.exceptions.DatabaseException;
import app.persistence.PartsListMapper;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PartsListMapperTest extends MapperTest {
    @Test
    void createPartsListTest() throws DatabaseException {
        PartsListMapper partsListMapper = new PartsListMapper();

        partsListMapper.createPartListId();
        int actualNumberOfIds = partsListMapper.getAllPartsListsIds().size();
        //There 6 generated from test, so should be 7 now
        assertEquals(7, actualNumberOfIds);

        partsListMapper.createProductPartsList(3, 7, 5);
        partsListMapper.createProductPartsList(6, 7, 3);

        int actualNumberOfProductById = partsListMapper.getProductsPartsListEntries(7).size();
        //As this started empty and added 2 PartsList it should be 2
        assertEquals(2, actualNumberOfProductById);
    }

    @Test
    void createProductPartsListTest() throws DatabaseException{
        PartsListMapper partsListMapper = new PartsListMapper();

        partsListMapper.createProductPartsList(1, 1, 4);

        //there should be 12 entries to partslistid 1 in the database after we add one
        int size = partsListMapper.getProductsPartsListEntries(1).size();

        assertEquals(12, size);
    }

    @Test
    void getPartListByIdTest() throws DatabaseException {
        PartsListMapper partsListMapper = new PartsListMapper();

        int actualPartsListCountById = partsListMapper.getProductsPartsListEntries(1).size();

        //PartListId 1 has 11 productPartsListId's so should be 11.
        assertEquals(11, actualPartsListCountById);
    }


    @Test
    void getPartsListById() throws DatabaseException{
        PartsListMapper partsListMapper = new PartsListMapper();
        PartsList partsList = partsListMapper.getPartsListById(1);

        //partslistid 1 has 11 entries
        int size = partsList.getPartsListEntries().size();

     assertEquals(11,size);
    }

    @Test
    void getAllPartsListsIdsTest() throws DatabaseException {
        PartsListMapper partsListMapper = new PartsListMapper();

        int partListSize = partsListMapper.getAllPartsListsIds().size();

        // There is 6 unique partListId's
        assertEquals(6, partListSize);
    }

    @Test
    void deleteAllInProductPartsListByIdTest() throws DatabaseException {
        PartsListMapper partsListMapper = new PartsListMapper();

        partsListMapper.deleteAllProductPartsListById(1);

        int actualPartsListCountById = partsListMapper.getProductsPartsListEntries(1).size();

        //PartListId 1 has 11 productPartsListId's so should be 0, af deletion.
        assertEquals(0, actualPartsListCountById);

        partsListMapper.deletePartsListById(1);
        int partListSize = partsListMapper.getAllPartsListsIds().size();

        // There is 6 unique partListId's
        assertEquals(5, partListSize);
    }

    @Test
    void deleteProductPartsListByIDTest() throws DatabaseException {
        PartsListMapper partsListMapper = new PartsListMapper();
        //we create a new "empty" partslistid so we can test it can be deleted

        partsListMapper.createPartListId();

        partsListMapper.deletePartsListById(7);

        int size = partsListMapper.getAllPartsListsIds().size();

        assertEquals(6, size);
    }
}
