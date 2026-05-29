package app.services;

import app.entities.ProductsPartsListEntry;
import app.services.utils.PriceCalculator;
import java.util.List;

public class PriceService {

    public double getTotalCostPrice(List<ProductsPartsListEntry> allEntries){
        return PriceCalculator.calculateInquiryCostPrice(allEntries);
    }

    public double getTotalRetailPrice(List<ProductsPartsListEntry> allEntries){
        return PriceCalculator.calculateRetailPrice(allEntries);
    }

    public double getServiceFee(List<ProductsPartsListEntry> allEntries){
        return PriceCalculator.calculateServiceFee(allEntries);
    }

    public double getRevenue(double retailPrice, double serviceFee, double discount){
        return PriceCalculator.getRevenue(retailPrice, serviceFee, discount);
    }

    public double getGrossProfit(double costPrice, double retailPrice, double serviceFee, double discount){
        return PriceCalculator.getGrossProfit(costPrice, retailPrice, serviceFee, discount);
    }

    public double getGrossMarginInPercent(double costPrice, double retailPrice, double serviceFee, double discount){
        return PriceCalculator.getGrossMarginInPercent(costPrice, retailPrice, serviceFee, discount);
    }
}
