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
import app.services.utils.PasswordUtil;
import app.services.utils.UserValidator;
import java.util.List;

public class UserService {
    private final UserConverter userConverter;
    private final LoginMapper loginMapper;
    private final CustomerMapper customerMapper;

    public UserService(LoginMapper loginMapper, CustomerMapper customerMapper) {
        this.loginMapper = loginMapper;
        this.customerMapper = customerMapper;
        this.userConverter = new UserConverter();
    }

    private SalesRepResponseDTO adminLogin(LoginSalesRepRequestDTO loginSalesRepDTO) throws DatabaseException {
        String email = loginSalesRepDTO.getEmail();
        String password = loginSalesRepDTO.getPassword();

        SalesRep salesRep = loginMapper.salesRepLogin(email);

        if (!PasswordUtil.checkPassword(password, salesRep.getPassword())){
            throw new DatabaseException("Forkert email eller adgangskode, prøv igen!");
        }
        return userConverter.convertSalesRepToDto(salesRep);
    }

    private CustomerResponseDTO customerLogin(LoginCustomerRequestDTO loginCustomerDTO) throws DatabaseException {
        String email = loginCustomerDTO.getEmail();
        String password = loginCustomerDTO.getPassword();

        Customer customer = loginMapper.customerLogin(email);

        if (!PasswordUtil.checkPassword(password, customer.getPassword())){
            throw new DatabaseException("Forkert email eller adgangskode, prøv igen!");
        }
        return userConverter.convertCustomerToDto(customer);
    }

    public void createCustomer(CustomerRequestDTO customerRequestDTO) throws DatabaseException {
        List<String> messages = UserValidator.validate(customerRequestDTO);

        String hashedPassword = PasswordUtil.hashPassword(customerRequestDTO.getPassword());
        customerRequestDTO.setPassword(hashedPassword);

        Customer customer = userConverter.convertCustomerDTOtoEntity(customerRequestDTO);
        if (messages.isEmpty()){
            customerMapper.createCustomer(customer);
        }
        if (!messages.isEmpty()){
            throw new DatabaseException(messages.toString());
        }
    }
}
