package app.controllers;

import app.dto.requestDTO.RoofRequestDTO;
import app.dto.requestDTO.ShedRequestDTO;
import app.dto.requestDTO.carports.CarportRequestDTO;
import app.dto.requestDTO.carports.CarportShedRequestDTO;
import app.exceptions.CalculatorException;
import app.exceptions.DatabaseException;
import app.services.ServiceFactory;
import io.javalin.Javalin;
import io.javalin.http.Context;

public class BlueprintController {

    public void addRoutes(Javalin app, ServiceFactory serviceFactory){
        app.post("/blueprint/preview", ctx -> showCarportSvg(ctx, serviceFactory));
    }

    private void showCarportSvg(Context ctx, ServiceFactory serviceFactory) throws CalculatorException, DatabaseException {
// 1. Extract Carport measurements
        double carportWidth = Double.parseDouble(ctx.formParam("carport_width"));
        double carportLength = Double.parseDouble(ctx.formParam("carport_length"));
        double carportHeight = 230; // default standard

        // 2. Extract Roof data
        String roofType = ctx.formParam("roof_type");
        if (roofType == null || roofType.isBlank()) roofType = "Fladt tag";

        double roofSlope = 1.7;
        if (!roofType.equals("Fladt tag") && ctx.formParam("roof_slope") != null) {
            roofSlope = Double.parseDouble(ctx.formParam("roof_slope"));
        }
        String roofMaterial = ctx.formParam("roof_material");
        RoofRequestDTO roofRequestDTO = new RoofRequestDTO(roofSlope, roofMaterial, roofType);

        // 3. Extract Shed data
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

        // 4. Generate DTO and call your Blueprint drawing service
        CarportRequestDTO carportRequestDTO = serviceFactory.getCarportService()
                .checkShed(shedWidth, shedLength, shedSiding, floor, carportWidth, carportHeight, carportLength, roofRequestDTO);

        String svgCarport = serviceFactory.getBlueprintService().createBlueprint(carportRequestDTO);

        // 5. Return the raw SVG string back to the browser
        ctx.result(svgCarport);
    }
}
