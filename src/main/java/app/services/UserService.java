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
import app.persistence.SalesRepMapper;
import app.services.converters.UserConverter;
import app.services.utils.PasswordUtil;
import app.services.utils.UserValidator;

import java.util.ArrayList;
import java.util.List;

public class UserService {
    private final UserConverter userConverter;
    private final LoginMapper loginMapper;
    private final CustomerMapper customerMapper;
    private SalesRepMapper salesRepMapper;

    public UserService(LoginMapper loginMapper, CustomerMapper customerMapper, SalesRepMapper salesRepMapper) {
        this.loginMapper = loginMapper;
        this.customerMapper = customerMapper;
        this.userConverter = new UserConverter();
        this.salesRepMapper = salesRepMapper;
    }

    public SalesRepResponseDTO adminLogin(LoginSalesRepRequestDTO loginSalesRepDTO) throws DatabaseException {
        String email = loginSalesRepDTO.getEmail();
        String password = loginSalesRepDTO.getPassword();

        SalesRep salesRep = loginMapper.salesRepLogin(email);

        if (!PasswordUtil.checkPassword(password, salesRep.getPassword())){
            throw new DatabaseException("Forkert email eller adgangskode, prøv igen!");
        }
        return userConverter.convertSalesRepToDto(salesRep);
    }

    public CustomerResponseDTO customerLogin(LoginCustomerRequestDTO loginCustomerDTO) throws DatabaseException {
        String email = loginCustomerDTO.getEmail();
        String password = loginCustomerDTO.getPassword();

        Customer customer = loginMapper.customerLogin(email);

        if (!PasswordUtil.checkPassword(password, customer.getPassword())){
            throw new DatabaseException("Forkert email eller adgangskode, prøv igen!");
        }
        return userConverter.convertCustomerToDto(customer);
    }

    public List<String> createCustomer(CustomerRequestDTO customerRequestDTO) throws DatabaseException {
        List<String> messages = UserValidator.validate(customerRequestDTO);

        String hashedPassword = PasswordUtil.hashPassword(customerRequestDTO.getPassword());
        customerRequestDTO.setPassword(hashedPassword);

        Customer customer = userConverter.convertCustomerDTOtoEntity(customerRequestDTO);
        if (messages.isEmpty()){
            customerMapper.createCustomer(customer);
        }
        return messages;
    }

    public CustomerResponseDTO getCustomer (int customerId) throws DatabaseException {
        Customer customer = customerMapper.getCustomerById(customerId);

        return userConverter.convertCustomerToDto(customer);
    }

    public List<CustomerResponseDTO> getAllCustomers() throws DatabaseException {
        List<Customer> customers = customerMapper.getAllCustomers();

        List<CustomerResponseDTO> responseDTOS = new ArrayList<>();
        for (Customer customer : customers) {
            responseDTOS.add(userConverter.convertCustomerToDto(customer));
        }
        return responseDTOS;
    }

    public SalesRepResponseDTO getSalesRep (int salesRepId) throws DatabaseException {
        SalesRep salesRep = salesRepMapper.getSalesRepById(salesRepId);

        return userConverter.convertSalesRepToDto(salesRep);
    }
}
