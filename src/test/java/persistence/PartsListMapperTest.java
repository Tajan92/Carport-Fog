package persistence;
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

        int actualNumberOfProductById = partsListMapper.getPartsListById(7).size();
        //As this started empty and added 2 PartsList it should be 2
        assertEquals(2, actualNumberOfProductById);
    }

    @Test
    void getPartListByIdTest() throws DatabaseException {
        PartsListMapper partsListMapper = new PartsListMapper();

        int actualPartsListCountById = partsListMapper.getPartsListById(1).size();

        //PartListId 1 has 11 productPartsListId's so should be 11.
        assertEquals(11, actualPartsListCountById);
    }

    @Test
    void getAllProductPartsListsTest() throws DatabaseException {
        PartsListMapper partsListMapper = new PartsListMapper();

        int actualPartsListCount = partsListMapper.getAllProductPartsLists().size();

        //products_parts_lists has 68 rows
        assertEquals(68, actualPartsListCount);
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

        partsListMapper.deleteProductPartsListById(1);

        int actualPartsListCountById = partsListMapper.getPartsListById(1).size();

        //PartListId 1 has 11 productPartsListId's so should be 0, af deletion.
        assertEquals(0, actualPartsListCountById);

        partsListMapper.deletePartsListById(1);
        int partListSize = partsListMapper.getAllPartsListsIds().size();

        // There is 6 unique partListId's
        assertEquals(5, partListSize);
    }
}
