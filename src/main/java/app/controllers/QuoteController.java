package app.controllers;

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
        app.get("/loadCreateQuote/{inquiry_id}", ctx -> loadCreateQuotePage(ctx, serviceFactory));
        app.post("/previewQuote/{inquiry_id}", ctx -> previewQuote(ctx, serviceFactory));
        app.get("/getQuote/{quote_id}", ctx -> getQuoteAdmin(ctx, serviceFactory));
        app.get("/quotes/admin", ctx -> getAllQuotesAdmin(ctx, serviceFactory));
        app.post("/confirmQuote", ctx -> createQuote(ctx, serviceFactory));
        app.post("/deleteQuote/{quote_id}", ctx -> deleteQuote(ctx, serviceFactory));
        app.get("/getAllQuotesCustomer", ctx -> getAllQuotesCustomer(ctx, serviceFactory));
        app.get("/getQuoteCustomer/{quote_id}", ctx -> getQuoteCustomer(ctx, serviceFactory));
    }

    public void loadCreateQuotePage(Context ctx, ServiceFactory serviceFactory) throws DatabaseException, CalculatorException {
        int inquiryId = Integer.parseInt(ctx.pathParam("inquiry_id"));
        InquiryResponseDTO inquiryResponseDTO = serviceFactory.getInquiryService().getInquiry(inquiryId);
        CarportResponseDTO carportResponseDTO = serviceFactory.getCarportService().getCarport(inquiryResponseDTO.getCarportRespondDto().getCarportId());

        ctx.attribute("inquiry_quote_preview", inquiryResponseDTO);
        ctx.attribute("carport_quote_preview", carportResponseDTO);
        ctx.render("create-quote.html");
    }

    public void previewQuote(Context ctx, ServiceFactory serviceFactory) throws DatabaseException, CalculatorException {
        //Method sets quote ready to be made with default values gotten from inquiry, salesrep can adjust if needed then render new page
        UserResponseDTO salesRepResponseDTO = ctx.sessionAttribute("currentUser");
        int inquiryId = Integer.parseInt(ctx.formParam("inquiry_id"));
        int customerId = Integer.parseInt(ctx.formParam("customer_id"));
        CarportRequestDTO carportRequestDTO = buildCarportRequest(ctx, serviceFactory);

        List<ProductsPartsListEntry> allEntries = serviceFactory.getPartsListService().createProductsPartsListEntries(carportRequestDTO);

        //Load default prices for the carport request
        double discount = 0;
        double costPrice = serviceFactory.getPriceService().getCostPrice(allEntries);
        double retailPrice = serviceFactory.getPriceService().getRetailPrice(allEntries);
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

        ctx.render("quote-preview.html");
    }

    public void createQuote(Context ctx, ServiceFactory serviceFactory) throws DatabaseException, CalculatorException {
        UserResponseDTO salesRepResponseDTO = ctx.sessionAttribute("currentUser");
        int customerId = Integer.parseInt(ctx.formParam("customer_id"));
        double discount = Double.parseDouble(ctx.formParam("discount_quote"));

        //Calculate prices again for added security
        CarportRequestDTO carportRequestDTO = buildCarportRequest(ctx, serviceFactory);
        List<ProductsPartsListEntry> entries = serviceFactory.getPartsListService().createProductsPartsListEntries(carportRequestDTO);
        double retailPrice = serviceFactory.getPriceService().getRetailPrice(entries);
        double serviceFee  = serviceFactory.getPriceService().getServiceFee(entries);
        double revenue     = serviceFactory.getPriceService().getRevenue(retailPrice, serviceFee, discount);

        int carportId = serviceFactory.getCarportService().createCarport(carportRequestDTO);
        QuoteRequestDTO quoteRequestDTO = new QuoteRequestDTO(customerId, revenue, carportId, salesRepResponseDTO.getId());
        serviceFactory.getQuoteService().createQuote(quoteRequestDTO);

        ctx.redirect("/quotes/admin");
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

    private CarportRequestDTO buildCarportRequest(Context ctx, ServiceFactory serviceFactory) {
        double carportWidth  = Double.parseDouble(ctx.formParam("quote_carport_width"));
        double carportHeight = 230;
        double carportLength = Double.parseDouble(ctx.formParam("quote_carport_length"));
        double roofSlope     = Double.parseDouble(ctx.formParam("quote_roof_slope"));
        String roofMaterial  = ctx.formParam("quote_roof_material");
        String roofType      = ctx.formParam("quote_roof_type");
        RoofRequestDTO roofRequestDTO = new RoofRequestDTO(roofSlope, roofMaterial, roofType);

        String shedWidth   = ctx.formParam("quote_shed_width");
        String shedLength  = ctx.formParam("quote_shed_length");
        String shedSiding  = ctx.formParam("quote_shed_siding");
        boolean floor      = Boolean.parseBoolean(ctx.formParam("quote_floor"));

        return serviceFactory.getShedService().checkShed(shedWidth, shedLength, shedSiding, floor, carportWidth, carportHeight, carportLength, roofRequestDTO);
    }
}
