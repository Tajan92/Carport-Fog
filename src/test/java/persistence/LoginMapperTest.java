package persistence;

import app.entities.Customer;
import app.entities.SalesRep;
import app.exceptions.DatabaseException;
import app.persistence.LoginMapper;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class LoginMapperTest extends MapperTest{
    LoginMapper loginMapper = new LoginMapper();

    @Test
    void salesRepLogin() throws DatabaseException {
        SalesRep expectedSalesRep = new SalesRep(1, "Thomas", "Møller", "thomas@carport.dk", "hashed_rep1", "11111111");
        SalesRep actualSalesRep = loginMapper.salesRepLogin("thomas@carport.dk", "hashed_rep1");

        assertEquals(expectedSalesRep, actualSalesRep);

    }


    @Test
    void customerLogin() throws DatabaseException {
        Customer expectedCustomer = new Customer(1, "Anders", "Jensen", "anders@email.dk", "hashed_pw1", "12345678", "Elmevej 4", "2100", "København Ø");
        Customer actualCustomer = loginMapper.customerLogin("anders@email.dk", "hashed_pw1");

        assertEquals(expectedCustomer, actualCustomer);

    }



}
