package app.dto.requestDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor

public class ShedRequestDTO {
    private double width;
    private double length;
    private String siding;
    private boolean floor;
}
