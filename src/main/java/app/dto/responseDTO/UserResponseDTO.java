package app.dto.responseDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter

public abstract class UserResponseDTO {
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
}
