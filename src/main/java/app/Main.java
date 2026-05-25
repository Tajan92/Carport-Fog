package app;

import app.config.SessionConfig;
import app.config.ThymeleafConfig;
import app.controllers.CustomerController;
import app.controllers.MainController;
import app.controllers.SalesRepController;
import app.persistence.ConnectionPool;
import app.services.ServiceFactory;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinThymeleaf;

public class Main {

    public static void main(String[] args) {
        ServiceFactory serviceFactory = new ServiceFactory();

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
        salesRepController.addRoutes(app,serviceFactory);
        customerController.addRoutes(app, serviceFactory);
        mainController.addRoutes(app, serviceFactory);
    }
}