package app.dto.responseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter

public class SalesRepResponseDTO {
    int id;
    String firstName;
    String lastName;
    String email;
    String phoneNumber;
}
