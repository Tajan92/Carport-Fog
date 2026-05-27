package app.controllers;

import app.dto.requestDTO.users.CustomerRequestDTO;
import app.dto.requestDTO.users.LoginCustomerRequestDTO;
import app.dto.responseDTO.CustomerResponseDTO;
import app.dto.responseDTO.InquiryResponseDTO;
import app.dto.responseDTO.OrderResponseDTO;
import app.dto.responseDTO.QuoteResponseDTO;
import app.exceptions.CalculatorException;
import app.exceptions.DatabaseException;
import app.services.ServiceFactory;
import app.services.utils.UserValidator;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.List;

public class CustomerController {

    public void addRoutes(Javalin app, ServiceFactory serviceFactory){
        app.get("/register", ctx -> ctx.render("register.html"));
        app.post("/register", ctx -> createCustomer(ctx, serviceFactory));
        app.get("/customer/login", ctx -> ctx.render("customer-login.html"));
        app.post("/customer/login", ctx -> customerLogin(ctx, serviceFactory));
        app.get("/load/customer/profile/page", ctx -> loadCustomerProfilePage(ctx, serviceFactory));
        app.get("/getAllCustomers", ctx -> loadAllCustomers(ctx, serviceFactory));
        app.get("/getCustomerById/{customer_id}", ctx -> getCustomerById(ctx, serviceFactory));
    }

    public void createCustomer(Context ctx, ServiceFactory serviceFactory){
        String firstName = ctx.formParam("first_name");
        String lastName = ctx.formParam("last_name");
        String email = ctx.formParam("email");
        String password = ctx.formParam("password");
        String passwordCheck = ctx.formParam("password-check");
        String phoneNumber = ctx.formParam("phone_number");
        String address = ctx.formParam("address");
        String zip = ctx.formParam("zip");

        CustomerRequestDTO request = new CustomerRequestDTO(firstName, lastName, email, password, passwordCheck, phoneNumber, address, zip);

        try {
            List<String> messages = serviceFactory.getUserService().createCustomer(request);
            if (messages.isEmpty()) {
                ctx.redirect("/customer/login");
            } else {
                StringBuilder messageBuilder = new StringBuilder("| ");
                for (String errorMsg : messages) {
                    if (errorMsg != null && messageBuilder.length() < 95) {
                        messageBuilder.append(errorMsg).append(" | ");
                    }
                }
                ctx.attribute("errorMessage", messageBuilder.toString());
                ctx.render("register.html");
            }
        } catch (DatabaseException e) {
            ctx.attribute("errorMessage", "Der skete en databasefejl: " + e.getMessage());
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

    public void loadCustomerProfilePage(Context ctx, ServiceFactory serviceFactory) throws DatabaseException, CalculatorException {
        if (!UserValidator.isCustomer(ctx)){
            ctx.redirect("/");
            return;
        }

        CustomerResponseDTO response = ctx.sessionAttribute("currentUser");
        //information about customer
        ctx.attribute("customer-profile", response);

        //Inquiries
        List<InquiryResponseDTO> inquiries = serviceFactory.getInquiryService().getAllInquiriesByCustomerId(response.getId());
        ctx.attribute("inquiries-profile", inquiries);

        //Received quotes
        List<QuoteResponseDTO> quotes = serviceFactory.getQuoteService().getAllQuotesByCustomerId(response.getId());
        ctx.attribute("quotes-profile", quotes);

        //Orders
        List<OrderResponseDTO> orders = serviceFactory.getOrderService().getAllOrdersByCustomerId(response.getId());
        ctx.attribute("orders-profile", orders);

        ctx.render("profile-page.html");
    }

    public void loadAllCustomers(Context ctx, ServiceFactory serviceFactory) throws DatabaseException {
        if (!UserValidator.isAdmin(ctx)){
            ctx.redirect("/");
            return;
        }
        List<CustomerResponseDTO> customers = serviceFactory.getUserService().getAllCustomers();
        ctx.attribute("all-customers", customers);

        ctx.render("all-customers.html");
    }

    public void getCustomerById(Context ctx, ServiceFactory serviceFactory) throws DatabaseException, CalculatorException {
        if (!UserValidator.isAdmin(ctx)){
            ctx.redirect("/");
            return;
        }
        int customerId = Integer.parseInt(ctx.pathParam("customer_id"));

        CustomerResponseDTO response = serviceFactory.getUserService().getCustomer(customerId);
        ctx.attribute("customer-overview", response);

        //Inquiries
        List<InquiryResponseDTO> inquiries = serviceFactory.getInquiryService().getAllInquiriesByCustomerId(response.getId());
        ctx.attribute("customer-inquiries-overview", inquiries);

        //Received quotes
        List<QuoteResponseDTO> quotes = serviceFactory.getQuoteService().getAllQuotesByCustomerId(response.getId());
        ctx.attribute("customer-quotes-profile", quotes);

        //Orders
        List<OrderResponseDTO> orders = serviceFactory.getOrderService().getAllOrdersByCustomerId(response.getId());
        ctx.attribute("customer-orders-profile", orders);

        ctx.render("customer-details.html");
    }
}
