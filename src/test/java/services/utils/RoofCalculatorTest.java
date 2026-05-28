package services.utils;

import app.entities.*;
import app.exceptions.CalculatorException;
import app.services.utils.partsListCalculator.RoofCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RoofCalculatorTest {
    private RoofCalculator roofCalculator;
    private List<Product> products;
    private List<ProductsPartsListEntry> partsList;

    @BeforeEach
    void setup() {
        roofCalculator = new RoofCalculator();
        products = new ArrayList<>();
        partsList = new ArrayList<>();

        products.add(new Product(24, 95.0, 149.0, 360.0, "Stk", "Træ & Tagplader", "Plastmo Ecolite blåtonet"));
        products.add(new Product(25, 160.0, 249.0, 600.0, "Stk", "Træ & Tagplader", "Plastmo Ecolite blåtonet"));
        products.add(new Product(22, 340.0, 525.0, 550.0, "Stk", "Træ & Tagplader", "45x245 mm. spærtræ ubh."));
        products.add(new Product(14, 230.0, 355.0, 550.0, "Stk", "Træ & Tagplader", "45x195 mm. spærtræ ubh."));
        products.add(new Product(29, 84.0, 129.0, 118.0, "Stk", "Træ & Tagplader", "Eternittag B6 - sortblå"));
        products.add(new Product(28, 78.0, 119.0, 118.0, "Stk", "Træ & Tagplader", "Eternittag B6 - grå"));
        products.add(new Product(10, 52.0, 82.0, 540.0, "Stk", "Træ & Tagplader", "19x100 mm. trykimp. Brædt"));
        products.add(new Product(15, 320.0, 495.0, 720.0, "Stk", "Træ & Tagplader", "45x195 mm. spærtræ ubh."));
        products.add(new Product(49, 275.0, 410.0, 400.0, "Stk", "Træ & Tagplader", "45x295 mm. spærtræ ubh."));
        products.add(new Product(11, 35.0, 55.0, 360.0, "Stk", "Træ & Tagplader", "19x100 mm. trykimp. Brædt"));
        products.add(new Product(7, 40.0, 64.0, 240.0, "Stk", "Træ & Tagplader", "45x95 mm. Reglar ub."));
        products.add(new Product(16, 120.0, 185.0, 240.0, "Stk", "Træ & Tagplader", "45x220 mm. spærtræ ubh."));
        products.add(new Product(20, 150.0, 230.0, 240.0, "Stk", "Træ & Tagplader", "45x245 mm. spærtræ ubh."));
        products.add(new Product(21, 245.0, 380.0, 400.0, "Stk", "Træ & Tagplader", "45x245 mm. spærtræ ubh."));
        products.add(new Product(6, 45.0, 72.0, 270.0, "Stk", "Træ & Tagplader", "45x95 mm. Reglar ub."));
        products.add(new Product(9, 20.0, 32.0, 210.0, "Stk", "Træ & Tagplader", "19x100 mm. trykimp. Brædt"));
        products.add(new Product(17, 200.0, 310.0, 400.0, "Stk", "Træ & Tagplader", "45x220 mm. spærtræ ubh."));
        products.add(new Product(19, 380.0, 590.0, 720.0, "Stk", "Træ & Tagplader", "45x220 mm. spærtræ ubh."));
        products.add(new Product(12, 100.0, 155.0, 240.0, "Stk", "Træ & Tagplader", "45x195 mm. spærtræ ubh."));
        products.add(new Product(27, 8.5, 13.5, 42.0, "Stk", "Træ & Tagplader", "Betontagsten - sort"));
        products.add(new Product(26, 9.0, 14.5, 42.0, "Stk", "Træ & Tagplader", "Betontagsten - koralrød"));
        products.add(new Product(23, 465.0, 720.0, 720.0, "Stk", "Træ & Tagplader", "45x245 mm. spærtræ ubh."));
        products.add(new Product(4, 92.0, 142.0, 540.0, "Stk", "Træ & Tagplader", "25x125mm. trykimp. Brædt"));
        products.add(new Product(51, 495.0, 750.0, 720.0, "Stk", "Træ & Tagplader", "45x295 mm. spærtræ ubh."));
        products.add(new Product(48, 180.0, 260.0, 240.0, "Stk", "Træ & Tagplader", "45x295 mm. spærtræ ubh."));
        products.add(new Product(5, 40.0, 65.0, 420.0, "Stk", "Træ & Tagplader", "38x73 mm. Lægte ubh. (Lægte til dør)"));
        products.add(new Product(2, 140.0, 215.0, 540.0, "Stk", "Træ & Tagplader", "25x200 mm. trykimp. Brædt (Sternbræt)"));
        products.add(new Product(8, 125.0, 195.0, 300.0, "Stk", "Træ & Tagplader", "97x97 mm. trykimp. Stolpe"));
        products.add(new Product(30, 88.0, 135.0, 118.0, "Stk", "Træ & Tagplader", "Eternittag B7 - sortblå"));
        products.add(new Product(3, 60.0, 95.0, 360.0, "Stk", "Træ & Tagplader", "25x125mm. trykimp. Brædt"));
        products.add(new Product(13, 170.0, 260.0, 400.0, "Stk", "Træ & Tagplader", "45x195 mm. spærtræ ubh."));
        products.add(new Product(1, 95.0, 145.0, 360.0, "Stk", "Træ & Tagplader", "25x200 mm. trykimp. Brædt (Sternbræt)"));
        products.add(new Product(18, 275.0, 425.0, 550.0, "Stk", "Træ & Tagplader", "45x220 mm. spærtræ ubh."));
        products.add(new Product(50, 370.0, 555.0, 550.0, "Stk", "Træ & Tagplader", "45x295 mm. spærtræ ubh."));
    }

    @Test
    void addProductsToListTest() throws CalculatorException {
        Carport carport = new Carport(1, 600, 240, 780, 22000, 1, 1, 1);
        Roof roof = new Roof(0, "Plastmo Ecolite blåtonet", "Fladt tag");

        roofCalculator.addRoofProducts(partsList, roof, carport, products);

        assertFalse(partsList.isEmpty());
    }

    @Test
    void addRoofMaterialFlatRoofTest() throws CalculatorException {
        Carport carport = new Carport(1, 600, 240, 780, 22000, 1, 1, 1);
        Roof roof = new Roof(0, "Plastmo Ecolite blåtonet", "Fladt tag");

        roofCalculator.addRoofProducts(partsList, roof, carport, products);

        assertTrue(partsList.stream()
                .anyMatch(e -> e.getPlacementDescription()
                        .equals("Tag monteres på spær")));
        assertTrue(partsList.stream()
                .anyMatch(e -> e.getProduct().getDescription()
                        .equals("Plastmo Ecolite blåtonet")));
    }

    @Test
    void addRoofMaterialHighRoofTest() throws CalculatorException {
        Carport carport = new Carport(1, 600, 240, 780, 22000, 1, 1, 1);
        Roof roof = new Roof(15, "Eternittag B7 - sortblå", "Højt tag");

        roofCalculator.addRoofProducts(partsList, roof, carport, products);

        assertTrue(partsList.stream()
                .anyMatch(e -> e.getPlacementDescription()
                        .equals("Tag monteres på spær")));
        assertTrue(partsList.stream()
                .anyMatch(e -> e.getProduct().getDescription()
                        .equals("Eternittag B7 - sortblå")));
    }

    @Test
    void addFasciaCappingTest() throws CalculatorException {
        Carport carport = new Carport(1, 600, 240, 780, 22000, 1, 1, 1);
        Roof roof = new Roof(0, "Plastmo Ecolite blåtonet", "Fladt tag");

        roofCalculator.addRoofProducts(partsList, roof, carport, products);

        assertTrue(partsList.stream()
                .anyMatch(e -> e.getProduct().getDescription()
                        .equals("19x100 mm. trykimp. Brædt")));

        assertTrue(partsList.stream()
                .anyMatch(e -> e.getPlacementDescription()
                        .equals("Vandbrædt på stern i sider")));
        assertTrue(partsList.stream()
                .anyMatch(e -> e.getPlacementDescription()
                        .equals("Vandbrædt på stern i forende")));
    }

    @Test
    void addFasciaBoardsTest() throws CalculatorException {
        Carport carport = new Carport(1, 600, 240, 780, 22000, 1, 1, 1);
        Roof roof = new Roof(0, "Plastmo Ecolite blåtonet", "Fladt tag");

        roofCalculator.addRoofProducts(partsList, roof, carport, products);

        assertTrue(partsList.stream()
                .anyMatch(e -> e.getProduct().getDescription()
                        .equals("25x125mm. trykimp. Brædt")));

        assertTrue(partsList.stream()
                .anyMatch(e -> e.getProduct().getDescription()
                        .equals("25x200 mm. trykimp. Brædt (Sternbræt)")));

        assertTrue(partsList.stream()
                .anyMatch(e -> e.getPlacementDescription()
                        .equals("Oversternbrædder til siderne")));

        assertTrue(partsList.stream()
                .anyMatch(e -> e.getPlacementDescription()
                        .equals("Understernbrædder til for & bag ende")));
    }

    @Test
    void addTopPlatesTest() throws CalculatorException {
        Carport carport = new Carport(1, 600, 240, 780, 22000, 1, 1, 1);
        Roof roof = new Roof(0, "Plastmo Ecolite blåtonet", "Fladt tag");

        roofCalculator.addRoofProducts(partsList, roof, carport, products);

        assertTrue(partsList.stream()
                .anyMatch(e -> e.getProduct().getDescription()
                        .equals("45x195 mm. spærtræ ubh.")));

        assertTrue(partsList.stream()
                .anyMatch(e -> e.getPlacementDescription()
                        .equals("Remme i sider, sadles ned i stolper")));
    }

    @Test
    void addRaftersTest() throws CalculatorException {
        Carport carport = new Carport(1, 600, 240, 780, 22000, 1, 1, 1);
        Roof roof = new Roof(0, "Plastmo Ecolite blåtonet", "Fladt tag");

        roofCalculator.addRoofProducts(partsList, roof, carport, products);

        assertTrue(partsList.stream()
                .anyMatch(e -> e.getProduct().getDescription()
                        .equals("45x245 mm. spærtræ ubh.")));
    }

    @Test
    void addQuantitiesToListTest() throws CalculatorException {
        Carport carport = new Carport(1, 600, 240, 780, 22000, 1, 1, 1);
        Roof roof = new Roof(0, "Plastmo Ecolite blåtonet", "Fladt tag");

        roofCalculator.addRoofProducts(partsList, roof, carport, products);

        assertTrue(partsList.stream()
                .anyMatch(e -> e.getQuantity() > 0));
    }
}