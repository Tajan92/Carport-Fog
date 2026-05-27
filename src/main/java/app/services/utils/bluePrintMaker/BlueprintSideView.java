package app.services.utils.bluePrintMaker;

import app.entities.*;
import app.exceptions.CalculatorException;

import java.util.List;

public class BlueprintSideView {
    private Carport carport;
    private Shed shed;
    private Roof roof;
    private List<ProductsPartsListEntry> productsPartsListEntries;
    private Svg svg;

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
        double x1 = BluePrintData.GROUND_START_X;
        double y = BluePrintData.GROUND_Y;
        double x2 = carport.getLength() - BluePrintData.GROUND_END_MARGIN;

        svg.addLine(x1, y, x2, y);
    }

    private void addPolesAndShedSiding() {
        double topOfPoleY = BluePrintData.SIDE_POLE_TOP_Y;
        double polesPerSide = getQuantityByPlacementDescription("Stolper nedgraves 90 cm. i jord") / 2;
        double availablePoleSpacing = carport.getLength() - BluePrintData.POLE_START_GAP - BluePrintData.POLE_END_GAP;

        if (shed != null) {
            availablePoleSpacing -= shed.getLength();
        }

        double poleSpacing = availablePoleSpacing / (polesPerSide - 1);

        // Shed
        if (shed != null) {
            double shedStartX = carport.getLength() - shed.getLength();
            double shedEndX = shedStartX + shed.getLength();

            svg.addShedRectangle(shedStartX, topOfPoleY, BluePrintData.POLE_HEIGHT, shed.getLength());

            addShedSiding(shedStartX, shedEndX, topOfPoleY);
        }

        // Poles
        double poleX = BluePrintData.POLE_START_GAP;

        for (int i = 0; i < polesPerSide; i++) {
            svg.addRectangle(poleX, topOfPoleY, BluePrintData.POLE_HEIGHT, BluePrintData.POLE_SIZE, BluePrintData.FILL_WHITE);
            poleX += poleSpacing;
        }
    }

    private void addShedSiding(double shedStartX, double shedEndX, double shedTopY) {
        // Vertical siding
        if (shed.getSiding().contains("19x100 mm. trykimp. Brædt") || shed.getSiding().equals("Beklædning: 19x100mm Profilbrædt (1 på 2 beklædning)")) {
            double plateWidth = BluePrintData.VERTICAL_SIDING_BOARD_WIDTH;
            double overlapGap = BluePrintData.VERTICAL_SIDING_OVERLAP;
            double totalWidth = plateWidth + overlapGap;
            double sidingQuantity = Math.ceil(shed.getLength() / totalWidth);

            for (int i = 0; i < sidingQuantity; i++) {
                shedStartX += plateWidth;

                if (shedStartX >= shedEndX) {
                    return;
                }
                svg.addLine(shedStartX, shedTopY, shedStartX, shedTopY + BluePrintData.POLE_HEIGHT);
                shedStartX += overlapGap;

                if (shedStartX >= shedEndX) {
                    return;
                }
                svg.addLine(shedStartX, shedTopY, shedStartX, shedTopY + BluePrintData.POLE_HEIGHT);
            }
        }
        // Plywood siding
        else if (shed.getSiding().contains("1220x2440mm Krydsfiner")) {
            double plateWidth = BluePrintData.PLYWOOD_SIDING_WIDTH;
            double sidingQuantity = Math.ceil(shed.getLength() / plateWidth);

            for (int i = 0; i < sidingQuantity; i++) {
                double remainingWidth = shedEndX - shedStartX;
                double currentPlateWidth = Math.min(plateWidth, remainingWidth);

                svg.addRectangle(shedStartX, shedTopY, BluePrintData.POLE_HEIGHT, currentPlateWidth, BluePrintData.FILL_GREY);
                shedStartX += currentPlateWidth;
            }
        }
        // Horizontal siding
        else {
            double groundY = shedTopY + BluePrintData.POLE_HEIGHT;
            double plateSize = BluePrintData.HORIZONTAL_SIDING_BOARD_HEIGHT;
            shedTopY += plateSize;
            double sidingQuantity = Math.ceil(BluePrintData.POLE_HEIGHT / plateSize);

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
            double leftX = 0.0;
            double topLeftY = BluePrintData.FLAT_ROOF_TOP_LEFT_Y;
            double rightX = carport.getLength();
            double topRightY = BluePrintData.FLAT_ROOF_TOP_RIGHT_Y;

            // Top roof line
            svg.addLine(leftX, topLeftY, rightX, topRightY);

            // Upper fascia
            svg.addLine(leftX, topLeftY, leftX - BluePrintData.FASCIA_OUTER_OFFSET, topLeftY + BluePrintData.FASCIA_HEIGHT);
            svg.addLine(leftX - BluePrintData.FASCIA_OUTER_OFFSET, topLeftY + BluePrintData.FASCIA_HEIGHT, rightX - BluePrintData.FASCIA_OUTER_OFFSET, topRightY + BluePrintData.FASCIA_HEIGHT);
            svg.addLine(rightX - BluePrintData.FASCIA_OUTER_OFFSET, topRightY + BluePrintData.FASCIA_HEIGHT, rightX, topRightY);

            // Under fascia
            svg.addLine(leftX - BluePrintData.UNDER_FASCIA_TOP_OFFSET, topLeftY + BluePrintData.FASCIA_HEIGHT, leftX - BluePrintData.UNDER_FASCIA_OFFSET, topLeftY + BluePrintData.UNDER_FASCIA_HEIGHT);
            svg.addLine(leftX - BluePrintData.UNDER_FASCIA_OFFSET, topLeftY + BluePrintData.UNDER_FASCIA_HEIGHT, rightX - BluePrintData.UNDER_FASCIA_OFFSET, topRightY + BluePrintData.UNDER_FASCIA_HEIGHT);
            svg.addLine(rightX - BluePrintData.UNDER_FASCIA_OFFSET, topRightY + BluePrintData.UNDER_FASCIA_HEIGHT, rightX - BluePrintData.UNDER_FASCIA_TOP_OFFSET, topRightY + BluePrintData.FASCIA_HEIGHT);
        } else {
            double height = calculateRoofHeight(carport.getWidth(), roof.getRoofSlope());

            if (height > BluePrintData.HIGH_ROOF_MAX_HEIGHT) {
                height = BluePrintData.HIGH_ROOF_MAX_HEIGHT;
            }
            svg.addRectangle(0, BluePrintData.HIGH_ROOF_BASE_Y, 4, carport.getLength(), BluePrintData.FILL_WHITE);
            svg.addRoofRectangle(2, BluePrintData.HIGH_ROOF_BASE_Y - height, height, carport.getLength() - 4);
            svg.addRectangle(4, BluePrintData.HIGH_ROOF_BASE_Y - height - 4, 4, carport.getLength() - 8, BluePrintData.FILL_NONE);
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