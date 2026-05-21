package app.services.utils;

import app.entities.ProductsPartsListEntry;

import java.util.List;

public class PriceCalculator {

    public static double calculateDiscount(double carportPrice, double discount){
        return carportPrice-discount;
    }

    public static double calculateInquiryCostPrice(List<ProductsPartsListEntry> productsPartsListEntryList){
        double totalCostPrice = 0;
        for (ProductsPartsListEntry entry : productsPartsListEntryList) {
            totalCostPrice += entry.getProduct().getCostPrice();
        }
        return totalCostPrice;
    }

    public static double calculateInquiryRetailPrice(List<ProductsPartsListEntry> productsPartsListEntryList){
        double totalRetailPrice = 0;
        for (ProductsPartsListEntry entry : productsPartsListEntryList) {
            totalRetailPrice += entry.getProduct().getRetailPrice() * entry.getQuantity();
        }
        return totalRetailPrice;
    }

    public static double calculateServiceFee(List<ProductsPartsListEntry> productsPartsListEntries){
        double totalServiceFee = 0;
        for (ProductsPartsListEntry entry : productsPartsListEntries) {
            totalServiceFee += (entry.getProduct().getCostPrice() * entry.getQuantity()) * 1.1;
        }
        return totalServiceFee;
    }
}
