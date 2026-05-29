package app.services.utils;
import app.entities.ProductsPartsListEntry;
import java.util.List;

public class PriceCalculator {

    private static double roundUpToTwoDecimals(double value) {
        return Math.ceil(value * 100.0) / 100.0;
    }

    public static double calculateDiscount(double carportPrice, double discount){
        return roundUpToTwoDecimals(carportPrice - discount);
    }

    public static double calculateInquiryCostPrice(List<ProductsPartsListEntry> productsPartsListEntryList){
        double totalCostPrice = 0;
        for (ProductsPartsListEntry entry : productsPartsListEntryList) {
            totalCostPrice += entry.getProduct().getCostPrice() * entry.getQuantity();
        }
        return roundUpToTwoDecimals(totalCostPrice);
    }

    public static double calculateInquiryRetailPrice(List<ProductsPartsListEntry> productsPartsListEntryList){
        double totalRetailPrice = 0;
        for (ProductsPartsListEntry entry : productsPartsListEntryList) {
            totalRetailPrice += entry.getProduct().getRetailPrice() * entry.getQuantity();
        }
        return roundUpToTwoDecimals(totalRetailPrice);
    }

    public static double calculateServiceFee(List<ProductsPartsListEntry> productsPartsListEntries){
        double totalServiceFee = 0;
        for (ProductsPartsListEntry entry : productsPartsListEntries) {
            totalServiceFee += (entry.getProduct().getCostPrice() * entry.getQuantity()) * 0.1;
        }
        return roundUpToTwoDecimals(totalServiceFee);
    }

    public static double getRevenue (double retailPrice, double serviceFee, double discount){
        double rawRevenue = (retailPrice + serviceFee) * (1.0 - discount);
        return roundUpToTwoDecimals(rawRevenue);
    }

    public static double getGrossProfit(double costPrice, double retailPrice, double serviceFee, double discount){
        return roundUpToTwoDecimals((retailPrice + serviceFee - costPrice) * (1.0 - discount));
    }

    public static double getGrossMarginInPercent(double costPrice, double retailPrice, double serviceFee, double discount){
        double revenue = (retailPrice + serviceFee) * (1.0 - discount);
        if (revenue == 0){
            return 0;
        }
        double grossProfit = revenue - costPrice;
        return roundUpToTwoDecimals((grossProfit / revenue) * 100.0);
    }
}
