package app.services.utils.partsListCalculator;

import app.entities.Product;
import app.exceptions.CalculatorException;

import java.util.Comparator;
import java.util.List;

public class ProductFinder {


    public Product findProductByDescription(List<Product> products, String description) throws CalculatorException {
        return products.stream()
                .filter(p -> p.getDescription().equals(description))
                .findFirst()
                .orElseThrow(() -> new CalculatorException("Produkt ikke fundet: " + description));
    }

    public Product findProductByDescriptionAndNearestLength(List<Product> products, String description, double minLength) throws CalculatorException {
        return products.stream()
                .filter(p -> p.getDescription().equals(description))
                .filter(p -> p.getLength() >= minLength)
                .min(Comparator.comparing(Product::getLength))
                .orElseThrow(() -> new CalculatorException("Produkt ikke fundet: " + description + " med minimum længde " + minLength + " cm"));
    }
}
