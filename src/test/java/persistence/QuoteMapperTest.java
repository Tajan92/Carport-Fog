package persistence;

import app.entities.Inquiry;
import app.entities.Quote;
import app.exceptions.DatabaseException;
import app.persistence.InquiryMapper;
import app.persistence.QuoteMapper;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class QuoteMapperTest extends MapperTest {


    @Test
    void createQuoteTest() throws DatabaseException {
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


                QuoteMapper quoteMapper = new QuoteMapper();
                int quoteId = quoteMapper.createQuote(new Quote(20.000, 7, 1, 1));

                assertEquals(9, quoteId);
            }
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    @Test
    void getQuoteByIdTest() throws DatabaseException {
        QuoteMapper quoteMapper = new QuoteMapper();
        Quote expectedQuote = new Quote(1, 24999, 1, 1, 1);
        Quote actualQuot = quoteMapper.getQuoteById(1);

        assertEquals(expectedQuote, actualQuot);
    }

    @Test
    void getAllQuotesTest() throws DatabaseException{
        QuoteMapper quoteMapper = new QuoteMapper();
        int allQuotes =  quoteMapper.getAllQuotes().size();

        assertEquals(6,allInquiries);
    }
}
