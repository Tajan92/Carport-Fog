package app.entities;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter

public class Quote {
    private int quoteId;
    private double quotePrice;
    private int carportId;
    private int customerId;
    private int salesRepId;
}
