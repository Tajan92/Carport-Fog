package app.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor

public class Carport {

    private int carportId;
    private double width;
    private double length;
    private double price;
    private PartsList partsList;
    private Shed shed;
    private Roof roof;
    private Inquiry inquiry;
    private Quote quote;
    private Order order;

    public void setQuote(Quote quote) {
        this.quote = quote;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
