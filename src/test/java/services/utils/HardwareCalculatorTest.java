package services.utils;

import app.entities.*;
import app.exceptions.CalculatorException;
import app.services.utils.partsListCalculator.HardwareCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HardwareCalculatorTest {
    private HardwareCalculator hardwareCalculator;
    private List<Product> products;
    private List<ProductsPartsListEntry> partsList;

    @BeforeEach
    void setup() {
        hardwareCalculator = new HardwareCalculator();
        products = new ArrayList<>();
        partsList = new ArrayList<>();

        products.add(new Product(35, 190.0, 289.0, 0.0, "pakke", "Beslag & Skruer", "plastmo bundskruer 200 stk."));
        products.add(new Product(39, 60.0, 99.0, 0.0, "Pakke", "Beslag & Skruer", "4,5 x 60 mm. skruer 200 stk."));
        products.add(new Product(40, 75.0, 125.0, 0.0, "pakke", "Beslag & Skruer", "4,0 x 50 mm. beslagskruer 250 stk."));
        products.add(new Product(42, 3.0, 6.0, 0.0, "Stk", "Beslag & Skruer", "firkantskiver 40x40x11mm"));
        products.add(new Product(43, 115.0, 189.0, 0.0, "pk.", "Beslag & Skruer", "4,5 x 70 mm. Skruer 400 stk."));
        products.add(new Product(44, 85.0, 139.0, 0.0, "pk.", "Beslag & Skruer", "4,5 x 50 mm. Skruer 300 stk."));
        products.add(new Product(45, 105.0, 165.0, 0.0, "Sæt", "Beslag & Skruer", "stalddørsgreb 50x75"));
        products.add(new Product(47, 2.2, 4.5, 0.0, "Stk", "Beslag & Skruer", "vinkelbeslag 35"));
        products.add(new Product(41, 4.5, 8.5, 12.0, "Stk", "Beslag & Skruer", "bræddebolt 10 x 120 mm."));
        products.add(new Product(46, 48.0, 79.0, 39.0, "Stk", "Beslag & Skruer", "t hængsel 390 mm"));
        products.add(new Product(36, 85.0, 135.0, 1000.0, "Rulle", "Beslag & Skruer", "hulbånd 1x20 mm. 10 mtr."));
        products.add(new Product(38, 8.0, 14.5, 19.0, "Stk", "Beslag & Skruer", "universal 190 mm venstre"));
        products.add(new Product(37, 8.0, 14.5, 19.0, "Stk", "Beslag & Skruer", "universal 190 mm højre"));
    }

    @Test
    void addPoleProductsTest() throws CalculatorException {
        Carport carport = new Carport(1, 600, 240, 780, 22000, 1, 1, 1);
        Shed shed = new Shed(300, 210, "Beklædning: 19x100 mm. trykimp. Brædt", true);
        Roof roof = new Roof(0, "Plastmo Ecolite blåtonet", "Fladt tag");
        double rafterQuantity = 16;
        double poleQuantity = 8;
        double sidingQuantity = 18;

        hardwareCalculator.addPoleProducts(partsList, roof, shed, carport, rafterQuantity, poleQuantity, sidingQuantity, products);

        assertFalse(partsList.isEmpty());
    }

    @Test
    void addScrewsToRoofTest() throws CalculatorException {
        Carport carport = new Carport(1, 600, 240, 780, 22000, 1, 1, 1);
        Shed shed = new Shed(300, 210, "Beklædning: 19x100 mm. trykimp. Brædt", true);
        Roof roof = new Roof(0, "Plastmo Ecolite blåtonet", "Fladt tag");
        double rafterQuantity = 16;
        double poleQuantity = 8;
        double sidingQuantity = 18;

        hardwareCalculator.addPoleProducts(partsList, roof, shed, carport, rafterQuantity, poleQuantity, sidingQuantity, products);

        assertTrue(partsList.stream()
                .anyMatch(e -> e.getPlacementDescription()
                        .equals("Skruer til tag")));

        assertTrue(partsList.stream()
                .anyMatch(e -> e.getProduct().getDescription()
                        .equals("plastmo bundskruer 200 stk.")));
    }

    @Test
    void addBracingStrapTest() throws CalculatorException {
        Carport carport = new Carport(1, 600, 240, 780, 22000, 1, 1, 1);
        Shed shed = new Shed(300, 210, "Beklædning: 19x100 mm. trykimp. Brædt", true);
        Roof roof = new Roof(0, "Plastmo Ecolite blåtonet", "Fladt tag");
        double rafterQuantity = 16;
        double poleQuantity = 8;
        double sidingQuantity = 18;

        hardwareCalculator.addPoleProducts(partsList, roof, shed, carport, rafterQuantity, poleQuantity, sidingQuantity, products);

        assertTrue(partsList.stream()
                .anyMatch(e -> e.getPlacementDescription()
                        .equals("Til vindkryds på spær")));
        assertTrue(partsList.stream()
                .anyMatch(e -> e.getProduct().getDescription()
                        .equals("hulbånd 1x20 mm. 10 mtr.")));
    }

    @Test
    void addUniversalConnectorAndScrewsTest() throws CalculatorException {
        Carport carport = new Carport(1, 600, 240, 780, 22000, 1, 1, 1);
        Shed shed = new Shed(300, 210, "Beklædning: 19x100 mm. trykimp. Brædt", true);
        Roof roof = new Roof(0, "Plastmo Ecolite blåtonet", "Fladt tag");
        double rafterQuantity = 16;
        double poleQuantity = 8;
        double sidingQuantity = 18;

        hardwareCalculator.addPoleProducts(partsList, roof, shed, carport, rafterQuantity, poleQuantity, sidingQuantity, products);

        assertTrue(partsList.stream()
                .anyMatch(e -> e.getPlacementDescription()
                        .equals("Til montering af spær på rem")));
        assertTrue(partsList.stream()
                .anyMatch(e -> e.getPlacementDescription()
                        .equals("Til montering af universalbeslag + hulbånd")));

        assertTrue(partsList.stream()
                .anyMatch(e -> e.getProduct().getDescription()
                        .equals("universal 190 mm venstre")));
        assertTrue(partsList.stream()
                .anyMatch(e -> e.getProduct().getDescription()
                        .equals("universal 190 mm højre")));
        assertTrue(partsList.stream()
                .anyMatch(e -> e.getProduct().getDescription()
                        .equals("4,0 x 50 mm. beslagskruer 250 stk.")));
    }

    @Test
    void addFasciaAndFasciaCappingScrewsTest() throws CalculatorException {
        Carport carport = new Carport(1, 600, 240, 780, 22000, 1, 1, 1);
        Shed shed = new Shed(300, 210, "Beklædning: 19x100 mm. trykimp. Brædt", true);
        Roof roof = new Roof(0, "Plastmo Ecolite blåtonet", "Fladt tag");
        double rafterQuantity = 16;
        double poleQuantity = 8;
        double sidingQuantity = 18;

        hardwareCalculator.addPoleProducts(partsList, roof, shed, carport, rafterQuantity, poleQuantity, sidingQuantity, products);

        assertTrue(partsList.stream()
                .anyMatch(e -> e.getPlacementDescription()
                        .equals("Til montering af stern & vandbrædt")));
        assertTrue(partsList.stream()
                .anyMatch(e -> e.getProduct().getDescription()
                        .equals("4,5 x 60 mm. skruer 200 stk.")));
    }

    @Test
    void addBoltsToPolesTest() throws CalculatorException {
        Carport carport = new Carport(1, 600, 240, 780, 22000, 1, 1, 1);
        Shed shed = new Shed(300, 210, "Beklædning: 19x100 mm. trykimp. Brædt", true);
        Roof roof = new Roof(0, "Plastmo Ecolite blåtonet", "Fladt tag");
        double rafterQuantity = 16;
        double poleQuantity = 8;
        double sidingQuantity = 18;

        hardwareCalculator.addPoleProducts(partsList, roof, shed, carport, rafterQuantity, poleQuantity, sidingQuantity, products);

        assertTrue(partsList.stream()
                .anyMatch(e -> e.getPlacementDescription()
                        .equals("Til montering af rem på stolper")));
        assertTrue(partsList.stream()
                .anyMatch(e -> e.getProduct().getDescription()
                        .equals("bræddebolt 10 x 120 mm.")));
    }

    @Test
    void addScrewsToSidingTest() throws CalculatorException {
        Carport carport = new Carport(1, 600, 240, 780, 22000, 1, 1, 1);
        Shed shed = new Shed(300, 210, "Beklædning: 19x100 mm. trykimp. Brædt", true);
        Roof roof = new Roof(0, "Plastmo Ecolite blåtonet", "Fladt tag");
        double rafterQuantity = 16;
        double poleQuantity = 8;
        double sidingQuantity = 18;

        hardwareCalculator.addPoleProducts(partsList, roof, shed, carport, rafterQuantity, poleQuantity, sidingQuantity, products);

        assertTrue(partsList.stream()
                .anyMatch(e -> e.getPlacementDescription()
                        .equals("Til montering af beklædning")));
        assertTrue(partsList.stream()
                .anyMatch(e -> e.getProduct().getDescription()
                        .equals("4,5 x 70 mm. Skruer 400 stk.")));
        assertTrue(partsList.stream()
                .anyMatch(e -> e.getProduct().getDescription()
                        .equals("4,5 x 50 mm. Skruer 300 stk.")));
    }

    @Test
    void addHardwareToShedDoorTest() throws CalculatorException {
        Carport carport = new Carport(1, 600, 240, 780, 22000, 1, 1, 1);
        Shed shed = new Shed(300, 210, "Beklædning: 19x100 mm. trykimp. Brædt", true);
        Roof roof = new Roof(0, "Plastmo Ecolite blåtonet", "Fladt tag");
        double rafterQuantity = 16;
        double poleQuantity = 8;
        double sidingQuantity = 18;

        hardwareCalculator.addPoleProducts(partsList, roof, shed, carport, rafterQuantity, poleQuantity, sidingQuantity, products);

        assertTrue(partsList.stream()
                .anyMatch(e -> e.getPlacementDescription()
                        .equals("Til skur dør")));
        assertTrue(partsList.stream()
                .anyMatch(e -> e.getPlacementDescription()
                        .equals("Til lås på dør i skur")));
        assertTrue(partsList.stream()
                .anyMatch(e -> e.getProduct().getDescription()
                        .equals("t hængsel 390 mm")));
        assertTrue(partsList.stream()
                .anyMatch(e -> e.getProduct().getDescription()
                        .equals("stalddørsgreb 50x75")));
    }

    @Test
    void addAngleBracketsToShedWithScrewsTest() throws CalculatorException {
        Carport carport = new Carport(1, 600, 240, 780, 22000, 1, 1, 1);
        Shed shed = new Shed(300, 210, "Beklædning: 19x100 mm. trykimp. Brædt", true);
        Roof roof = new Roof(0, "Plastmo Ecolite blåtonet", "Fladt tag");
        double rafterQuantity = 16;
        double poleQuantity = 8;
        double sidingQuantity = 18;

        hardwareCalculator.addPoleProducts(partsList, roof, shed, carport, rafterQuantity, poleQuantity, sidingQuantity, products);

        assertTrue(partsList.stream()
                .anyMatch(e -> e.getPlacementDescription()
                        .equals("Til montering af løsholter i skur")));
        assertTrue(partsList.stream()
                .anyMatch(e -> e.getPlacementDescription()
                        .equals("Til montering af vinkelbeslag 35 i skur")));
        assertTrue(partsList.stream()
                .anyMatch(e -> e.getProduct().getDescription()
                        .equals("vinkelbeslag 35")));
        assertTrue(partsList.stream()
                .anyMatch(e -> e.getProduct().getDescription()
                        .equals("4,0 x 50 mm. beslagskruer 250 stk.")));
    }

    @Test
    void addQuantitiesToListTest() throws CalculatorException {
        Carport carport = new Carport(1, 600, 240, 780, 22000, 1, 1, 1);
        Shed shed = new Shed(300, 210, "Beklædning: 19x100 mm. trykimp. Brædt", true);
        Roof roof = new Roof(0, "Plastmo Ecolite blåtonet", "Fladt tag");
        double rafterQuantity = 16;
        double poleQuantity = 8;
        double sidingQuantity = 18;

        hardwareCalculator.addPoleProducts(partsList, roof, shed, carport, rafterQuantity, poleQuantity, sidingQuantity, products);

        assertTrue(partsList.stream()
                .anyMatch(e -> e.getQuantity() > 0));
    }
}
