package app.services.bluePrintService;

import app.entities.*;
import app.exceptions.CalculatorException;
import app.services.utils.PartsListCalculator;
import app.services.utils.Svg;

import java.util.List;

public class BlueprintSideView {
    private PartsListCalculator partsListCalculator;
    private Carport carport;
    private Shed shed;
    private Roof roof;
    private List<Product> products;
    private List<ProductsPartsListEntry> productsPartsListEntries;
    private Svg svg;
    private static final String FILL_COLOR_WHITE = "#ffffff";
    private static final String FILL_COLOR_GREY = "#a6a5a5";
    private static final double POLE_HEIGHT = 210.0;

    public String addSideView(Carport carport, Shed shed, Roof roof, List<Product> allProducts) throws CalculatorException {
        this.carport = carport;
        this.shed = shed;
        this.roof = roof;
        this.products = allProducts;
        this.partsListCalculator = new PartsListCalculator();
        productsPartsListEntries = partsListCalculator.createProductsPartsList(carport, shed, roof, products);
        this.svg = new Svg(80, 10, "1000", "600", "0 0 1000 600", 0);

        addGround();
        addPolesAndShedSiding();
        addRoof();

        return svg.toString();
    }

    private void addGround() {
        double x1 = 10.0;
        double y = 300.0;
        double x2 = carport.getLength() - 20;
        svg.addLine(x1, y, x2, y);
    }

    private void addPolesAndShedSiding() {
        double y = 90.0;
        double poleWidth = 9.7; // pole is 9.7x9.7
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
            svg.addShedRectangle(shedStartX, y, POLE_HEIGHT, shed.getLength());
            addShedSiding(shedStartX, shedEndX, y);
        }

        double poleX = startGap;

        for (int i = 0; i < polesPerSide; i++) {
            svg.addRectangle(poleX, y, POLE_HEIGHT, poleWidth, FILL_COLOR_WHITE);
            poleX += poleSpacing;
        }
    }

    private void addShedSiding(double shedStartX, double shedEndX, double shedTopY) {
        //Vertical Siding
        if (shed.getSiding().contains("Beklædning: 19x100") || shed.getSiding().equals("Beklædning: 19x100mm Profilbrædt (1 på 2 beklædning)")) {
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
            double roofTopLefCornerY = 70.0;
            double roofTopRightCornerY = 80.0;
            double roofBottomRightCornerX = roofTopLeftCornerX + carport.getLength();

            // Upper Fascia
            svg.addLine(roofTopLeftCornerX,roofTopLefCornerY, roofBottomRightCornerX ,roofTopRightCornerY);
            svg.addLine(roofTopLeftCornerX,roofTopLefCornerY, roofTopLeftCornerX-2,roofTopLefCornerY+20);
            svg.addLine(roofTopLeftCornerX-2,roofTopLefCornerY+20, roofBottomRightCornerX-2,roofTopRightCornerY+20);
            svg.addLine(roofBottomRightCornerX-2,roofTopRightCornerY+20, roofBottomRightCornerX, roofTopRightCornerY);

            // Under Fascia
            svg.addLine(roofTopLeftCornerX-1, roofTopLefCornerY+20, roofTopLeftCornerX-3 ,roofTopLefCornerY+35);
            svg.addLine(roofTopLeftCornerX-3 ,roofTopLefCornerY+35, roofBottomRightCornerX-3,roofTopRightCornerY+35);
            svg.addLine(roofBottomRightCornerX-3,roofTopRightCornerY+35, roofBottomRightCornerX-1,roofTopRightCornerY+20);
        } else { // If high roof


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
