package app.controllers;

import app.services.ServiceFactory;
import io.javalin.Javalin;

public class CustomerController {

    public void addRoutes(Javalin app, ServiceFactory serviceFactory) {
        app.get("/customerRegister", ctx -> ctx.render("customer-register.html"));
//        app.post("/register", ctx -> createrCustomer(ctx, serviceFactory));
        app.get("/customerLogin", ctx -> ctx.render("customer-login.html"));
//        app.post("/login", ctx -> customerLogin(ctx, serviceFactory));
//
        app.get("/customerProfilePage", ctx -> ctx.render("customer-my-page.html"));
//        app.get("/getCustomerById", ctx -> getCustomerById(ctx, serviceFactory));
//        app.get("/getAllCustomers", ctx -> getAllCustomers(ctx, serviceFactory));
    }


}
