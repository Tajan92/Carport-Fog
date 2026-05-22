package app.controllers;

import app.dto.responseDTO.carports.CarportResponseDTO;
import app.exceptions.CalculatorException;
import app.exceptions.DatabaseException;
import app.services.ServiceFactory;
import io.javalin.Javalin;
import io.javalin.http.Context;

public class CarportController {

    public void addRoutes(Javalin app, ServiceFactory serviceFactory) {
        app.get("/getCarport/{carport_id}", ctx -> getCarport(ctx, serviceFactory));
    }

    public void getCarport(Context ctx, ServiceFactory serviceFactory) throws CalculatorException, DatabaseException {
       int carportId = Integer.parseInt(ctx.pathParam("carport_id"));
       CarportResponseDTO carportResponseDTO = serviceFactory.getCarportService().getCarport(carportId);
       ctx.attribute("getCarport", carportResponseDTO);
       ctx.render("admin-quote-maker");
    }





}
