package persistence;

import app.entities.Customer;
import app.entities.Inquiry;
import app.exceptions.DatabaseException;
import app.persistence.CustomerMapper;
import app.persistence.InquiryMapper;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class InquiryMapperTest extends MapperTest {

   @Test
   void createInquiryTest() throws DatabaseException {
       InquiryMapper inquiryMapper = new InquiryMapper();
       int inquiryId = inquiryMapper.createInquiry(new Inquiry(2,"Ønsker skur med halvt bredde",4));

       assertEquals(7,inquiryId);
   }



}
