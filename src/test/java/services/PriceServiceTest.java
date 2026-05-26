package services;
import app.entities.Product;
import app.entities.ProductsPartsListEntry;
import app.services.PriceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PriceServiceTest {

    List<Product> products;
    List<ProductsPartsListEntry> entries;
    double expectedServiceFee;
    double expectedRetailPrice;
    double expectedTotalCostPrice;
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
        this.entries = new ArrayList<>();
        double quantity = 1.0;
        for (Product product : products) {
            ProductsPartsListEntry entry = new ProductsPartsListEntry(product, quantity);
            this.entries.add(entry);
            quantity++;
        }
        this.expectedTotalCostPrice = 2211.60;
        this.expectedRetailPrice = 3429.50;
        this.expectedServiceFee = 221.16;
    }

    @Test
    public void getTotalCostPriceTest(){
        //All cost prices put multiplied by their qunatity added up
        double actualTotalCostPrice = priceService.getTotalCostPrice(entries);

        //2211.60
        assertEquals(expectedTotalCostPrice, actualTotalCostPrice);
    }

    @Test
    public void getTotalRetailPrice(){
        //All retail prices multiplied by their quantity added up
        double actualTotalRetailPrice = priceService.getTotalRetailPrice(entries);

        //3429.50
        assertEquals(expectedRetailPrice, actualTotalRetailPrice);
    }

    @Test
    public void getServiceFeeTest(){
        //All cost prices multiplied by their quantity and multiplied again by 0.1 (10% fee)

        double actualServiceFeePrice = priceService.getServiceFee(entries);

        //221.16
        assertEquals(expectedServiceFee, actualServiceFeePrice);
    }

    @Test
    public void getRevenueTest(){
        //5% discount
        double discount = 0.05;
        double expectedRevenue = 3468.13;

        double actualRevenue = priceService.getRevenue(expectedRetailPrice, expectedServiceFee, discount);

        assertEquals(expectedRevenue, actualRevenue);
    }

    @Test
    public void getGrossProfitTest(){
        double discount = 0.1;
        double expectedGrossProfit = 1295.16;
        double actualGrossProfit = priceService.getGrossProfit(expectedTotalCostPrice, expectedRetailPrice, expectedServiceFee, discount);

        assertEquals(expectedGrossProfit, actualGrossProfit);
    }

    @Test
    public void getGrossMarginInPercentTest(){
        double discount = 0.15;
        double expectedGrossMarginPercent = 28.73;

        double actualGrossMarginPercent = priceService.getGrossMarginInPercent(expectedTotalCostPrice, expectedRetailPrice, expectedServiceFee, discount);

        assertEquals(expectedGrossMarginPercent, actualGrossMarginPercent);
    }
}

