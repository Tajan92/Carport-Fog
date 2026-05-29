package app.controllers;

import app.dto.requestDTO.OrderRequestDTO;
import app.dto.requestDTO.carports.CarportRequestDTO;
import app.dto.responseDTO.OrderAdminResponseDTO;
import app.dto.responseDTO.CustomerResponseDTO;
import app.dto.responseDTO.OrderResponseDTO;
import app.dto.responseDTO.QuoteResponseDTO;
import app.dto.responseDTO.UserResponseDTO;
import app.exceptions.CalculatorException;
import app.exceptions.DatabaseException;
import app.services.ServiceFactory;
import app.services.utils.UserValidator;
import io.javalin.Javalin;
import io.javalin.http.Context;
import java.util.List;

public class OrderController {

    public void getRoutes(Javalin app, ServiceFactory serviceFactory) {
        app.post("/create/order", ctx -> createOrder(ctx, serviceFactory));
        app.get("/customer/get/order", ctx -> getOrderCustomer(ctx, serviceFactory));
        app.get("/admin/get/order", ctx -> getOrderAdmin(ctx, serviceFactory));
        //app.post("/updateOrder", ctx -> updateOrder(ctx, serviceFactory));
        app.post("/deleteOrder", ctx -> deleteOrder(ctx, serviceFactory));
    }

    public void createOrder(Context ctx, ServiceFactory serviceFactory) throws DatabaseException, CalculatorException {
        CustomerResponseDTO customerResponseDTO = ctx.sessionAttribute("currentUser");
        int quoteId = Integer.parseInt(ctx.formParam("quote_id"));
        QuoteResponseDTO quoteResponseDTO = serviceFactory.getQuoteService().getQuote(quoteId);

        int salesRepId = quoteResponseDTO.getSalesRepResponseDTO().getId();

        //Gets carport from quote and makes it into a request
        CarportRequestDTO carportRequestDTO = serviceFactory.getCarportService().convertCarportResponseToRequest(quoteResponseDTO.getCarportResponseDTO());
        int carportId = serviceFactory.getCarportService().createCarport(carportRequestDTO);
        double orderPrice = quoteResponseDTO.getPrice();
        //Gets partslistresponse and collects id directly
        int partsListId = serviceFactory.getPartsListService().getPartsList(carportId).getPartsListId();
        double discount = quoteResponseDTO.getDiscount();
        serviceFactory.getOrderService().createOrder(new OrderRequestDTO(customerId, salesRepId, carportId, orderPrice, partsListId, discount));

        serviceFactory.getOrderService().createOrder(new OrderRequestDTO(customerResponseDTO.getId(), salesRepId, carportId, orderPrice, partsListId));
        ctx.redirect("/customer/my/page");
    }

    public void getOrderAdmin(Context ctx, ServiceFactory serviceFactory) throws DatabaseException, CalculatorException {
        if (!UserValidator.isAdmin(ctx)) {
            ctx.redirect("/");
            return;
        }
        int orderId = Integer.parseInt(ctx.formParam("order_id"));
        OrderAdminResponseDTO orderResponseDTO = serviceFactory.getOrderService().getOrderAdmin(orderId);

        ctx.attribute("selected_order", orderResponseDTO);
        ctx.render("admin-order-details.html");
    }

    public void getOrderCustomer(Context ctx, ServiceFactory serviceFactory) throws DatabaseException, CalculatorException {
        if (!UserValidator.isCustomer(ctx)) {
            ctx.redirect("/");
            return;
        }
        int orderId = Integer.parseInt(ctx.formParam("order_id"));
        OrderResponseDTO orderResponseDTO = serviceFactory.getOrderService().getOrder(orderId);

        ctx.attribute("selected_order", orderResponseDTO);
        ctx.render("customer-order-details.html");
    }

    public void deleteOrder(Context ctx, ServiceFactory serviceFactory) throws DatabaseException, CalculatorException {
        int orderId = Integer.parseInt(ctx.formParam("order_id"));
        OrderResponseDTO orderResponseDTO = serviceFactory.getOrderService().getOrder(orderId);
        serviceFactory.getOrderService().deleteOrder(orderId);
        serviceFactory.getCarportService().deleteCarport(orderResponseDTO.getCarportResponseDTO().getCarportId());
        ctx.render("admin-all-orders.html");
    }
}
