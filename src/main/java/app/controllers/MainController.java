package app.controllers;

import app.services.ServiceFactory;
import io.javalin.Javalin;
import io.javalin.http.Context;

public class MainController {

    public void addRoutes(Javalin app, ServiceFactory serviceFactory) {
        app.get("/", ctx -> ctx.render("customer-carport-maker.html"));
        app.get("/logout", ctx -> logout(ctx));
    }

    public void logout(Context ctx) {
        ctx.req().getSession().invalidate();
        ctx.redirect("/login");
    }
}
