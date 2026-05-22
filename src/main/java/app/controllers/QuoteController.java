package app.controllers;

import app.dto.requestDTO.QuoteRequestDTO;
import app.dto.requestDTO.RoofRequestDTO;
import app.dto.requestDTO.carports.CarportRequestDTO;
import app.dto.responseDTO.InquiryResponseDTO;
import app.dto.responseDTO.QuoteResponseDTO;
import app.dto.responseDTO.SalesRepResponseDTO;
import app.entities.ProductsPartsListEntry;
import app.exceptions.CalculatorException;
import app.exceptions.DatabaseException;
import app.services.ServiceFactory;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.List;
import java.util.Objects;

public class QuoteController {

    public void addRoutes(Javalin app, ServiceFactory serviceFactory) {
        app.get("/createQuote/{inquiry_id}", ctx -> showCreateQuotePage(ctx, serviceFactory));
        app.post("/createQuote/{inquiry_id}", ctx -> createQuote(ctx, serviceFactory));
        app.post("/getQuote/{quote_id}", ctx -> getQuote(ctx, serviceFactory));
        app.post("/getAllQuotes", ctx -> getAllQuotes(ctx, serviceFactory));
        app.post("/deleteQuote", ctx -> deleteQuote(ctx, serviceFactory));
    }

    public void showCreateQuotePage(Context ctx, ServiceFactory serviceFactory) throws DatabaseException, CalculatorException {
        int inquiryId = Integer.parseInt(ctx.pathParam("inquiry_id"));
        InquiryResponseDTO inquiryResponseDTO = serviceFactory.getInquiryService().getInquiry(inquiryId);
        ctx.attribute("inquiry_responseDTO", inquiryResponseDTO);
        ctx.render("create-quote.html");
    }

    public void createQuote(Context ctx, ServiceFactory serviceFactory) throws DatabaseException, CalculatorException {
        //Carport
        double carportWidth = Double.parseDouble(ctx.formParam("quote_carport_width"));
        double carportHeight = 230;
        double carportLength = Double.parseDouble(ctx.formParam("quote_carport_length"));
        CarportRequestDTO carportRequestDTO;

        //Roof
        double roofSlope = Double.parseDouble(ctx.formParam("quote_roof_slope"));
        String roofMaterial = ctx.formParam("quote_roof_material");
        String roofType = ctx.formParam("quote-roof_type");
        RoofRequestDTO roofRequestDTO = new RoofRequestDTO(roofSlope, roofMaterial, roofType);

        //Shed
        String shedWidth = ctx.formParam("quote_shed_width");
        String shedLength = ctx.formParam("quote_shed_length");
        String shedSiding = ctx.formParam("quote_shed_siding");
        boolean floor = Boolean.parseBoolean(ctx.formParam("quote_floor"));

        //Price
        double revenue = Double.parseDouble(Objects.requireNonNull(ctx.formParam("revenue")));
        InquiryResponseDTO inquiryResponseDTO = serviceFactory.getInquiryService().getInquiry(inquiryId);
        CarportResponseDTO carportResponseDTO = serviceFactory.getCarportService().getCarport(inquiryResponseDTO.getCarportRespondDto().getCarportId());

        CarportRequestDTO carportRequestDTO = serviceFactory.getShedService().checkShed(shedLength, shedWidth, shedSiding, floor, carportWidth, carportHeight, carportLength, roofRequestDTO);

        int carportId = serviceFactory.getCarportService().createCarport(carportRequestDTO);

        QuoteRequestDTO quoteRequestDTO = new QuoteRequestDTO();
        serviceFactory.getQuoteService().createQuote(quoteRequestDTO);

    }

    public void getQuote(Context ctx, ServiceFactory serviceFactory) throws DatabaseException {
        int quoteId = Integer.parseInt(ctx.pathParam("quote_id"));
        QuoteResponseDTO quoteResponseDTO = serviceFactory.getQuoteService().getQuote(quoteId);

        ctx.attribute("selected_quote", quoteResponseDTO);
        ctx.render("admin-quote-details.html");
    }


    public void getAllQuotes(Context ctx, ServiceFactory serviceFactory) throws DatabaseException {
        List<QuoteResponseDTO> quoteResponseDTOS = serviceFactory.getQuoteService().getAllQuotes();
        ctx.attribute("all_quotes", quoteResponseDTOS);
        ctx.render("admin-all-quotes.html");
    }

    public void deleteQuote(Context ctx, ServiceFactory serviceFactory) throws DatabaseException {
        int quoteId = Integer.parseInt(ctx.pathParam("quote_id"));
        QuoteResponseDTO quoteResponseDTO = serviceFactory.getQuoteService().getQuote(quoteId);
        serviceFactory.getQuoteService().deleteQuote(quoteId);
        serviceFactory.getCarportService().deleteCarport(quoteResponseDTO.getCarportResponseDTO().getCarportId());
        ctx.redirect("/getAllQuotes");
    }
}
