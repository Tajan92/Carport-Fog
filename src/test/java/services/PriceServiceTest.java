package services;
import app.entities.Product;
import app.entities.ProductsPartsListEntry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import persistence.MapperTest;

import java.util.ArrayList;
import java.util.List;

public class PriceServiceTest {

    List<Product> products;
    List<ProductsPartsListEntry> productsPartsListEntries;

    double expectedTotalCostPrice;
    double expectedRetailPrice;
    double expectedServiceFee;

    PriceService priceService;

    @BeforeEach
    void setUp() {
        this.priceService = new PriceService();

        //Initialzing products for testing
        this.products = List.of(
                new Product(1, 95.00, 145.00, 360.00, "Stk", "Træ & Tagplader", "25x200 mm. trykimp. Brædt (Sternbræt)"),
                new Product(2, 140.00, 215.00, 540.00, "Stk", "Træ & Tagplader", "25x200 mm. trykimp. Brædt (Sternbræt)"),
                new Product(47, 2.20, 4.50, null, "Stk", "Beslag & Skruer", "vinkelbeslag 35"),
                new Product(34, 220.00, 349.00, 244.00, "Stk", "Træ & Tagplader", "Beklædning: 9x1220x2440mm Krydsfiner m/spor (Svalehale)"),
                new Product(35, 190.00, 289.00, null, "pakke", "Beslag & Skruer", "plastmo bundskruer 200 stk.")
        );

        //Create list
        this.productsPartsListEntries = new ArrayList<>();
        double quantity = 1.0;
        for (Product product : products) {
            ProductsPartsListEntry entry = new ProductsPartsListEntry(product, quantity);
            this.productsPartsListEntries.add(entry);
            quantity++;
        }

        //Calculated expected prices to check later
        this.expectedTotalCostPrice = 647.20;
        this.expectedRetailPrice = 1002.50;
        this.expectedServiceFee = 221.16;
    }
}

