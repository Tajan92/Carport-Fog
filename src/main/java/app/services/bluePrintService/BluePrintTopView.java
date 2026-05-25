package app.services.bluePrintService;

import app.entities.*;
import app.exceptions.CalculatorException;
import app.services.utils.PartsListCalculator;
import app.services.utils.Svg;

import java.util.List;

public class BluePrintTopView {
    private PartsListCalculator partsListCalculator;
    private Carport carport;
    private Shed shed;
    private Roof roof;
    private List<Product> products;
    private List<ProductsPartsListEntry> productsPartsListEntries;
    private Svg svg;
    private double spacing;
    private final String fillColorWhite = "#ffffff";
    private final String fillColorNone = "none";

    public String addTopView(Carport carport, Shed shed, Roof roof, List<Product> allProducts) throws CalculatorException {
        this.carport = carport;
        this.shed = shed;
        this.roof = roof;
        this.products = allProducts;
        this.partsListCalculator = new PartsListCalculator();
        productsPartsListEntries = partsListCalculator.createProductsPartsList(carport, shed, roof, products);
        this.svg = new Svg(80, 450, "1000", "600", "0 0 1000 600", 0);

        addTopPlates();
        addRafters();
        addPoles();
        addDashedLine();

        return svg.toString();
    }

    private void addRafters() {
        double x = 0.0;
        double y = 0.0;
        double width = 4.5; // Rafters always 4,5 cm wide
        double height = carport.getWidth() - width; //Accounting space for the last rafter

        double quantity = getQuantityByPlacementDescription("Spær, monteres på rem");

        spacing = (int) Math.ceil(carport.getLength() / (quantity + 1));
        for (int i = 0; i < quantity; i++) {
            svg.addRectangle(x, y, height, width, fillColorWhite);
            x += spacing;
        }
    }

    private void addTopPlates() {
        double x = 0.0;
        double y = 35.0; // Standard spacing 35 cm.
        double height = 4.5; // Top plates always 4,5 cm wide
        double width = carport.getLength();
        svg.addRectangle(x, y, height, width, null);
        svg.addRectangle(x, carport.getWidth() - x - height, height, width, fillColorNone);
    }

    private void addPoles() {
        double x = 0.0;
        double y;
        double height = 9.7; // pole is 9.7x9.7
        double width = 9.7;
        double quantity = getQuantityByPlacementDescription("Remme i sider, sadles ned i stolper") / 2; //2 sides
        double poleSpacing = (carport.getLength() - x - 35) / quantity + 1;

        if (shed != null) {
            poleSpacing = (carport.getLength() - x - 35 - shed.getLength()) / quantity + 1;
            x = carport.getLength() - shed.getLength();
            for (int i = 0; i < 2; i++) {
                y = 35;
                addShed(x, y, height);
                svg.addRectangle(x, y, height, width, fillColorWhite);
                y = (carport.getWidth() / 2) - (9.7 / 2); // pole is 9.7x9.7 - 2 to find center
                svg.addRectangle(x, y, height, width, fillColorWhite);
                y = carport.getWidth() - 35 - height;
                svg.addRectangle(x, y, height, width, fillColorWhite);
                x += shed.getLength() - height;
            }
        }
        x = 100.0;
        for (int i = 0; i < quantity; i++) {
            y = 35; // Standard spacing end and sides = 35 cm.
            svg.addRectangle(x, y, height, width, fillColorWhite);
            y = carport.getWidth() - y - height;
            svg.addRectangle(x, y, height, width, fillColorWhite);
            x += poleSpacing;
        }
    }

    private void addDashedLine() {
        double x1 = spacing;
        double y1 = 35.0;
        double x2 = carport.getLength() - spacing;
        double y2 = carport.getWidth() - 35;
        if (shed != null) {
            x2 -= shed.getLength();
        }
        svg.addDashedLine(x1, y1, x2, y2);
        svg.addLine(x1, y2, x2, y1);
    }

    private void addShed(double x, double y, double height) {
        svg.addShedDashedLine(x, y + height, x, y + shed.getWidth() - height);
        svg.addShedRectangle(x + 1, y + height, 4.5, shed.getWidth());
    }

    private double getQuantityByPlacementDescription(String placementDescription) {
        return productsPartsListEntries.stream()
                .filter(x -> x.getPlacementDescription().equals(placementDescription))
                .map(x -> x.getQuantity())
                .findFirst()
                .orElse(0.0);
    }
}
