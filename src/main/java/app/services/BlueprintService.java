package app.services;

import app.dto.requestDTO.carports.CarportRequestDTO;
import app.dto.requestDTO.carports.CarportShedRequestDTO;
import app.entities.*;
import app.exceptions.CalculatorException;
import app.exceptions.DatabaseException;
import app.persistence.ProductMapper;
import app.services.utils.bluePrintMaker.*;
import app.services.converters.CarportConverter;
import app.services.converters.RoofConverter;
import app.services.converters.ShedConverter;
import app.services.utils.PartsListCalculator;

import java.util.List;

public class BlueprintService {
    private Svg svg;
    private Carport carport;
    private Shed shed;
    private Roof roof;
    private List<Product> products;
    private List<ProductsPartsListEntry> productsPartsListEntries;
    private BlueprintSideView sideView;
    private BluePrintTopView topView;
    private BluePrintMeasure bluePrintMeasure;
    private PartsListCalculator partsListCalculator;
    private CarportConverter carportConverter;
    private RoofConverter roofConverter;
    private ShedConverter shedConverter;
    private ProductMapper productMapper;

    public BlueprintService(ProductMapper productMapper) {
        this.carportConverter = new CarportConverter();
        this.roofConverter = new RoofConverter();
        this.shedConverter = new ShedConverter();
        this.partsListCalculator = new PartsListCalculator();
        this.sideView = new BlueprintSideView();
        this.topView = new BluePrintTopView();
        this.bluePrintMeasure = new BluePrintMeasure();
        this.productMapper = new ProductMapper();
    }

    public String createBlueprint(CarportRequestDTO carportRequestDTO) throws CalculatorException, DatabaseException {
        this.products = productMapper.getAllProducts();
        this.carport = carportConverter.covertCarportDTOToEntity(carportRequestDTO);
        this.roof = roofConverter.convertRoofDTOtoEntity(carportRequestDTO.getRoofRequestDTO());

        if (carportRequestDTO instanceof CarportShedRequestDTO carportShedRequestDTO) {
            this.shed = shedConverter.convertShedDTOtoEntity(carportShedRequestDTO.getShedRequestDTO());
        } else {
            this.shed = null;
        }

        this.productsPartsListEntries = partsListCalculator.createProductsPartsList(carport, shed, roof, products);
        double roofHeight = RoofHeightCalculator.calculateRoofHeight(carport.getWidth(), roof.getRoofSlope());
        double viewHeight = (carport.getHeight()*2)+ roofHeight + 600;
        double viewWidth = carport.getWidth()+300;
        svg = new Svg(0, 0, "100%", "auto", "0 -"+roofHeight+ " " + viewWidth + " " + viewHeight, 0);

        // SIDE VIEW
        svg.startGroup(BluePrintData.OFFSET_X, BluePrintData.OFFSET_Y_SIDE);
        sideView.addDrawing(svg, carport, shed, roof, productsPartsListEntries);
        svg.endGroup();

        // TOP VIEW
        svg.startGroup(BluePrintData.OFFSET_X, BluePrintData.OFFSET_Y_TOP);
        topView.addDrawing(svg, carport, shed, roof, productsPartsListEntries);
        svg.endGroup();

        // Arrows
        bluePrintMeasure.addDrawing(svg, carport, shed, roof, productsPartsListEntries);

        return svg.toString();
    }


}