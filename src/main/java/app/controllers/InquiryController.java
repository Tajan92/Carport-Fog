package app.controllers;

import app.dto.requestDTO.InquiryRequestDTO;
import app.dto.requestDTO.RoofRequestDTO;
import app.dto.requestDTO.carports.CarportRequestDTO;
import app.dto.responseDTO.InquiryResponseDTO;
import app.dto.responseDTO.SalesRepResponseDTO;
import app.dto.responseDTO.ShedResponseDTO;
import app.dto.responseDTO.UserResponseDTO;
import app.dto.responseDTO.carports.CarportResponseDTO;
import app.dto.responseDTO.carports.CarportShedResponseDTO;
import app.exceptions.CalculatorException;
import app.exceptions.DatabaseException;
import app.exceptions.UserExperienceException;
import app.services.ServiceFactory;
import app.services.utils.UserValidator;
import io.javalin.Javalin;
import io.javalin.http.Context;
import jakarta.mail.MessagingException;

public class InquiryController {

    public void addRoutes(Javalin app, ServiceFactory serviceFactory) {
        app.post("/create/inquiry", ctx -> createInquiry(ctx, serviceFactory));
        app.get("/customer/inquiry/details/{inquiry_id}", ctx -> customerGetInquiryDetails(ctx, serviceFactory));
        app.get("/admin/inquiry/details/{inquiry_id}", ctx -> adminGetInquiryDetails(ctx, serviceFactory));
        app.post("/admin/delete/inquiry", ctx -> adminDeleteInquiry(ctx, serviceFactory));
    }

    public void createInquiry(Context ctx, ServiceFactory serviceFactory) throws CalculatorException, DatabaseException, UserExperienceException, MessagingException {
        //Carport
        String carportWidthResponse = ctx.formParam("carport_width");
        String carportLengthResponse = ctx.formParam("carport_length");
        double carportWidth = 0.0;
        double carportLength = 0.0;
        if (carportWidthResponse != null && !carportWidthResponse.isBlank()) {
            carportWidth = Double.parseDouble(carportWidthResponse);
        }
        if (carportLengthResponse != null && !carportLengthResponse.isBlank()) {
            carportLength = Double.parseDouble(carportLengthResponse);
        }
        double carportHeight = 230;

        //Roof
        String roofSlopeResponse = ctx.formParam("roof_slope");
        String roofMaterial = ctx.formParam("roof_material");
        String roofType = ctx.formParam("roof_type");
        double roofSlope = 1.7;
        if (roofType == null || roofType.isBlank()) {
            roofType = "Fladt tag";
        }

        if (roofMaterial == null || roofMaterial.isBlank()) {
            roofMaterial = "Plastmo Ecolite blåtonet";
        }

        if (roofSlopeResponse != null && !roofSlopeResponse.isBlank()) {
            roofSlope = Double.parseDouble(roofSlopeResponse);
        }
        if (roofType.equals("Fladt tag")) {
            roofSlope = 1.7;
        }
        RoofRequestDTO roofRequestDTO = new RoofRequestDTO(roofSlope, roofMaterial, roofType);

        //Shed
        String shedWidth = "";
        String shedLength = "";
        String shedStatus = ctx.formParam("shed_status");
        String shedSiding = ctx.formParam("shed_siding");
        String floorStatus = ctx.formParam("shed_floor");
        boolean floor = false;


        if (floorStatus.equals("TRUE")) {
            floor = true;
        }
        //Check for shed size full, half, none or null
        if (shedStatus == null) {
            ctx.redirect("/");
        }else if (shedStatus.matches("HALF")) {
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

        UserResponseDTO userResponseDTO = ctx.sessionAttribute("currentUser");
        String inquiryRemark = ctx.formParam("inquiry_remark");
        if (inquiryRemark == null || inquiryRemark.isEmpty()) {
            inquiryRemark = "";
        }

        if (userResponseDTO == null) {
            // save all form params to session before redirecting
            ctx.sessionAttribute("pending_carport_width", ctx.formParam("carport_width"));
            ctx.sessionAttribute("pending_carport_length", ctx.formParam("carport_length"));
            ctx.sessionAttribute("pending_roof_material", ctx.formParam("roof_material"));
            ctx.sessionAttribute("pending_roof_type", ctx.formParam("roof_type"));
            ctx.sessionAttribute("pending_roof_slope", ctx.formParam("roof_slope"));
            ctx.sessionAttribute("pending_shed_status", ctx.formParam("shed_status"));
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
        int inquiryId = serviceFactory.getInquiryService().createInquiry(inquiryRequestDTO);
        InquiryResponseDTO inquiryResponseDTO = serviceFactory.getInquiryService().getInquiry(inquiryId);
        serviceFactory.getMailService().sendInquiryNotice(inquiryResponseDTO);

        String svgCarport = serviceFactory.getBlueprintService().createBlueprintNoMeasures(carportRequestDTO);
        ctx.attribute("svg_carport_width", svgCarport);
        ctx.redirect("/customer/my/page");
    }

    public void customerGetInquiryDetails(Context ctx, ServiceFactory serviceFactory) throws DatabaseException, CalculatorException {
        if (!UserValidator.isCustomer(ctx)) {
            ctx.redirect("/");
            return;
        }
        int inquiryId = Integer.parseInt(ctx.pathParam("inquiry_id"));
        InquiryResponseDTO inquiryResponseDTO = serviceFactory.getInquiryService().getInquiry(inquiryId);
        CarportResponseDTO carportResponseDTO = inquiryResponseDTO.getCarportResponseDTO();
        String floor = "";
        ShedResponseDTO shed = null;
        if (carportResponseDTO instanceof CarportShedResponseDTO withShed) {
            shed = withShed.getShedResponseDTO();
            if (shed.isFloor()) {
                floor = "ja";
            } else {
                floor = "nej";
            }
        }
        CarportRequestDTO carportRequestDTO = serviceFactory.getCarportService().convertCarportResponseToRequest(carportResponseDTO);
        String svgCarport = serviceFactory.getBlueprintService().createBlueprintNoMeasures(carportRequestDTO);

        ctx.attribute("svg_carport_details", svgCarport);
        ctx.sessionAttribute("inquiry_responseDTO", inquiryResponseDTO);
        ctx.attribute("selected_inquiry", inquiryResponseDTO);
        ctx.attribute("selected_carport", carportResponseDTO);
        ctx.attribute("selected_shed", shed);
        ctx.attribute("selected_floor", floor);
        ctx.render("customer-inquiry-details.html");
    }

    public void adminGetInquiryDetails(Context ctx, ServiceFactory serviceFactory) throws DatabaseException, CalculatorException {
        if (!UserValidator.isAdmin(ctx)) {
            ctx.redirect("/");
            return;
        }
        SalesRepResponseDTO admin = ctx.sessionAttribute("currentUser");
        ctx.attribute("currentUser", admin);

        int inquiryId = Integer.parseInt(ctx.pathParam("inquiry_id"));
        InquiryResponseDTO inquiryResponseDTO = serviceFactory.getInquiryService().getInquiry(inquiryId);

        CarportResponseDTO carportResponseDTO = inquiryResponseDTO.getCarportResponseDTO();

        ShedResponseDTO shed = null;
        if (carportResponseDTO instanceof CarportShedResponseDTO withShed) {
            shed = withShed.getShedResponseDTO();
        }
        CarportRequestDTO carportRequestDTO = serviceFactory.getCarportService().convertCarportResponseToRequest(carportResponseDTO);
        String svgCarport = serviceFactory.getBlueprintService().createBlueprintNoMeasures(carportRequestDTO);

        ctx.attribute("svg_carport_details", svgCarport);
        ctx.attribute("shed", shed);
        ctx.attribute("inquiry_quote_preview", inquiryResponseDTO);
        ctx.attribute("carport_quote_preview", inquiryResponseDTO.getCarportResponseDTO());
        ctx.attribute("customer_quote_preview", inquiryResponseDTO.getCustomerResponseDTO());
        ctx.render("admin-inquiry-details.html");
    }

    public void adminDeleteInquiry(Context ctx, ServiceFactory serviceFactory) throws DatabaseException, CalculatorException {
        if (!UserValidator.isAdmin(ctx)) {
            ctx.redirect("/");
            return;
        }
        SalesRepResponseDTO admin = ctx.sessionAttribute("currentUser");
        ctx.attribute("currentUser", admin);

        int inquiryId = Integer.parseInt(ctx.pathParam("inquiry_id"));
        InquiryResponseDTO inquiryResponseDTO = serviceFactory.getInquiryService().getInquiry(inquiryId);
        serviceFactory.getInquiryService().deleteInquiry(inquiryId);
        serviceFactory.getCarportService().deleteCarport(inquiryResponseDTO.getCarportResponseDTO().getCarportId());
        ctx.render("admin-all-inquiries.html");
    }
}
