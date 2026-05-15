package app.services;

import app.dto.requestDTO.users.CustomerRequestDTO;
import app.dto.requestDTO.users.LoginCustomerRequestDTO;
import app.dto.requestDTO.users.LoginSalesRepRequestDTO;
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

    private SalesRepResponseDTO adminLogin(LoginSalesRepRequestDTO loginSalesRepDTO) throws DatabaseException {
        String email = loginSalesRepDTO.getEmail();
        String password = loginSalesRepDTO.getPassword();
        SalesRep salesRep = loginMapper.salesRepLogin(email, password);
        return userConverter.convertSalesRepToDto(salesRep);
    }

    private CustomerResponseDTO customerLogin(LoginCustomerRequestDTO loginCustomerDTO) throws DatabaseException {
        String email = loginCustomerDTO.getEmail();
        String password = loginCustomerDTO.getPassword();
        Customer customer = loginMapper.customerLogin(email, password);
        return userConverter.convertCustomerToDto(customer);
    }

    private void createCustomer(CustomerRequestDTO customerRequestDTO) throws DatabaseException {
        List<String> messages = userValidator.validate(customerRequestDTO);
        Customer customer = userConverter.convertCustomerDTOtoEntity(customerRequestDTO);
        if (messages.isEmpty()){
            customerMapper.createCustomer(customer);
        }
    }

}
