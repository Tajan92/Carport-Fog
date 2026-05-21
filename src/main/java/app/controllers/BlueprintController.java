package app.controllers;

import app.services.ServiceFactory;
import io.javalin.Javalin;

public class BlueprintController {

    public void addRoutes(Javalin app, ServiceFactory serviceFactory){
        app.post("/createBlueprint", ctx -> ctx.createBlueprint(ctx, serviceFactory));
        app.get("/getBlueprint", ctx -> ctx.getBluePrint(ctx, serviceFactory));
    }
}
