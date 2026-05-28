package services.utils;

import app.entities.*;
import app.exceptions.CalculatorException;
import app.services.utils.partsListCalculator.ShedCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ShedCalculatorTest {
    private ShedCalculator shedCalculator;
    private List<Product> products;
    private List<ProductsPartsListEntry> partsList;

    @BeforeEach
    void setup() {
        shedCalculator = new ShedCalculator();
        products = new ArrayList<>();
        partsList = new ArrayList<>();

        products.add(new Product(1, 200.0, 300.0, 210.0, "stk.", "Træ","38x73 mm. Lægte ubh. (Lægte til dør)"));
        products.add(new Product(2, 200.0, 300.0, 240.0, "stk.", "Træ","45x95 mm. Reglar ub."));
        products.add(new Product(3, 200.0, 300.0, 270.0, "stk.", "Træ","45x95 mm. Reglar ub."));
        products.add(new Product(4, 200.0, 300.0, 210.0, "stk.", "Træ","Beklædning: 19x100 mm. trykimp. Brædt"));
    }

    @Test
    void addShedProducts_ShouldAddExpectedEntries() throws CalculatorException {

        Carport carport = new Carport(1,600, 240, 780, 22000, 1, 1, 1);
        Roof roof = new Roof(0, "Plastmo", "Fladt tag");
        Shed shed = new Shed(300, 210,
                "Beklædning: 19x100 mm. trykimp. Brædt", true);

        shedCalculator.addShedProducts(partsList, carport, roof, shed, products);

        // Checks that list is not empty
        assertFalse(partsList.isEmpty());

        // Checks if door stabilizer has been added
        assertTrue(partsList.stream()
                .anyMatch(e -> e.getProduct().getDescription()
                        .equals("38x73 mm. Lægte ubh. (Lægte til dør)")));

        // Checks that studs have been added
        assertTrue(partsList.stream()
                .anyMatch(e -> e.getProduct().getDescription()
                        .equals("45x95 mm. Reglar ub.")));

        // Checks that quantities is not 0
        assertTrue(partsList.stream()
                .anyMatch(e -> e.getQuantity() > 0));
    }
}