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
        app.get("/customer/register", ctx -> ctx.render("customer-register.html"));
        app.get("/customer/login", ctx -> ctx.render("customer-login.html"));

        app.get("/customer/my/page", ctx -> customerMyPage(ctx, serviceFactory));
        app.get("/admin/customer/details/{customer_id}", ctx -> getCustomerDetails(ctx, serviceFactory));

        app.post("/customer/register", ctx -> createCustomer(ctx, serviceFactory));
        app.post("/customer/login", ctx -> customerLogin(ctx, serviceFactory));
        //inquiry, quote, order details page
    }

    public void createCustomer(Context ctx, ServiceFactory serviceFactory){
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
                ctx.redirect("/");
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

    public void customerMyPage(Context ctx, ServiceFactory serviceFactory) throws DatabaseException, CalculatorException {
        if (!UserValidator.isCustomer(ctx)){
            ctx.redirect("/");
            return;
        }

        CustomerResponseDTO response = ctx.sessionAttribute("currentUser");
        //information about customer
        ctx.attribute("customer-my-page", response);

        //Inquiries
        List<InquiryResponseDTO> inquiries = serviceFactory.getInquiryService().getAllInquiriesByCustomerId(response.getId());
        ctx.attribute("customer-inquiries-my-page", inquiries);

        //Received quotes
        List<QuoteResponseDTO> quotes = serviceFactory.getQuoteService().getAllQuotesByCustomerId(response.getId());
        ctx.attribute("customer-quotes-my-page", quotes);

        //Orders
        List<OrderResponseDTO> orders = serviceFactory.getOrderService().getAllOrdersByCustomerId(response.getId());
        ctx.attribute("customer-orders-my-page", orders);

        ctx.render("customer-my-page.html");
    }

    public void getCustomerDetails(Context ctx, ServiceFactory serviceFactory) throws DatabaseException, CalculatorException {
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
