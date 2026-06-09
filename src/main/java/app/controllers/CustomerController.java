package app.controllers;

import app.dto.requestDTO.users.CustomerRequestDTO;
import app.dto.requestDTO.users.LoginCustomerRequestDTO;
import app.dto.responseDTO.*;
import app.exceptions.CalculatorException;
import app.exceptions.DatabaseException;
import app.services.ServiceFactory;
import app.services.utils.UserValidator;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.List;

public class CustomerController {

    public void addRoutes(Javalin app, ServiceFactory serviceFactory) {
        app.get("/customer/register", ctx -> ctx.render("customer-register.html"));
        app.get("/customer/login", ctx -> ctx.render("customer-login.html"));
        app.get("/load/customer/carport/maker", ctx -> loadCarportMaker(ctx));

        app.get("/customer/my/page", ctx -> customerMyPage(ctx, serviceFactory));
        app.get("/admin/customer/details/{customer_id}", ctx -> getCustomerDetails(ctx, serviceFactory));

        app.post("/customer/register", ctx -> createCustomer(ctx, serviceFactory));
        app.post("/customer/login", ctx -> customerLogin(ctx, serviceFactory));
        //inquiry, quote, order details page
    }

    public void createCustomer(Context ctx, ServiceFactory serviceFactory) {
        String firstName = ctx.formParam("first_name");
        String lastName = ctx.formParam("last_name");
        String email = ctx.formParam("email");
        String password = ctx.formParam("password");
        String passwordCheck = ctx.formParam("confirm_password");
        String phoneNumber = ctx.formParam("phone_number");
        String address = ctx.formParam("address");
        String zip = ctx.formParam("zipcode");

        CustomerRequestDTO request = new CustomerRequestDTO(firstName, lastName, email, password, passwordCheck, phoneNumber, address, zip);

        try {
            List<String> messages = serviceFactory.getUserService().createCustomer(request);
            if (messages.isEmpty()) {
                LoginCustomerRequestDTO loginRequest = new LoginCustomerRequestDTO(email, password);
                CustomerResponseDTO response = serviceFactory.getUserService().customerLogin(loginRequest);
                ctx.sessionAttribute("currentUser", response);
                ctx.redirect("/load/customer/carport/maker");
            } else {
                StringBuilder messageBuilder = new StringBuilder("| ");
                for (String errorMsg : messages) {
                    if (errorMsg != null && messageBuilder.length() < 95) {
                        messageBuilder.append(errorMsg).append(" | ");
                    }
                }
                ctx.attribute("errorMessage", messageBuilder.toString());
                System.out.println(messageBuilder);
                ctx.render("customer-register.html");
            }
        } catch (DatabaseException e) {
            ctx.attribute("errorMessage", "Der skete en databasefejl: " + e.getMessage());
            ctx.render("customer-register.html");
        }
    }

    public void customerLogin(Context ctx, ServiceFactory serviceFactory) {
        String email = ctx.formParam("email");
        String password = ctx.formParam("password");
        LoginCustomerRequestDTO request = new LoginCustomerRequestDTO(email, password);
        try {
            CustomerResponseDTO response = serviceFactory.getUserService().customerLogin(request);
            ctx.sessionAttribute("currentUser", response);
            ctx.redirect("/load/customer/carport/maker");
        } catch (DatabaseException e) {
            ctx.attribute("msg", e.getMessage());
            ctx.render("customer-login.html");
        }
    }

    public void customerMyPage(Context ctx, ServiceFactory serviceFactory) throws DatabaseException, CalculatorException {
        if (!UserValidator.isCustomer(ctx)) {
            ctx.redirect("/");
            return;
        }

        CustomerResponseDTO user = ctx.sessionAttribute("currentUser");
        ctx.attribute("currentUser", user);

        //Inquiries
        List<InquiryResponseDTO> inquiries = serviceFactory.getInquiryService().getAllInquiriesByCustomerId(user.getId());
        ctx.attribute("customer_inquiries", inquiries);

        //Received quotes
        List<QuoteResponseDTO> quotes = serviceFactory.getQuoteService().getAllQuotesByCustomerId(user.getId());
        ctx.attribute("customer_quotes", quotes);

        //Orders
        List<OrderResponseDTO> orders = serviceFactory.getOrderService().getAllOrdersByCustomerId(user.getId());
        ctx.attribute("customer_orders", orders);

        ctx.render("customer-my-page.html");
    }

    public void getCustomerDetails(Context ctx, ServiceFactory serviceFactory) throws DatabaseException, CalculatorException {
        if (!UserValidator.isAdmin(ctx)) {
            ctx.redirect("/");
            return;
        }
        SalesRepResponseDTO admin = ctx.sessionAttribute("currentUser");


        int customerId = Integer.parseInt(ctx.pathParam("customer_id"));

        CustomerResponseDTO response = serviceFactory.getUserService().getCustomer(customerId);
        ctx.attribute("customer_overview", response);

        //Inquiries
        List<InquiryResponseDTO> inquiries = serviceFactory.getInquiryService().getAllInquiriesByCustomerId(response.getId());
        ctx.attribute("customer_inquiries_overview", inquiries);

        //Received quotes
        List<QuoteResponseDTO> quotes = serviceFactory.getQuoteService().getAllQuotesByCustomerId(response.getId());
        ctx.attribute("customer_quotes_overview", quotes);

        //Orders
        List<OrderResponseDTO> orders = serviceFactory.getOrderService().getAllOrdersByCustomerId(response.getId());
        ctx.attribute("customer_orders_overview", orders);

        ctx.render("admin-customer-details.html");
    }

    public void loadCarportMaker(Context ctx) {
        UserResponseDTO userResponseDTO = ctx.sessionAttribute("currentUser");
        if (userResponseDTO != null){
            ctx.attribute("currentUser", userResponseDTO);
        }

        Boolean inquiryPending = ctx.sessionAttribute("pending_inquiry");
        if (Boolean.TRUE.equals(inquiryPending)) {
            ctx.attribute("carport_width", Double.parseDouble(ctx.sessionAttribute("pending_carport_width")));
            ctx.attribute("carport_length", Double.parseDouble(ctx.sessionAttribute("pending_carport_length")));
            ctx.attribute("roof_slope", ctx.sessionAttribute("pending_roof_slope"));
            ctx.attribute("roof_material", ctx.sessionAttribute("pending_roof_material"));
            ctx.attribute("roof_type", ctx.sessionAttribute("pending_roof_type"));
            ctx.attribute("shed_status", ctx.sessionAttribute("pending_shed_status"));
            ctx.attribute("shed_length", ctx.sessionAttribute("pending_shed_length"));
            ctx.attribute("shed_siding", ctx.sessionAttribute("pending_shed_siding"));
            ctx.attribute("floor", Boolean.parseBoolean(ctx.sessionAttribute("pending_floor")));
            ctx.attribute("remark", ctx.sessionAttribute("pending_remark"));

            ctx.sessionAttribute("pending_inquiry", null);
            ctx.sessionAttribute("pending_carport_width", null);
            ctx.sessionAttribute("pending_carport_length", null);
            ctx.sessionAttribute("pending_roof_slope", null);
            ctx.sessionAttribute("pending_roof_material", null);
            ctx.sessionAttribute("pending_roof_type", null);
            ctx.sessionAttribute("pending_shed_status", null);
            ctx.sessionAttribute("pending_shed_length", null);
            ctx.sessionAttribute("pending_shed_siding", null);
            ctx.sessionAttribute("pending_floor", null);
            ctx.sessionAttribute("pending_remark", null);
        }
        ctx.render("customer-carport-maker.html");
    }
}
