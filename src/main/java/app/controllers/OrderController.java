package app.controllers;

import app.dto.responseDTO.OrderResponseDTO;
import app.dto.responseDTO.QuoteResponseDTO;
import app.exceptions.CalculatorException;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.services.ServiceFactory;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.List;

public class OrderController {

    public void getRoutes(Javalin app, ServiceFactory serviceFactory) {
//        app.get("/createOrder", ctx -> createOrder(ctx, serviceFactory));
//        app.get("/getOrder", ctx -> getOrder(ctx, serviceFactory));
//        app.get("/getAllOrders", ctx -> getAllOrders(ctx, serviceFactory));
//        app.post("/updateOrder", ctx -> updateOrder(ctx, serviceFactory));
//        app.post("/deleteOrder", ctx -> deleteOrder(ctx, serviceFactory));
        app.get("/getOrderCustomer", ctx -> ctx.render("customer-order-details")); /* Test route af AJ*/

    }

    public void getOrder(Context ctx, ServiceFactory serviceFactory) throws DatabaseException, CalculatorException {
        int orderId = Integer.parseInt(ctx.pathParam("order_id"));
        OrderResponseDTO orderResponseDTO = serviceFactory.getOrderService().getOrder(orderId);

        ctx.attribute("selected_order", orderResponseDTO);
        ctx.render("admin-order-details.html");
    }

    public void getAllOrders(Context ctx, ServiceFactory serviceFactory) throws DatabaseException, CalculatorException {
        List<OrderResponseDTO> orderResponseDTOS = serviceFactory.getOrderService().getAllOrders();
        ctx.attribute("all_orders", orderResponseDTOS);
        ctx.render("admin-all-orders.html");
    }

    public void deleteOrder(Context ctx, ServiceFactory serviceFactory) throws DatabaseException, CalculatorException {
        int orderId = Integer.parseInt(ctx.pathParam("order_id"));
        OrderResponseDTO orderResponseDTO = serviceFactory.getOrderService().getOrder(orderId);
        serviceFactory.getOrderService().deleteOrder(orderId);
        serviceFactory.getCarportService().deleteCarport(orderResponseDTO.getCarportResponseDTO().getCarportId());
        ctx.render("admin-all-orders.html");

    }
}
