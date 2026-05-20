package app.services.utils;

import app.entities.*;
import app.exceptions.CalculatorException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PartsListCalculator {
    private List<ProductsPartsListEntry> productsPartsListEntries;
    private List<Product> products;
    private double width;
    private double length;
    private Integer shedId;
    private double shedLength;
    private double shedWidth;
    private String siding;
    private double sidingQuantity;
    private String roofType;
    private double roofSlope;
    private String roofMaterial;
    private double rafterDistance;
    private int rafterQuantity;
    private String rafterDescription;
    private int poleQuantity;
    private int bracingQuantity;
    private final String flatRoof = "Fladt tag";
    private final String highRoof = "Højt tag";

    public List<ProductsPartsListEntry> createProductsPartsList(Carport carport, Shed shed, Roof roof, List<Product> allProducts) throws CalculatorException {
        this.width = carport.getWidth();
        this.length = carport.getLength();
        this.shedId = carport.getShedId();
        this.roofType = roof.getRoofType();
        this.roofSlope = roof.getRoofSlope();
        this.roofMaterial = roof.getRoofMaterial();
        this.products = allProducts;
        this.productsPartsListEntries = new ArrayList<>();
        if (shedId != null) {
            if (shed == null) {
                throw new CalculatorException("Skur mangler — carport har shed_id men ingen shed blev givet");
            }
            this.shedLength = shed.getLength();
            this.shedWidth = shed.getWidth();
            this.siding = shed.getSiding();
        }

        //======= Timber & Roofing ======= //
        addPoles();
        addRafters();
        addTopPlates();
        addFasciaBoards();
        addFasciaCapping();
        addRoofMaterial();
        if (shedId != null) {
            addShedStuds();
            addShedSiding();
            addStabilizerToShedDoor();
            addPolesToShed();
        }

        //======= Hardware & Screws ======= //
        addScrewsToRoof();
        addBracingStrap();
        addUniversalConnectorAndScrews();
        addFasciaAndFasciaCappingScrews();
        addBoltsToPoles();
        addBoltPlates();
        if (shedId != null) {
            addScrewsToSiding();
            addHardwareToShedDoor();
            addAngleBracketsToShedWithScrews();
        }
        return this.productsPartsListEntries;
    }

    //======= Timber & Roofing ======= //

    private void addPolesToShed() throws CalculatorException {
        Product product = findProductByDescription("97x97 mm. trykimp. Stolpe");
        productsPartsListEntries.add(new ProductsPartsListEntry(product, 2, "Midterstolper til skur"));
        productsPartsListEntries.add(new ProductsPartsListEntry(product, 4, "Hjørnestolper til skur"));
    }

    private void addStabilizerToShedDoor() throws CalculatorException {
        Product product = findProductByDescription("38x73 mm. Lægte ubh. (Lægte til dør)");
        productsPartsListEntries.add(new ProductsPartsListEntry(product, 1, "Til Z på bagside af dør"));
    }

    private void addRoofMaterial() throws CalculatorException {
        String placementDescriptionRoofMaterial = "Tag monteres på spær";
        String descriptionRoofMaterial = roofMaterial;
        Product roofProduct = null;
        double quantity;
        double plateCoverageLength = 0;
        double plateCoverageWidth = 100;

        if (roofType.equals(flatRoof)) {
            if (length >= 600) {
                plateCoverageLength = 600;
            } else {
                plateCoverageLength = 360;
            }
            roofProduct = findProductByDescriptionAndNearestLength(descriptionRoofMaterial, plateCoverageLength);

            double lengthPlates = Math.ceil(length / plateCoverageLength);
            double widthPlates = Math.ceil(width / plateCoverageWidth);

            quantity = lengthPlates * widthPlates;
            productsPartsListEntries.add(new ProductsPartsListEntry(roofProduct, quantity, placementDescriptionRoofMaterial));

            return;
        } else if (roofType.equals(highRoof)) {
            switch (roofMaterial) {
                case "Eternittag B6 - sortblå", "Eternittag B6 - grå" -> {
                    plateCoverageLength = 103;
                    plateCoverageWidth = 101;
                }
                case "Eternittag B7 - sortblå" -> {
                    plateCoverageLength = 46;
                    plateCoverageWidth = 102;
                }
                case "Betontagsten - sort", "Betontagsten - koralrød" -> {
                    plateCoverageLength = 30;
                    plateCoverageWidth = 37;
                }
                default -> {
                    throw new CalculatorException("Kan ikke finde materialet " + roofMaterial + " til dette tag.");
                }
            }
            roofProduct = findProductByDescription(descriptionRoofMaterial);

            double actualRoofWidthOneSide = (width / 2.0) / Math.cos(Math.toRadians(roofSlope));
            double lengthPlates = Math.ceil(length / plateCoverageLength);
            double widthPlates = Math.ceil(actualRoofWidthOneSide / plateCoverageWidth);

            quantity = (lengthPlates * widthPlates) * 2;

            productsPartsListEntries.add(new ProductsPartsListEntry(roofProduct, quantity, placementDescriptionRoofMaterial));
            return;
        }
            throw new CalculatorException("Kan ikke finde materialet til dette tag.");
    }

    private void addShedSiding() throws CalculatorException {
        String placementDescriptionShedSiding = "til beklædning af skur";
        double sidingCover = 0;
        double add10percent = 1.1;
        double shedHeight = 210;
        Product shedSiding;

        switch (siding) {
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
            default -> throw new CalculatorException("Kan ikke tilføje " + siding + " som beklædning");
        }

        shedSiding = findProductByDescriptionAndNearestLength(siding, shedHeight);
        productsPartsListEntries.add(new ProductsPartsListEntry(shedSiding, sidingQuantity * add10percent, placementDescriptionShedSiding));
    }

    private void addFasciaCapping() throws CalculatorException {
        String placementDescriptionFasciaCappingSides = "Vandbrædt på stern i sider";
        String placementDescriptionFasciaCappingFront = "Vandbrædt på stern i forende";
        String productDescription = "19x100 mm. trykimp. Brædt";
        Product fasciasCappingFront;
        Product fasciaCappingSides;
        double frontBackQuantity;
        double sidesQuantity;
        double fasciaCappingWidth = width;

        if (roofType.equals(highRoof)) {
            fasciaCappingWidth = width / Math.cos(Math.toRadians(roofSlope));
        }

        if (width >= 540) {
            frontBackQuantity = Math.ceil(fasciaCappingWidth / 540);
            fasciasCappingFront = findProductByDescriptionAndNearestLength(productDescription, 540);
        } else if (width >= 360) {
            frontBackQuantity = Math.ceil(fasciaCappingWidth / 360);
            fasciasCappingFront = findProductByDescriptionAndNearestLength(productDescription, 360);
        } else {
            frontBackQuantity = Math.ceil(fasciaCappingWidth / 210);
            fasciasCappingFront = findProductByDescriptionAndNearestLength(productDescription, 210);
        }

        if (length >= 540) {
            sidesQuantity = Math.ceil(length / 540) * 2;
            fasciaCappingSides = findProductByDescriptionAndNearestLength(productDescription, 540);
        } else if (length == 360) {
            sidesQuantity = Math.ceil(length / 360) * 2;
            fasciaCappingSides = findProductByDescriptionAndNearestLength(productDescription, 360);
        } else {
            sidesQuantity = Math.ceil(length / 210) * 2;
            fasciaCappingSides = findProductByDescriptionAndNearestLength(productDescription, 210);
        }
        productsPartsListEntries.add(new ProductsPartsListEntry(fasciasCappingFront, frontBackQuantity, placementDescriptionFasciaCappingFront));
        productsPartsListEntries.add(new ProductsPartsListEntry(fasciaCappingSides, sidesQuantity, placementDescriptionFasciaCappingSides));
    }

    private void addFasciaBoards() throws CalculatorException {
        String placementDescriptionUnderFasciasFrontBack = "Understernbrædder til for & bag ende";
        String placementDescriptionUnderFasciasSides = "Understernbrædder til siderne";
        String placementDescriptionOverFasciasFront = "Oversternbrædder til forenden";
        String placementDescriptionOverFasciasSides = "Oversternbrædder til siderne";
        String productDescriptionUnderFascias = "25x200 mm. trykimp. Brædt (Sternbræt)";
        String productDescriptionOverFascias = "25x125mm. trykimp. Brædt";
        Product underFasciasFrontBack;
        Product underFasciasSides;
        Product overFasciasFront;
        Product overFasciasSides;
        double frontBackQuantity;
        double sidesQuantity;
        double fasciaBoardWidth = width;

        if (roofType.equals(highRoof)) {
            fasciaBoardWidth = width / Math.cos(Math.toRadians(roofSlope));
        }

        if (width >= 720) {
            frontBackQuantity = Math.ceil(fasciaBoardWidth / 540) * 2;
            underFasciasFrontBack = findProductByDescriptionAndNearestLength(productDescriptionUnderFascias, 540);
            overFasciasFront = findProductByDescriptionAndNearestLength(productDescriptionOverFascias, 540);
        } else if (width == 540 && !roofType.equals(highRoof)) {
            frontBackQuantity = 2;
            underFasciasFrontBack = findProductByDescriptionAndNearestLength(productDescriptionUnderFascias, 540);
            overFasciasFront = findProductByDescriptionAndNearestLength(productDescriptionOverFascias, 540);
        } else {
            frontBackQuantity = Math.ceil(fasciaBoardWidth / 360) * 2;
            underFasciasFrontBack = findProductByDescriptionAndNearestLength(productDescriptionUnderFascias, 360);
            overFasciasFront = findProductByDescriptionAndNearestLength(productDescriptionOverFascias, 360);
        }

        if (length >= 720) {
            sidesQuantity = Math.ceil(length / 540) * 2;
            underFasciasSides = findProductByDescriptionAndNearestLength(productDescriptionUnderFascias, 540);
            overFasciasSides = findProductByDescriptionAndNearestLength(productDescriptionOverFascias, 540);
        } else if (length == 540) {
            sidesQuantity = 2;
            underFasciasSides = findProductByDescriptionAndNearestLength(productDescriptionUnderFascias, 540);
            overFasciasSides = findProductByDescriptionAndNearestLength(productDescriptionOverFascias, 540);
        } else {
            sidesQuantity = Math.ceil(length / 360) * 2;
            underFasciasSides = findProductByDescriptionAndNearestLength(productDescriptionUnderFascias, 360);
            overFasciasSides = findProductByDescriptionAndNearestLength(productDescriptionOverFascias, 360);
        }
        productsPartsListEntries.add(new ProductsPartsListEntry(underFasciasFrontBack, frontBackQuantity, placementDescriptionUnderFasciasFrontBack));
        productsPartsListEntries.add(new ProductsPartsListEntry(underFasciasSides, sidesQuantity, placementDescriptionUnderFasciasSides));
        productsPartsListEntries.add(new ProductsPartsListEntry(overFasciasFront, frontBackQuantity, placementDescriptionOverFasciasFront));
        productsPartsListEntries.add(new ProductsPartsListEntry(overFasciasSides, sidesQuantity, placementDescriptionOverFasciasSides));
    }

    private void addShedStuds() throws CalculatorException {
        String description = "45x95 mm. Reglar ub.";
        String placementDescriptionEnds = "Løsholter til skur gavle";
        String placementDescriptionSides = "Løsholter til skur sider";
        Product product = findProductByDescriptionAndNearestLength(description, 240);
        Product productEnds = findProductByDescriptionAndNearestLength(description, 270);

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

    private void addTopPlates() throws CalculatorException {
        String description = "45x195 mm. spærtræ ubh.";
        String placementDescription = "Remme i sider, sadles ned i stolper";
        int quantity = 2;
        double topPlateLength = length;
        double maxTopPlateLength = 720;
        if (length > maxTopPlateLength) {
            quantity = 4;
            topPlateLength = length / 2;
        }
        Product product = findProductByDescriptionAndNearestLength(description, topPlateLength);
        this.productsPartsListEntries.add(new ProductsPartsListEntry(product, quantity, placementDescription));
    }

    private void addPoles() throws CalculatorException {
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

        Product product = findProductByDescription("97x97 mm. trykimp. Stolpe");
        this.productsPartsListEntries.add(new ProductsPartsListEntry(product, poleQuantity, placementDescription));
    }

    private void addRafters() throws CalculatorException {
        if (roofType.equals(flatRoof)) {
            calculateQuantityOfRafterLightRoof();
        } else if (roofType.equals(highRoof)) {
            calculateQuantityOfRafterHeavyRoof();
        } else {
            throw new CalculatorException("Ukendt tagtype: " + roofType);
        }
    }

    //======= Hardware & Screws ======= //

    private void addScrewsToRoof() throws CalculatorException {
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

        Product product = findProductByDescription(productDescription);
        productsPartsListEntries.add(new ProductsPartsListEntry(product, quantity, placementDescription));
    }

    private void addBracingStrap() throws CalculatorException {
        String placementDescription = "Til vindkryds på spær";
        String productDescription = "hulbånd 1x20 mm. 10 mtr.";
        Product strapRollProduct = findProductByDescription(productDescription);
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

    private void addUniversalConnectorAndScrews() throws CalculatorException {
        Product leftConnector = findProductByDescription("universal 190 mm venstre");
        Product rightConnector = findProductByDescription("universal 190 mm højre");
        Product screws = findProductByDescription("4,0 x 50 mm. beslagskruer 250 stk.");

        productsPartsListEntries.add(new ProductsPartsListEntry(leftConnector, rafterQuantity, "Til montering af spær på rem"));
        productsPartsListEntries.add(new ProductsPartsListEntry(rightConnector, rafterQuantity, "Til montering af spær på rem"));

        double totalConnectors = rafterQuantity * 2;
        double screwsQuantity = (totalConnectors * 14) + (bracingQuantity * rafterQuantity); //14 screws pr connector + adding screws for bracing
        double screwBox = Math.ceil(screwsQuantity / 250); // 250 screws pr box
        productsPartsListEntries.add(new ProductsPartsListEntry(screws, screwBox, "Til montering af universalbeslag + hulbånd"));
    }

    private void addFasciaAndFasciaCappingScrews() throws CalculatorException {
        Product product = findProductByDescription("4,5 x 60 mm. skruer 200 stk.");
        productsPartsListEntries.add(new ProductsPartsListEntry(product, 1, "Til montering af stern & vandbrædt"));
    }

    private void addBoltsToPoles() throws CalculatorException {
        Product product = findProductByDescription("bræddebolt 10 x 120 mm.");
        double ifShed = 0;
        if (shedId != null) {
            ifShed = 12+4; // 2x6 extra bolts + 4 ekstra bolts when shed starts
        }
        double quantity = Math.ceil(((poleQuantity * 2) + ifShed) / 12); // 12 per package
        productsPartsListEntries.add(new ProductsPartsListEntry(product, quantity, "Til montering af rem på stolper"));
    }

    private void addBoltPlates() throws CalculatorException {
        Product product = findProductByDescription("firkantskiver 40x40x11mm");
        productsPartsListEntries.add(new ProductsPartsListEntry(product, poleQuantity*2, "Til montering af rem på stolper"));
    }

    private void addScrewsToSiding() throws CalculatorException {
        double totalShortScrews = 0;
        double totalLongScrews = 0;

        switch (siding) {
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
            Product outerProduct = findProductByDescription("4,5 x 70 mm. Skruer 400 stk.");
            productsPartsListEntries.add(new ProductsPartsListEntry(outerProduct, box, "Til montering af beklædning"));
        }
        if (totalShortScrews > 0) {
            double box = Math.ceil(totalShortScrews / 300.0);
            Product innerProduct = findProductByDescription("4,5 x 50 mm. Skruer 300 stk.");
            productsPartsListEntries.add(new ProductsPartsListEntry(innerProduct, box, "Til montering af beklædning"));
        }
    }

    private void addHardwareToShedDoor() throws CalculatorException {
        Product hinge = findProductByDescription("t hængsel 390 mm");
        Product latch = findProductByDescription("stalddørsgreb 50x75");
        productsPartsListEntries.add(new ProductsPartsListEntry(hinge, 2, "Til skur dør"));
        productsPartsListEntries.add(new ProductsPartsListEntry(latch, 1, "Til lås på dør i skur"));
    }

    private void addAngleBracketsToShedWithScrews() throws CalculatorException {
        double studsEnds = getStudQuantity(shedWidth) + 1;
        double studsSides = getStudQuantity(shedLength) + 1;

        double totalColumns = (studsEnds * 2) + (studsSides * 2); // 2 end walls, 2 side walls

        int totalRows = 3; // Always 3
        double totalHorizontalBeams = totalColumns * totalRows;

        double totalBracketsNeeded = (totalHorizontalBeams * 2) + 4; // 2 brackets pr beam, 4 to reinforce around door.

        Product angleBracket = findProductByDescription("vinkelbeslag 35");
        productsPartsListEntries.add(new ProductsPartsListEntry(angleBracket, totalBracketsNeeded, "Til montering af løsholter i skur"));

        double totalBracketScrews = totalBracketsNeeded * 6; // 6 screws pr bracket
        double screwBoxes = Math.ceil(totalBracketScrews / 250.0); // 250 pr package

        Product screws = findProductByDescription("4,0 x 50 mm. beslagskruer 250 stk.");
        productsPartsListEntries.add(new ProductsPartsListEntry(screws, screwBoxes, "Til montering af vinkelbeslag 35 i skur"));
    }

    //======= Service Methods ======= //

    private void calculateQuantityOfRafterLightRoof() throws CalculatorException {
        String placementDescription = "Spær, monteres på rem";

        if (width <= 372) {
            rafterDistance = 100;
            rafterDescription = "45x195 mm. spærtræ ubh.";
        } else if (width <= 397) {
            rafterDistance = 80;
            rafterDescription = "45x195 mm. spærtræ ubh.";
        } else if (width <= 434) {
            rafterDistance = 60;
            rafterDescription = "45x195 mm. spærtræ ubh.";
        } else if (width <= 488) {
            rafterDistance = 60;
            rafterDescription = "45x220 mm. spærtræ ubh.";
        } else if (width <= 542) {
            rafterDistance = 60;
            rafterDescription = "45x245 mm. spærtræ ubh.";
        } else if (width <= 607) {
            rafterDistance = 40;
            rafterDescription = "45x245 mm. spærtræ ubh.";
        } else if (width <= 648) {
            rafterDistance = 60;
            rafterDescription = "45x295 mm. spærtræ ubh.";
        } else if (width <= 723) {
            rafterDistance = 40;
            rafterDescription = "45x295 mm. spærtræ ubh.";
        } else {
            throw new CalculatorException("Carporten er for bred til let tag: " + width + " cm");
        }

        Product product = findProductByDescriptionAndNearestLength(rafterDescription, width);

        rafterQuantity = (int) (Math.ceil(length / rafterDistance) + 1);
        productsPartsListEntries.add(new ProductsPartsListEntry(product, rafterQuantity, placementDescription));
    }

    private void calculateQuantityOfRafterHeavyRoof() throws CalculatorException {
        String placementDescription = "Spær, monteres på rem";
        String placementDescriptionRoof = "Spær, til taghældning";

        if (width <= 319) {
            rafterDistance = 100;
            rafterDescription = "45x195 mm. spærtræ ubh.";
        } else if (width <= 343) {
            rafterDistance = 80;
            rafterDescription = "45x195 mm. spærtræ ubh.";
        } else if (width <= 376) {
            rafterDistance = 60;
            rafterDescription = "45x195 mm. spærtræ ubh.";
        } else if (width <= 388) {
            rafterDistance = 80;
            rafterDescription = "45x220 mm. spærtræ ubh.";
        } else if (width <= 431) {
            rafterDistance = 80;
            rafterDescription = "45x245 mm. spærtræ ubh.";
        } else if (width <= 472) {
            rafterDistance = 60;
            rafterDescription = "45x245 mm. spærtræ ubh.";
        } else if (width <= 518) {
            rafterDistance = 80;
            rafterDescription = "45x295 mm. spærtræ ubh.";
        } else if (width <= 566) {
            rafterDistance = 60;
            rafterDescription = "45x295 mm. spærtræ ubh.";
        } else if (width <= 637) {
            rafterDistance = 40;
            rafterDescription = "45x295 mm. spærtræ ubh.";
        } else {
            throw new CalculatorException("Carporten er for bred til tungt tag: " + width + " cm");
        }
        double roofRafterWidth = (width / 2) / Math.cos(Math.toRadians(roofSlope));

        Product productRafter = findProductByDescriptionAndNearestLength(rafterDescription, width);
        Product productRoofRafter = findProductByDescriptionAndNearestLength(rafterDescription, roofRafterWidth);

        rafterQuantity = (int) (Math.ceil(length / rafterDistance) + 1);
        productsPartsListEntries.add(new ProductsPartsListEntry(productRafter, rafterQuantity, placementDescription));
        productsPartsListEntries.add(new ProductsPartsListEntry(productRoofRafter, rafterQuantity * 2, placementDescriptionRoof));
    }

    private double getStudQuantity(double lengthOrWidth) {
        double studSpace = 70;
        return (Math.ceil(lengthOrWidth / studSpace) - 1);
    }

    private Product findProductByDescription(String description) throws CalculatorException {
        return products.stream()
                .filter(p -> p.getDescription().equals(description))
                .findFirst()
                .orElseThrow(() -> new CalculatorException("Produkt ikke fundet: " + description));
    }

    private Product findProductByDescriptionAndNearestLength(String description, double minLength) throws CalculatorException {
        return products.stream()
                .filter(p -> p.getDescription().equals(description))
                .filter(p -> p.getLength() >= minLength)
                .min(Comparator.comparing(Product::getLength))
                .orElseThrow(() -> new CalculatorException("Produkt ikke fundet: " + description + " med minimum længde " + minLength + " cm"));
    }
}
