package app.services;

import app.persistence.*;
import lombok.Getter;

@Getter

public class ServiceFactory {
    private CarportService carportService;
    private InquiryService inquiryService;
    private QuoteService quoteService;
    private UserService userService;

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

        this.carportService = new CarportService(partsListMapper, carportMapper, roofMapper, shedMapper);
        this.inquiryService = new InquiryService(carportMapper, customerMapper, inquiryMapper);
        this.quoteService = new QuoteService(quoteMapper, carportMapper, customerMapper, salesRepMapper);
        this.userService = new UserService(loginMapper, customerMapper);
    }
}
