package app.services.utils.partsListCalculator;

import app.entities.*;
import app.exceptions.CalculatorException;
import lombok.Getter;

import java.util.List;

@Getter

public class ShedCalculator {
    private double sidingQuantity;
    private final String flatRoof = "Fladt tag";
    private final String highRoof = "Højt tag";
    private ProductFinder productFinder = new ProductFinder();

    public void addShedProducts(List<ProductsPartsListEntry> productsPartsListEntries,Carport carport, Roof roof, Shed shed, List<Product> products) throws CalculatorException {
       if (carport.getShedId() != null){
           addStabilizerToShedDoor(productsPartsListEntries, products);
           addShedSiding(productsPartsListEntries, shed.getSiding(), shed.getWidth(), shed.getLength(), products);
           addShedStuds(productsPartsListEntries, roof.getRoofType(), shed.getWidth(), shed.getLength(), products);
       }
    }

    private void addStabilizerToShedDoor(List<ProductsPartsListEntry> productsPartsListEntries, List<Product> products) throws CalculatorException {
        Product product = productFinder.findProductByDescription(products,"38x73 mm. Lægte ubh. (Lægte til dør)");
        productsPartsListEntries.add(new ProductsPartsListEntry(product, 1, "Til Z på bagside af dør"));
    }

    private void addShedSiding(List<ProductsPartsListEntry> productsPartsListEntries, String shedSiding, double shedWidth, double shedLength, List<Product> products) throws CalculatorException {
        String placementDescriptionShedSiding = "til beklædning af skur";
        double sidingCover = 0;
        double add10percent = 1.1;
        double shedHeight = 210;
        Product shedSidingProduct;

        switch (shedSiding) {
            case "Beklædning: 19x100 mm. trykimp. Brædt", "Beklædning: 19x100mm Profilbrædt (1 på 2 beklædning)" -> {
                sidingCover = 8.5;
                sidingQuantity = Math.ceil(((shedWidth * 2) + (shedLength * 2)) / sidingCover) * 2;
            }
            case "Beklædning: 19x125mm Klinkbeklædning trykimp." -> {
                sidingCover = 10.0;
                sidingQuantity = Math.ceil(((shedWidth * 2) + (shedLength * 2)) / sidingCover);
            }
            case "Beklædning: 25x150mm Blokhusbrædder ubehandlet" -> {
                sidingCover = 14.3;
                sidingQuantity = Math.ceil(((shedWidth * 2) + (shedLength * 2)) / sidingCover);
            }
            case "Beklædning: 9x1220x2440mm Krydsfiner m/spor (Svalehale)" -> {
                double plateWidth = 122.0;
                double plateHeight = 244.0;
                double perimeterCm = (shedWidth * 2 + shedLength * 2);
                double platesHorizontal = Math.ceil(perimeterCm / plateWidth);
                double platesVertical = Math.ceil(shedHeight / plateHeight);
                sidingQuantity = platesHorizontal * platesVertical;
            }
            default -> throw new CalculatorException("Kan ikke tilføje " + shedSiding + " som beklædning");
        }

        shedSidingProduct = productFinder.findProductByDescriptionAndNearestLength(products, shedSiding, shedHeight);
        productsPartsListEntries.add(new ProductsPartsListEntry(shedSidingProduct, sidingQuantity * add10percent, placementDescriptionShedSiding));
    }

    private void addShedStuds(List<ProductsPartsListEntry> productsPartsListEntries, String roofType, double shedWidth, double shedLength, List<Product> products) throws CalculatorException {
        String description = "45x95 mm. Reglar ub.";
        String placementDescriptionEnds = "Løsholter til skur gavle";
        String placementDescriptionSides = "Løsholter til skur sider";
        Product product = productFinder.findProductByDescriptionAndNearestLength(products, description, 240);
        Product productEnds = productFinder.findProductByDescriptionAndNearestLength(products, description, 270);

        double spaceEnds = getStudQuantity(shedWidth) + 1;
        double spaceSides = getStudQuantity(shedLength) + 1;
        double quantityEnds = spaceEnds * 2 * 3; // 2 walls, 3 rows
        double quantitySides = spaceSides * 2 * 3; // 2 walls, 3 rows

        if (roofType.equals(flatRoof)) {
            productsPartsListEntries.add(new ProductsPartsListEntry(product, quantityEnds, placementDescriptionEnds));
            productsPartsListEntries.add(new ProductsPartsListEntry(product, quantitySides, placementDescriptionSides));
        } else if (roofType.equals(highRoof)) {
            productsPartsListEntries.add(new ProductsPartsListEntry(productEnds, quantityEnds, placementDescriptionEnds));
            productsPartsListEntries.add(new ProductsPartsListEntry(product, quantitySides, placementDescriptionSides));
        } else {
            throw new CalculatorException("Ukendt tagtype til løsholter: " + roofType);
        }
    }

    //======= Service Methods ======= //
    private double getStudQuantity(double lengthOrWidth) {
        double studSpace = 70;
        return (Math.ceil(lengthOrWidth / studSpace) - 1);
    }
}
