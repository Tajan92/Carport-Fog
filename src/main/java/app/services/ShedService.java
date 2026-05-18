package app.services;

import app.dto.requestDTO.ShedRequestDTO;
import app.dto.responseDTO.ShedResponseDTO;
import app.entities.Shed;
import app.exceptions.DatabaseException;
import app.persistence.ShedMapper;
import app.services.converters.ShedConverter;

public class ShedService {
    private ShedConverter shedConverter;
    private ShedMapper shedMapper;

    public ShedService(ShedMapper shedMapper) {
        this.shedConverter = new ShedConverter();
        this.shedMapper = shedMapper;
    }

    public int createShed(ShedRequestDTO shedRequestDTO) throws DatabaseException {
        Shed shed = shedConverter.convertShedDTOtoEntity(shedRequestDTO);
        return shedMapper.createShed(shed);
    }

    public ShedResponseDTO getShed(int shedId) throws DatabaseException {
        Shed shed = shedMapper.getShedById(shedId);
        return shedConverter.convertShedToDto(shed);
    }

    public void updateShed(int shedId) throws DatabaseException {
        Shed shed = shedMapper.getShedById(shedId);
        shedMapper.updateShedById(shed);
    }

    public void deleteShed(int shedId) throws DatabaseException {
        shedMapper.deleteShed(shedId);
    }
}
