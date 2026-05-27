package app.controllers;

import app.dto.requestDTO.InquiryRequestDTO;
import app.dto.requestDTO.RoofRequestDTO;
import app.dto.requestDTO.carports.CarportRequestDTO;
import app.dto.responseDTO.InquiryResponseDTO;
import app.dto.responseDTO.UserResponseDTO;
import app.exceptions.CalculatorException;
import app.exceptions.DatabaseException;
import app.services.ServiceFactory;
import io.javalin.Javalin;
import io.javalin.http.Context;
import java.util.List;

public class InquiryController {

    public void addRoutes(Javalin app, ServiceFactory serviceFactory) {
        app.post("/createInquiry", ctx -> createInquiry(ctx, serviceFactory));
        app.post("/getInquiry/{inquiry_id}", ctx -> getInquiry(ctx, serviceFactory));
        app.post("/getAllInquiries", ctx -> getAllInquiries(ctx, serviceFactory));
        app.post("/deleteInquiry", ctx -> deleteInquiry(ctx, serviceFactory));
        app.get("/getInquiryCustomer", ctx -> ctx.render("customer-inquiry-details")); /* Test route af AJ*/
        app.get("/getInquiryAdmin", ctx -> ctx.render("admin-inquiry-details")); /* Test route af AJ*/

    }

    public void createInquiry(Context ctx, ServiceFactory serviceFactory) throws CalculatorException, DatabaseException {
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
        String shedWidth = ctx.formParam("shed_width");
        String shedLength = ctx.formParam("shed_length");
        String shedSiding = ctx.formParam("shed_siding");
        boolean floor = Boolean.parseBoolean(ctx.formParam("floor"));


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
            ctx.sessionAttribute("pending_shed_width", ctx.formParam("shed_width"));
            ctx.sessionAttribute("pending_shed_length", ctx.formParam("shed_length"));
            ctx.sessionAttribute("pending_shed_siding", ctx.formParam("shed_siding"));
            ctx.sessionAttribute("pending_floor", ctx.formParam("floor"));
            ctx.sessionAttribute("pending_remark", ctx.formParam("inquiry_remark"));
            ctx.sessionAttribute("pending_inquiry", true);

            ctx.redirect("/login");
            return;
        }

        int carportId = serviceFactory.getCarportService().createCarport(carportRequestDTO);
        int customerId = userResponseDTO.getId();

        InquiryRequestDTO inquiryRequestDTO = new InquiryRequestDTO(customerId, inquiryRemark, carportId);
        serviceFactory.getInquiryService().createInquiry(inquiryRequestDTO);

        ctx.render("/profilePage");
    }

    public void getInquiry(Context ctx, ServiceFactory serviceFactory) throws DatabaseException, CalculatorException {
        int inquiryId = Integer.parseInt(ctx.pathParam("inquiry_id"));
        UserResponseDTO userResponseDTO = ctx.sessionAttribute("currentUser");
        InquiryResponseDTO inquiryResponseDTO = serviceFactory.getInquiryService().getInquiry(inquiryId);

        if (inquiryResponseDTO.getCustomerResponseDTO().getId() != userResponseDTO.getId()){
            ctx.status(403);
            return;
        }

        ctx.sessionAttribute("inquiry_responseDTO",inquiryResponseDTO);
        ctx.attribute("selected_inquiry", inquiryResponseDTO);
        ctx.render("admin-inquiry-details.html");
    }

    public void getAllInquiries(Context ctx, ServiceFactory serviceFactory) throws DatabaseException {
        List<InquiryResponseDTO> inquiryResponseDTOList = serviceFactory.getInquiryService().getAllInquiries();
        ctx.attribute("all_inquiries", inquiryResponseDTOList);
        ctx.render("admin-all-inquiries.html");
    }

    public void deleteInquiry(Context ctx, ServiceFactory serviceFactory) throws DatabaseException, CalculatorException {
        int inquiryId = Integer.parseInt(ctx.pathParam("inquiry_id"));
        InquiryResponseDTO inquiryResponseDTO = serviceFactory.getInquiryService().getInquiry(inquiryId);
        serviceFactory.getInquiryService().deleteInquiry(inquiryId);
        serviceFactory.getCarportService().deleteCarport(inquiryResponseDTO.getCarportResponseDTO().getCarportId());
        ctx.render("admin-all-inquiries.html");
    }
}
