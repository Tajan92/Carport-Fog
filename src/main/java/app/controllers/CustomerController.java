package app.controllers;

import app.services.ServiceFactory;
import io.javalin.Javalin;

public class CustomerController {

    public void addRoutes(Javalin app, ServiceFactory serviceFactory){
        app.get("/register", ctx -> ctx.render("customer-register.html"));
        app.post("/register", ctx -> createrCustomer(ctx, serviceFactory));
        app.get("/login", ctx -> ctx.render("login.html"));
        app.post("/login", ctx -> customerLogin(ctx, serviceFactory));

        app.get("/profilPage", ctx -> ctx.render("profil-page.html"));
        app.get("/getCustomerById", ctx -> getCustomerById(ctx, serviceFactory));
        app.get("/getAllCustomers", ctx -> getAllCustomers(ctx, serviceFactory));
    }




}
