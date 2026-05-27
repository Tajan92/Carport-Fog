package app.controllers;
import app.dto.requestDTO.users.LoginSalesRepRequestDTO;
import app.dto.responseDTO.*;
import app.exceptions.CalculatorException;
import app.exceptions.DatabaseException;
import app.services.ServiceFactory;
import app.services.utils.UserValidator;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.List;

public class SalesRepController {

    public void addRoutes(Javalin app, ServiceFactory serviceFactory){
        app.get("/loginSalesRepRender", ctx -> ctx.render("salesRepLogin.html"));
        app.post("/loginSalesRep", ctx -> loginSalesRep(ctx, serviceFactory));
        app.get("/admin/my/page", ctx -> loadAdminMyPage(ctx, serviceFactory));
    }

    public void loginSalesRep(Context ctx, ServiceFactory serviceFactory){
        String email = ctx.formParam("email");
        String password = ctx.formParam("password");
        LoginSalesRepRequestDTO request = new LoginSalesRepRequestDTO(email, password);
        try {
            SalesRepResponseDTO response = serviceFactory.getUserService().adminLogin(request);
            ctx.sessionAttribute("currentUser", response);
            ctx.redirect("/");
        } catch (DatabaseException e) {
            ctx.attribute("msg", e.getMessage());
            ctx.render("customer-login.html");
        }
    }

    public void loadAdminMyPage(Context ctx, ServiceFactory serviceFactory) throws DatabaseException, CalculatorException {
        if (!UserValidator.isAdmin(ctx)){
            ctx.redirect("/");
            return;
        }

        //Get lists of customers, inquiries, quotes and orders to display
        List<CustomerResponseDTO> customers = serviceFactory.getUserService().getAllCustomers();
        List<InquiryResponseDTO> inquiries = serviceFactory.getInquiryService().getAllInquiries();
        List<QuoteResponseDTO> quotes = serviceFactory.getQuoteService().getAllQuotes();
        List<OrderResponseDTO> orders = serviceFactory.getOrderService().getAllOrders();

        ctx.attribute("admin-customers", customers);
        ctx.attribute("admin-inquiries", inquiries);
        ctx.attribute("admin-quotes", quotes);
        ctx.attribute("admin-orders", orders);

        ctx.render("admin-my-page.html");
    }
}
