package app.entities;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter

public class Order {
    private int orderId;
    private double orderPrice;
    private Customer customer;
    private Carport carport;
    private SalesRep salesRep;
    private PartsList partsListId;
}
