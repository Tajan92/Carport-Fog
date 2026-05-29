package app.services.utils.partsListCalculator;

import app.entities.*;
import app.exceptions.CalculatorException;
import java.util.List;


public class RoofCalculator {
    private double rafterDistance;
    private int rafterQuantity;
    private String rafterDescription;
    private final String flatRoof = "Fladt tag";
    private final String highRoof = "Højt tag";
    private ProductFinder productFinder = new ProductFinder();

    public double addRoofProducts(List<ProductsPartsListEntry> productsPartsListEntries, Roof roof, Carport carport, List<Product> products) throws CalculatorException {
        addRoofMaterial(productsPartsListEntries, roof.getRoofType(), roof.getRoofMaterial(), roof.getRoofSlope(), carport.getWidth(), carport.getLength(), products);
        addFasciaCapping(productsPartsListEntries, roof.getRoofType(), roof.getRoofSlope(), carport.getWidth(), carport.getLength(), products);
        addFasciaBoards(productsPartsListEntries,roof.getRoofType(), roof.getRoofSlope(), carport.getWidth(), carport.getLength(), products);
        addTopPlates(productsPartsListEntries, carport.getLength(),  products);
        addRafters(productsPartsListEntries, roof.getRoofType(), roof.getRoofSlope(), carport.getWidth(), carport.getLength(), products);
    return rafterQuantity;
    }

    private void addRoofMaterial(List<ProductsPartsListEntry> productsPartsListEntries, String roofType, String roofMaterial, double roofSlope, double width, double length, List<Product> products) throws CalculatorException {
        String placementDescriptionRoofMaterial = "Tag monteres på spær";
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
            roofProduct = productFinder.findProductByDescriptionAndNearestLength(products, roofMaterial, plateCoverageLength);

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
            roofProduct = productFinder.findProductByDescription(products, roofMaterial);

            double actualRoofWidthOneSide = (width / 2.0) / Math.cos(Math.toRadians(roofSlope));
            double lengthPlates = Math.ceil(length / plateCoverageLength);
            double widthPlates = Math.ceil(actualRoofWidthOneSide / plateCoverageWidth);

            quantity = (lengthPlates * widthPlates) * 2;

            productsPartsListEntries.add(new ProductsPartsListEntry(roofProduct, quantity, placementDescriptionRoofMaterial));
            return;
        }
        throw new CalculatorException("Kan ikke finde materialet til dette tag.");
    }

    private void addFasciaCapping(List<ProductsPartsListEntry> productsPartsListEntries, String roofType, double roofSlope, double width, double length, List<Product> products) throws CalculatorException {
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
            fasciasCappingFront = productFinder.findProductByDescriptionAndNearestLength(products, productDescription, 540);
        } else if (width >= 360) {
            frontBackQuantity = Math.ceil(fasciaCappingWidth / 360);
            fasciasCappingFront = productFinder.findProductByDescriptionAndNearestLength(products, productDescription, 360);
        } else {
            frontBackQuantity = Math.ceil(fasciaCappingWidth / 210);
            fasciasCappingFront = productFinder.findProductByDescriptionAndNearestLength(products, productDescription, 210);
        }

        if (length >= 540) {
            sidesQuantity = Math.ceil(length / 540) * 2;
            fasciaCappingSides = productFinder.findProductByDescriptionAndNearestLength(products, productDescription, 540);
        } else if (length == 360) {
            sidesQuantity = Math.ceil(length / 360) * 2;
            fasciaCappingSides = productFinder.findProductByDescriptionAndNearestLength(products, productDescription, 360);
        } else {
            sidesQuantity = Math.ceil(length / 210) * 2;
            fasciaCappingSides = productFinder.findProductByDescriptionAndNearestLength(products, productDescription, 210);
        }
        productsPartsListEntries.add(new ProductsPartsListEntry(fasciasCappingFront, frontBackQuantity, placementDescriptionFasciaCappingFront));
        productsPartsListEntries.add(new ProductsPartsListEntry(fasciaCappingSides, sidesQuantity, placementDescriptionFasciaCappingSides));
    }

    private void addFasciaBoards(List<ProductsPartsListEntry> productsPartsListEntries, String roofType, double roofSlope, double width, double length, List<Product> products) throws CalculatorException {
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
            underFasciasFrontBack = productFinder.findProductByDescriptionAndNearestLength(products, productDescriptionUnderFascias, 540);
            overFasciasFront = productFinder.findProductByDescriptionAndNearestLength(products, productDescriptionOverFascias, 540);
        } else if (width == 540 && !roofType.equals(highRoof)) {
            frontBackQuantity = 2;
            underFasciasFrontBack = productFinder.findProductByDescriptionAndNearestLength(products, productDescriptionUnderFascias, 540);
            overFasciasFront = productFinder.findProductByDescriptionAndNearestLength(products, productDescriptionOverFascias, 540);
        } else {
            frontBackQuantity = Math.ceil(fasciaBoardWidth / 360) * 2;
            underFasciasFrontBack = productFinder.findProductByDescriptionAndNearestLength(products, productDescriptionUnderFascias, 360);
            overFasciasFront = productFinder.findProductByDescriptionAndNearestLength(products, productDescriptionOverFascias, 360);
        }

        if (length >= 720) {
            sidesQuantity = Math.ceil(length / 540) * 2;
            underFasciasSides = productFinder.findProductByDescriptionAndNearestLength(products, productDescriptionUnderFascias, 540);
            overFasciasSides = productFinder.findProductByDescriptionAndNearestLength(products, productDescriptionOverFascias, 540);
        } else if (length == 540) {
            sidesQuantity = 2;
            underFasciasSides = productFinder.findProductByDescriptionAndNearestLength(products, productDescriptionUnderFascias, 540);
            overFasciasSides = productFinder.findProductByDescriptionAndNearestLength(products, productDescriptionOverFascias, 540);
        } else {
            sidesQuantity = Math.ceil(length / 360) * 2;
            underFasciasSides = productFinder.findProductByDescriptionAndNearestLength(products, productDescriptionUnderFascias, 360);
            overFasciasSides = productFinder.findProductByDescriptionAndNearestLength(products, productDescriptionOverFascias, 360);
        }
        productsPartsListEntries.add(new ProductsPartsListEntry(underFasciasFrontBack, frontBackQuantity, placementDescriptionUnderFasciasFrontBack));
        productsPartsListEntries.add(new ProductsPartsListEntry(underFasciasSides, sidesQuantity, placementDescriptionUnderFasciasSides));
        productsPartsListEntries.add(new ProductsPartsListEntry(overFasciasFront, frontBackQuantity, placementDescriptionOverFasciasFront));
        productsPartsListEntries.add(new ProductsPartsListEntry(overFasciasSides, sidesQuantity, placementDescriptionOverFasciasSides));
    }

    private void addTopPlates(List<ProductsPartsListEntry> productsPartsListEntries, double length, List<Product> products) throws CalculatorException {
        String description = "45x195 mm. spærtræ ubh.";
        String placementDescription = "Remme i sider, sadles ned i stolper";
        int quantity = 2;
        double topPlateLength = length;
        double maxTopPlateLength = 720;
        if (length > maxTopPlateLength) {
            quantity = 4;
            topPlateLength = length / 2;
        }
        Product product = productFinder.findProductByDescriptionAndNearestLength(products, description, topPlateLength);
        productsPartsListEntries.add(new ProductsPartsListEntry(product, quantity, placementDescription));
    }

    private void addRafters(List<ProductsPartsListEntry> productsPartsListEntries, String roofType, double roofSlope, double width, double length, List<Product> products) throws CalculatorException {
        if (roofType.equals(flatRoof)) {
            calculateQuantityOfRafterLightRoof(productsPartsListEntries, width, length, products);
        } else if (roofType.equals(highRoof)) {
            calculateQuantityOfRafterHeavyRoof(productsPartsListEntries, roofSlope, width, length, products);
        } else {
            throw new CalculatorException("Ukendt tagtype: " + roofType);
        }
    }

    //======= Service Methods ======= //

    private void calculateQuantityOfRafterLightRoof(List<ProductsPartsListEntry> productsPartsListEntries, double width, double length, List<Product> products) throws CalculatorException {
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

        Product product = productFinder.findProductByDescriptionAndNearestLength(products, rafterDescription, width);

        rafterQuantity = (int) (Math.ceil(length / rafterDistance) + 1);
        productsPartsListEntries.add(new ProductsPartsListEntry(product, rafterQuantity, placementDescription));
    }

    private void calculateQuantityOfRafterHeavyRoof(List<ProductsPartsListEntry> productsPartsListEntries, double roofSlope, double width, double length, List<Product> products) throws CalculatorException {
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

        Product productRafter = productFinder.findProductByDescriptionAndNearestLength(products, rafterDescription, width);
        Product productRoofRafter = productFinder.findProductByDescriptionAndNearestLength(products, rafterDescription, roofRafterWidth);

        rafterQuantity = (int) (Math.ceil(length / rafterDistance) + 1);
        productsPartsListEntries.add(new ProductsPartsListEntry(productRafter, rafterQuantity, placementDescription));
        productsPartsListEntries.add(new ProductsPartsListEntry(productRoofRafter, rafterQuantity * 2, placementDescriptionRoof));
    }
}
