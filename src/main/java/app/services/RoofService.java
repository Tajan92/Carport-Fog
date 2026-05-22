package app.services;
import app.dto.requestDTO.RoofRequestDTO;
import app.dto.responseDTO.RoofResponseDTO;
import app.entities.Roof;
import app.exceptions.DatabaseException;
import app.persistence.RoofMapper;
import app.services.converters.RoofConverter;

public class RoofService {
    private RoofConverter roofConverter;
    private RoofMapper roofMapper;

    public RoofService(RoofMapper roofMapper) {
        this.roofConverter = new RoofConverter();
        this.roofMapper = roofMapper;
    }

    public int createRoof(RoofRequestDTO roofRequestDTO) throws DatabaseException {
        Roof roof = roofConverter.convertRoofDTOtoEntity(roofRequestDTO);
        return roofMapper.createRoof(roof);
    }

    public RoofResponseDTO getRoof(int roofId) throws DatabaseException {
        Roof roof = roofMapper.getRoofById(roofId);
        return roofConverter.convertRoofToDto(roof);
    }

    public void updateRoof(int roofId) throws DatabaseException {
        Roof roof = roofMapper.getRoofById(roofId);
        roofMapper.updateRoof(roof);
    }

    public void deleteRoof(int roofId) throws DatabaseException {
        roofMapper.deleteRoofById(roofId);
    }
}
