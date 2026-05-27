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

        Svg svg = new Svg(0, 0, "1400", "1200",
                "0 0 1400 1200", 1);

        // SIDE VIEW
        svg.startGroup(80, 10);
        sideView.addDrawing(svg, carport, shed, roof, productsPartsListEntries);
        svg.endGroup();

        // TOP VIEW
        svg.startGroup(80, 580);
        topView.addDrawing(svg, carport, shed, roof, productsPartsListEntries);
        svg.endGroup();

        // Measurements/arrows can be added here
        addMeasurements(svg, carport);

        return svg.toString();
    }

    private void addMeasurements(Svg svg, Carport carport) {

        svg.addArrow(50, 650, 650, 650);

        svg.addText(
                350,
                640,
                0,
                carport.getLength() + " cm"
        );
    }
}