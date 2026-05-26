package app.controllers;

import app.services.ServiceFactory;
import io.javalin.Javalin;

public class SalesRepController {

    public void addRoutes(Javalin app, ServiceFactory serviceFactory){
        app.get("/loginSalesRep", ctx -> ctx.render("admin-login.html"));
        app.get("/carportMakerSalesRep", ctx -> ctx.render("admin-carport-maker.html"));
        app.get("/adminProfilePage", ctx -> ctx.render("admin-my-page.html")); /* Test route lavet af AJ*/

//        app.post("/loginSalesRep", ctx -> loginSalesRep(ctx, serviceFactory));
    }
}
