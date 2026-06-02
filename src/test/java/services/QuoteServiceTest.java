package services;

import app.dto.requestDTO.QuoteRequestDTO;
import app.dto.responseDTO.QuoteAdminResponseDTO;
import app.dto.responseDTO.QuoteResponseDTO;
import app.exceptions.DatabaseException;
import app.services.ServiceFactory;
import org.junit.jupiter.api.Test;
import org.thymeleaf.TemplateEngine;
import persistence.MapperTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class QuoteServiceTest extends MapperTest {
    TemplateEngine templateEngine = new TemplateEngine();
    ServiceFactory serviceFactory = new ServiceFactory(templateEngine);

    @Test
    public void createQuoteTest(){
        QuoteRequestDTO request = new QuoteRequestDTO(1, 250000.00, 3, 3, 0);

        //Creating order should not give exception if successful
        assertDoesNotThrow(() -> serviceFactory.getQuoteService().createQuote(request));
    }

    @Test
    public void getQuoteTest() throws DatabaseException {
        int existingCustomerId = 1;
        int existingSalesRepId = 1;
        int existingCarportId = 1;
        double existingQuotePrice = 24999.00;

        QuoteResponseDTO response = serviceFactory.getQuoteService().getQuote(1);

        //Verify that the attatched DTO's are on the response
        assertNotNull(response.getCustomerResponseDTO());
        assertNotNull(response.getCarportResponseDTO());
        assertNotNull(response.getSalesRepResponseDTO());

        //Check the id's and price is correct from db
        assertEquals(existingCustomerId, response.getCustomerResponseDTO().getId());
        assertEquals(existingCarportId, response.getCarportResponseDTO().getCarportId());
        assertEquals(existingSalesRepId, response.getSalesRepResponseDTO().getId());
        assertEquals(existingQuotePrice, response.getTotalPrice());
    }

    @Test
    public void getAllQuotesTest() throws DatabaseException {
        List<QuoteAdminResponseDTO> quotes = serviceFactory.getQuoteService().getAllQuotesAdmin();
        double firstOrderPrice = 24999.00;

        //The list shouldn't be null or empty
        assertNotNull(quotes);
        assertFalse(quotes.isEmpty());

        QuoteResponseDTO firstQuote = quotes.get(0);
        assertNotNull(firstQuote.getCustomerResponseDTO());
        assertNotNull(firstQuote.getSalesRepResponseDTO());
        assertNotNull(firstQuote.getCarportResponseDTO());

        //Check if the first quote has correct id = 1 and the price is correct
        assertEquals(1, firstQuote.getQuoteId());
        assertEquals(firstOrderPrice, firstQuote.getTotalPrice());
    }

    @Test
    public void getAllQuotesByCustomerId() throws DatabaseException {
        int customerId = 1;
        int amountOfQuotesByCustomer = 2;

        List<QuoteResponseDTO> quotes = serviceFactory.getQuoteService().getAllQuotesByCustomerId(customerId);

        //The list shouldn't be null or empty
        assertNotNull(quotes);
        assertFalse(quotes.isEmpty());

        QuoteResponseDTO firstQuote = quotes.get(0);

        //Check if other DTO's are attached
        assertNotNull(firstQuote.getCustomerResponseDTO());
        assertNotNull(firstQuote.getSalesRepResponseDTO());
        assertNotNull(firstQuote.getCarportResponseDTO());

        //Check if the first order has correct id = 1 and the size of list is 2
        assertEquals(customerId, firstQuote.getQuoteId());
        assertEquals(amountOfQuotesByCustomer, quotes.size());
    }

    @Test
    public void deleteQuoteTest() throws DatabaseException {
        int quoteIdToDelete = 1;

        QuoteResponseDTO deletedQuote = serviceFactory.getQuoteService().getQuote(quoteIdToDelete);

        //Confirm that the quote to be deleted exists
        assertNotNull(deletedQuote);

        serviceFactory.getQuoteService().deleteQuote(quoteIdToDelete);

        //Now it's deleted method should throw exception
        assertThrows(Exception.class, () -> serviceFactory.getQuoteService().deleteQuote(quoteIdToDelete));
    }
}
