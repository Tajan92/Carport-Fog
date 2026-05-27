package app.services.converters;

import app.dto.requestDTO.users.CustomerRequestDTO;
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
        String role = "CUSTOMER";

        return new CustomerResponseDTO(id, firstName, lastName, email, phoneNumber, role, address, zipCode, town);
    }

    public Customer convertCustomerDTOtoEntity(CustomerRequestDTO customerRequestDTO) {
        String firstName = customerRequestDTO.getFirstName();
        String lastName = customerRequestDTO.getLastName();
        String email = customerRequestDTO.getEmail();
        String password = customerRequestDTO.getPassword();
        String phoneNumber = customerRequestDTO.getPhoneNumber();
        String address = customerRequestDTO.getAddress();
        String zipCode = customerRequestDTO.getZipCode();

        return new Customer(firstName,lastName,email,password,phoneNumber,address,zipCode);
    }

    public SalesRepResponseDTO convertSalesRepToDto(SalesRep salesRep) {
        int id = salesRep.getId();
        String firstName = salesRep.getFirstName();
        String lastName = salesRep.getLastName();
        String email = salesRep.getEmail();
        String phoneNumber = salesRep.getPhoneNumber();
        String role = "ADMIN";

        return new SalesRepResponseDTO(id, firstName, lastName, email, phoneNumber, role);
    }
}
