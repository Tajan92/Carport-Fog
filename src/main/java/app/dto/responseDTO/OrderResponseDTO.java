package app.dto.responseDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter

public class OrderResponseDTO {
    private int orderId;
    private int customerId;
    private int salesRepId;
    private int carportId;
    private double orderPrice;
    private int partsListId;
}
