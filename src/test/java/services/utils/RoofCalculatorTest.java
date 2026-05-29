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

        products.add(new Product(25, 160.0, 249.0, 600.0, "Stk", "Træ & Tagplader", "Plastmo Ecolite blåtonet"));
        products.add(new Product(14, 230.0, 355.0, 550.0, "Stk", "Træ & Tagplader", "45x195 mm. spærtræ ubh."));
        products.add(new Product(10, 52.0, 82.0, 540.0, "Stk", "Træ & Tagplader", "19x100 mm. trykimp. Brædt"));
        products.add(new Product(12, 100.0, 155.0, 240.0, "Stk", "Træ & Tagplader", "45x195 mm. spærtræ ubh."));
        products.add(new Product(23, 465.0, 720.0, 720.0, "Stk", "Træ & Tagplader", "45x245 mm. spærtræ ubh."));
        products.add(new Product(4, 92.0, 142.0, 540.0, "Stk", "Træ & Tagplader", "25x125mm. trykimp. Brædt"));
        products.add(new Product(51, 495.0, 750.0, 720.0, "Stk", "Træ & Tagplader", "45x295 mm. spærtræ ubh."));
        products.add(new Product(2, 140.0, 215.0, 540.0, "Stk", "Træ & Tagplader", "25x200 mm. trykimp. Brædt (Sternbræt)"));
        products.add(new Product(30, 88.0, 135.0, 118.0, "Stk", "Træ & Tagplader", "Eternittag B7 - sortblå"));
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