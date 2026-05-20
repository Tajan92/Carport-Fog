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
    private String roofType;
    private double roofSlope;
    private String roofMaterial;
    private double rafterDistance;
    private double rafterQuantity;
    private String rafterDescription;
    private double poleQuantity;
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
        double plateCoverageWidth = 1;

        if (roofType.equals(flatRoof)) {
            if (length >= 1200 || length == 600) {
                plateCoverageLength = 600;
                roofProduct = findProductByDescriptionAndNearestLength(descriptionRoofMaterial, 600);
            } else {
                plateCoverageLength = 360;
                roofProduct = findProductByDescriptionAndNearestLength(descriptionRoofMaterial, 360);
            }
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
        }

        if (plateCoverageLength > 0 && plateCoverageWidth > 0 && roofProduct != null) {
            double lengthPlates = Math.ceil(length / plateCoverageLength);
            double widthPlates = Math.ceil(width / plateCoverageWidth);
            quantity = lengthPlates * widthPlates;
            productsPartsListEntries.add(new ProductsPartsListEntry(roofProduct, quantity, placementDescriptionRoofMaterial));
        }
    }

    private void addShedSiding() throws CalculatorException {
        String placementDescriptionShedSiding = "til beklædning af skur";
        double sidingCover = 0;
        double quantity = 0;
        double add10percent = 1.1;
        double shedHeight = 210;
        Product shedSiding;

        if (siding.equals("Beklædning: 19x100 mm. trykimp. Brædt")) {
            sidingCover = 8.5;
            quantity = Math.ceil(((shedWidth * 2) + (shedLength * 2)) / sidingCover) * 2;

        } else if (siding.equals("Beklædning: 19x125mm Klinkbeklædning trykimp.")) {
            sidingCover = 10.0;
            quantity = Math.ceil(((shedWidth * 2) + (shedLength * 2)) / sidingCover);

        } else if (siding.equals("Beklædning: 25x150mm Blokhusbrædder ubehandlet")) {
            sidingCover = 14.3;
            quantity = Math.ceil(((shedWidth * 2) + (shedLength * 2)) / sidingCover);

        } else if (siding.equals("Beklædning: 19x100mm Profilbrædt (1 på 2 beklædning)")) {
            sidingCover = 8.5;
            quantity = Math.ceil(((shedWidth * 2) + (shedLength * 2)) / sidingCover) * 2;


        } else if (siding.equals("Beklædning: 9x1220x2440mm Krydsfiner m/spor (Svalehale)")) {
            double plateWidth = 122.0;
            double plateHeight = 244.0;
            double perimeterCm = (shedWidth * 2 + shedLength * 2);
            double platesHorizontal = Math.ceil(perimeterCm / plateWidth);
            double platesVertical = Math.ceil(shedHeight / plateHeight);
            quantity = platesHorizontal * platesVertical;
        } else {
            throw new CalculatorException("Kan ikke tilføje " + siding + " som beklædning");
        }

        shedSiding = findProductByDescriptionAndNearestLength(siding, shedHeight);
        productsPartsListEntries.add(new ProductsPartsListEntry(shedSiding, quantity * add10percent, placementDescriptionShedSiding));
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
        double quantityEnds = getStudQuantity(shedWidth);
        double quantitySides = getStudQuantity(shedLength);

        if (roofType.equals(flatRoof)) {
            productsPartsListEntries.add(new ProductsPartsListEntry(product, quantityEnds, placementDescriptionEnds));
            productsPartsListEntries.add(new ProductsPartsListEntry(product, quantitySides, placementDescriptionSides));
        }
        if (roofType.equals(highRoof)) {
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

    private void calculateQuantityOfRafterLightRoof() throws CalculatorException {
        String placementDescription = "Spær, monteres på rem";

        if (length <= 372) {
            rafterDistance = 100;
            rafterDescription = "45x195 mm. spærtræ ubh.";
        } else if (length <= 397) {
            rafterDistance = 80;
            rafterDescription = "45x195 mm. spærtræ ubh.";
        } else if (length <= 434) {
            rafterDistance = 60;
            rafterDescription = "45x195 mm. spærtræ ubh.";
        } else if (length <= 488) {
            rafterDistance = 60;
            rafterDescription = "45x220 mm. spærtræ ubh.";
        } else if (length <= 542) {
            rafterDistance = 60;
            rafterDescription = "45x245 mm. spærtræ ubh.";
        } else if (length <= 607) {
            rafterDistance = 40;
            rafterDescription = "45x245 mm. spærtræ ubh.";
        } else if (length <= 648) {
            rafterDistance = 60;
            rafterDescription = "45x295 mm. spærtræ ubh.";
        } else if (length <= 723) {
            rafterDistance = 40;
            rafterDescription = "45x295 mm. spærtræ ubh.";
        } else {
            throw new CalculatorException("Carporten er for lang til let tag: " + length + " cm");
        }

        Product product = findProductByDescriptionAndNearestLength(rafterDescription, width);

        rafterQuantity = Math.ceil(length / rafterDistance) + 1;
        productsPartsListEntries.add(new ProductsPartsListEntry(product, rafterQuantity, placementDescription));
    }

    private void calculateQuantityOfRafterHeavyRoof() throws CalculatorException {
        String placementDescription = "Spær, monteres på rem";
        String placementDescriptionRoof = "Spær, til taghældning";

        if (length <= 319) {
            rafterDistance = 100;
            rafterDescription = "45x195 mm. spærtræ ubh.";
        } else if (length <= 343) {
            rafterDistance = 80;
            rafterDescription = "45x195 mm. spærtræ ubh.";
        } else if (length <= 376) {
            rafterDistance = 60;
            rafterDescription = "45x195 mm. spærtræ ubh.";
        } else if (length <= 388) {
            rafterDistance = 80;
            rafterDescription = "45x220 mm. spærtræ ubh.";
        } else if (length <= 431) {
            rafterDistance = 80;
            rafterDescription = "45x245 mm. spærtræ ubh.";
        } else if (length <= 472) {
            rafterDistance = 60;
            rafterDescription = "45x245 mm. spærtræ ubh.";
        } else if (length <= 518) {
            rafterDistance = 80;
            rafterDescription = "45x295 mm. spærtræ ubh.";
        } else if (length <= 566) {
            rafterDistance = 60;
            rafterDescription = "45x295 mm. spærtræ ubh.";
        } else if (length <= 637) {
            rafterDistance = 40;
            rafterDescription = "45x295 mm. spærtræ ubh.";
        } else {
            throw new CalculatorException("Carporten er for lang til tungt tag: " + length + " cm");
        }
        double roofRafterWidth = (width / 2) / Math.cos(Math.toRadians(roofSlope));

        Product productRafter = findProductByDescriptionAndNearestLength(rafterDescription, width);
        Product productRoofRafter = findProductByDescriptionAndNearestLength(rafterDescription, roofRafterWidth);

        rafterQuantity = Math.ceil(length / rafterDistance) + 1;
        productsPartsListEntries.add(new ProductsPartsListEntry(productRafter, rafterQuantity, placementDescription));
        productsPartsListEntries.add(new ProductsPartsListEntry(productRoofRafter, rafterQuantity * 2, placementDescriptionRoof));
    }

    private double getStudQuantity(double lengthOrWidth) {
        double studSpace = 70;
        return (Math.ceil(lengthOrWidth / studSpace) - 1) * 2;
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

    //======= Hardware & Screws ======= //

    private void addScrewsToRoof() {

    }
}