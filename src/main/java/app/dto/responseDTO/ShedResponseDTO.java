package app.dto.responseDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor

public class ShedResponseDTO {
    private int shedId;
    private double width;
    private double length;
    private String siding;
    private boolean floor;

}
