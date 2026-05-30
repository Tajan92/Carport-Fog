package app.services.utils.bluePrintMaker;

import app.entities.*;
import app.exceptions.CalculatorException;

import java.util.List;

public class BluePrintTopView {
    private Carport carport;
    private Shed shed;
    private List<ProductsPartsListEntry> productsPartsListEntries;
    private Svg svg;
    private double spacing;

    public void addDrawing(Svg svg, Carport carport, Shed shed, Roof roof, List<ProductsPartsListEntry> productsPartsListEntries) {
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
            svg.addRectangle(x, 0, carport.getWidth(), BluePrintData.RAFTER_WIDTH, BluePrintData.FILL_LIGHT_BROWN);
            x += spacing;
        }
    }

    private void addTopPlates() {
        double x = 0.0;
        double width = carport.getLength();
        double topY = BluePrintData.TOP_PLATE_Y;
        double bottomY = carport.getWidth() - BluePrintData.TOP_PLATE_Y - BluePrintData.RAFTER_WIDTH;

        svg.addRectangle(x, topY, BluePrintData.RAFTER_WIDTH, width, BluePrintData.FILL_LIGHT_BROWN);
        svg.addRectangle(x, bottomY, BluePrintData.RAFTER_WIDTH, width, BluePrintData.FILL_LIGHT_BROWN);
    }

    private void addPolesAndShedSiding() {
        double x = 0.0;
        double y = BluePrintData.TOP_PLATE_Y;
        double polesPerSide = getQuantityByPlacementDescription("Stolper nedgraves 90 cm. i jord") / 2;
        double poleSpacing = (carport.getLength() - BluePrintData.POLE_START_GAP - BluePrintData.POLE_END_GAP) / (polesPerSide - 1);

        if (shed != null) {
            poleSpacing = (carport.getLength() - BluePrintData.POLE_START_GAP - BluePrintData.POLE_END_GAP - shed.getLength()) / (polesPerSide);
            x = carport.getLength() - shed.getLength() - BluePrintData.POLE_END_GAP;

            addShedSiding(x, y);

            for (int i = 0; i < 2; i++) {
                y = BluePrintData.TOP_PLATE_Y;

                svg.addRectangle(x, y, BluePrintData.POLE_SIZE, BluePrintData.POLE_SIZE, BluePrintData.FILL_LIGHTEST_BROWN);

                y = (carport.getWidth() / 2) - (BluePrintData.POLE_SIZE / 2);

                svg.addRectangle(x, y, BluePrintData.POLE_SIZE, BluePrintData.POLE_SIZE, BluePrintData.FILL_LIGHTEST_BROWN);

                y = carport.getWidth() - BluePrintData.TOP_PLATE_Y - BluePrintData.POLE_SIZE;

                svg.addRectangle(x, y, BluePrintData.POLE_SIZE, BluePrintData.POLE_SIZE, BluePrintData.FILL_LIGHTEST_BROWN);

                x += shed.getLength() - BluePrintData.POLE_SIZE;
            }
        }
        x = BluePrintData.POLE_START_GAP;

        for (int i = 0; i < polesPerSide; i++) {

            y = BluePrintData.TOP_PLATE_Y;

            svg.addRectangle(x, y, BluePrintData.POLE_SIZE, BluePrintData.POLE_SIZE, BluePrintData.FILL_LIGHTEST_BROWN);

            y = carport.getWidth() - y - BluePrintData.POLE_SIZE;

            svg.addRectangle(x, y, BluePrintData.POLE_SIZE, BluePrintData.POLE_SIZE, BluePrintData.FILL_LIGHTEST_BROWN);

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
        svg.addDashedLine(x1, y2, x2, y1);
    }

    private void addShedSiding(double x, double y) {
        double multiplier = 2;
        if (shed.getWidth() < carport.getWidth()) {
            multiplier = 1.0;
        }
        double width = shed.getWidth() - (BluePrintData.POLE_SIZE * multiplier)-(BluePrintData.POLE_END_GAP * multiplier);

        svg.addShedDashedLine(x, y + BluePrintData.POLE_SIZE, x, y + width);
        svg.addShedRectangle(x + 1, y + BluePrintData.POLE_SIZE, BluePrintData.RAFTER_WIDTH, width);

        if (shed.getWidth() < carport.getWidth()) {
            svg.addShedDashedLine(x+3 , shed.getWidth()+4.5, x + 1 + shed.getLength(), shed.getWidth()+4.5);
            svg.addShedRectangle(x+3 , shed.getWidth()-1, shed.getLength()-4, BluePrintData.RAFTER_WIDTH);
        }

        x = x + shed.getLength();

        svg.addShedDashedLine(x + 1, y + BluePrintData.POLE_SIZE, x + 1, y + width);
        svg.addShedRectangle(x-4.5, y + BluePrintData.POLE_SIZE, BluePrintData.RAFTER_WIDTH, width);


    }

    private double getQuantityByPlacementDescription(String placementDescription) {
        return productsPartsListEntries.stream()
                .filter(x -> x.getPlacementDescription().contains(placementDescription))
                .map(x -> x.getQuantity())
                .findFirst()
                .orElse(0.0);
    }
}