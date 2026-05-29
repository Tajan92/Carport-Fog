package app.services.utils.partsListCalculator;

import app.entities.*;
import app.exceptions.CalculatorException;


import java.util.List;


public class PoleCalculator {
    ProductFinder productFinder = new ProductFinder();
    private int poleQuantity;

    public double addPoleProducts(List<ProductsPartsListEntry> productsPartsListEntries, Shed shed, Carport carport, List<Product> products) throws CalculatorException {
        addPoles(productsPartsListEntries, carport.getLength(), shed.getLength(), carport.getShedId(), products);
        if (carport.getShedId() != null) {
            addPolesToShed(productsPartsListEntries, products);
        }
        return poleQuantity;
    }

    private void addPoles(List<ProductsPartsListEntry> productsPartsListEntries, double length, double shedLength, Integer shedId, List<Product> products) throws CalculatorException {
        double marginFront = 100;
        double marginBack = 35;
        double maxPoleDistance = 300;
        String placementDescription = "Stolper nedgraves 90 cm. i jord";
        double coverageLength;

        if (shedId == null) {
            coverageLength = length - marginFront - marginBack;
        } else {
            coverageLength = length - marginFront - marginBack - shedLength;
        }

        coverageLength = Math.max(0, coverageLength);
        int spaces = (int) Math.ceil(coverageLength / maxPoleDistance);
        poleQuantity = (spaces + 1) * 2;

        Product product = productFinder.findProductByDescription(products, "97x97 mm. trykimp. Stolpe");
        productsPartsListEntries.add(new ProductsPartsListEntry(product, poleQuantity, placementDescription));
    }

    private void addPolesToShed(List<ProductsPartsListEntry> productsPartsListEntries, List<Product> products) throws CalculatorException {
        Product product = productFinder.findProductByDescription(products, "97x97 mm. trykimp. Stolpe");
        productsPartsListEntries.add(new ProductsPartsListEntry(product, 2, "Midterstolper til skur"));
        productsPartsListEntries.add(new ProductsPartsListEntry(product, 4, "Hjørnestolper til skur"));
    }
}
