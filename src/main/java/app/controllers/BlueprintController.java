package app.controllers;

import app.services.ServiceFactory;
import io.javalin.Javalin;
import io.javalin.http.Context;

public class BlueprintController {

    public void addRoutes(Javalin app, ServiceFactory serviceFactory){
        app.post("/createBlueprint", ctx -> showCarportSvg(ctx, serviceFactory));
    }

    private void showCarportSvg(Context ctx, ServiceFactory serviceFactory){
        String carportSvg = serviceFactory.getBlueprintService().toString();

        ctx.attribute("carportSvg", carportSvg);
        ctx.render("admin-quote-maker");
    }
}
