package app.controllers;
import app.dto.requestDTO.OrderRequestDTO;
import app.dto.requestDTO.carports.CarportRequestDTO;
import app.dto.responseDTO.OrderResponseDTO;
import app.dto.responseDTO.QuoteResponseDTO;
import app.dto.responseDTO.UserResponseDTO;
import app.exceptions.CalculatorException;
import app.exceptions.DatabaseException;
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
        app.get("/getOrderAdmin", ctx -> ctx.render("admin-order-details")); /* Test route af AJ*/

    }

    public void createOrder(Context ctx, ServiceFactory serviceFactory) throws DatabaseException, CalculatorException {
        int customerId = ctx.sessionAttribute("currentUser");
        int quoteId = Integer.parseInt(ctx.pathParam("quote_id"));
        QuoteResponseDTO quoteResponseDTO = serviceFactory.getQuoteService().getQuote(quoteId);
        double orderPrice = quoteResponseDTO.getQuotePrice();
        int salesRepId = quoteResponseDTO.getSalesRepResponseDTO().getId();

        //Gets carport from quote and makes it into a request
        CarportRequestDTO carportRequestDTO = serviceFactory.getCarportService().convertCarportResponseToRequest(quoteResponseDTO.getCarportResponseDTO());
        int carportId = serviceFactory.getCarportService().createCarport(carportRequestDTO);

        //Gets partslistresponse and collects id directly
        int partsListId = serviceFactory.getPartsListService().getPartsList(carportId).getPartsListId();

        serviceFactory.getOrderService().createOrder(new OrderRequestDTO(customerId, salesRepId, carportId, orderPrice, partsListId));
    }

    public void getOrderAdmin(Context ctx, ServiceFactory serviceFactory) throws DatabaseException, CalculatorException {
        int orderId = Integer.parseInt(ctx.pathParam("order_id"));
        OrderResponseDTO orderResponseDTO = serviceFactory.getOrderService().getOrder(orderId);

        ctx.attribute("selected_order", orderResponseDTO);
        ctx.render("admin-order-details.html");
    }

    public void getOrderCustomer(Context ctx, ServiceFactory serviceFactory) throws DatabaseException, CalculatorException {
        int orderId = Integer.parseInt(ctx.pathParam("order_id"));
        OrderResponseDTO orderResponseDTO = serviceFactory.getOrderService().getOrder(orderId);

        ctx.attribute("selected_order", orderResponseDTO);
        ctx.render("customer-order-details.html");
    }

    public void getAllOrdersCustomer(Context ctx, ServiceFactory serviceFactory) throws DatabaseException, CalculatorException {
        UserResponseDTO customerResponseDTO = ctx.sessionAttribute("currentUser");
        List<OrderResponseDTO> orderResponseDTOS = serviceFactory.getOrderService().getAllOrdersByCustomerId(customerResponseDTO.getId());
        ctx.attribute("all_orders", orderResponseDTOS);
        ctx.render("customer-all-orders.html");
    }

    public void getAllOrdersAdmin(Context ctx, ServiceFactory serviceFactory) throws DatabaseException, CalculatorException {
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
