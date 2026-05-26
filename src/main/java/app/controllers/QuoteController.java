package app.controllers;

import app.dto.requestDTO.QuoteRequestDTO;
import app.dto.requestDTO.RoofRequestDTO;
import app.dto.requestDTO.carports.CarportRequestDTO;
import app.dto.responseDTO.*;
import app.entities.ProductsPartsListEntry;
import app.exceptions.CalculatorException;
import app.exceptions.DatabaseException;
import app.services.ServiceFactory;
import app.services.utils.UserValidator;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.List;
import java.util.Objects;

public class QuoteController {

    public void addRoutes(Javalin app, ServiceFactory serviceFactory) {
        app.get("/createQuote/{inquiry_id}", ctx -> showCreateQuotePage(ctx, serviceFactory));
        app.post("/createQuote/{inquiry_id}", ctx -> createQuote(ctx, serviceFactory));
        app.get("/getQuote/{quote_id}", ctx -> getQuoteAdmin(ctx, serviceFactory));
        app.get("/quotes/admin", ctx -> getAllQuotesAdmin(ctx, serviceFactory));
        app.post("/deleteQuote/{quote_id}", ctx -> deleteQuote(ctx, serviceFactory));
        app.get("/getAllQuotesCustomer", ctx -> getAllQuotesCustomer(ctx, serviceFactory));
        app.get("/getQuoteCustomer/{quote_id}", ctx -> getQuoteCustomer(ctx, serviceFactory));
        app.get("/getQuoteCustomer", ctx -> ctx.render("customer-quote-details")); /* Test route af AJ*/
    }

    public void showCreateQuotePage(Context ctx, ServiceFactory serviceFactory) throws DatabaseException, CalculatorException {
        int inquiryId = Integer.parseInt(ctx.pathParam("inquiry_id"));
        InquiryResponseDTO inquiryResponseDTO = serviceFactory.getInquiryService().getInquiry(inquiryId);
        ctx.attribute("inquiry_responseDTO", inquiryResponseDTO);

        CarportRequestDTO carportRequestDTO = serviceFactory.getCarportService().convertCarportResponseToRequest(inquiryResponseDTO.getCarportRespondDto());
        List<ProductsPartsListEntry> allEntries = serviceFactory.getPartsListService().createProductsPartsListEntries(carportRequestDTO);

        //Load default prices for the carport request
        double costPrice = serviceFactory.getPriceService().getCostPrice(allEntries);
        double retailPrice = serviceFactory.getPriceService().getRetailPrice(allEntries);
        double serviceFee = serviceFactory.getPriceService().getServiceFee(allEntries);
        double discount = 0;
        double revenue = serviceFactory.getPriceService().getRevenue(retailPrice, serviceFee, discount);
        double grossProfit = serviceFactory.getPriceService().getGrossProfit(costPrice, retailPrice, serviceFee, discount);
        double grossMarginPercent = serviceFactory.getPriceService().getGrossMarginInPercent(costPrice, retailPrice, serviceFee, discount);

        ctx.attribute("retail_price", retailPrice);
        ctx.attribute("service_fee", serviceFee);

        ctx.attribute("cost_price", costPrice);
        ctx.attribute("gross_margin_percent", grossMarginPercent);
        ctx.attribute("gross_profit", grossProfit);
        ctx.attribute("revenue", revenue);
        ctx.attribute("discount", discount);
        ctx.render("create-quote.html");
    }

    public void createQuote(Context ctx, ServiceFactory serviceFactory) throws DatabaseException, CalculatorException {
        UserResponseDTO salesRepResponseDTO = ctx.sessionAttribute("currentUser");

        int inquiryId = Integer.parseInt(ctx.pathParam("inquiry_id"));
        //Carport
        double carportWidth = Double.parseDouble(ctx.formParam("quote_carport_width"));
        double carportHeight = 230;
        double carportLength = Double.parseDouble(ctx.formParam("quote_carport_length"));

        //Roof
        double roofSlope = Double.parseDouble(ctx.formParam("quote_roof_slope"));
        String roofMaterial = ctx.formParam("quote_roof_material");
        String roofType = ctx.formParam("quote_roof_type");
        RoofRequestDTO roofRequestDTO = new RoofRequestDTO(roofSlope, roofMaterial, roofType);

        //Shed
        String shedWidth = ctx.formParam("quote_shed_width");
        String shedLength = ctx.formParam("quote_shed_length");
        String shedSiding = ctx.formParam("quote_shed_siding");
        boolean floor = Boolean.parseBoolean(ctx.formParam("quote_floor"));

//        //Price
//        double discount = ctx.formParam("discount_admin");
//        double revenue = serviceFactory.getPriceService().getRevenue()
//        InquiryResponseDTO inquiryResponseDTO = serviceFactory.getInquiryService().getInquiry(inquiryId);
//
//        CarportRequestDTO carportRequestDTO = serviceFactory.getShedService().checkShed(shedLength, shedWidth, shedSiding, floor, carportWidth, carportHeight, carportLength, roofRequestDTO);
//
//        int carportId = serviceFactory.getCarportService().createCarport(carportRequestDTO);
//        QuoteRequestDTO quoteRequestDTO = new QuoteRequestDTO(inquiryResponseDTO.getCustomerResponseDTO().getId(),revenue, carportId, salesRepResponseDTO.getId());
//        serviceFactory.getQuoteService().createQuote(quoteRequestDTO);
//        ctx.redirect("/quotes/admin");
    }

    public void getQuoteAdmin(Context ctx, ServiceFactory serviceFactory) throws DatabaseException {
        if (!UserValidator.isAdmin(ctx)){
            ctx.redirect("/");
            return;
        }
        int quoteId = Integer.parseInt(ctx.pathParam("quote_id"));
        UserResponseDTO customerResponseDTO = ctx.sessionAttribute("currentUser");
        QuoteResponseDTO quoteResponseDTO = serviceFactory.getQuoteService().getQuote(quoteId);

        if (quoteResponseDTO.getCustomerResponseDTO().getId() != customerResponseDTO.getId()) {
            ctx.status(403);
            return;
        }

        ctx.attribute("selected_quote", quoteResponseDTO);
        ctx.render("admin-customer-quote-details.html");
    }

    public void getAllQuotesCustomer(Context ctx, ServiceFactory serviceFactory) throws DatabaseException {
        CustomerResponseDTO customerResponseDTO = ctx.sessionAttribute("currentUser");
        List<QuoteResponseDTO> allCustomerQuotes = serviceFactory.getQuoteService().getAllQuotesByCustomerId(customerResponseDTO.getId());

        ctx.attribute("customer_quotes", allCustomerQuotes);
        ctx.render("customer-all-quotes.html");
    }

    public void getQuoteCustomer(Context ctx, ServiceFactory serviceFactory) throws DatabaseException {
        int quoteId = Integer.parseInt(ctx.pathParam("quote_id"));
        QuoteResponseDTO quoteResponseDTO = serviceFactory.getQuoteService().getQuote(quoteId);
        ctx.attribute("quote", quoteResponseDTO);
        ctx.render("customer-quote-details.html");
    }

    public void getAllQuotesAdmin(Context ctx, ServiceFactory serviceFactory) throws DatabaseException {
        List<QuoteResponseDTO> quoteResponseDTOS = serviceFactory.getQuoteService().getAllQuotes();
        ctx.attribute("all_quotes", quoteResponseDTOS);
        ctx.render("admin-all-quotes.html");
    }

    public void deleteQuote(Context ctx, ServiceFactory serviceFactory) throws DatabaseException {
        int quoteId = Integer.parseInt(ctx.pathParam("quote_id"));
        QuoteResponseDTO quoteResponseDTO = serviceFactory.getQuoteService().getQuote(quoteId);
        serviceFactory.getQuoteService().deleteQuote(quoteId);
        serviceFactory.getCarportService().deleteCarport(quoteResponseDTO.getCarportResponseDTO().getCarportId());
        ctx.redirect("/quotes/admin");
    }
}
