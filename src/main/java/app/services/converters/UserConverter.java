package app.services.converters;

import app.dto.requestDTO.users.CreateCustomerDTO;
import app.dto.responseDTO.CustomerResponseDTO;
import app.dto.responseDTO.SalesRepResponseDTO;
import app.entities.Customer;
import app.entities.SalesRep;

public class UserConverter {

    public CustomerResponseDTO convertCustomerToDto(Customer customer) {
        int id = customer.getId();
        String firstName = customer.getFirstName();
        String lastName = customer.getLastName();
        String email = customer.getEmail();
        String phoneNumber = customer.getPhoneNumber();
        String address = customer.getAddress();
        String zipCode = customer.getZipCode();
        String town = customer.getTown();

        return new CustomerResponseDTO(id, firstName, lastName, email, phoneNumber, address, zipCode, town);
    }

    public Customer convertCustomerDTOtoEntity(CreateCustomerDTO createCustomerDTO) {
        String firstName = createCustomerDTO.getFirstName();
        String lastName = createCustomerDTO.getLastName();
        String email = createCustomerDTO.getEmail();
        String password = createCustomerDTO.getPassword();
        String phoneNumber = createCustomerDTO.getPhoneNumber();
        String address = createCustomerDTO.getAddress();
        String zipCode = createCustomerDTO.getZipCode();

        return new Customer(firstName,lastName,email,password,phoneNumber,address,zipCode);
    }

    public SalesRepResponseDTO convertSalesRepToDto(SalesRep salesRep) {
        int id = salesRep.getId();
        String firstName = salesRep.getFirstName();
        String lastName = salesRep.getLastName();
        String email = salesRep.getEmail();
        String phoneNumber = salesRep.getPhoneNumber();

        return new SalesRepResponseDTO(id, firstName, lastName, email, phoneNumber);
    }
}
