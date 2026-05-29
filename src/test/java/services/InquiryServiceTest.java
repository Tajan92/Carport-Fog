package services;
import app.dto.requestDTO.InquiryRequestDTO;
import app.dto.responseDTO.InquiryResponseDTO;
import app.exceptions.CalculatorException;
import app.exceptions.DatabaseException;
import app.services.ServiceFactory;
import org.junit.jupiter.api.Test;
import persistence.MapperTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class InquiryServiceTest extends MapperTest {

    private ServiceFactory serviceFactory = new ServiceFactory();

    @Test
    public void createInquiryTest() throws DatabaseException {
        //Adding both existing customer and carport from db
        int existingCustomerId = 1;
        int existingCarportId = 1;
        String remark = "Vi kunne ikke se hvilke gulve i har, det kunne vi godt tænke os at høre mere om";

        InquiryRequestDTO inquiryRequestDTO = new InquiryRequestDTO(existingCustomerId, remark, existingCarportId);

        //Should not give an error
        assertDoesNotThrow(() -> serviceFactory.getInquiryService().createInquiry(inquiryRequestDTO));
    }

    @Test
    public void getInquiryTest() throws DatabaseException, CalculatorException {
        int existingInquiryId = 1;
        int existingCustomerId = 1;
        int existingCarportId = 1;
        String existingRemark = "Ønsker carport med skur til haveredskaber";

        InquiryResponseDTO response = serviceFactory.getInquiryService().getInquiry(existingInquiryId);

        //Verify that the attatched DTO's are on the response
        assertNotNull(response.getCustomerResponseDTO());
        assertNotNull(response.getCarportResponseDTO());

        //Check if the information matches
        assertEquals(existingInquiryId, response.getInquiryId());
        assertEquals(existingCustomerId, response.getCustomerResponseDTO().getId());
        assertEquals(existingCarportId, response.getCarportResponseDTO().getCarportId());
        assertEquals(existingRemark, response.getRemark());
    }

    @Test
    public void getAllInquiriesTest() throws DatabaseException, CalculatorException {
        List<InquiryResponseDTO> inquiries = serviceFactory.getInquiryService().getAllInquiries();

        //The list shouldn't be null or empty
        assertNotNull(inquiries);
        assertFalse(inquiries.isEmpty());

        InquiryResponseDTO firstInquiry = inquiries.get(0);

        //Check if the first inquiry has correct id = 1
        assertEquals(1, firstInquiry.getInquiryId());
        assertNotNull(firstInquiry.getRemark());
    }

    @Test
    public void deleteInquiryTest() throws DatabaseException, CalculatorException {
        int inquiryIdToDelete = 1;

        InquiryResponseDTO inquiryResponseDTO = serviceFactory.getInquiryService().getInquiry(inquiryIdToDelete);

        //Confirm that the inquiry to be deleted exists
        assertNotNull(inquiryResponseDTO);

        serviceFactory.getInquiryService().deleteInquiry(inquiryIdToDelete);

        //Now it's deleted method should throw exception
        assertThrows(Exception.class, () -> serviceFactory.getInquiryService().getInquiry(inquiryIdToDelete));
    }
}