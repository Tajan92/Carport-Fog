package persistence;

import app.entities.Customer;
import app.entities.SalesRep;
import app.exceptions.DatabaseException;
import app.persistence.CustomerMapper;
import app.persistence.SalesRepMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SalesRepMapperTest extends MapperTest {

    @Test
    void getSalesRepByIdTest() throws DatabaseException {
        SalesRepMapper salesRepMapper = new SalesRepMapper();
        SalesRep expectedSalesRep = new SalesRep(1, "Thomas", "Møller", "thomas@carport.dk", "hashed_rep1", "11111111");
        SalesRep actualSalesRep = salesRepMapper.getSalesRepById(1);

        assertEquals(expectedSalesRep, actualSalesRep);
    }
}
