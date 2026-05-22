package app.dto.responseDTO;
import lombok.Getter;

@Getter

public class CustomerResponseDTO extends UserResponseDTO {
    private String address;
    private String zipCode;
    private String town;

    public CustomerResponseDTO(int id, String firstName, String lastName, String email, String phoneNumber, String role, String address, String zipCode, String town) {
        super(id, firstName, lastName, email, phoneNumber, role);
        this.address = address;
        this.zipCode = zipCode;
        this.town = town;
    }
}
