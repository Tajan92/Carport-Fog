package app.services;
import app.persistence.*;
import lombok.Getter;

@Getter
public class ServiceFactory {
    CarportService carportService;
    InquiryService inquiryService;
    QuoteService quoteService;
    OrderService orderService;
    UserService userService;

    public ServiceFactory() {
        CarportMapper carportMapper = new CarportMapper();
        CustomerMapper customerMapper = new CustomerMapper();
        InquiryMapper inquiryMapper = new InquiryMapper();
        LoginMapper loginMapper = new LoginMapper();
        OrderMapper orderMapper = new OrderMapper();
        PartsListMapper partsListMapper = new PartsListMapper();
        ProductMapper productMapper = new ProductMapper();
        QuoteMapper quoteMapper = new QuoteMapper();
        RoofMapper roofMapper = new RoofMapper();
        SalesRepMapper salesRepMapper = new SalesRepMapper();
        ShedMapper shedMapper = new ShedMapper();

        //TODO:we need to add the mappers later
        this.carportService = new CarportService(partsListMapper, carportMapper, roofMapper, shedMapper);
        this.inquiryService = new InquiryService();
        this.quoteService = new QuoteService();
        this.orderService = new OrderService();
        this.userService = new UserService();
    }
}
