package persistence;

import app.entities.Customer;
import app.entities.Inquiry;
import app.exceptions.DatabaseException;
import app.persistence.CarportMapper;
import app.persistence.CustomerMapper;
import app.persistence.InquiryMapper;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InquiryMapperTest extends MapperTest {

    @Test
    void createInquiryTest() throws DatabaseException {
        try (Connection connection = connectionPool.getConnection()) {
            try (Statement statement = connection.createStatement()) {
                statement.execute("INSERT INTO test.sheds (shed_width, shed_length, siding, floor) VALUES " +
                        "(2.40, 3.00, 'Trykimp. bræddebeklædning', true)");

                statement.execute("INSERT INTO test.roofs (roof_slope, roof_material, roof_type) VALUES " +
                        "(15, 'Betontagsten', 'Rejsning')");

                statement.execute("INSERT INTO test.addons (shed_id, roof_id) VALUES" +
                        "(5, 5)");

                statement.execute("INSERT INTO test.parts_lists DEFAULT VALUES");

                statement.execute("INSERT INTO test.carports (carport_width, carport_height, carport_length, addon_id, price, parts_list_id) VALUES" +
                        "(3.00, 2.08, 5.00, 1, 24999.00, 7)");


                InquiryMapper inquiryMapper = new InquiryMapper();
                int inquiryId = inquiryMapper.createInquiry(new Inquiry(2, "Ønsker skur med halv bredde", 7));

                assertEquals(7, inquiryId);
            }
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
    }
    @Test
    void getInquiryById() throws DatabaseException {
        InquiryMapper inquiryMapper = new InquiryMapper();
        Inquiry expectedInquiry = new Inquiry(1, 1, "Ønsker carport med skur til haveredskaber", 1);
        Inquiry actualInquiry = inquiryMapper.getInquiryById(1);

        assertEquals(expectedInquiry,actualInquiry);
    }

    @Test
    void removeInquiryByIdTest() throws DatabaseException {
        InquiryMapper inquiryMapper = new InquiryMapper();
        inquiryMapper.deleteInquiryById(1);
        int size = inquiryMapper.getAllInquiries().size();

        assertEquals(5,size);
    }


}
