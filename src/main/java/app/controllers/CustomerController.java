package app.controllers;

import app.dto.requestDTO.users.CustomerRequestDTO;
import app.dto.requestDTO.users.LoginCustomerRequestDTO;
import app.dto.responseDTO.CustomerResponseDTO;
import app.dto.responseDTO.InquiryResponseDTO;
import app.entities.User;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.services.ServiceFactory;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.List;

public class CustomerController {

    public void addRoutes(Javalin app, ServiceFactory serviceFactory){
        app.get("/register", ctx -> ctx.render("customer-register.html"));
        app.post("/register", ctx -> createCustomer(ctx, serviceFactory));
        app.get("/customer/login", ctx -> ctx.render("customer-login.html"));
        app.post("/customer/login", ctx -> customerLogin(ctx, serviceFactory));
        app.get("/load/customer/profile/page", ctx -> loadCustomerProfilePage(ctx, serviceFactory));
        app.get("/view/customer/profile/page", ctx -> viewCustomerProfilePage(ctx, serviceFactory));
        app.get("/getCustomerById/{customer_id}", ctx -> getCustomerById(ctx, serviceFactory));
        app.get("/getAllCustomers", ctx -> getAllCustomers(ctx, serviceFactory));
    }

    public void createCustomer(Context ctx, ServiceFactory serviceFactory) throws DatabaseException {
        String firstName = ctx.formParam("first_name");
        String lastName = ctx.formParam("last_name");
        String email = ctx.formParam("email");
        String password = ctx.formParam("password");
        String passwordCheck = ctx.formParam("password-check");
        String phoneNumber = ctx.formParam("phone_number");
        String address = ctx.formParam("address");
        String zip = ctx.formParam("zip");

        CustomerRequestDTO request = new CustomerRequestDTO(firstName, lastName, email, password, passwordCheck, phoneNumber, address, zip);

        List<String> messages = serviceFactory.getUserService().createCustomer(request);
        if (messages.isEmpty()){
            ctx.redirect("/login");
        } else {
            String message = "| ";
            for (String errorMsg : messages) {
                if (errorMsg != null && message.length()<95) {
                    message = message + errorMsg + " | ";
                    System.out.println(message);
                }
            }
            ctx.attribute("errorMessage", message);
            ctx.render("register.html");
        }
    }

    public void customerLogin(Context ctx, ServiceFactory serviceFactory){
            String email = ctx.formParam("email");
            String password = ctx.formParam("password");
            LoginCustomerRequestDTO request = new LoginCustomerRequestDTO(email, password);
            try {
                CustomerResponseDTO response = serviceFactory.getUserService().customerLogin(request);
                ctx.sessionAttribute("currentUser", response);
                ctx.redirect("/");
            } catch (DatabaseException e) {
                ctx.attribute("msg", e.getMessage());
                ctx.render("customer-login.html");
            }
    }

    public void loadCustomerProfilePage(Context ctx, ServiceFactory serviceFactory){
        CustomerResponseDTO response = ctx.sessionAttribute("currentUser");
        //information about customer
        ctx.attribute("customer", response);

        //Get all inquiries from customer and pass to frontend
        List<InquiryResponseDTO> inquries = serviceFactory.getInquiryService().

        //Recieved quotes

        //Payed quotes = orders
    }




}
