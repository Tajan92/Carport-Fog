package app.services.bluePrintService;

import app.entities.*;
import app.exceptions.CalculatorException;
import app.services.utils.Svg;

import java.util.List;

public class BluePrintTopView {
    private Carport carport;
    private Shed shed;
    private List<ProductsPartsListEntry> productsPartsListEntries;
    private Svg svg;
    private double spacing;

    public void addDrawing(Svg svg, Carport carport, Shed shed, Roof roof, List<ProductsPartsListEntry> productsPartsListEntries) throws CalculatorException {
        this.svg = svg;
        this.carport = carport;
        this.shed = shed;
        this.productsPartsListEntries = productsPartsListEntries;

        addTopPlates();
        addRafters();
        addPolesAndShedSiding();
        addDashedLine();
    }

    private void addRafters() {
        double x = 0.0;
        double quantity = getQuantityByPlacementDescription("Spær, monteres på rem");
        spacing = (carport.getLength() - BluePrintData.RAFTER_WIDTH) / (quantity - 1);

        for (int i = 0; i < quantity; i++) {
            svg.addRectangle(x, 0, carport.getWidth(), BluePrintData.RAFTER_WIDTH, BluePrintData.FILL_WHITE);
            x += spacing;
        }
    }

    private void addTopPlates() {
        double x = 0.0;
        double width = carport.getLength();
        double topY = BluePrintData.TOP_PLATE_Y;
        double bottomY = carport.getWidth() - BluePrintData.TOP_PLATE_Y - BluePrintData.RAFTER_WIDTH;

        svg.addRectangle(x, topY, BluePrintData.RAFTER_WIDTH, width, BluePrintData.FILL_NONE);
        svg.addRectangle(x, bottomY, BluePrintData.RAFTER_WIDTH, width, BluePrintData.FILL_NONE);
    }

    private void addPolesAndShedSiding() {
        double x = 0.0;
        double y = BluePrintData.TOP_PLATE_Y;
        double polesPerSide = getQuantityByPlacementDescription("Stolper nedgraves 90 cm. i jord") / 2;
        double poleSpacing = (carport.getLength() - BluePrintData.POLE_START_GAP - BluePrintData.POLE_END_GAP) / (polesPerSide - 1);

        if (shed != null) {
            poleSpacing = (carport.getLength() - BluePrintData.POLE_START_GAP - BluePrintData.POLE_END_GAP - shed.getLength()) / (polesPerSide - 1);
            x = carport.getLength() - shed.getLength();

            addShedSiding(x, y);

            for (int i = 0; i < 2; i++) {
                y = BluePrintData.TOP_PLATE_Y;

                svg.addRectangle(x, y, BluePrintData.POLE_SIZE, BluePrintData.POLE_SIZE, BluePrintData.FILL_WHITE);

                y = (carport.getWidth() / 2) - (BluePrintData.POLE_SIZE / 2);

                svg.addRectangle(x, y, BluePrintData.POLE_SIZE, BluePrintData.POLE_SIZE, BluePrintData.FILL_WHITE);

                y = carport.getWidth() - BluePrintData.TOP_PLATE_Y - BluePrintData.POLE_SIZE;

                svg.addRectangle(x, y, BluePrintData.POLE_SIZE, BluePrintData.POLE_SIZE, BluePrintData.FILL_WHITE);

                x += shed.getLength() - BluePrintData.POLE_SIZE;
            }
        }
        x = BluePrintData.POLE_START_GAP;

        for (int i = 0; i < polesPerSide; i++) {

            y = BluePrintData.TOP_PLATE_Y;

            svg.addRectangle(x, y, BluePrintData.POLE_SIZE, BluePrintData.POLE_SIZE, BluePrintData.FILL_WHITE);

            y = carport.getWidth() - y - BluePrintData.POLE_SIZE;

            svg.addRectangle(x, y, BluePrintData.POLE_SIZE, BluePrintData.POLE_SIZE, BluePrintData.FILL_WHITE);

            x += poleSpacing;
        }
    }

    private void addDashedLine() {
        double halfRafterWidth = BluePrintData.RAFTER_WIDTH / 2;
        double x1 = spacing - halfRafterWidth;
        double y1 = BluePrintData.TOP_PLATE_Y;
        double x2 = carport.getLength() - spacing - halfRafterWidth;
        double y2 = carport.getWidth() - BluePrintData.TOP_PLATE_Y;

        if (shed != null) {
            x2 -= shed.getLength();
        }
        svg.addDashedLine(x1, y1, x2, y2);
        svg.addLine(x1, y2, x2, y1);
    }

    private void addShedSiding(double x, double y) {
        svg.addShedDashedLine(x, y + BluePrintData.POLE_SIZE, x, y + shed.getWidth() - BluePrintData.POLE_SIZE);
        svg.addShedRectangle(x + 1, y + BluePrintData.POLE_SIZE, shed.getWidth(), BluePrintData.RAFTER_WIDTH);

        x = x + shed.getLength() - BluePrintData.POLE_SIZE;

        svg.addShedDashedLine(x + 1, y + BluePrintData.POLE_SIZE, x + 1, y + shed.getWidth() - BluePrintData.POLE_SIZE);
        svg.addShedRectangle(x + shed.getLength() - BluePrintData.POLE_SIZE, y + BluePrintData.POLE_SIZE, shed.getWidth(), BluePrintData.RAFTER_WIDTH);
    }

    private double getQuantityByPlacementDescription(String placementDescription) {
        return productsPartsListEntries.stream()
                .filter(x -> x.getPlacementDescription().contains(placementDescription))
                .map(x -> x.getQuantity())
                .findFirst()
                .orElse(0.0);
    }
}