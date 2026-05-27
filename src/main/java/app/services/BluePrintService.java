package app.services;

import app.dto.requestDTO.carports.CarportRequestDTO;
import app.dto.requestDTO.carports.CarportShedRequestDTO;
import app.entities.*;
import app.exceptions.CalculatorException;
import app.exceptions.DatabaseException;
import app.persistence.ProductMapper;
import app.services.utils.bluePrintMaker.BluePrintData;
import app.services.utils.bluePrintMaker.BluePrintTopView;
import app.services.utils.bluePrintMaker.BlueprintSideView;
import app.services.converters.CarportConverter;
import app.services.converters.RoofConverter;
import app.services.converters.ShedConverter;
import app.services.utils.PartsListCalculator;
import app.services.utils.bluePrintMaker.Svg;

import java.util.List;

public class BluePrintService {
    private Svg svg;
    private Carport carport;
    private Shed shed;
    private Roof roof;
    private List<Product> products;
    private List<ProductsPartsListEntry> productsPartsListEntries;
    private BlueprintSideView sideView;
    private BluePrintTopView topView;
    private PartsListCalculator partsListCalculator;
    private CarportConverter carportConverter;
    private RoofConverter roofConverter;
    private ShedConverter shedConverter;

    public BluePrintService(ProductMapper productMapper) throws DatabaseException {
        this.products = productMapper.getAllProducts();
        this.carportConverter = new CarportConverter();
        this.roofConverter = new RoofConverter();
        this.shedConverter = new ShedConverter();
        this.partsListCalculator = new PartsListCalculator();
        this.sideView = new BlueprintSideView();
        this.topView = new BluePrintTopView();
    }

    public String createBlueprint(CarportRequestDTO carportRequestDTO) throws CalculatorException {
        this.carport = carportConverter.covertCarportDTOToEntity(carportRequestDTO);
        this.roof = roofConverter.convertRoofDTOtoEntity(carportRequestDTO.getRoofRequestDTO());

        if (carportRequestDTO instanceof CarportShedRequestDTO carportShedRequestDTO) {
            this.shed = shedConverter.convertShedDTOtoEntity(carportShedRequestDTO.getShedRequestDTO());
        } else {
            this.shed = null;
        }

        this.productsPartsListEntries = partsListCalculator.createProductsPartsList(carport, shed, roof, products);

        svg = new Svg(0, 0, "1400", "1200", "0 0 1100 1100", 1);

        // SIDE VIEW
        svg.startGroup(BluePrintData.OFFSET_X, BluePrintData.OFFSET_Y_SIDE);
        sideView.addDrawing(svg, carport, shed, roof, productsPartsListEntries);
        svg.endGroup();

        // TOP VIEW
        svg.startGroup(BluePrintData.OFFSET_X, BluePrintData.OFFSET_Y_TOP);
        topView.addDrawing(svg, carport, shed, roof, productsPartsListEntries);
        svg.endGroup();

        // Arrows
        addMeasurements();
        return svg.toString();
    }

    private void addMeasurements() {
        addHorizontalArrowsToTopView();
        addVerticalArrowsToTopView();
        addVerticalArrowsToSideView();
        addHorizontalArrowsToSideView();
    }

    private void addVerticalArrowsToTopView() {
        double x = BluePrintData.OFFSET_X - BluePrintData.ARROW_OFFSET;
        double y1 = BluePrintData.OFFSET_Y_TOP;
        double y2 = BluePrintData.OFFSET_Y_TOP + carport.getWidth();
        drawVerticalArrow(x, y1 + BluePrintData.POLE_END_GAP, y2 - BluePrintData.POLE_END_GAP, carport.getWidth()-(BluePrintData.POLE_END_GAP * 2));

        drawVerticalArrow(x-20, y1, y2,carport.getWidth());
    }

    private void addHorizontalArrowsToTopView() {
        double horizontalStartX = BluePrintData.OFFSET_X;
        double horizontalStartY = BluePrintData.OFFSET_Y_TOP + carport.getWidth() + BluePrintData.ARROW_OFFSET;

        drawHorizontalArrow(horizontalStartX, horizontalStartX + carport.getLength(), horizontalStartY, carport.getLength(), true);

        double rafterQuantity = getQuantityByPlacementDescription("Spær, monteres på rem");
        if (rafterQuantity <= 1) return;

        double rafterSpacing = carport.getLength() / (rafterQuantity - 1);
        double x = horizontalStartX;
        double x2 = x + rafterSpacing;

        double y = BluePrintData.OFFSET_Y_TOP - BluePrintData.ARROW_OFFSET;
        for (int i = 0; i < rafterQuantity - 1; i++) {
            boolean drawTick = (i == rafterQuantity - 2);

            drawHorizontalArrow(x, x2, y, rafterSpacing, drawTick);
            x += rafterSpacing;
            x2 += rafterSpacing;
        }
    }

    private void addVerticalArrowsToSideView() {
        double verticalStartX = BluePrintData.OFFSET_X - BluePrintData.ARROW_OFFSET;
        double carportWithRoofHeight = BluePrintData.CARPORT_HEIGHT_FLAT_ROOF;
        if (roof.getRoofType().contains("Højt tag")) {
            carportWithRoofHeight = BluePrintData.POLE_HEIGHT + calculateRoofHeight(carport.getWidth(), roof.getRoofSlope());
        }
        double verticalStartY = BluePrintData.GROUND_Y - carportWithRoofHeight;

        drawVerticalArrow(verticalStartX - 20, verticalStartY, BluePrintData.GROUND_Y, carportWithRoofHeight);
        drawVerticalArrow(verticalStartX, verticalStartY + BluePrintData.VERTICAL_SIDING_OVERLAP, BluePrintData.GROUND_Y, carportWithRoofHeight - BluePrintData.VERTICAL_SIDING_OVERLAP);
        if (!roof.getRoofType().contains("Højt tag")) {
            drawVerticalArrow(verticalStartX + carport.getLength() + BluePrintData.ARROW_OFFSET, verticalStartY - 10, BluePrintData.GROUND_Y, carportWithRoofHeight - 10);
        }
    }

    private void addHorizontalArrowsToSideView() {
        double horizontalStartX = BluePrintData.OFFSET_X;
        double horizontalStartY = BluePrintData.GROUND_Y + BluePrintData.ARROW_OFFSET;
        double roofOverhangStart = BluePrintData.POLE_START_GAP;
        double roofOverhangEnd = BluePrintData.POLE_END_GAP;
        int quantityAdjustment = -1;

        // Front overhang
        drawHorizontalArrow(horizontalStartX, horizontalStartX + roofOverhangStart, horizontalStartY, roofOverhangStart, false);
        double polesPerSide = getQuantityByPlacementDescription("Stolper nedgraves 90 cm. i jord") / 2;
        double polePlacementWidth = carport.getLength() - roofOverhangStart - roofOverhangEnd;

        if (shed != null) {
            polePlacementWidth -= shed.getLength();
            quantityAdjustment = 0;
        }
        double spaceDistance = polePlacementWidth / (polesPerSide - quantityAdjustment);
        double poleDistanceStartX = horizontalStartX + roofOverhangStart;

        for (int i = 0; i < polesPerSide - quantityAdjustment; i++) {
            drawHorizontalArrow(poleDistanceStartX, poleDistanceStartX + spaceDistance, horizontalStartY, spaceDistance, false);
            poleDistanceStartX += spaceDistance;
        }
        // Shed width
        if (shed != null) {
            double shedStartX = horizontalStartX + carport.getLength() - roofOverhangEnd - shed.getLength();
            drawHorizontalArrow(shedStartX, shedStartX + shed.getLength(), horizontalStartY, shed.getLength(), false);
        }

        // Rear overhang
        double endArrowStartX = horizontalStartX + carport.getLength() - roofOverhangEnd;
        drawHorizontalArrow(endArrowStartX, endArrowStartX + roofOverhangEnd, horizontalStartY, roofOverhangEnd, true);
    }

    private void drawHorizontalArrow(double x1, double x2, double y, double measure, boolean drawEndTick) {
        // Arrow
        svg.addArrow(x1, y, x2, y);

        // Text
        double midX = (x1 + x2) / 2;

        svg.addText(midX, y - BluePrintData.TEXT_OFFSET, 0, String.format("%.1f cm", measure));

        // Start tick
        svg.addLine(x1, y - BluePrintData.HALF_TICK_SIZE, x1, y + BluePrintData.HALF_TICK_SIZE);

        // End tick
        if (drawEndTick) {
            svg.addLine(x2, y - BluePrintData.HALF_TICK_SIZE, x2, y + BluePrintData.HALF_TICK_SIZE);
        }
    }

    private void drawVerticalArrow(double x, double yTop, double yBottom, double measure) {
        // Arrow
        svg.addArrow(x, yTop, x, yBottom);

        // Text
        double midY = (yTop + yBottom) / 2;
        svg.addText(x - BluePrintData.TEXT_OFFSET, midY, -90, String.format("%.1f cm", measure));
        // Top tick
        svg.addLine(x - BluePrintData.HALF_TICK_SIZE, yTop, x + BluePrintData.HALF_TICK_SIZE, yTop);

        // Bottom tick
        svg.addLine(x - BluePrintData.HALF_TICK_SIZE, yBottom, x + BluePrintData.HALF_TICK_SIZE, yBottom);
    }

    private double getQuantityByPlacementDescription(String placementDescription) {
        return productsPartsListEntries.stream()
                .filter(x -> x.getPlacementDescription().contains(placementDescription))
                .map(ProductsPartsListEntry::getQuantity)
                .findFirst()
                .orElse(0.0);
    }

    public double calculateRoofHeight(double width, double roofSlope) {
        double run = width / 2.0;
        double angleInRadians = Math.toRadians(roofSlope);
        return run * Math.tan(angleInRadians);
    }
}