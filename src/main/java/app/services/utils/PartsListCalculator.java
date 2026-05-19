package app.services.utils;

import app.entities.*;
import app.exceptions.DatabaseException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PartsListCalculator {
    private List<ProductsPartsListEntry> productsPartsListEntries;
    private List<Product> products;
    private double width;
    private double height;
    private double length;
    private double price;
    private int partsListId;
    private Integer shedId;
    private int roofId;

    private double shedLength;
    private double shedWidth;

    private String roofType;
    private double roofSlope;
    private String roofMaterial;
    private double rafterDistance;
    private double rafterQuantity;
    private String rafterDescription;

    // TODO: Make a exception for partsList
    public List<ProductsPartsListEntry> createProductsPartsList(Carport carport, Shed shed, Roof roof, List<Product> allProducts) throws DatabaseException {
        this.width = carport.getWidth();
        this.height = carport.getHeight();
        this.length = carport.getLength();
        this.price = carport.getPrice();
        this.partsListId = carport.getPartsListId();
        this.shedId = carport.getShedId();
        this.roofId = carport.getRoofId();
        this.shedLength = shed.getLength();
        this.shedWidth = shed.getWidth();
        this.roofType = roof.getRoofType();
        this.roofSlope = roof.getRoofSlope();
        this.roofMaterial = roof.getRoofMaterial();
        this.products = allProducts;
        this.productsPartsListEntries = new ArrayList<>();

        addPoles();
        addRafters();
        addTopPlates();
        addShedStuds();
        addFasciaBoards();
        addFasciaCapping();
        addShedSiding();
        addRoofMaterial();
        if (shedId != null) {
            addStabilizerToShedDoor();
        }
        return this.productsPartsListEntries;
    }

    private void addStabilizerToShedDoor() {
        Product product = findProductByDescription("38x73 mm. Lægte ubh. (Lægte til dør)");
        productsPartsListEntries.add(new ProductsPartsListEntry(product, 1, "Til Z på bagside af dør"));
    }

    private void addRoofMaterial() {
        String placementDescriptionRoofMaterial = "Tag monteres på spær";
        String descriptionRoofMaterial = roofMaterial;
        Product roofProduct = new Product(0, 0, 0, 0, "", "", "");
        double quantity;
        double plateCoverageLength = 0;
        double plateCoverageWidth = 1;

        if (roofType.equals("light")) {
            if (length < 599 || length > 600 || length < 1200) {
                plateCoverageLength = 360;
                roofProduct = findProductByDescriptionAndNearestLength(descriptionRoofMaterial, 360);
            }
            if (length >= 1200 || length == 600) {
                plateCoverageLength = 600;
                roofProduct = findProductByDescriptionAndNearestLength(descriptionRoofMaterial, 600);
            }

        } else if (roofType.equals("heavy")) {
            if (roofMaterial.equals("Eternittag B6 - sortblå") || roofMaterial.equals("Eternittag B6 - grå")) {
                plateCoverageLength = 1.03;
                plateCoverageWidth = 1.01;
                roofProduct = findProductByDescription(descriptionRoofMaterial);
            }
            if (roofMaterial.equals("Eternittag B7 - sortblå")) {
                plateCoverageLength = 0.46;
                plateCoverageWidth = 1.02;
                roofProduct = findProductByDescription(descriptionRoofMaterial);
            }
            if (roofMaterial.equals("Betontagsten - sort") || roofMaterial.equals("Betontagsten - koralrød")) {
                plateCoverageLength = 0.3;
                plateCoverageWidth = 0.37;
                roofProduct = findProductByDescription(descriptionRoofMaterial);
            }
        }
        double lengthPlates = Math.ceil(length * plateCoverageLength);
        double widthPlates = Math.ceil(width / plateCoverageWidth);
        quantity = lengthPlates + widthPlates;
        productsPartsListEntries.add(new ProductsPartsListEntry(roofProduct, quantity, placementDescriptionRoofMaterial));
    }

    private void addShedSiding() {
        String placementDescriptionShedSiding = "til beklædning af skur 1 på 2";
        String productDescription = "19x100 mm. trykimp. Brædt";
        Product shedSiding = findProductByDescriptionAndNearestLength("19x100 mm. trykimp. Brædt", 210);
        double sidingCover = 11.5; //is coverage on 19x100mm siding with 1 on 2
        double add10percent = 1.1; //for material waste, when cutting shed door.
        double quantity = (((shedWidth * 2) + (shedLength * 2)) / sidingCover) * 2;

        productsPartsListEntries.add(new ProductsPartsListEntry(shedSiding, quantity * add10percent, placementDescriptionShedSiding));
    }

    private void addFasciaCapping() {
        String placementDescriptionFasciaCappingSides = "Vandbrædt på stern i sider";
        String placementDescriptionFasciaCappingFront = "Vandbrædt på stern i forende";
        String productDescription = "19x100 mm. trykimp. Brædt";
        Product fasciasCappingFront;
        Product fasciaCappingSides;
        double frontBackQuantity;
        double sidesQuantity;

        if (roofType.equals("heavy")) {
            width = width / Math.cos(Math.toRadians(roofSlope));
        }
        if (width >= 540) {
            frontBackQuantity = Math.ceil(width / 540);
            fasciasCappingFront = findProductByDescriptionAndNearestLength(productDescription, 540);
        } else if (width >= 360) {
            frontBackQuantity = Math.ceil(width / 360);
            fasciasCappingFront = findProductByDescriptionAndNearestLength(productDescription, 360);
        } else {
            frontBackQuantity = Math.ceil(width / 210);
            fasciasCappingFront = findProductByDescriptionAndNearestLength(productDescription, 210);
        }

        if (length >= 540) {
            sidesQuantity = Math.ceil(width / 540) * 2;
            fasciaCappingSides = findProductByDescriptionAndNearestLength(productDescription, 540);
        } else if (length == 360) {
            sidesQuantity = Math.ceil(width / 360) * 2;
            fasciaCappingSides = findProductByDescriptionAndNearestLength(productDescription, 360);
        } else {
            sidesQuantity = Math.ceil(length / 210) * 2;
            fasciaCappingSides = findProductByDescriptionAndNearestLength(productDescription, 210);
        }
        productsPartsListEntries.add(new ProductsPartsListEntry(fasciasCappingFront, frontBackQuantity, placementDescriptionFasciaCappingFront));
        productsPartsListEntries.add(new ProductsPartsListEntry(fasciaCappingSides, sidesQuantity, placementDescriptionFasciaCappingSides));
    }

    private void addFasciaBoards() {
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
        if (roofType.equals("heavy")) {
            width = width / Math.cos(Math.toRadians(roofSlope));
        }
        if (width >= 720) {
            frontBackQuantity = Math.ceil(width / 540) * 2;
            underFasciasFrontBack = findProductByDescriptionAndNearestLength(productDescriptionUnderFascias, 540);
            overFasciasFront = findProductByDescriptionAndNearestLength(productDescriptionOverFascias, 540);
        } else if (width == 540 && !roofType.equals("heavy")) {
            frontBackQuantity = 2;
            underFasciasFrontBack = findProductByDescriptionAndNearestLength(productDescriptionUnderFascias, 540);
            overFasciasFront = findProductByDescriptionAndNearestLength(productDescriptionOverFascias, 540);
        } else {
            frontBackQuantity = Math.ceil(width / 360) * 2;
            underFasciasFrontBack = findProductByDescriptionAndNearestLength(productDescriptionUnderFascias, 360);
            overFasciasFront = findProductByDescriptionAndNearestLength(productDescriptionOverFascias, 360);
        }

        if (length >= 720) {
            sidesQuantity = Math.ceil(width / 540) * 2;
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

    private void addShedStuds() {
        String description = "45x95 mm. Reglar ub.";
        String placementDescriptionEnds = "Løsholter til skur gavle";
        String placementDescriptionSides = "Løsholter til skur sider";
        Product product = findProductByDescriptionAndNearestLength(description, 240);
        Product productEnds = findProductByDescriptionAndNearestLength(description, 270);
        double quantityEnds = getStudQuantity(shedWidth);
        double quantitySides = getStudQuantity(shedLength);

        if (roofType.equals("light")) {
            productsPartsListEntries.add(new ProductsPartsListEntry(product, quantityEnds, placementDescriptionEnds));
            productsPartsListEntries.add(new ProductsPartsListEntry(product, quantitySides, placementDescriptionSides));
        }
        if (roofType.equals("heavy")) {
            productsPartsListEntries.add(new ProductsPartsListEntry(productEnds, quantityEnds, placementDescriptionEnds));
            productsPartsListEntries.add(new ProductsPartsListEntry(product, quantitySides, placementDescriptionSides));
        }
    }

    private void addTopPlates() {
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

    private void addPoles() {
        double poleQuantity = 0;
        double marginFront = 100;
        double marginBack = 35;
        double maxPoleDistance = 300;
        String placementDescription = "Stolper nedgraves 90 cm. i jord";

        if (shedId == null) {
            double coverageLength = length - marginFront - marginBack;
            int spaces = (int) Math.ceil(coverageLength / maxPoleDistance);
            poleQuantity = (spaces + 1) * 2;
        } else {
            double coverageLength = length - marginFront - marginBack - shedLength;
            int spaces = (int) Math.ceil(coverageLength / maxPoleDistance);
            poleQuantity = (spaces + 1) * 2;
        }
        Product product = findProductByDescription("97x97 mm. trykimp. Stolpe");

        this.productsPartsListEntries.add(new ProductsPartsListEntry(product, poleQuantity, placementDescription));
    }

    private void addRafters() throws DatabaseException {
        if (roofType.equals("light")) {
            calculateQuantityOfRafterLightRoof();
        } else if (roofType.equals("heavy")) {
            calculateQuantityOfRafterHeavyRoof();
        }
    }

    private void calculateQuantityOfRafterLightRoof() throws DatabaseException {
        String placementDescription = "Spær, monteres på rem";

        if (length <= 372) {
            rafterDistance = 1.0;
            rafterDescription = "45x195 mm. spærtræ ubh.";
        } else if (length <= 397) {
            rafterDistance = 0.8;
            rafterDescription = "45x195 mm. spærtræ ubh.";
        } else if (length <= 434) {
            rafterDistance = 0.6;
            rafterDescription = "45x195 mm. spærtræ ubh.";
        } else if (length <= 488) {
            rafterDistance = 0.6;
            rafterDescription = "45x220 mm. spærtræ ubh.";
        } else if (length <= 542) {
            rafterDistance = 0.6;
            rafterDescription = "45x245 mm. spærtræ ubh.";
        } else if (length <= 607) {
            rafterDistance = 0.4;
            rafterDescription = "45x245 mm. spærtræ ubh.";
        } else if (length <= 648) {
            rafterDistance = 0.6;
            rafterDescription = "45x295 mm. spærtræ ubh.";
        } else if (length <= 723) {
            rafterDistance = 0.4;
            rafterDescription = "45x295 mm. spærtræ ubh.";
        }

        Product product = findProductByDescriptionAndNearestLength(rafterDescription, width);

        rafterQuantity = Math.ceil(length / rafterDistance);
        productsPartsListEntries.add(new ProductsPartsListEntry(product, rafterQuantity, placementDescription));
    }

    private void calculateQuantityOfRafterHeavyRoof() throws DatabaseException {
        String placementDescription = "Spær, monteres på rem";

        if (length <= 319) {
            rafterDistance = 1.0;
            rafterDescription = "45x195 mm. spærtræ ubh.";
        } else if (length <= 343) {
            rafterDistance = 0.8;
            rafterDescription = "45x195 mm. spærtræ ubh.";
        } else if (length <= 376) {
            rafterDistance = 0.6;
            rafterDescription = "45x195 mm. spærtræ ubh.";
        } else if (length <= 388) {
            rafterDistance = 0.9;
            rafterDescription = "45x220 mm. spærtræ ubh.";
        } else if (length <= 431) {
            rafterDistance = 0.8;
            rafterDescription = "45x245 mm. spærtræ ubh.";
        } else if (length <= 472) {
            rafterDistance = 0.6;
            rafterDescription = "45x245 mm. spærtræ ubh.";
        } else if (length <= 518) {
            rafterDistance = 0.8;
            rafterDescription = "45x295 mm. spærtræ ubh.";
        } else if (length <= 566) {
            rafterDistance = 0.6;
            rafterDescription = "45x295 mm. spærtræ ubh.";
        } else if (length <= 637) {
            rafterDistance = 0.4;
            rafterDescription = "45x295 mm. spærtræ ubh.";
        }

        Product product = findProductByDescriptionAndNearestLength(rafterDescription, width);

        rafterQuantity = Math.ceil(length / rafterDistance);
        productsPartsListEntries.add(new ProductsPartsListEntry(product, rafterQuantity, placementDescription));
    }

    private double getStudQuantity(double lengthOrWidth) {
        double studSpace = 70;
        return (Math.ceil(lengthOrWidth / studSpace) - 1) * 2;
    }

    private Product findProductByDescription(String description) {
        return products.stream()
                .filter(p -> p.getDescription().equals(description))
                .findFirst()
                .orElse(null);
    }

    private Product findProductByDescriptionAndNearestLength(String description, double minLength) {
        return products.stream()
                .filter(p -> p.getDescription().equals(description))
                .filter(p -> p.getLength() >= minLength)
                .min(Comparator.comparing(Product::getLength))
                .orElse(null);
    }
}
