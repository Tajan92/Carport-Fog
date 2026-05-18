package app.services;
import app.dto.requestDTO.carports.CarportNoShedRequestDTO;
import app.dto.requestDTO.carports.CarportRequestDTO;
import app.dto.requestDTO.carports.CarportShedRequestDTO;
import app.dto.responseDTO.carports.CarportNoShedResponseDTO;
import app.dto.responseDTO.carports.CarportShedResponseDTO;
import app.entities.Carport;
import app.entities.Roof;
import app.entities.Shed;
import app.exceptions.DatabaseException;
import app.persistence.CarportMapper;
import app.persistence.PartsListMapper;
import app.persistence.RoofMapper;
import app.persistence.ShedMapper;
import app.services.converters.CarportConverter;
import app.services.converters.RoofConverter;
import app.services.converters.ShedConverter;

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

    public int createCarportWithShed(CarportShedRequestDTO carportShedRequestDTO) throws DatabaseException {
        Carport carportNoId = carportConverter.covertCarportDTOToEntity(carportShedRequestDTO);
        Roof roof = roofConverter.convertRoofDTOtoEntity(carportShedRequestDTO.getRoofRequestDTO());
        Shed shed = shedConverter.convertShedDTOtoEntity(carportShedRequestDTO.getShedRequestDTO());
        int partsListId = partsListMapper.createPartListId();
        int roofId = roofMapper.createRoof(roof);
        int shedId = shedMapper.createShed(shed);
        int addonId = carportMapper.createAddonId(roofId, shedId);

        return carportMapper.createCarport(carportNoId, addonId, partsListId);
    }

    public int createCarportNoShed(CarportNoShedRequestDTO carportNoShedRequestDTO) throws DatabaseException {
        Carport carportNoId = carportConverter.covertCarportDTOToEntity(carportNoShedRequestDTO);
        Roof roof = roofConverter.convertRoofDTOtoEntity(carportNoShedRequestDTO.getRoofRequestDTO());
        int partsListId = partsListMapper.createPartListId();
        int roofId = roofMapper.createRoof(roof);
        int addonId = carportMapper.createAddonId(roofId, null);

        return carportMapper.createCarport(carportNoId, addonId, partsListId);
    }

    public CarportShedResponseDTO getCarportShed(int carportId) throws DatabaseException {
        Carport carport = carportMapper.getCarportById(carportId);
        return carportConverter.convertCarportShedEntityToDTO(carport);
    }

    public CarportNoShedResponseDTO getNoCarportShed(int carportId) throws DatabaseException {
        Carport carport = carportMapper.getCarportById(carportId);
        return carportConverter.convertCarportNoShedEntityToDTO(carport);
    }

    public void updateCarport(CarportRequestDTO carportRequestDTO, int carportId) throws DatabaseException {
        Carport carport = carportConverter.covertCarportDTOToEntity(carportRequestDTO);
        carport.setCarportId(carportId);
        carportMapper.updateCarportById(carport);
    }

    public void deleteCarport(int carportId) throws DatabaseException {
        carportMapper.deleteCarportById(carportId);
    }
}
