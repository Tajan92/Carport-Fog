package app.services;

import app.dto.requestDTO.users.CreateCustomerDTO;
import app.dto.requestDTO.users.LoginCustomerDTO;
import app.dto.requestDTO.users.LoginSalesRepDTO;
import app.dto.responseDTO.CustomerResponseDTO;
import app.dto.responseDTO.SalesRepResponseDTO;
import app.entities.Customer;
import app.entities.SalesRep;
import app.exceptions.DatabaseException;
import app.persistence.CustomerMapper;
import app.persistence.LoginMapper;
import app.services.converters.UserConverter;
import app.services.utils.UserValidator;

import java.util.List;


public class UserService {
    UserConverter userConverter = new UserConverter();
    UserValidator userValidator = new UserValidator();
    LoginMapper loginMapper = new LoginMapper();
    CustomerMapper customerMapper = new CustomerMapper();

    private SalesRepResponseDTO adminLogin(LoginSalesRepDTO loginSalesRepDTO) throws DatabaseException {
        String email = loginSalesRepDTO.getEmail();
        String password = loginSalesRepDTO.getPassword();
        SalesRep salesRep = loginMapper.salesRepLogin(email, password);
        return userConverter.convertSalesRepToDto(salesRep);
    }

    private CustomerResponseDTO customerLogin(LoginCustomerDTO loginCustomerDTO) throws DatabaseException {
        String email = loginCustomerDTO.getEmail();
        String password = loginCustomerDTO.getPassword();
        Customer customer = loginMapper.customerLogin(email, password);
        return userConverter.convertCustomerToDto(customer);
    }

    private void createCustomer(CreateCustomerDTO createCustomerDTO) throws DatabaseException {
        List<String> messages = userValidator.validate(createCustomerDTO);
        Customer customer = userConverter.convertCustomerDTOtoEntity(createCustomerDTO);
        if (messages.isEmpty()){
            customerMapper.createCustomer(customer);
        }
    }

}
