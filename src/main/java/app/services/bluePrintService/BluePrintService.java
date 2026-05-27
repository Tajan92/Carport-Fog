package app.services.bluePrintService;

import app.dto.requestDTO.carports.CarportRequestDTO;
import app.dto.requestDTO.carports.CarportShedRequestDTO;
import app.entities.*;
import app.exceptions.CalculatorException;
import app.exceptions.DatabaseException;
import app.persistence.ProductMapper;
import app.services.converters.CarportConverter;
import app.services.converters.RoofConverter;
import app.services.converters.ShedConverter;
import app.services.utils.PartsListCalculator;
import app.services.utils.Svg;
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
        svg.addArrow(BluePrintData.OFFSET_X, 650, 650, 650);
        svg.addText(350, 640, 0, carport.getLength() + " cm");
    }

    private void addVerticalArrowsToSideView() {
        drawVerticalArrow(40, 70, 300, 230);
        drawVerticalArrow(70, 90, 300, 210);
        drawVerticalArrow(800, 70, 300, 230);
    }

    private void addHorizontalArrowsToSideView() {
        double horizontalStartX = 0.0;// TODO: Find positionsCoordinate
        double horizontalStartY = 0.0; // TODO: Find positionsCoordinate
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

        svg.addText(midX, y - BluePrintData.TEXT_OFFSET, 0, measure + " cm");

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
        svg.addText(x - BluePrintData.TEXT_OFFSET, midY, -90, measure + " cm");
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
}