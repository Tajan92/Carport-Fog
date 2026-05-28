package app.services.utils;

import app.entities.*;
import app.exceptions.CalculatorException;
import app.services.utils.partsListCalculator.HardwareCalculator;
import app.services.utils.partsListCalculator.PoleCalculator;
import app.services.utils.partsListCalculator.RoofCalculator;
import app.services.utils.partsListCalculator.ShedCalculator;

import java.util.ArrayList;
import java.util.List;

public class PartsListCalculator {
    public List<ProductsPartsListEntry> createProductsPartsList(Carport carport, Shed shed, Roof roof, List<Product> allProducts) throws CalculatorException {
        List<ProductsPartsListEntry> productsPartsListEntries = new ArrayList<>();
        Integer shedId = carport.getShedId();
        if (shedId != null) {
            if (shed == null) {
                throw new CalculatorException("Skur mangler — carport har shed_id men ingen shed blev givet");
            }
        }
        ShedCalculator shedCalculator = new ShedCalculator();
        RoofCalculator roofCalculator = new RoofCalculator();
        PoleCalculator poleCalculator = new PoleCalculator();
        HardwareCalculator hardwareCalculator = new HardwareCalculator();

        poleCalculator.addPoleProducts(productsPartsListEntries, shed, carport, allProducts);
        roofCalculator.addRoofProducts(productsPartsListEntries, roof, carport, allProducts);
        shedCalculator.addShedProducts(productsPartsListEntries, carport, roof, shed, allProducts);
        hardwareCalculator.addPoleProducts(productsPartsListEntries, roof, shed, carport, roofCalculator.getRafterQuantity(), poleCalculator.getPoleQuantity(), shedCalculator.getSidingQuantity(), allProducts);

        return productsPartsListEntries;
    }
}
