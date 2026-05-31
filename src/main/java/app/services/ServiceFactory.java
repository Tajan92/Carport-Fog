package app.services;

import app.persistence.*;
import app.services.utils.GmailEmailSender;
import app.services.utils.GmailEmailSenderHTML;
import lombok.Getter;

@Getter

public class ServiceFactory {
    private CarportService carportService;
    private InquiryService inquiryService;
    private QuoteService quoteService;
    private UserService userService;
    private OrderService orderService;
    private ShedService shedService;
    private RoofService roofService;
    private PartsListService partsListService;
    private BlueprintService blueprintService;
    private PriceService priceService;
    private MailService mailService;

    public ServiceFactory()  {
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
        GmailEmailSender gmailEmailSender = new GmailEmailSender();
        GmailEmailSenderHTML gmailEmailSenderHTML = new GmailEmailSenderHTML();

        this.partsListService = new PartsListService(partsListMapper,shedMapper, roofMapper, productMapper, carportMapper);
        this.carportService = new CarportService(partsListService, partsListMapper, carportMapper, roofMapper, shedMapper);
        this.userService = new UserService(loginMapper, customerMapper,salesRepMapper);
        this.inquiryService = new InquiryService(inquiryMapper, carportService, customerMapper, carportMapper, roofMapper, shedMapper, productMapper, userService, partsListService);
        this.quoteService = new QuoteService(quoteMapper, carportService, customerMapper, salesRepMapper, carportMapper, userService, productMapper, partsListMapper);
        this.orderService = new OrderService(orderMapper, carportService, userService, partsListService, productMapper);
        this.shedService = new ShedService(shedMapper);
        this.roofService = new RoofService(roofMapper);
        this.blueprintService = new BlueprintService(productMapper);
        this.priceService = new PriceService();
        this.mailService = new MailService(gmailEmailSender, gmailEmailSenderHTML);
    }
}
