package persistence;
import app.entities.Customer;
import app.exceptions.DatabaseException;
import app.persistence.CustomerMapper;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CustomerMapperTest extends MapperTest{
    @Test
    void createCustomerTest() throws DatabaseException {
        CustomerMapper customerMapper = new CustomerMapper();
        int customerId = customerMapper.createCustomer(new Customer("Peter", "Parker", "peter@parker.com","Spiderman", "59524752", "firskovvej 1", "2800", "Kongens Lyngby"));

        // Created 6 in MapperTest, so should be 7.
        assertEquals(7, customerId);
    }

    @Test
    void getCustomerByIdTest() throws DatabaseException {
        CustomerMapper customerMapper = new CustomerMapper();
        Customer expectedCustomer = new Customer(1,"Anders","Jensen","anders@email.dk","hashed_pw1","12345678","Elmevej 4","2100","København Ø");
        Customer actualCustomer = customerMapper.getCustomerById(1);

        assertEquals(expectedCustomer.getId(), actualCustomer.getId());
    }
}
