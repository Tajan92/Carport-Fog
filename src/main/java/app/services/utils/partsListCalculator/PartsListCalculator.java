package app.services.utils.partsListCalculator;

import app.entities.*;
import app.exceptions.CalculatorException;

import java.util.ArrayList;
import java.util.List;

public class PartsListCalculator {
    private final ShedCalculator shedCalculator = new ShedCalculator();
    private final RoofCalculator roofCalculator = new RoofCalculator();
    private final PoleCalculator poleCalculator = new PoleCalculator();
    private final HardwareCalculator hardwareCalculator = new HardwareCalculator();

    public List<ProductsPartsListEntry> createProductsPartsList(Carport carport, Shed shed, Roof roof, List<Product> allProducts) throws CalculatorException {
        List<ProductsPartsListEntry> productsPartsListEntries = new ArrayList<>();
        Integer shedId = carport.getShedId();

        if (shedId != null) {
            if (shed == null) {
                throw new CalculatorException("Skur mangler — carport har shed_id men ingen shed blev givet");
            }
        }
        double poleQuantity = poleCalculator.addPoleProducts(productsPartsListEntries, shed, carport, allProducts);
        double rafterQuantity =roofCalculator.addRoofProducts(productsPartsListEntries, roof, carport, allProducts);
        double sidingQuantity = shedCalculator.addShedProducts(productsPartsListEntries, carport, roof, shed, allProducts);
        hardwareCalculator.addHardwareProducts(productsPartsListEntries, roof, shed, carport, rafterQuantity, poleQuantity, sidingQuantity, allProducts);

        return productsPartsListEntries;
    }
}
