package services;

import app.dto.requestDTO.users.CustomerRequestDTO;
import app.dto.requestDTO.users.LoginCustomerRequestDTO;
import app.dto.requestDTO.users.LoginSalesRepRequestDTO;
import app.dto.responseDTO.CustomerResponseDTO;
import app.dto.responseDTO.SalesRepResponseDTO;
import app.exceptions.DatabaseException;
import app.services.ServiceFactory;
import org.junit.jupiter.api.Test;
import org.thymeleaf.TemplateEngine;
import persistence.MapperTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest extends MapperTest {
    TemplateEngine templateEngine = new TemplateEngine();
    ServiceFactory serviceFactory = new ServiceFactory(templateEngine);

    @Test
    public void adminLoginTest() throws DatabaseException {
        int expectedId = 1;
        String expectedFName = "Thomas";
        String expectedLName = "Møller";
        String expectedEmail = "thomas@carport.dk";
        String expectedPNumber = "11111111";
        String expectedRole = "ADMIN";

        //Creating request based on existing salesrep in db
        LoginSalesRepRequestDTO loginInfo = new LoginSalesRepRequestDTO(expectedEmail, "hashed_rep!1");

        //Used for getting the correct hashed pw token
        //System.out.println(org.mindrot.jbcrypt.BCrypt.hashpw("hashed_rep!1", org.mindrot.jbcrypt.BCrypt.gensalt()));

        SalesRepResponseDTO response = serviceFactory.getUserService().adminLogin(loginInfo);

        //Now to check response for correct info
        assertEquals(expectedId, response.getId());
        assertEquals(expectedFName, response.getFirstName());
        assertEquals(expectedLName, response.getLastName());
        assertEquals(expectedEmail, response.getEmail());
        assertEquals(expectedPNumber, response.getPhoneNumber());
        assertEquals(expectedRole, response.getRole());
    }

    @Test
    public void customerLoginTest() throws DatabaseException {
        int expectedId = 1;
        String expectedFName = "Anders";
        String expectedLName = "Jensen";
        String expectedEmail = "anders@email.dk";
        String expectedPNumber = "12345678";
        String expectedRole = "CUSTOMER";
        String expectedAddress = "Elmevej 4";
        String expectedZip = "2100";
        String expectedTown = "København Ø";


        //Creating request based on existing customer in db
        LoginCustomerRequestDTO loginInfo = new LoginCustomerRequestDTO(expectedEmail, "hashed_pw?1");

        //Used for getting the correct hashed pw token
        //System.out.println(org.mindrot.jbcrypt.BCrypt.hashpw("hashed_pw?1", org.mindrot.jbcrypt.BCrypt.gensalt()));

        CustomerResponseDTO response = serviceFactory.getUserService().customerLogin(loginInfo);

        //Now to check response for correct info
        assertEquals(expectedId, response.getId());
        assertEquals(expectedFName, response.getFirstName());
        assertEquals(expectedLName, response.getLastName());
        assertEquals(expectedEmail, response.getEmail());
        assertEquals(expectedPNumber, response.getPhoneNumber());
        assertEquals(expectedRole, response.getRole());
        assertEquals(expectedAddress, response.getAddress());
        assertEquals(expectedZip, response.getZipCode());
        assertEquals(expectedTown, response.getTown());
    }

    @Test
    public void createCustomerTest() throws DatabaseException {
        List<String> error = new ArrayList<>();
        error.add("Adgangskoder skal være ens");

        CustomerRequestDTO badCustomerInput = new CustomerRequestDTO(
                "Jens",
                "Hansen",
                "jens.hansen@gmail.com",
                "Hansen2026!",
                "Hansen2024",
                "20304050",
                "Algade 42, 1. th",
                "4760");

        CustomerRequestDTO goodCustomerInput = new CustomerRequestDTO(
                "Jens",
                "Hansen",
                "jens.hansen@gmail.com",
                "Hansen2026!",
                "Hansen2026!",
                "20304050",
                "Algade 42, 1. th",
                "4760"
        );

        //Password and passwordcheck doesn't match so Uservalidator should return errormessage from arraylist
        assertEquals(error, serviceFactory.getUserService().createCustomer(badCustomerInput));

        //Good input should work without issues
        assertDoesNotThrow(() -> serviceFactory.getUserService().createCustomer(goodCustomerInput));
    }

    @Test
    public void getCustomerTest() throws DatabaseException {
        int expectedId = 1;
        String expectedFName = "Anders";
        String expectedLName = "Jensen";
        String expectedEmail = "anders@email.dk";
        String expectedPNumber = "12345678";
        String expectedRole = "CUSTOMER";
        String expectedAddress = "Elmevej 4";
        String expectedZip = "2100";
        String expectedTown = "København Ø";

        CustomerResponseDTO response = serviceFactory.getUserService().getCustomer(expectedId);

        //Check if correct data is gotten from db
        assertEquals(expectedId, response.getId());
        assertEquals(expectedFName, response.getFirstName());
        assertEquals(expectedLName, response.getLastName());
        assertEquals(expectedEmail, response.getEmail());
        assertEquals(expectedPNumber, response.getPhoneNumber());
        assertEquals(expectedRole, response.getRole());
        assertEquals(expectedAddress, response.getAddress());
        assertEquals(expectedZip, response.getZipCode());
        assertEquals(expectedTown, response.getTown());
    }

    @Test
    public void getSalesRepTest() throws DatabaseException {
        int expectedId = 1;
        String expectedFName = "Thomas";
        String expectedLName = "Møller";
        String expectedEmail = "thomas@carport.dk";
        String expectedPNumber = "11111111";
        String expectedRole = "ADMIN";

        SalesRepResponseDTO response = serviceFactory.getUserService().getSalesRep(expectedId);

        //Now to check response for correct info
        assertEquals(expectedId, response.getId());
        assertEquals(expectedFName, response.getFirstName());
        assertEquals(expectedLName, response.getLastName());
        assertEquals(expectedEmail, response.getEmail());
        assertEquals(expectedPNumber, response.getPhoneNumber());
        assertEquals(expectedRole, response.getRole());
    }


}
