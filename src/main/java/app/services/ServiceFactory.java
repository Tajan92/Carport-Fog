package app.services;

import app.persistence.*;
import lombok.Getter;
import org.thymeleaf.TemplateEngine;

@Getter

public class ServiceFactory {
    private CarportService carportService;
    private InquiryService inquiryService;
    private QuoteService quoteService;
    private UserService userService;
    private OrderService orderService;
    private PartsListService partsListService;
    private BlueprintService blueprintService;
    private PriceService priceService;
    private MailService mailService;

    public ServiceFactory(TemplateEngine templateEngine)  {
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


        this.partsListService = new PartsListService(partsListMapper,shedMapper, roofMapper, productMapper, carportMapper);
        this.carportService = new CarportService(partsListService, partsListMapper, carportMapper, roofMapper, shedMapper);
        this.userService = new UserService(loginMapper, customerMapper,salesRepMapper);
        this.inquiryService = new InquiryService(inquiryMapper, carportService, carportMapper, roofMapper, shedMapper, productMapper, userService, partsListService);
        this.quoteService = new QuoteService(quoteMapper, carportService, customerMapper, salesRepMapper, carportMapper, userService, productMapper, partsListMapper);
        this.orderService = new OrderService(orderMapper, carportService, userService, partsListService, productMapper);
        this.blueprintService = new BlueprintService(productMapper);
        this.priceService = new PriceService();
        this.mailService = new MailService(templateEngine);
    }
}
