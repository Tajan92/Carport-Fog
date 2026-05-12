package app.entities;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter

public class Order {
    private int orderId;
    private int customerId;
    private int salesRepId;
    private int carportId;
    private double orderPrice;
    private int partsListId;
}
