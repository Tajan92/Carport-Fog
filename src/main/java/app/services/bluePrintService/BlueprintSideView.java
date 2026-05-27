package app.services.bluePrintService;

import app.entities.*;
import app.exceptions.CalculatorException;
import app.services.utils.Svg;

import java.util.List;

public class BlueprintSideView {
    private Carport carport;
    private Shed shed;
    private Roof roof;
    private List<ProductsPartsListEntry> productsPartsListEntries;
    private Svg svg;
    private static final String FILL_COLOR_WHITE = "#ffffff";
    private static final String FILL_COLOR_GREY = "#a6a5a5";
    private static final String FILL_COLOR_NONE = "none";
    private static final double POLE_HEIGHT = 210.0;

    public void addDrawing(Svg svg, Carport carport, Shed shed, Roof roof, List<ProductsPartsListEntry> productsPartsListEntries) throws CalculatorException {
        this.svg = svg;
        this.carport = carport;
        this.shed = shed;
        this.roof = roof;
        this.productsPartsListEntries = productsPartsListEntries;

        addGround();
        addPolesAndShedSiding();
        addRoof();
    }

    private void addGround() {
        double x1 = 10.0;
        double y = 300.0;
        double x2 = carport.getLength() - 20;
        svg.addLine(x1, y, x2, y);
    }

    private void addPolesAndShedSiding() {
        double topOfPoleY = 90.0;
        double poleThickness = 9.7; // pole is 9.7x9.7
        double startGap = 100;
        double endGap = 30;
        double polesPerSide = getQuantityByPlacementDescription("Stolper nedgraves 90 cm. i jord") / 2; //2 sides
        double availablePoleSpacing = carport.getLength() - startGap - endGap;

        if (shed != null) {
            availablePoleSpacing -= shed.getLength();
        }
        double poleSpacing = availablePoleSpacing / (polesPerSide - 1);

        if (shed != null) {
            double shedStartX = carport.getLength() - shed.getLength();
            double shedEndX = shedStartX + shed.getLength();
            svg.addShedRectangle(shedStartX, topOfPoleY, POLE_HEIGHT, shed.getLength());
            addShedSiding(shedStartX, shedEndX, topOfPoleY);
        }

        double poleX = startGap;

        for (int i = 0; i < polesPerSide; i++) {
            svg.addRectangle(poleX, topOfPoleY, POLE_HEIGHT, poleThickness, FILL_COLOR_WHITE);
            poleX += poleSpacing;
        }
    }

    private void addShedSiding(double shedStartX, double shedEndX, double shedTopY) {
        //Vertical Siding
        if (shed.getSiding().contains("Beklædning: 19x100 mm. trykimp. Brædt") || shed.getSiding().equals("Beklædning: 19x100mm Profilbrædt (1 på 2 beklædning)")) {
            double plateWidth = 100.0;
            double plateOverlapGap = 20.0;
            double totalWidth = plateWidth + plateOverlapGap;
            double sidingQuantity = Math.ceil(shed.getLength() / totalWidth);

            for (int i = 0; i < sidingQuantity; i++) {
                shedStartX += plateWidth;
                if (shedStartX >= shedEndX) {
                    return;
                }
                svg.addLine(shedStartX, shedTopY, shedStartX, shedTopY + POLE_HEIGHT);
                shedStartX += plateOverlapGap;
                if (shedStartX >= shedEndX) {
                    return;
                }
                svg.addLine(shedStartX, shedTopY, shedStartX, shedTopY + POLE_HEIGHT);
            }
            //Plate Siding
        } else if (shed.getSiding().contains("Beklædning: 9x1220x2440mm Krydsfiner m/spor (Svalehale)")) {
            double plateWidth = 122.0;
            double sidingQuantity = Math.ceil(shed.getLength() / plateWidth);

            for (int i = 0; i < sidingQuantity; i++) {
                double remainingWidth = shedEndX - shedStartX;
                double currentPlateWidth = plateWidth;
                if (remainingWidth < currentPlateWidth) {
                    currentPlateWidth = remainingWidth;
                }
                svg.addRectangle(shedStartX, shedTopY, POLE_HEIGHT, currentPlateWidth, FILL_COLOR_GREY);
                shedStartX += currentPlateWidth;
            }
            //Horizontal Siding
        } else {
            double groundY = shedTopY + POLE_HEIGHT;
            double plateSize = 13.5;
            shedTopY += plateSize;
            double sidingQuantity = Math.ceil(POLE_HEIGHT / plateSize);
            for (int i = 0; i < sidingQuantity; i++) {
                if (shedTopY >= groundY) {
                    return;
                }
                svg.addLine(shedStartX, shedTopY, shedEndX, shedTopY);
                shedTopY += plateSize;
            }
        }
    }

    private void addRoof() {
        if (roof.getRoofType().contains("Fladt tag")) {
            double roofTopLeftCornerX = 0.0;
            double roofTopLeftCornerY = 70.0;
            double roofTopRightCornerY = 80.0;
            double fasciaHeight = 20;
            double underFasciaHeight = 35;
            double roofBottomRightCornerX = roofTopLeftCornerX + carport.getLength();

            // Upper Fascia ------ Magic Numbers 1,2 and 3 is just to give angles and placement to give style to drawing.
            svg.addLine(roofTopLeftCornerX, roofTopLeftCornerY, roofBottomRightCornerX, roofTopRightCornerY);
            svg.addLine(roofTopLeftCornerX, roofTopLeftCornerY, roofTopLeftCornerX - 2, roofTopLeftCornerY + fasciaHeight);
            svg.addLine(roofTopLeftCornerX - 2, roofTopLeftCornerY + fasciaHeight, roofBottomRightCornerX - 2, roofTopRightCornerY + fasciaHeight);
            svg.addLine(roofBottomRightCornerX - 2, roofTopRightCornerY + fasciaHeight, roofBottomRightCornerX, roofTopRightCornerY);

            // Under Fascia
            svg.addLine(roofTopLeftCornerX - 1, roofTopLeftCornerY + fasciaHeight, roofTopLeftCornerX - 3, roofTopLeftCornerY + underFasciaHeight);
            svg.addLine(roofTopLeftCornerX - 3, roofTopLeftCornerY + underFasciaHeight, roofBottomRightCornerX - 3, roofTopRightCornerY + underFasciaHeight);
            svg.addLine(roofBottomRightCornerX - 3, roofTopRightCornerY + underFasciaHeight, roofBottomRightCornerX - 1, roofTopRightCornerY + fasciaHeight);

            // High roof
        } else {
            double height = calculateRoofHeight(carport.getWidth(), roof.getRoofSlope());

            if (height > 85) {
                height = 85;
            }

            svg.addRectangle(0, 90, 4, carport.getLength(), FILL_COLOR_WHITE);
            svg.addRoofRectangle(2, 90 - height, height, carport.getLength() - 4);
            svg.addRectangle(4, 90 - height - 4, 4, carport.getLength() - 8, FILL_COLOR_NONE);
        }
    }

    public double calculateRoofHeight(double width, double roofSlope) {
        double run = width / 2.0;

        double angleInRadians = Math.toRadians(roofSlope);

        return run * Math.tan(angleInRadians);
    }

    private double getQuantityByPlacementDescription(String placementDescription) {
        return productsPartsListEntries.stream()
                .filter(x -> x.getPlacementDescription().contains(placementDescription))
                .map(x -> x.getQuantity())
                .findFirst()
                .orElse(0.0);
    }
}
