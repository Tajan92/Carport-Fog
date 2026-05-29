package services.utils;

import app.entities.*;
import app.exceptions.CalculatorException;
import app.services.utils.partsListCalculator.PoleCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PoleCalculatorTest {
    private PoleCalculator poleCalculator;
    private List<Product> products;
    private List<ProductsPartsListEntry> partsList;

    @BeforeEach
    void setup() {
        poleCalculator = new PoleCalculator();
        products = new ArrayList<>();
        partsList = new ArrayList<>();

        products.add(new Product(8, 125.0, 195.0, 300.0, "Stk", "Træ & Tagplader", "97x97 mm. trykimp. Stolpe"));
    }

    @Test
    void addPoleProductsTest() throws CalculatorException {
        Carport carport = new Carport(1, 600, 240, 780, 22000, 1, 1, 1);
        Shed shed = new Shed(300, 210, "Beklædning: 19x100 mm. trykimp. Brædt", true);

        poleCalculator.addPoleProducts(partsList, shed,  carport, products);

        assertFalse(partsList.isEmpty());
    }

    @Test
    void addPolesTest() throws CalculatorException {
        Carport carport = new Carport(1, 600, 240, 780, 22000, 1, 1, 1);
        Shed shed = new Shed(300, 210, "Beklædning: 19x100 mm. trykimp. Brædt", true);

        poleCalculator.addPoleProducts(partsList, shed, carport, products);

        assertTrue(partsList.stream()
                .anyMatch(e -> e.getPlacementDescription()
                        .equals("Stolper nedgraves 90 cm. i jord")));
    }

    @Test
    void addPolesToShedTest() throws CalculatorException {
        Carport carport = new Carport(1, 600, 240, 780, 22000, 1, 1, 1);
        Shed shed = new Shed(300, 210, "Beklædning: 19x100 mm. trykimp. Brædt", true);

        poleCalculator.addPoleProducts(partsList, shed, carport, products);

        assertTrue(partsList.stream()
                .anyMatch(e -> e.getPlacementDescription()
                        .equals("Midterstolper til skur")));
        assertTrue(partsList.stream()
                .anyMatch(e -> e.getPlacementDescription()
                        .equals("Hjørnestolper til skur")));
    }

    @Test
    void addQuantitiesToList() throws CalculatorException {
        Carport carport = new Carport(1, 600, 240, 780, 22000, 1, 1, 1);
        Shed shed = new Shed(300, 210, "Beklædning: 19x100 mm. trykimp. Brædt", true);

        poleCalculator.addPoleProducts(partsList, shed, carport, products);

        assertTrue(partsList.stream()
                .anyMatch(e -> e.getQuantity() > 0));
    }
}