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

    private double rafterDistance;
    private double rafterQuantity;
    private String rafterDescription;

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
        this.products = allProducts;
        this.productsPartsListEntries = new ArrayList<>();

        calculateQuantityOfPoles();
        getRafters();

        return this.productsPartsListEntries;
    }

    private void calculateQuantityOfPoles() throws DatabaseException {
        double poleQuantity = 0;
        double marginFront = 100;
        double marginBack = 35;
        double maxPoleDistance = 300;

        if (shedId == null) {
            double coverageLength = length - marginFront - marginBack;
            int spaces = (int) Math.ceil(coverageLength / maxPoleDistance);
            poleQuantity = (spaces + 1) * 2;

        } else {
            double coverageLength = length - marginFront - marginBack - shedLength;
            int spaces = (int) Math.ceil(coverageLength / maxPoleDistance);
            poleQuantity = (spaces + 1) * 2;
        }
        Product product = products.stream()
                .filter(p -> p.getDescription().equals("97x97 mm. trykimp. Stolpe"))
                .findFirst()
                .orElse(null);

        this.productsPartsListEntries.add(new ProductsPartsListEntry(product, poleQuantity));
    }

    private void getRafters() throws DatabaseException {
        if (roofType.equals("light")) {
            calculateQuantityOfRafterLightRoof();
        } else if (roofType.equals("heavy")) {
            calculateQuantityOfRafterHeavyRoof();
        }
    }

    private void calculateQuantityOfRafterLightRoof() throws DatabaseException {
        String rafterDescription = "";

        if (length <= 3.72) {
            rafterDistance = 1.0;
            rafterDescription = "45x195 mm. spærtræ ubh.";
        } else if (length <= 3.97) {
            rafterDistance = 0.8;
            rafterDescription = "45x195 mm. spærtræ ubh.";
        } else if (length <= 4.34) {
            rafterDistance = 0.6;
            rafterDescription = "45x195 mm. spærtræ ubh.";
        } else if (length <= 4.88) {
            rafterDistance = 0.6;
            rafterDescription = "45x220 mm. spærtræ ubh.";
        } else if (length <= 5.42) {
            rafterDistance = 0.6;
            rafterDescription = "45x245 mm. spærtræ ubh.";
        } else if (length <= 6.07) {
            rafterDistance = 0.4;
            rafterDescription = "45x245 mm. spærtræ ubh.";
        } else if (length <= 6.48) {
            rafterDistance = 0.6;
            rafterDescription = "45x295 mm. spærtræ ubh.";
        } else if (length <= 7.23) {
            rafterDistance = 0.4;
            rafterDescription = "45x295 mm. spærtræ ubh.";
        }

        Product product = findRafterProduct(rafterDescription);

        rafterQuantity = Math.ceil(length / rafterDistance);
        productsPartsListEntries.add(new ProductsPartsListEntry(product, rafterQuantity));
    }

    private void calculateQuantityOfRafterHeavyRoof() throws DatabaseException {
        String rafterDescription = "";
        if (length <= 3.19) {
            rafterDistance = 1.0;
            rafterDescription = "45x195 mm. spærtræ ubh.";
        } else if (length <= 3.43) {
            rafterDistance = 0.8;
            rafterDescription = "45x195 mm. spærtræ ubh.";
        } else if (length <= 3.76) {
            rafterDistance = 0.6;
            rafterDescription = "45x195 mm. spærtræ ubh.";
        } else if (length <= 3.88) {
            rafterDistance = 0.9;
            rafterDescription = "45x220 mm. spærtræ ubh.";
        } else if (length <= 4.31) {
            rafterDistance = 0.8;
            rafterDescription = "45x245 mm. spærtræ ubh.";
        } else if (length <= 4.72) {
            rafterDistance = 0.6;
            rafterDescription = "45x245 mm. spærtræ ubh.";
        } else if (length <= 5.18) {
            rafterDistance = 0.8;
            rafterDescription = "45x295 mm. spærtræ ubh.";
        } else if (length <= 5.66) {
            rafterDistance = 0.6;
            rafterDescription = "45x295 mm. spærtræ ubh.";
        } else if (length <= 6.37) {
            rafterDistance = 0.4;
            rafterDescription = "45x295 mm. spærtræ ubh.";
        }

        Product product = findRafterProduct(rafterDescription);

        rafterQuantity = Math.ceil(length / rafterDistance);
        productsPartsListEntries.add(new ProductsPartsListEntry(product, rafterQuantity));
    }

    private Product findRafterProduct(String rafterDescription) {
        return products.stream()
                .filter(p -> p.getDescription().equals(rafterDescription))
                .filter(p -> p.getLength() >= width)
                .min(Comparator.comparing(Product::getLength))
                .orElse(null);
    }
}
