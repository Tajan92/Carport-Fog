package app.services.converters;
import app.dto.requestDTO.carports.CarportRequestDTO;
import app.entities.Carport;

public class CarportConverter {

    public Carport covertCarportDTOToEntity(CarportRequestDTO carportRequestDTO){
        double width = carportRequestDTO.getWidth();
        double height = carportRequestDTO.getHeight();
        double length = carportRequestDTO.getLength();
        double price = carportRequestDTO.getPrice();

        return new Carport(width, height, length, price);
    }
}
