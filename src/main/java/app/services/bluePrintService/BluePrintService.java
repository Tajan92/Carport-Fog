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
        sideView = new BlueprintSideView();
        topView = new BluePrintTopView();
    }

    public String createBlueprint(CarportRequestDTO carportRequestDTO) throws CalculatorException {
        this.carport = carportConverter.covertCarportDTOToEntity(carportRequestDTO);
        this.roof = roofConverter.convertRoofDTOtoEntity(carportRequestDTO.getRoofRequestDTO());

        if (carportRequestDTO instanceof CarportShedRequestDTO carportShedRequestDTO) {
            shed = shedConverter.convertShedDTOtoEntity(carportShedRequestDTO.getShedRequestDTO());
        } else {
            shed = null;
        }

        this.productsPartsListEntries = partsListCalculator.createProductsPartsList(carport, shed, roof, products);

        svg = new Svg(0, 0, "1400", "1200",
                "0 0 1100 1100", 1);

        // SIDE VIEW
        svg.startGroup(80, 10);
        sideView.addDrawing(svg, carport, shed, roof, productsPartsListEntries);
        svg.endGroup();

        // TOP VIEW
        svg.startGroup(80, 580);
        topView.addDrawing(svg, carport, shed, roof, productsPartsListEntries);
        svg.endGroup();

        // Measurements/arrows can be added here
        addMeasurements();

        return svg.toString();
    }

    private void addMeasurements() {

        svg.addArrow(80, 650, 650, 650);

        svg.addText(
                350,
                640,
                0,
                carport.getLength() + " cm"
        );
    }

    private void addArrowsToSideView() {
        // Horizontal Arrows
        double horizontalStartX = 0.0;
        double horizontalStartY = 0.0;
        double roofOverhangStart = 100.0;
        double roofOverhangEnd = 35.0;
        double polesPerSide = getQuantityByPlacementDescription("Stolper nedgraves 90 cm. i jord") / 2; //2 sides

        drawHorizontalArrow(horizontalStartX, horizontalStartX + roofOverhangStart, horizontalStartY, roofOverhangStart, false);


//         <!--Horizontal Arrow - Side view-->
//                <line x1="80" y1="130" x2="80" y2="340" style="stroke: #006600;" />
//
//                <line x1="80" y1="330" x2="190" y2="330"
//        style="stroke: #006600;  marker-start: url(#beginArrow); marker-end: url(#endArrow);" />
//                <text style="text-anchor: middle" transform="translate(135,350)">1,10</text>
//
//                <line x1="190" y1="320" x2="190" y2="340" style="stroke: #006600;" />
//
//                <line x1="190" y1="330" x2="490" y2="330"
//        style="stroke: #006600;  marker-start: url(#beginArrow); marker-end: url(#endArrow);" />
//                <text style="text-anchor: middle" transform="translate(340,350)">3,00</text>
//
//                <line x1="490" y1="320" x2="490" y2="340" style="stroke: #006600;" />
//
//                <line x1="490" y1="330" x2="790" y2="330"
//        style="stroke: #006600;  marker-start: url(#beginArrow); marker-end: url(#endArrow);" />
//                <text style="text-anchor: middle" transform="translate(640,350)">3,00</text>
//
//                <line x1="790" y1="320" x2="790" y2="340" style="stroke: #006600;" />
//
//                <line x1="790" y1="330" x2="859.5" y2="330"
//        style="stroke: #006600;  marker-start: url(#beginArrow); marker-end: url(#endArrow);" />
//                <text style="text-anchor: middle" transform="translate(824.75,350)">0,70</text>
//
//                <line x1="859.5" y1="145" x2="859.5" y2="340" style="stroke: #006600;" />
    }

    private void drawHorizontalArrow(double x1, double x2, double y, double measure, boolean endVerticalEndLine) {
        svg.addArrow(x1, y, x2, y);
        svg.addText((x1 + x2) / 2, y - 10, 0, measure + " cm");
        svg.addLine(x1, y - 10, x1, y + 10);

        if (endVerticalEndLine == true) {
            svg.addLine(x2, y - 10, x2, y + 10);
        }
    }

    private void drawVerticalArrow(double x, double y1, double y2, double measure, boolean endHorizontalEndLine) {
        svg.addArrow(x, y1, x, y2);
        svg.addText(x - 10, (y1 + y2) / 2, -90, measure + " cm");
        svg.addLine(x - 10, y1, x + 10, y2);

        if (endHorizontalEndLine == true) {
            svg.addLine(x - 10, y1, x + 10, y2);
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