package app.dto.responseDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter

public class ProductResponseDTO {
    private double length;
    private String unit;
    private String productGroup;
    private String description;
}
