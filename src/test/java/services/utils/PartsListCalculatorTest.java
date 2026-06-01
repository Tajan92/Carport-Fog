package services.utils;

import app.entities.*;
import app.exceptions.CalculatorException;
import app.services.utils.partsListCalculator.PartsListCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class PartsListCalculatorTest {
    private PartsListCalculator partsListCalculator;
    private List<Product> products;

    @BeforeEach
    public void setup() {
        partsListCalculator = new PartsListCalculator();
        products = new ArrayList<>();
        products.add(new Product(35, 190.0, 289.0, 0.0, "pakke", "Beslag & Skruer", "plastmo bundskruer 200 stk."));
        products.add(new Product(39, 60.0, 99.0, 0.0, "Pakke", "Beslag & Skruer", "4,5 x 60 mm. skruer 200 stk."));
        products.add(new Product(40, 75.0, 125.0, 0.0, "pakke", "Beslag & Skruer", "4,0 x 50 mm. beslagskruer 250 stk."));
        products.add(new Product(42, 3.0, 6.0, 0.0, "Stk", "Beslag & Skruer", "firkantskiver 40x40x11mm"));
        products.add(new Product(43, 115.0, 189.0, 0.0, "pk.", "Beslag & Skruer", "4,5 x 70 mm. Skruer 400 stk."));
        products.add(new Product(44, 85.0, 139.0, 0.0, "pk.", "Beslag & Skruer", "4,5 x 50 mm. Skruer 300 stk."));
        products.add(new Product(45, 105.0, 165.0, 0.0, "Sæt", "Beslag & Skruer", "stalddørsgreb 50x75"));
        products.add(new Product(47, 2.2, 4.5, 0.0, "Stk", "Beslag & Skruer", "vinkelbeslag 35"));
        products.add(new Product(24, 95.0, 149.0, 360.0, "Stk", "Træ & Tagplader", "Plastmo Ecolite blåtonet"));
        products.add(new Product(25, 160.0, 249.0, 600.0, "Stk", "Træ & Tagplader", "Plastmo Ecolite blåtonet"));
        products.add(new Product(14, 230.0, 355.0, 550.0, "Stk", "Træ & Tagplader", "45x195 mm. spærtræ ubh."));
        products.add(new Product(10, 52.0, 82.0, 540.0, "Stk", "Træ & Tagplader", "19x100 mm. trykimp. Brædt"));
        products.add(new Product(41, 4.5, 8.5, 12.0, "Stk", "Beslag & Skruer", "bræddebolt 10 x 120 mm."));
        products.add(new Product(46, 48.0, 79.0, 39.0, "Stk", "Beslag & Skruer", "t hængsel 390 mm"));
        products.add(new Product(6, 45.0, 72.0, 270.0, "Stk", "Træ & Tagplader", "45x95 mm. Reglar ub."));
        products.add(new Product(36, 85.0, 135.0, 1000.0, "Rulle", "Beslag & Skruer", "hulbånd 1x20 mm. 10 mtr."));
        products.add(new Product(23, 465.0, 720.0, 720.0, "Stk", "Træ & Tagplader", "45x245 mm. spærtræ ubh."));
        products.add(new Product(4, 92.0, 142.0, 540.0, "Stk", "Træ & Tagplader", "25x125mm. trykimp. Brædt"));
        products.add(new Product(5, 40.0, 65.0, 420.0, "Stk", "Træ & Tagplader", "38x73 mm. Lægte ubh. (Lægte til dør)"));
        products.add(new Product(2, 140.0, 215.0, 540.0, "Stk", "Træ & Tagplader", "25x200 mm. trykimp. Brædt (Sternbræt)"));
        products.add(new Product(38, 8.0, 14.5, 19.0, "Stk", "Beslag & Skruer", "universal 190 mm venstre"));
        products.add(new Product(37, 8.0, 14.5, 19.0, "Stk", "Beslag & Skruer", "universal 190 mm højre"));
        products.add(new Product(8, 125.0, 195.0, 300.0, "Stk", "Træ & Tagplader", "97x97 mm. trykimp. Stolpe"));
        products.add(new Product(52, 20.0, 32.0, 210.0, "Stk", "Træ & Tagplader", "Beklædning: 19x100 mm. trykimp. Brædt"));
    }

    @Test
    public void calculatePartsList() throws CalculatorException {
        Carport carport = new Carport(1, 600, 240, 780, 22000, 1, 1, 1);
        Roof roof = new Roof(0, "Plastmo Ecolite blåtonet", "Fladt tag");
        Shed shed = new Shed(300, 210, "Beklædning: 19x100 mm. trykimp. Brædt", true);
        List<ProductsPartsListEntry> productsPartsListEntries = partsListCalculator.createProductsPartsList(carport, shed, roof, products);

        assertTrue(productsPartsListEntries.stream()
                .anyMatch(e -> e.getQuantity() > 0));
    }
}
