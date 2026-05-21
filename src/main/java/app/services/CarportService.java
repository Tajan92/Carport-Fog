package app.services;
import app.dto.requestDTO.carports.CarportRequestDTO;
import app.dto.requestDTO.carports.CarportShedRequestDTO;
import app.dto.responseDTO.RoofResponseDTO;
import app.dto.responseDTO.ShedResponseDTO;
import app.dto.responseDTO.carports.CarportNoShedResponseDTO;
import app.dto.responseDTO.carports.CarportResponseDTO;
import app.dto.responseDTO.carports.CarportShedResponseDTO;
import app.entities.Carport;
import app.entities.Roof;
import app.entities.Shed;
import app.exceptions.CalculatorException;
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
    private PartsListService partsListService;

    public CarportService(PartsListMapper partsListMapper, CarportMapper carportMapper, RoofMapper roofMapper, ShedMapper shedMapper) {
        this.carportConverter = new CarportConverter();
        this.shedConverter = new ShedConverter();
        this.roofConverter = new RoofConverter();
        this.partsListMapper = partsListMapper;
        this.carportMapper = carportMapper;
        this.roofMapper = roofMapper;
        this.shedMapper = shedMapper;
    }

    public int createCarport(CarportRequestDTO carportRequestDTO) throws DatabaseException, CalculatorException {
        Carport carport = carportConverter.covertCarportDTOToEntity(carportRequestDTO);
        Integer shedId = null;
        Roof roof = roofConverter.convertRoofDTOtoEntity(carportRequestDTO.getRoofRequestDTO());

        //if the carport has a shed dto, a not null id needs to be created for shed in db
        if (carportRequestDTO instanceof CarportShedRequestDTO withShed){
            Shed shed = shedConverter.convertShedDTOtoEntity(withShed.getShedRequestDTO());
            shedId = shedMapper.createShed(shed);
        }
        int roofId = roofMapper.createRoof(roof);
        int partsListId = partsListMapper.createPartListId();
        int addonId = carportMapper.createAddonId(roofId, shedId);

        //create the parts list in database but don't return anything
        partsListService.createProductsPartsListEntries(carportRequestDTO);

        return carportMapper.createCarport(carport, addonId, partsListId);
    }

    public CarportResponseDTO getCarport(int carportId) throws DatabaseException {
        Carport carport = carportMapper.getCarportById(carportId);
        Roof roof = roofMapper.getRoofById(carport.getRoofId());
        RoofResponseDTO roofResponseDTO = roofConverter.convertRoofToDto(roof);

        //return different DTO based on shed or no shed
        if (carport.getShedId() != null){
            Shed shed = shedMapper.getShedById(carport.getShedId());
            ShedResponseDTO shedResponseDTO = shedConverter.convertShedToDto(shed);
            CarportShedResponseDTO carportShedResponseDTO = carportConverter.convertCarportShedEntityToDTO(carport);
            carportShedResponseDTO.setShedResponseDTO(shedResponseDTO);
            carportShedResponseDTO.setRoofResponseDTO(roofResponseDTO);

            return carportShedResponseDTO;
        } else {
            CarportNoShedResponseDTO carportNoShedResponseDTO = carportConverter.convertCarportNoShedEntityToDTO(carport);
            carportNoShedResponseDTO.setRoofResponseDTO(roofResponseDTO);

            return carportNoShedResponseDTO;
        }
    }

    public void updateCarport(int carportId, CarportRequestDTO carportRequestDTO) throws DatabaseException {
        Carport carport = carportConverter.covertCarportDTOToEntity(carportRequestDTO);
        carport.setCarportId(carportId);
        carportMapper.updateCarport(carport);
    }

    public void deleteCarport(int carportId) throws DatabaseException {
        Carport carport = carportMapper.getCarportById(carportId);
        partsListMapper.deleteAllProductPartsListById(carport.getPartsListId());
        carportMapper.deleteCarportById(carportId);
        partsListMapper.deletePartsListById(carport.getPartsListId());
        carportMapper.deleteAddonId(carport.getRoofId(), carport.getShedId());
        shedMapper.deleteShed(carport.getShedId());
        roofMapper.deleteRoofById(carport.getRoofId());
    }
}