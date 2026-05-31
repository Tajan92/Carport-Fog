package app.controllers;

import app.dto.requestDTO.OrderRequestDTO;
import app.dto.requestDTO.carports.CarportRequestDTO;
import app.dto.responseDTO.*;
import app.dto.responseDTO.carports.CarportResponseDTO;
import app.dto.responseDTO.carports.CarportShedResponseDTO;
import app.exceptions.CalculatorException;
import app.exceptions.DatabaseException;
import app.services.ServiceFactory;
import app.services.utils.UserValidator;
import io.javalin.Javalin;
import io.javalin.http.Context;
import jakarta.mail.MessagingException;

import java.sql.SQLException;
import java.util.List;

public class OrderController {

    public void getRoutes(Javalin app, ServiceFactory serviceFactory) {
        app.post("/create/order", ctx -> createOrder(ctx, serviceFactory));
        app.get("/customer/order/details/{order_id}", ctx -> getOrderCustomer(ctx, serviceFactory));
        app.get("/admin/order/details/{order_id}", ctx -> getOrderAdmin(ctx, serviceFactory));
        //app.post("/updateOrder", ctx -> updateOrder(ctx, serviceFactory));
        app.post("/deleteOrder", ctx -> deleteOrder(ctx, serviceFactory));
    }

    public void createOrder(Context ctx, ServiceFactory serviceFactory) throws DatabaseException, CalculatorException, SQLException, MessagingException {
        CustomerResponseDTO customerResponseDTO = ctx.sessionAttribute("currentUser");
        int quoteId = Integer.parseInt(ctx.formParam("quote_id"));
        QuoteResponseDTO quoteResponseDTO = serviceFactory.getQuoteService().getQuote(quoteId);

        int salesRepId = quoteResponseDTO.getSalesRepResponseDTO().getId();

        //Gets carport from quote and makes it into a request
        CarportRequestDTO carportRequestDTO = serviceFactory.getCarportService().convertCarportResponseToRequest(quoteResponseDTO.getCarportResponseDTO());
        int carportId = serviceFactory.getCarportService().createCarport(carportRequestDTO);
        double orderPrice = quoteResponseDTO.getTotalPrice();
        //Gets partslistresponse and collects id directly
        int partsListId = serviceFactory.getPartsListService().getPartsList(carportId).getPartsListId();
        double discount = quoteResponseDTO.getDiscount();

        //Create order first, then set status to paid
        int orderId = serviceFactory.getOrderService().createOrder(new OrderRequestDTO(customerResponseDTO.getId(), salesRepId, carportId, orderPrice, partsListId, discount));
        serviceFactory.getQuoteService().updateQuoteStatus(quoteId);

        //Send partslist to customer mail
        List<ProductsPartsListEntryResponseDTO> woodAndRoof = serviceFactory.getPartsListService().getWoodAndRoofEntryList(carportId);
        List<ProductsPartsListEntryResponseDTO> hardware = serviceFactory.getPartsListService().getHardwareEntryList(carportId);
        serviceFactory.getMailService().sendPartsList(customerResponseDTO.getEmail(), orderId, woodAndRoof, hardware);

        ctx.redirect("/customer/my/page");
    }

    public void getOrderAdmin(Context ctx, ServiceFactory serviceFactory) throws DatabaseException, CalculatorException {
        UserResponseDTO salesRep = ctx.sessionAttribute("currentUser");
        ctx.attribute("currentUser", salesRep);

        int orderId = Integer.parseInt(ctx.pathParam("order_id"));
        OrderAdminResponseDTO orderResponse = serviceFactory.getOrderService().getOrderAdmin(orderId);
        CarportResponseDTO carportResponseDTO = orderResponse.getCarportResponseDTO();

        ShedResponseDTO shed = null;
        if (orderResponse.getCarportResponseDTO() instanceof CarportShedResponseDTO withShed) {
            shed = withShed.getShedResponseDTO();
        }
        List<ProductsPartsListEntryResponseDTO> woodAndRoof = serviceFactory.getPartsListService().getWoodAndRoofEntryList(carportResponseDTO.getCarportId());
        List<ProductsPartsListEntryResponseDTO> hardware = serviceFactory.getPartsListService().getHardwareEntryList(carportResponseDTO.getCarportId());

        CarportRequestDTO carportRequestDTO = serviceFactory.getCarportService().convertCarportResponseToRequest(carportResponseDTO);
        String svgCarport = serviceFactory.getBlueprintService().createBlueprint(carportRequestDTO);

        ctx.attribute("svg_carport_details", svgCarport);
        ctx.attribute("parts_list_wood", woodAndRoof);
        ctx.attribute("parts_list_hardware", hardware);
        ctx.attribute("order_admin_preview", orderResponse);
        ctx.attribute("customer_order_preview", orderResponse.getCustomerResponseDTO());
        ctx.attribute("carport_order_preview", orderResponse.getCarportResponseDTO());
        ctx.attribute("shed", shed);
        ctx.render("admin-order-details.html");
    }

    public void getOrderCustomer(Context ctx, ServiceFactory serviceFactory) throws DatabaseException, CalculatorException {
        if (!UserValidator.isCustomer(ctx)) {
            ctx.redirect("/");
            return;
        }
        CustomerResponseDTO user = ctx.sessionAttribute("currentUser");
        ctx.attribute("currentUser", user);

        int orderId = Integer.parseInt(ctx.pathParam("order_id"));
        OrderResponseDTO orderResponse = serviceFactory.getOrderService().getOrder(orderId);
        CarportResponseDTO carportResponseDTO = orderResponse.getCarportResponseDTO();
        SalesRepResponseDTO salesRepResponseDTO = orderResponse.getSalesRepResponseDTO();

        //One list for each of the product groups for partslist
        List<ProductsPartsListEntryResponseDTO> woodAndRoof = serviceFactory.getPartsListService().getWoodAndRoofEntryList(carportResponseDTO.getCarportId());
        List<ProductsPartsListEntryResponseDTO> hardware = serviceFactory.getPartsListService().getHardwareEntryList(carportResponseDTO.getCarportId());

        ShedResponseDTO shed = null;
        if (carportResponseDTO instanceof CarportShedResponseDTO withShed) {
            shed = withShed.getShedResponseDTO();
        }

        CarportRequestDTO carportRequestDTO = serviceFactory.getCarportService().convertCarportResponseToRequest(carportResponseDTO);
        String svgCarport = serviceFactory.getBlueprintService().createBlueprint(carportRequestDTO);

        ctx.attribute("svg_carport_details", svgCarport);
        ctx.attribute("order_selected_order", orderResponse);
        ctx.attribute("order_selected_sales_rep", salesRepResponseDTO);
        ctx.attribute("order_selected_carport", carportResponseDTO);
        ctx.attribute("order_selected_shed", shed);
        ctx.attribute("parts_list_wood", woodAndRoof);
        ctx.attribute("parts_list_hardware", hardware);
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
