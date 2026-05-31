package app.controllers;

import app.dto.requestDTO.QuoteRequestDTO;
import app.dto.requestDTO.RoofRequestDTO;
import app.dto.requestDTO.carports.CarportRequestDTO;
import app.dto.responseDTO.*;
import app.dto.responseDTO.carports.CarportResponseDTO;
import app.dto.responseDTO.carports.CarportShedResponseDTO;
import app.entities.ProductsPartsListEntry;
import app.exceptions.CalculatorException;
import app.exceptions.UserExperienceException;
import app.exceptions.DatabaseException;
import app.services.ServiceFactory;
import app.services.utils.UserValidator;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.sql.SQLException;

import java.util.List;

public class QuoteController {

    public void addRoutes(Javalin app, ServiceFactory serviceFactory) {
        app.get("/load/create/quote/{inquiry_id}", ctx -> loadCreateQuotePage(ctx, serviceFactory));
        app.post("/admin/preview/quote", ctx -> previewQuote(ctx, serviceFactory));
        app.get("/admin/quote/details/{quote_id}", ctx -> getQuoteAdmin(ctx, serviceFactory));
        app.post("/admin/create/quote", ctx -> createQuote(ctx, serviceFactory));
        app.post("/admin/delete/quote/{quote_id}", ctx -> deleteQuote(ctx, serviceFactory));
        app.get("/customer/quote/details/{quote_id}", ctx -> getQuoteCustomer(ctx, serviceFactory));
    }

    public void loadCreateQuotePage(Context ctx, ServiceFactory serviceFactory) throws DatabaseException, CalculatorException {
        if (!UserValidator.isAdmin(ctx)) {
            ctx.redirect("/");
            return;
        }
        int inquiryId = Integer.parseInt(ctx.pathParam("inquiry_id"));
        InquiryResponseDTO inquiryResponseDTO = serviceFactory.getInquiryService().getInquiry(inquiryId);
        ShedResponseDTO shed = null;

        if (inquiryResponseDTO.getCarportResponseDTO() instanceof CarportShedResponseDTO withShed) {
            shed = withShed.getShedResponseDTO();
        }

        ctx.attribute("inquiry_quote_preview", inquiryResponseDTO);
        ctx.attribute("carport_quote_preview", inquiryResponseDTO.getCarportResponseDTO());
        ctx.attribute("customer_quote_preview", inquiryResponseDTO.getCustomerResponseDTO());
        ctx.attribute("shed", shed);
        ctx.render("admin-carport-maker.html");
    }

    public void previewQuote(Context ctx, ServiceFactory serviceFactory) throws DatabaseException, CalculatorException, UserExperienceException {
        if (!UserValidator.isAdmin(ctx)) {
            ctx.redirect("/");
            return;
        }

        UserResponseDTO salesRepResponseDTO = ctx.sessionAttribute("currentUser");
        int inquiryId = Integer.parseInt(ctx.formParam("inquiry_id"));
        int customerId = Integer.parseInt(ctx.formParam("customer_id"));

        // Fetch the full inquiry so the template has the DTOs it needs
        InquiryResponseDTO inquiryResponseDTO = serviceFactory.getInquiryService().getInquiry(inquiryId);
        ShedResponseDTO shed = null;

        if (inquiryResponseDTO.getCarportResponseDTO() instanceof CarportShedResponseDTO withShed) {
            shed = withShed.getShedResponseDTO();
        }
        ctx.attribute("inquiry_quote_preview", inquiryResponseDTO);
        ctx.attribute("carport_quote_preview", inquiryResponseDTO.getCarportResponseDTO());
        ctx.attribute("customer_quote_preview", inquiryResponseDTO.getCustomerResponseDTO());
        ctx.attribute("shed", shed);

        CarportRequestDTO carportRequestDTO = buildCarportRequest(ctx, serviceFactory);
        List<ProductsPartsListEntry> allEntries = serviceFactory.getPartsListService().createProductsPartsListEntries(carportRequestDTO);

        String discountResponse = ctx.formParam("discount_quote");
        double discount = 0;
        if (discountResponse != null && !discountResponse.isBlank()) {
            discount = Double.parseDouble(discountResponse);
        }
        double costPrice = serviceFactory.getPriceService().getTotalCostPrice(allEntries);
        double retailPrice = serviceFactory.getPriceService().getTotalRetailPrice(allEntries);
        double serviceFee = serviceFactory.getPriceService().getServiceFee(allEntries);
        double revenue = serviceFactory.getPriceService().getRevenue(retailPrice, serviceFee, discount);
        double grossProfit = serviceFactory.getPriceService().getGrossProfit(costPrice, retailPrice, serviceFee, discount);
        double grossMargin = serviceFactory.getPriceService().getGrossMarginInPercent(costPrice, retailPrice, serviceFee, discount);
        double customerPrice = retailPrice - discount;

        ctx.attribute("customer_price", customerPrice);
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

        ctx.render("admin-carport-maker.html");
    }

    public void createQuote(Context ctx, ServiceFactory serviceFactory) throws CalculatorException, DatabaseException, UserExperienceException, SQLException {
        if (!UserValidator.isAdmin(ctx)) {
            ctx.redirect("/");
            return;
        }

        UserResponseDTO salesRep = ctx.sessionAttribute("currentUser");
        ctx.attribute("currentUser", salesRep);
        int customerId = Integer.parseInt(ctx.formParam("customer_id"));
        int inquiryId = Integer.parseInt(ctx.formParam("inquiry_id"));
        String discountResponse = ctx.formParam("discount_quote");
        double discount = 0;
        if (discountResponse != null && !discountResponse.isBlank()) {
            discount = Double.parseDouble(discountResponse);
        }

        CarportRequestDTO carportRequestDTO = buildCarportRequest(ctx, serviceFactory);

        // Recalculate prices server-side for security
        List<ProductsPartsListEntry> entries = serviceFactory.getPartsListService().createProductsPartsListEntries(carportRequestDTO);
        double retailPrice = serviceFactory.getPriceService().getTotalRetailPrice(entries);

        int carportId = serviceFactory.getCarportService().createCarport(carportRequestDTO);
        QuoteRequestDTO quoteRequestDTO = new QuoteRequestDTO(customerId, retailPrice, carportId, salesRep.getId(), discount);
        serviceFactory.getQuoteService().createQuote(quoteRequestDTO);
        serviceFactory.getInquiryService().updateInquiryStatus(inquiryId);

        ctx.redirect("/admin/my/page");
    }

    public void getQuoteAdmin(Context ctx, ServiceFactory serviceFactory) throws DatabaseException {
        UserResponseDTO salesRep = ctx.sessionAttribute("currentUser");
        ctx.attribute("currentUser", salesRep);

        int quoteId = Integer.parseInt(ctx.pathParam("quote_id"));
        QuoteAdminResponseDTO response = serviceFactory.getQuoteService().getQuoteAdmin(quoteId);

        ShedResponseDTO shed = null;
        if (response.getCarportResponseDTO() instanceof CarportShedResponseDTO withShed) {
            shed = withShed.getShedResponseDTO();
        }

        ctx.attribute("quote_admin_preview", response);
        ctx.attribute("customer_quote_preview", response.getCustomerResponseDTO());
        ctx.attribute("carport_quote_preview", response.getCarportResponseDTO());
        ctx.attribute("shed", shed);
        ctx.render("admin-quote-details.html");
    }

    public void getQuoteCustomer(Context ctx, ServiceFactory serviceFactory) throws DatabaseException {
        if (!UserValidator.isCustomer(ctx)){
            ctx.redirect("/");
            return;
        }
        CustomerResponseDTO user = ctx.sessionAttribute("currentUser");
        ctx.attribute("currentUser", user);

        int quoteId = Integer.parseInt(ctx.pathParam("quote_id"));
        QuoteResponseDTO quoteResponseDTO = serviceFactory.getQuoteService().getQuote(quoteId);

        CarportResponseDTO carportResponseDTO = quoteResponseDTO.getCarportResponseDTO();
        SalesRepResponseDTO salesRepResponseDTO = quoteResponseDTO.getSalesRepResponseDTO();

        ShedResponseDTO shed = null;
        if (carportResponseDTO instanceof CarportShedResponseDTO withShed) {
            shed = withShed.getShedResponseDTO();
        }
        ctx.attribute("quote_selected_quote", quoteResponseDTO);
        ctx.attribute("quote_selected_sales_rep", salesRepResponseDTO);
        ctx.attribute("quote_selected_carport", carportResponseDTO);
        ctx.attribute("quote_selected_shed", shed);
        ctx.render("customer-quote-details.html");
    }

    public void deleteQuote(Context ctx, ServiceFactory serviceFactory) throws DatabaseException {
        int quoteId = Integer.parseInt(ctx.pathParam("quote_id"));
        QuoteResponseDTO quoteResponseDTO = serviceFactory.getQuoteService().getQuote(quoteId);
        serviceFactory.getQuoteService().deleteQuote(quoteId);
        serviceFactory.getCarportService().deleteCarport(quoteResponseDTO.getCarportResponseDTO().getCarportId());
        ctx.redirect("/admin/my/page");
    }

    private CarportRequestDTO buildCarportRequest(Context ctx, ServiceFactory serviceFactory) throws UserExperienceException {
        String carportWidthResponse = ctx.formParam("carport_width");
        String carportLengthResponse = ctx.formParam("carport_length");

        if (carportWidthResponse == null || carportWidthResponse.isBlank()) {
            throw new UserExperienceException("Vælg størrelse af carport");
        }
        double carportWidth = Double.parseDouble(carportWidthResponse);
        double carportLength = Double.parseDouble(carportLengthResponse);

        double carportHeight = 230;

        String roofSlopeResponse = ctx.formParam("roof_slope");
        String roofMaterial = ctx.formParam("roof_material");
        String roofType = ctx.formParam("roof_type");
        if (roofType == null || roofType.isBlank()) {
            roofType = "Fladt tag";
        }
        if (roofMaterial == null || roofMaterial.isBlank()) {
            roofMaterial = "Plastmo Ecolite blåtonet";
        }
        double roofSlope = 1.7;
        if (!"Fladt tag".equals(roofType) && roofSlopeResponse != null && !roofSlopeResponse.isBlank()) {
            roofSlope = Double.parseDouble(roofSlopeResponse);
        }

        RoofRequestDTO roofRequestDTO = new RoofRequestDTO(roofSlope, roofMaterial, roofType);

        String shedStatus = ctx.formParam("shed_status");
        String shedSiding = ctx.formParam("shed_siding");
        boolean floor = false;

        if (!"NONE".equals(shedStatus) && shedStatus != null) {
            String floorResponse = ctx.formParam("shed_floor");
            if (floorResponse == null || floorResponse.isBlank()) {
                throw new UserExperienceException("Til / fra vælg gulv");
            }
            floor = Boolean.parseBoolean(floorResponse);
        }

        String shedWidth = switch (shedStatus != null ? shedStatus : "NONE") {
            case "HALF" -> String.valueOf(carportWidth / 2);
            case "FULL" -> String.valueOf(carportWidth);
            default -> null;
        };
        String shedLength = shedWidth != null ? String.valueOf(carportLength / 3) : null;
        if (shedWidth == null) shedSiding = null;

        return serviceFactory.getCarportService().checkShed(shedWidth, shedLength, shedSiding, floor, carportWidth, carportHeight, carportLength, roofRequestDTO);
    }
}
