package app.controllers;

import app.services.ServiceFactory;
import io.javalin.Javalin;

public class ShedController {

    public void addRoutes(Javalin app, ServiceFactory serviceFactory) {
        app.post("/createShed", ctx -> createShed(ctx, serviceFactory));
        app.get("/getShed", ctx -> getShed(ctx, serviceFactory));
        app.post("/updateShed", ctx -> updateShed(ctx, serviceFactory));
        app.post("/deleteShed", ctx -> deleteShed(ctx, serviceFactory));
    }


}
