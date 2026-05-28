package app.services.utils.partsListCalculator;

import app.entities.*;
import app.exceptions.CalculatorException;

import java.util.List;

public class HardwareCalculator {
    private int bracingQuantity;
    private final String flatRoof = "Fladt tag";
    private final String highRoof = "Højt tag";
    ProductFinder productFinder = new ProductFinder();

    public void addPoleProducts(List<ProductsPartsListEntry> productsPartsListEntries, Roof roof, Shed shed, Carport carport, double rafterQuantity, double poleQuantity, double sidingQuantity, List<Product> products) throws CalculatorException {
        addScrewsToRoof(productsPartsListEntries, roof.getRoofType(), roof.getRoofSlope(), roof.getRoofMaterial(), carport.getLength(), shed.getLength(), products);
        addBracingStrap(productsPartsListEntries, roof.getRoofType(), roof.getRoofSlope(), carport.getLength(), shed.getLength(), products);
        addUniversalConnectorAndScrews(productsPartsListEntries, rafterQuantity, products);
        addFasciaAndFasciaCappingScrews(productsPartsListEntries, products);
        addBoltsToPoles(productsPartsListEntries, carport.getShedId(), poleQuantity, products);
        addBoltPlates(productsPartsListEntries, poleQuantity, products);

        if (carport.getShedId() != null) {
            addHardwareToShedDoor(productsPartsListEntries, products);
            addScrewsToSiding(productsPartsListEntries, shed.getSiding(), sidingQuantity, products);
            addAngleBracketsToShedWithScrews(productsPartsListEntries, shed.getWidth(), shed.getLength(), products);
        }
    }

    private void addScrewsToRoof(List<ProductsPartsListEntry> productsPartsListEntries, String roofType, double roofSlope, String roofMaterial, double width, double length, List<Product> products) throws CalculatorException {
        String placementDescription = "Skruer til tag";
        String productDescription = "";
        double quantity = 0;

        if (roofType.equals(flatRoof)) {
            double squareMeters = (width / 100.0) * (length / 100.0);
            double totalScrews = squareMeters * 12; // Plastmo — 12 screws pr. m²
            quantity = Math.ceil(totalScrews / 200); // 200 pr. package
            productDescription = "plastmo bundskruer 200 stk.";

        } else if (roofType.equals(highRoof)) {
            double actualRoofWidthOneSide = (width / 2.0) / Math.cos(Math.toRadians(roofSlope));

            switch (roofMaterial) {
                case "Betontagsten - sort", "Betontagsten - koralrød" -> {
                    double squareMeters = (actualRoofWidthOneSide / 100.0) * (length / 100.0) * 2;
                    double totalNails = squareMeters * 9 * 2; // 2 nails pr. stone and 9 stones pr. m²
                    quantity = Math.ceil(totalNails / 300); // Retted: divideret med 300 for at matche "300 stk." produktet
                    productDescription = "4,5 x 50 mm. Skruer 300 stk.";
                }
                case "Eternittag B6 - sortblå", "Eternittag B6 - grå" -> {
                    double roofArea = length * actualRoofWidthOneSide * 2;
                    double platesPerCm2 = 1.0 / (103 * 101); // plate covers 103*101
                    double totalScrews = roofArea * platesPerCm2 * 6; // 6 screws pr. plate
                    quantity = Math.ceil(totalScrews / 250); // 250 screw pr. package
                    productDescription = "4,0 x 50 mm. beslagskruer 250 stk.";
                }
                case "Eternittag B7 - sortblå" -> {
                    double roofArea = length * actualRoofWidthOneSide * 2;
                    double platesPerCm2 = 1.0 / (46 * 102); // plate covers 46*102
                    double totalScrews = roofArea * platesPerCm2 * 4; // 4 screws pr. plate
                    quantity = Math.ceil(totalScrews / 250);
                    productDescription = "4,0 x 50 mm. beslagskruer 250 stk.";
                }
                default -> throw new CalculatorException("Ukendt tagmateriale: " + roofMaterial);
            }
        } else {
            throw new CalculatorException("Ukendt tagtype: " + roofType);
        }

        Product product = productFinder.findProductByDescription(products, productDescription);
        productsPartsListEntries.add(new ProductsPartsListEntry(product, quantity, placementDescription));
    }

    private void addBracingStrap(List<ProductsPartsListEntry> productsPartsListEntries, String roofType, double roofSlope, double width, double length, List<Product> products) throws CalculatorException {
        String placementDescription = "Til vindkryds på spær";
        String productDescription = "hulbånd 1x20 mm. 10 mtr.";
        Product strapRollProduct = productFinder.findProductByDescription(products, productDescription);
        double bracingWidth = width;
        bracingQuantity = 2;

        if (roofType.equals(highRoof)) {
            bracingWidth = (width / 2.0) / Math.cos(Math.toRadians(roofSlope));
            bracingQuantity = 4;
        }
        double oneBracingLength = Math.sqrt(Math.pow(length, 2) + Math.pow(bracingWidth, 2));
        double bracingRollQuantity = Math.ceil(((bracingQuantity * oneBracingLength) * 1.1) / 1000); // 1.1 is adding 10% extra and 1000 is 10 mtr roll

        productsPartsListEntries.add(new ProductsPartsListEntry(strapRollProduct, bracingRollQuantity, placementDescription));
    }

    private void addUniversalConnectorAndScrews(List<ProductsPartsListEntry> productsPartsListEntries, double rafterQuantity, List<Product> products) throws CalculatorException {
        Product leftConnector = productFinder.findProductByDescription(products, "universal 190 mm venstre");
        Product rightConnector = productFinder.findProductByDescription(products, "universal 190 mm højre");
        Product screws = productFinder.findProductByDescription(products, "4,0 x 50 mm. beslagskruer 250 stk.");

        productsPartsListEntries.add(new ProductsPartsListEntry(leftConnector, rafterQuantity, "Til montering af spær på rem"));
        productsPartsListEntries.add(new ProductsPartsListEntry(rightConnector, rafterQuantity, "Til montering af spær på rem"));

        double totalConnectors = rafterQuantity * 2;
        double screwsQuantity = (totalConnectors * 14) + (bracingQuantity * rafterQuantity); //14 screws pr connector + adding screws for bracing
        double screwBox = Math.ceil(screwsQuantity / 250); // 250 screws pr box
        productsPartsListEntries.add(new ProductsPartsListEntry(screws, screwBox, "Til montering af universalbeslag + hulbånd"));
    }

    private void addFasciaAndFasciaCappingScrews(List<ProductsPartsListEntry> productsPartsListEntries, List<Product> products) throws CalculatorException {
        Product product = productFinder.findProductByDescription(products, "4,5 x 60 mm. skruer 200 stk.");
        productsPartsListEntries.add(new ProductsPartsListEntry(product, 1, "Til montering af stern & vandbrædt"));
    }

    private void addBoltsToPoles(List<ProductsPartsListEntry> productsPartsListEntries, Integer shedId, double poleQuantity, List<Product> products) throws CalculatorException {
        Product product = productFinder.findProductByDescription(products, "bræddebolt 10 x 120 mm.");
        double ifShed = 0;
        if (shedId != null) {
            ifShed = 12 + 4; // 2x6 extra bolts + 4 ekstra bolts when shed starts
        }
        double quantity = Math.ceil(((poleQuantity * 2) + ifShed) / 12); // 12 per package
        productsPartsListEntries.add(new ProductsPartsListEntry(product, quantity, "Til montering af rem på stolper"));
    }

    private void addBoltPlates(List<ProductsPartsListEntry> productsPartsListEntries, double poleQuantity, List<Product> products) throws CalculatorException {
        Product product = productFinder.findProductByDescription(products, "firkantskiver 40x40x11mm");
        productsPartsListEntries.add(new ProductsPartsListEntry(product, poleQuantity * 2, "Til montering af rem på stolper"));
    }

    private void addScrewsToSiding(List<ProductsPartsListEntry> productsPartsListEntries, String shedSiding, double sidingQuantity, List<Product> products) throws CalculatorException {
        double totalShortScrews = 0;
        double totalLongScrews = 0;

        switch (shedSiding) {
            case "Beklædning: 19x100mm Profilbrædt (1 på 2 beklædning)",
                 "Beklædning: 19x100 mm. trykimp. Brædt" -> {
                totalShortScrews = Math.ceil(sidingQuantity / 2) * 3;
                totalLongScrews = Math.ceil(sidingQuantity / 2) * 6;
            }
            case "Beklædning: 19x125mm Klinkbeklædning trykimp.",
                 "Beklædning: 25x150mm Blokhusbrædder ubehandlet" -> {
                totalLongScrews = sidingQuantity * 8;
            }
            case "Beklædning: 9x1220x2440mm Krydsfiner m/spor (Svalehale)" -> {
                totalShortScrews = sidingQuantity * 50;
            }
            default -> throw new CalculatorException("Kan ikke tilføje skruer");
        }
        if (totalLongScrews > 0) {
            double box = Math.ceil(totalLongScrews / 400.0);
            Product outerProduct = productFinder.findProductByDescription(products, "4,5 x 70 mm. Skruer 400 stk.");
            productsPartsListEntries.add(new ProductsPartsListEntry(outerProduct, box, "Til montering af beklædning"));
        }
        if (totalShortScrews > 0) {
            double box = Math.ceil(totalShortScrews / 300.0);
            Product innerProduct = productFinder.findProductByDescription(products, "4,5 x 50 mm. Skruer 300 stk.");
            productsPartsListEntries.add(new ProductsPartsListEntry(innerProduct, box, "Til montering af beklædning"));
        }
    }

    private void addHardwareToShedDoor(List<ProductsPartsListEntry> productsPartsListEntries, List<Product> products) throws CalculatorException {
        Product hinge = productFinder.findProductByDescription(products, "t hængsel 390 mm");
        Product latch = productFinder.findProductByDescription(products, "stalddørsgreb 50x75");
        productsPartsListEntries.add(new ProductsPartsListEntry(hinge, 2, "Til skur dør"));
        productsPartsListEntries.add(new ProductsPartsListEntry(latch, 1, "Til lås på dør i skur"));
    }

    private void addAngleBracketsToShedWithScrews(List<ProductsPartsListEntry> productsPartsListEntries, double shedWidth, double shedLength, List<Product> products) throws CalculatorException {
        double studsEnds = getStudQuantity(shedWidth) + 1;
        double studsSides = getStudQuantity(shedLength) + 1;

        double totalColumns = (studsEnds * 2) + (studsSides * 2); // 2 end walls, 2 side walls

        int totalRows = 3; // Always 3
        double totalHorizontalBeams = totalColumns * totalRows;

        double totalBracketsNeeded = (totalHorizontalBeams * 2) + 4; // 2 brackets pr beam, 4 to reinforce around door.

        Product angleBracket = productFinder.findProductByDescription(products, "vinkelbeslag 35");
        productsPartsListEntries.add(new ProductsPartsListEntry(angleBracket, totalBracketsNeeded, "Til montering af løsholter i skur"));

        double totalBracketScrews = totalBracketsNeeded * 6; // 6 screws pr bracket
        double screwBoxes = Math.ceil(totalBracketScrews / 250.0); // 250 pr package

        Product screws = productFinder.findProductByDescription(products, "4,0 x 50 mm. beslagskruer 250 stk.");
        productsPartsListEntries.add(new ProductsPartsListEntry(screws, screwBoxes, "Til montering af vinkelbeslag 35 i skur"));
    }

    //======= Service Methods ======= //
    private double getStudQuantity(double lengthOrWidth) {
        double studSpace = 70;
        return (Math.ceil(lengthOrWidth / studSpace) - 1);
    }
}
