package app.services;

import app.dto.requestDTO.RoofRequestDTO;
import app.dto.requestDTO.ShedRequestDTO;
import app.dto.requestDTO.carports.CarportNoShedRequestDTO;
import app.dto.requestDTO.carports.CarportRequestDTO;
import app.dto.requestDTO.carports.CarportShedRequestDTO;
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
        shedMapper.updateShed(shed);
    }

    public void deleteShed(int shedId) throws DatabaseException {
        shedMapper.deleteShed(shedId);
    }

    public CarportRequestDTO checkShed(String width, String length, String siding, boolean floor, double carportWidth, double carportHeight, double carportLength, RoofRequestDTO roofRequestDTO) {

        if (length == null || width == null || width.isBlank() || length.isBlank()) {
            return new CarportNoShedRequestDTO(carportWidth, carportHeight, carportLength, roofRequestDTO);
        } else {
            ShedRequestDTO shedRequestDTO = new ShedRequestDTO(Double.parseDouble(width), Double.parseDouble(length), siding, floor);
            return new CarportShedRequestDTO(carportWidth, carportHeight, carportLength, roofRequestDTO, shedRequestDTO);
        }
    }
}
