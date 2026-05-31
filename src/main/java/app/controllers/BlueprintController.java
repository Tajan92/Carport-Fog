package app.controllers;

import app.dto.requestDTO.RoofRequestDTO;
import app.dto.requestDTO.carports.CarportRequestDTO;
import app.exceptions.CalculatorException;
import app.exceptions.DatabaseException;
import app.services.ServiceFactory;
import io.javalin.Javalin;
import io.javalin.http.Context;

public class BlueprintController {

    public void addRoutes(Javalin app, ServiceFactory serviceFactory) {
        app.post("/blueprint/preview", ctx -> previewCarportSvg(ctx, serviceFactory));
    }

    private void previewCarportSvg(Context ctx, ServiceFactory serviceFactory) throws CalculatorException, DatabaseException {
        double carportWidth = Double.parseDouble(ctx.formParam("carport_width"));
        double carportLength = Double.parseDouble(ctx.formParam("carport_length"));
        double carportHeight = 230; // preset height

        String roofType = ctx.formParam("roof_type");
        if (roofType == null || roofType.isBlank()) {
            roofType = "Fladt tag";
        }

        double roofSlope = 1.7;
        if (!roofType.equals("Fladt tag") && ctx.formParam("roof_slope") != null) {
            roofSlope = Double.parseDouble(ctx.formParam("roof_slope"));
        }
        String roofMaterial = ctx.formParam("roof_material");
        if (roofType.equals("Fladt tag")) {
            roofMaterial = "Plastmo Ecolite blåtonet";
        }
        if (roofMaterial == null || roofMaterial.isBlank()) {
            roofMaterial = "Eternittag B6 - sortblå";
        }
        RoofRequestDTO roofRequestDTO = new RoofRequestDTO(roofSlope, roofMaterial, roofType);

        String shedStatus = ctx.formParam("shed_status");
        String shedSiding = ctx.formParam("shed_siding");
        boolean floor = "TRUE".equals(ctx.formParam("shed_floor"));

        String shedWidth = null;
        String shedLength = null;

        if ("HALF".equals(shedStatus)) {
            shedWidth = String.valueOf(carportWidth / 2);
            shedLength = String.valueOf(carportLength / 3);
        } else if ("FULL".equals(shedStatus)) {
            shedWidth = String.valueOf(carportWidth);
            shedLength = String.valueOf(carportLength / 3);
        }

        CarportRequestDTO carportRequestDTO = serviceFactory.getCarportService()
                .checkShed(shedWidth, shedLength, shedSiding, floor, carportWidth, carportHeight, carportLength, roofRequestDTO);

        String svgCarport = serviceFactory.getBlueprintService().createBlueprint(carportRequestDTO);

        ctx.result(svgCarport);
    }
}
