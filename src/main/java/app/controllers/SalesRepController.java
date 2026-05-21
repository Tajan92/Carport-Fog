package app.controllers;

import app.services.ServiceFactory;
import io.javalin.Javalin;

public class SalesRepController {

    public void addRoutes(Javalin app, ServiceFactory serviceFactory){
        app.get("/loginSalesRepRender", ctx -> ctx.render("salesRepLogin.html"));
        app.post("/loginSalesRep", ctx -> loginSalesRep(ctx, serviceFactory));
    }
}
