package app.dto.requestDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class OrderRequestDTO {
    private int customerId;
    private int salesRepId;
    private int carportId;
    private double orderPrice;
    private int partsListId;
    private double orderDiscount;
}
