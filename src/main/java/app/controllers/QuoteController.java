package app.controllers;

import app.dto.requestDTO.InquiryRequestDTO;
import app.dto.requestDTO.QuoteRequestDTO;
import app.dto.requestDTO.RoofRequestDTO;
import app.dto.requestDTO.carports.CarportRequestDTO;
import app.dto.responseDTO.*;
import app.dto.responseDTO.carports.CarportResponseDTO;
import app.entities.ProductsPartsListEntry;
import app.exceptions.CalculatorException;
import app.exceptions.DatabaseException;
import app.services.ServiceFactory;
import app.services.utils.UserValidator;
import io.javalin.Javalin;
import io.javalin.http.Context;
import java.util.List;

public class QuoteController {

    public void addRoutes(Javalin app, ServiceFactory serviceFactory) {
        app.get("/load/create/quote/{inquiry_id}", ctx -> loadCreateQuotePage(ctx, serviceFactory));
        app.post("/admin/preview/quote", ctx -> previewQuote(ctx, serviceFactory));
        app.get("/admin/get/quote/{quote_id}", ctx -> getQuoteAdmin(ctx, serviceFactory));
        app.post("/admin/create/quote", ctx -> createQuote(ctx, serviceFactory));
        app.post("/admin/delete/quote/{quote_id}", ctx -> deleteQuote(ctx, serviceFactory));
        app.get("/customer/get/quote/{quote_id}", ctx -> getQuoteCustomer(ctx, serviceFactory));
    }

    public void loadCreateQuotePage(Context ctx, ServiceFactory serviceFactory) throws DatabaseException, CalculatorException {
        if (!UserValidator.isAdmin(ctx)){
            ctx.redirect("/");
            return;
        }
        int inquiryId = Integer.parseInt(ctx.pathParam("inquiry_id"));
        InquiryResponseDTO inquiryResponseDTO = serviceFactory.getInquiryService().getInquiry(inquiryId);

        ctx.attribute("inquiry_quote_preview", inquiryResponseDTO);
        ctx.attribute("carport_quote_preview", inquiryResponseDTO.getCarportResponseDTO());
        ctx.attribute("customer_quote_preview", inquiryResponseDTO.getCustomerResponseDTO());
        ctx.render("admin-carport-maker.html");
    }

    public void previewQuote(Context ctx, ServiceFactory serviceFactory) throws DatabaseException, CalculatorException {
        if (!UserValidator.isAdmin(ctx)){
            ctx.redirect("/");
            return;
        }
        //Method sets quote ready to be made with default values gotten from inquiry, salesrep can adjust if needed then render new page
        UserResponseDTO salesRepResponseDTO = ctx.sessionAttribute("currentUser");
        int inquiryId = Integer.parseInt(ctx.formParam("inquiry_id"));
        int customerId = Integer.parseInt(ctx.formParam("customer_id"));
        CarportRequestDTO carportRequestDTO = buildCarportRequest(ctx, serviceFactory);

        List<ProductsPartsListEntry> allEntries = serviceFactory.getPartsListService().createProductsPartsListEntries(carportRequestDTO);

        //Load default prices for the carport request
        double discount = 0;
        double costPrice = serviceFactory.getPriceService().getTotalCostPrice(allEntries);
        double retailPrice = serviceFactory.getPriceService().getTotalRetailPrice(allEntries);
        double serviceFee = serviceFactory.getPriceService().getServiceFee(allEntries);
        double revenue = serviceFactory.getPriceService().getRevenue(retailPrice, serviceFee, discount);
        double grossProfit = serviceFactory.getPriceService().getGrossProfit(costPrice, retailPrice, serviceFee, discount);
        double grossMargin = serviceFactory.getPriceService().getGrossMarginInPercent(costPrice, retailPrice, serviceFee, discount);

        ctx.attribute("inquiry_id", inquiryId);
        ctx.attribute("sales_rep_id", salesRepResponseDTO.getId());
        ctx.attribute("customer_id", customerId);
        ctx.attribute("carport_request", carportRequestDTO);
        ctx.attribute("cost_price_quote", costPrice);
        ctx.attribute("discount_quote", discount);
        ctx.attribute("retail_price_quote", retailPrice);
        ctx.attribute("service_fee_quote", serviceFee);
        ctx.attribute("revenue_quote", revenue);
        ctx.attribute("gross_profit_quote", grossProfit);
        ctx.attribute("gross_margin_quote", grossMargin);

        ctx.render("admin-carport-maker");
    }

//    public void createQuote(Context ctx, ServiceFactory serviceFactory) throws DatabaseException, CalculatorException {
//        if (!UserValidator.isAdmin(ctx)){
//            ctx.redirect("/");
//            return;
//        }
//        UserResponseDTO salesRepResponseDTO = ctx.sessionAttribute("currentUser");
//        int customerId = Integer.parseInt(ctx.formParam("customer_id"));
//        double discount = Double.parseDouble(ctx.formParam("discount_quote"));
//
//        //Calculate prices again for added security
//        CarportRequestDTO carportRequestDTO = buildCarportRequest(ctx, serviceFactory);
//        List<ProductsPartsListEntry> entries = serviceFactory.getPartsListService().createProductsPartsListEntries(carportRequestDTO);
//        double retailPrice = serviceFactory.getPriceService().getTotalRetailPrice(entries);
//        double serviceFee  = serviceFactory.getPriceService().getServiceFee(entries);
//        double revenue     = serviceFactory.getPriceService().getRevenue(retailPrice, serviceFee, discount);
//
//        int carportId = serviceFactory.getCarportService().createCarport(carportRequestDTO);
//        QuoteRequestDTO quoteRequestDTO = new QuoteRequestDTO(customerId, revenue, carportId, salesRepResponseDTO.getId());
//        serviceFactory.getQuoteService().createQuote(quoteRequestDTO);
//
//        ctx.redirect("/admin/my/page");
//    }

    public void createQuote(Context ctx, ServiceFactory serviceFactory) throws CalculatorException, DatabaseException {
        //Carport
        double carportWidth = Double.parseDouble(ctx.formParam("carport_width"));
        double carportHeight = 230;
        double carportLength = Double.parseDouble(ctx.formParam("carport_length"));

        //Roof
        double roofSlope = Double.parseDouble(ctx.formParam("roof_slope"));
        String roofMaterial = ctx.formParam("roof_material");
        String roofType = ctx.formParam("roof_type");
        RoofRequestDTO roofRequestDTO = new RoofRequestDTO(roofSlope, roofMaterial, roofType);

        //Shed
        String shedWidth;
        String shedLength;
        String shedStatus = ctx.formParam("shed_status");
        String shedSiding = ctx.formParam("shed_siding");
        String floorStatus = ctx.formParam("shed_floor");
        boolean floor = false;

        //Check for floor
        if (floorStatus.equals("TRUE")){
            floor = true;
        }
        //Check for shed size full, half or none
        if (shedStatus.matches("HALF")) {
            shedWidth = String.valueOf(carportWidth / 2);
            shedLength = String.valueOf(carportLength / 3);
        } else if (shedStatus.matches("FULL")) {
            shedWidth = String.valueOf(carportWidth);
            shedLength = String.valueOf(carportLength / 3);
        } else {
            shedWidth = null;
            shedLength = null;
            shedSiding = null;
        }

        CarportRequestDTO carportRequestDTO = serviceFactory.getCarportService().checkShed(shedWidth, shedLength, shedSiding, floor, carportWidth, carportHeight, carportLength, roofRequestDTO);

        UserResponseDTO userResponseDTO  = ctx.sessionAttribute("currentUser");
        String inquiryRemark = ctx.formParam("inquiry_remark");

        if (userResponseDTO == null) {
            // save all form params to session before redirecting
            ctx.sessionAttribute("pending_carport_width", ctx.formParam("carport_width"));
            ctx.sessionAttribute("pending_carport_length", ctx.formParam("carport_length"));
            ctx.sessionAttribute("pending_roof_slope", ctx.formParam("roof_slope"));
            ctx.sessionAttribute("pending_roof_material", ctx.formParam("roof_material"));
            ctx.sessionAttribute("pending_roof_type", ctx.formParam("roof_type"));
            ctx.sessionAttribute("pending_shed_width", ctx.formParam("shed_status"));
            ctx.sessionAttribute("pending_shed_length", ctx.formParam("shed_length"));
            ctx.sessionAttribute("pending_shed_siding", ctx.formParam("shed_siding"));
            ctx.sessionAttribute("pending_floor", ctx.formParam("shed_floor"));
            ctx.sessionAttribute("pending_remark", ctx.formParam("inquiry_remark"));
            ctx.sessionAttribute("pending_inquiry", true);

            ctx.redirect("/customer/login");
            return;
        }

        int carportId = serviceFactory.getCarportService().createCarport(carportRequestDTO);
        int customerId = userResponseDTO.getId();

        InquiryRequestDTO inquiryRequestDTO = new InquiryRequestDTO(customerId, inquiryRemark, carportId);
        serviceFactory.getInquiryService().createInquiry(inquiryRequestDTO);

        ctx.redirect("/customer/my/page");
    }

    public void getQuoteAdmin(Context ctx, ServiceFactory serviceFactory) throws DatabaseException {
        if (!UserValidator.isAdmin(ctx)){
            ctx.redirect("/");
            return;
        }
        int quoteId = Integer.parseInt(ctx.pathParam("quote_id"));
        QuoteResponseDTO quoteResponseDTO = serviceFactory.getQuoteService().getQuote(quoteId);

        ctx.attribute("selected_quote", quoteResponseDTO);
        ctx.render("admin-quote-details.html");
    }

    public void getQuoteCustomer(Context ctx, ServiceFactory serviceFactory) throws DatabaseException {
        int quoteId = Integer.parseInt(ctx.pathParam("quote_id"));
        QuoteResponseDTO quoteResponseDTO = serviceFactory.getQuoteService().getQuote(quoteId);
        ctx.attribute("quote", quoteResponseDTO);
        ctx.render("customer-quote-details.html");
    }

    public void deleteQuote(Context ctx, ServiceFactory serviceFactory) throws DatabaseException {
        int quoteId = Integer.parseInt(ctx.pathParam("quote_id"));
        QuoteResponseDTO quoteResponseDTO = serviceFactory.getQuoteService().getQuote(quoteId);
        serviceFactory.getQuoteService().deleteQuote(quoteId);
        serviceFactory.getCarportService().deleteCarport(quoteResponseDTO.getCarportResponseDTO().getCarportId());
        ctx.redirect("/admin/my/page");
    }

    private CarportRequestDTO buildCarportRequest(Context ctx, ServiceFactory serviceFactory) {
        double carportWidth  = Double.parseDouble(ctx.formParam("carport_width"));
        double carportHeight = 230;
        double carportLength = Double.parseDouble(ctx.formParam("carport_length"));
        double roofSlope     = Double.parseDouble(ctx.formParam("roof_slope"));
        String roofMaterial  = ctx.formParam("roof_material");
        String roofType      = ctx.formParam("roof_type");
        RoofRequestDTO roofRequestDTO = new RoofRequestDTO(roofSlope, roofMaterial, roofType);

        String shedStatus  = ctx.formParam("shed_status");
        String shedSiding  = ctx.formParam("shed_siding");
        boolean floor      = Boolean.parseBoolean(ctx.formParam("shed_floor"));

        String shedWidth  = switch (shedStatus != null ? shedStatus : "NONE") {
            case "HALF" -> String.valueOf(carportWidth / 2);
            case "FULL" -> String.valueOf(carportWidth);
            default     -> null;
        };
        String shedLength = shedWidth != null ? String.valueOf(carportLength / 3) : null;
        if (shedWidth == null) shedSiding = null;

        return serviceFactory.getCarportService().checkShed(shedWidth, shedLength, shedSiding, floor, carportWidth, carportHeight, carportLength, roofRequestDTO);
    }
}
