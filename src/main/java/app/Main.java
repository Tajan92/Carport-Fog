package app;

import app.config.SessionConfig;
import app.config.ThymeleafConfig;
import app.controllers.*;
import app.entities.SalesRep;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.SalesRepMapper;
import app.services.ServiceFactory;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinThymeleaf;

import java.util.Locale;

public class Main {

    public static void main(String[] args) {
        ServiceFactory serviceFactory = new ServiceFactory(ThymeleafConfig.templateEngine());
        Locale.setDefault(new Locale("en", "US"));
        // Initializing Javalin and Jetty webserver
        Javalin app = Javalin.create(config -> {
            config.staticFiles.add("/public");
            config.jetty.modifyServletContextHandler(handler -> handler.setSessionHandler(SessionConfig.sessionConfig()));
            config.fileRenderer(new JavalinThymeleaf(ThymeleafConfig.templateEngine()));
            config.staticFiles.add("/templates");
        }).start(7070);

        CustomerController customerController = new CustomerController();
        MainController mainController = new MainController();
        SalesRepController salesRepController = new SalesRepController();
        QuoteController quoteController = new QuoteController();
        OrderController orderController = new OrderController();
        InquiryController inquiryController = new InquiryController();
        BlueprintController blueprintController = new BlueprintController();

        blueprintController.addRoutes(app,serviceFactory);
        salesRepController.addRoutes(app,serviceFactory);
        customerController.addRoutes(app, serviceFactory);
        inquiryController.addRoutes(app,serviceFactory);
        quoteController.addRoutes(app,serviceFactory);
        orderController.getRoutes(app,serviceFactory);
        mainController.addRoutes(app, serviceFactory);
    }
}