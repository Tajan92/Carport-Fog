package app.entities;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode


public class Quote {
    private int quoteId;
    private double quotePrice;
    private int carportId;
    private int customerId;
    private int salesRepId;

    public Quote(double quotePrice, int carportId, int customerId, int salesRepId) {
        this.quotePrice = quotePrice;
        this.carportId = carportId;
        this.customerId = customerId;
        this.salesRepId = salesRepId;
    }
}
