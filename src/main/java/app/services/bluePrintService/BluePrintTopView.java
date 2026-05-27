package app.services.bluePrintService;

import app.entities.*;
import app.exceptions.CalculatorException;
import app.services.utils.PartsListCalculator;
import app.services.utils.Svg;

import java.util.List;

public class BluePrintTopView {
    private Carport carport;
    private Shed shed;
    private List<ProductsPartsListEntry> productsPartsListEntries;
    private Svg svg;
    private double spacing;
    private static final String FILL_COLOR_WHITE = "#ffffff";
    private static final String FILL_COLOR_NONE = "none";
    private static final double RAFTER_AND_PLATE_SIZE = 4.5; // Rafters always 4,5 cm wide

    public void addDrawing(Svg svg, Carport carport, Shed shed, Roof roof, List<ProductsPartsListEntry> productsPartsListEntries) throws CalculatorException {
        this.svg = svg;
        this.carport = carport;
        this.shed = shed;

        this.productsPartsListEntries = productsPartsListEntries;

        addTopPlates();
        addRafters();
        addPolesAndShedSiding();
        addDashedLine();
        svg.endGroup();
    }

    private void addRafters() {
        double x = 0.0;
        double y = 0.0;

        double height = carport.getWidth();

        double quantity = getQuantityByPlacementDescription("Spær, monteres på rem");

        spacing = (carport.getLength() - RAFTER_AND_PLATE_SIZE) / (quantity - 1);
        for (int i = 0; i < quantity; i++) {
            svg.addRectangle(x, y, height, RAFTER_AND_PLATE_SIZE, FILL_COLOR_WHITE);
            x += spacing;
        }
    }

    private void addTopPlates() {
        double x = 0.0;
        double width = carport.getLength();
        double topY = 35.0;
        double bottomY = carport.getWidth() - 35 - RAFTER_AND_PLATE_SIZE;

        svg.addRectangle(x, topY, RAFTER_AND_PLATE_SIZE, width, FILL_COLOR_NONE);
        svg.addRectangle(x, bottomY, RAFTER_AND_PLATE_SIZE, width, FILL_COLOR_NONE);
    }

    private void addPolesAndShedSiding() {
        double x = 0.0;
        double y = 35.0;
        double poleSize = 9.7; // pole is 9.7x9.7
        double startGap = 100;
        double polesPerSide = getQuantityByPlacementDescription("Stolper nedgraves 90 cm. i jord") / 2; //2 sides
        double poleSpacing = (carport.getLength() - startGap - 35) / (polesPerSide - 1);

        if (shed != null) {
            poleSpacing = (carport.getLength() - startGap - 35 - shed.getLength()) / (polesPerSide - 1);
            x = carport.getLength() - shed.getLength();
            addShedSiding(x, y, poleSize);
            for (int i = 0; i < 2; i++) {
                y = 35;
                svg.addRectangle(x, y, poleSize, poleSize, FILL_COLOR_WHITE);
                y = (carport.getWidth() / 2) - (poleSize / 2); // divide by 2 to find center
                svg.addRectangle(x, y, poleSize, poleSize, FILL_COLOR_WHITE);
                y = carport.getWidth() - 35 - poleSize;
                svg.addRectangle(x, y, poleSize, poleSize, FILL_COLOR_WHITE);
                x += shed.getLength() - poleSize;
            }
        }
        x = 100.0;
        for (int i = 0; i < polesPerSide; i++) {
            y = 35; // Standard spacing end and sides = 35 cm.
            svg.addRectangle(x, y, poleSize, poleSize, FILL_COLOR_WHITE);
            y = carport.getWidth() - y - poleSize;
            svg.addRectangle(x, y, poleSize, poleSize, FILL_COLOR_WHITE);
            x += poleSpacing;
        }
    }

    private void addDashedLine() {
        double halfRafterWidth = RAFTER_AND_PLATE_SIZE / 2;
        double x1 = spacing - halfRafterWidth;
        double y1 = 35.0;

        double x2 = carport.getLength() - spacing - halfRafterWidth;
        double y2 = carport.getWidth() - 35;
        if (shed != null) {
            x2 -= shed.getLength();
        }
        svg.addDashedLine(x1, y1, x2, y2);
        svg.addLine(x1, y2, x2, y1);
    }

    private void addShedSiding(double x, double y, double poleSize) {
        svg.addShedDashedLine(x, y + poleSize, x, y + shed.getWidth() - poleSize);
        svg.addShedRectangle(x + 1, y + poleSize, shed.getWidth(), RAFTER_AND_PLATE_SIZE);
        x = x + shed.getLength() - poleSize;
        svg.addShedDashedLine(x + 1, y + poleSize, x + 1, y + shed.getWidth() - poleSize);
        svg.addShedRectangle(x + shed.getLength() - poleSize, y + poleSize, shed.getWidth(), RAFTER_AND_PLATE_SIZE);
    }

    private double getQuantityByPlacementDescription(String placementDescription) {
        return productsPartsListEntries.stream()
                .filter(x -> x.getPlacementDescription().contains(placementDescription))
                .map(x -> x.getQuantity())
                .findFirst()
                .orElse(0.0);
    }
}
