package app.entities;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode

public class Order {
    private int orderId;
    private int customerId;
    private int salesRepId;
    private int carportId;
    private double orderPrice;
    private int partsListId;
    private double orderDiscount;

    public Order(int customerId, int salesRepId, int carportId, double orderPrice, int partsListId, double orderDiscount) {
        this.customerId = customerId;
        this.salesRepId = salesRepId;
        this.carportId = carportId;
        this.orderPrice = orderPrice;
        this.partsListId = partsListId;
        this.orderDiscount = orderDiscount;
    }
}
