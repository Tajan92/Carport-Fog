package app.services.converters;
import app.dto.requestDTO.carports.CarportRequestDTO;
import app.dto.responseDTO.RoofResponseDTO;
import app.dto.responseDTO.ShedResponseDTO;
import app.dto.responseDTO.carports.CarportNoShedResponseDTO;
import app.dto.responseDTO.carports.CarportResponseDTO;
import app.dto.responseDTO.carports.CarportShedResponseDTO;
import app.entities.Carport;
import app.entities.Roof;
import app.entities.Shed;
import app.exceptions.DatabaseException;
import app.persistence.RoofMapper;
import app.persistence.ShedMapper;

public class CarportConverter {

    public Carport covertCarportDTOToEntity(CarportRequestDTO carportRequestDTO){
        double width = carportRequestDTO.getWidth();
        double height = carportRequestDTO.getHeight();
        double length = carportRequestDTO.getLength();

        return new Carport(width, height, length);
    }

    public CarportNoShedResponseDTO convertCarportNoShedEntityToDTO(Carport carport) throws DatabaseException {
        int id = carport.getCarportId();
        double width = carport.getWidth();
        double height = carport.getHeight();
        double length = carport.getLength();
        double price = carport.getPrice();

        return new CarportNoShedResponseDTO(id, width, height, length, price);
    }

    public CarportShedResponseDTO convertCarportShedEntityToDTO(Carport carport) throws DatabaseException {
        int id = carport.getCarportId();
        double width = carport.getWidth();
        double height = carport.getHeight();
        double length = carport.getLength();
        double price = carport.getPrice();

        return new CarportShedResponseDTO(id, width, height, length, price);
    }
}
