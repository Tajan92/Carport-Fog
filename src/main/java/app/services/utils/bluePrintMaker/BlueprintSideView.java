package app.services.utils.bluePrintMaker;

import app.entities.*;

import java.util.List;

public class BlueprintSideView {
    private Carport carport;
    private Shed shed;
    private Roof roof;
    private List<ProductsPartsListEntry> productsPartsListEntries;
    private Svg svg;

    public void addDrawing(Svg svg, Carport carport, Shed shed, Roof roof, List<ProductsPartsListEntry> productsPartsListEntries) {
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

        double poleSpacing = availablePoleSpacing / (polesPerSide);


        // Poles
        double poleX = BluePrintData.POLE_START_GAP;

        for (int i = 0; i < polesPerSide+1; i++) {
            svg.addRectangle(poleX, topOfPoleY, BluePrintData.POLE_HEIGHT, BluePrintData.POLE_SIZE, BluePrintData.FILL_LIGHTEST_BROWN);
            poleX += poleSpacing;
        }
        // Shed
        if (shed != null) {
            double shedStartX = carport.getLength() - shed.getLength() - BluePrintData.POLE_END_GAP;
            double shedEndX = shedStartX + shed.getLength();

            svg.addShedRectangle(shedStartX, topOfPoleY, shed.getLength(), BluePrintData.POLE_HEIGHT);

            addShedSiding(shedStartX, shedEndX, topOfPoleY);
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
            svg.addLine(leftX, topLeftY, leftX, topLeftY + BluePrintData.FASCIA_HEIGHT);
            svg.addLine(leftX, topLeftY + BluePrintData.FASCIA_HEIGHT, rightX, topRightY + BluePrintData.FASCIA_HEIGHT);
            for (int i = 1; i < BluePrintData.FASCIA_HEIGHT-1; i++) {
                svg.addWhiteLine(leftX, topLeftY + BluePrintData.FASCIA_HEIGHT-i, rightX, topRightY + BluePrintData.FASCIA_HEIGHT-i);
            }
            svg.addLine(rightX, topRightY + BluePrintData.FASCIA_HEIGHT, rightX, topRightY);

            // Under fascia
            svg.addLine(leftX, topLeftY + BluePrintData.FASCIA_HEIGHT, leftX, topLeftY + BluePrintData.UNDER_FASCIA_HEIGHT);
            svg.addLine(leftX, topLeftY + BluePrintData.UNDER_FASCIA_HEIGHT, rightX, topRightY + BluePrintData.UNDER_FASCIA_HEIGHT);
            for (int i = 1; i < BluePrintData.UNDER_FASCIA_HEIGHT - BluePrintData.FASCIA_HEIGHT-1; i++) {
                svg.addWhiteLine(leftX, topLeftY + BluePrintData.FASCIA_HEIGHT+i, rightX, topRightY + BluePrintData.FASCIA_HEIGHT+i);
            }
            svg.addLine(rightX, topRightY + BluePrintData.UNDER_FASCIA_HEIGHT, rightX, topRightY + BluePrintData.FASCIA_HEIGHT);
        } else {
            double height = RoofHeightCalculator.calculateRoofHeight(carport.getWidth(), roof.getRoofSlope());

            String color = "";
            switch (roof.getRoofMaterial()) {
                case "Eternittag B6 - sortblå", "Eternittag B7 - sortblå" -> color = "#1A1E28";
                case "Betontagsten - sort" -> color = "#1A1A1A";
                case "Betontagsten - koralrød" -> color = "#A55137";
                case "Eternittag B6 - grå" -> color = "#4A4F54";
            }

            svg.addRectangle(0, BluePrintData.HIGH_ROOF_BASE_Y, 4, carport.getLength(), BluePrintData.FILL_LIGHT_BROWN);
            svg.addRoofColoringRectangle(2, BluePrintData.HIGH_ROOF_BASE_Y - height, height, carport.getLength() - 4, color);
            svg.addRoofRectangle(2, BluePrintData.HIGH_ROOF_BASE_Y - height, height, carport.getLength() - 4);
            svg.addRectangle(4, BluePrintData.HIGH_ROOF_BASE_Y - height - 4, 4, carport.getLength() - 8, BluePrintData.FILL_LIGHT_BROWN);
        }
    }

    private double getQuantityByPlacementDescription(String placementDescription) {
        return productsPartsListEntries.stream()
                .filter(x -> x.getPlacementDescription().contains(placementDescription))
                .map(x -> x.getQuantity())
                .findFirst()
                .orElse(0.0);
    }
}