package app.controllers;
import app.dto.requestDTO.InquiryRequestDTO;
import app.dto.requestDTO.RoofRequestDTO;
import app.dto.requestDTO.carports.CarportRequestDTO;
import app.dto.responseDTO.InquiryResponseDTO;
import app.dto.responseDTO.UserResponseDTO;
import app.exceptions.CalculatorException;
import app.exceptions.DatabaseException;
import app.services.ServiceFactory;
import app.services.utils.UserValidator;
import io.javalin.Javalin;
import io.javalin.http.Context;
import java.util.List;

public class InquiryController {

    public void addRoutes(Javalin app, ServiceFactory serviceFactory) {
        app.post("/create/inquiry", ctx -> createInquiry(ctx, serviceFactory));
        app.get("/customer/get/inquiry/{inquiry_id}", ctx -> customerGetInquiryDetails(ctx, serviceFactory));
        app.get("/admin/get/inquiry/{inquiry_id}", ctx -> adminGetInquiryDetails(ctx, serviceFactory));
        app.post("/admin/delete/inquiry", ctx -> adminDeleteInquiry(ctx, serviceFactory));
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
        String shedWidth;
        String shedStatus = ctx.formParam("shed_status");
        String shedLength = ctx.formParam("shed_length");
        String shedSiding = ctx.formParam("shed_siding");
        String floorStatus = ctx.formParam("shed_floor");
        boolean floor;

        if (shedStatus.matches("HALF")){
            shedWidth = String.valueOf(carportWidth/2);
        } else if (shedStatus.matches("FULL")){
            shedWidth = String.valueOf(carportWidth);
        } else {
            shedLength = null;
            shedWidth = null;
            shedSiding = null;
            floor = false;
        }
        if (floorStatus.matches("TRUE")){
            floor=true;
        }else {
            floor = false;
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

    public void customerGetInquiryDetails(Context ctx, ServiceFactory serviceFactory) throws DatabaseException, CalculatorException {
        if (!UserValidator.isCustomer(ctx)){
            ctx.redirect("/");
            return;
        }
        int inquiryId = Integer.parseInt(ctx.pathParam("inquiry_id"));
        InquiryResponseDTO inquiryResponseDTO = serviceFactory.getInquiryService().getInquiry(inquiryId);

        ctx.sessionAttribute("inquiry_responseDTO",inquiryResponseDTO);
        ctx.attribute("selected_inquiry", inquiryResponseDTO);
        ctx.render("customer-inquiry-details.html");
    }

    public void adminGetInquiryDetails(Context ctx, ServiceFactory serviceFactory) throws DatabaseException, CalculatorException {
        if (!UserValidator.isAdmin(ctx)){
            ctx.redirect("/");
            return;
        }

        int inquiryId = Integer.parseInt(ctx.pathParam("inquiry_id"));
        InquiryResponseDTO inquiryResponseDTO = serviceFactory.getInquiryService().getInquiry(inquiryId);

        ctx.sessionAttribute("inquiry_responseDTO",inquiryResponseDTO);
        ctx.attribute("selected_inquiry", inquiryResponseDTO);
        ctx.render("admin-inquiry-details.html");
    }

    public void adminDeleteInquiry(Context ctx, ServiceFactory serviceFactory) throws DatabaseException, CalculatorException {
        if (!UserValidator.isAdmin(ctx)){
            ctx.redirect("/");
            return;
        }
        int inquiryId = Integer.parseInt(ctx.pathParam("inquiry_id"));
        InquiryResponseDTO inquiryResponseDTO = serviceFactory.getInquiryService().getInquiry(inquiryId);
        serviceFactory.getInquiryService().deleteInquiry(inquiryId);
        serviceFactory.getCarportService().deleteCarport(inquiryResponseDTO.getCarportResponseDTO().getCarportId());
        ctx.render("admin-all-inquiries.html");
    }
}
