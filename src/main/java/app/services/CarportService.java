package app.services;
import app.dto.requestDTO.carports.CarportShedRequestDTO;
import app.entities.Carport;
import app.entities.Roof;
import app.entities.Shed;
import app.exceptions.DatabaseException;
import app.persistence.CarportMapper;
import app.persistence.PartsListMapper;
import app.persistence.RoofMapper;
import app.persistence.ShedMapper;
import app.services.converters.CarportConverter;

public class CarportService {
    private CarportConverter carportConverter;
    private ShedConverter shedConverter;
    private RoofConverter roofConverter;
    private PartsListMapper partsListMapper;
    private CarportMapper carportMapper;
    private RoofMapper roofMapper;
    private ShedMapper shedMapper;

    public CarportService(PartsListMapper partsListMapper, CarportMapper carportMapper, RoofMapper roofMapper, ShedMapper shedMapper) {
        this.carportConverter = new CarportConverter();
        this.shedConverter = new ShedConverter();
        this.roofConverter = new RoofConverter();
        this.partsListMapper = partsListMapper;
        this.carportMapper = carportMapper;
        this.roofMapper = roofMapper;
        this.shedMapper = shedMapper;
    }

    public void createCarportWithShed(CarportShedRequestDTO carportShedRequestDTO) throws DatabaseException {
        Carport carportNoId = carportConverter.covertCarportDTOToEntity(carportShedRequestDTO);
        Roof roof = roofConverter.convertRoofDTOToEntity(carportShedRequestDTO.getShedRequestDTO());
        Shed shed = shedConverter.convertShedDTOToEntity(carportShedRequestDTO.getShedRequestDTO());
        int partsListId = partsListMapper.createPartListId();
        int roofId = roofMapper.createRoof(roof);
        int shedId = shedMapper.createShed(shed);
        int addonId = carportMapper.createAddonId(roofId, shedId);
        carportMapper.createCarport(carportNoId, addonId, partsListId);

    }

    public void createCarportNoShed(CarportShedRequestDTO carportShedRequestDTO) throws DatabaseException{
        Carport carportNoId = carportConverter.covertCarportDTOToEntity(carportShedRequestDTO);
        Roof roof = roofConverter.convertRoofDTOToEntity(carportShedRequestDTO.getShedRequestDTO());
        int partsListId = partsListMapper.createPartListId();
        int roofId = partsListMapper.createPartListId();
        int addonId = carportMapper.createAddonId(roofId, null);
        carportMapper.createCarport(carportNoId, addonId, partsListId);
    }
}
