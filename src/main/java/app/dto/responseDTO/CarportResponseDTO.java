package app.dto.responseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CarportResponseDTO {
    private int id;
    private double width;
    private double height;
    private double length;
    private double price;
    private int partsListId;
    private int shedId;
    private int roofId;
}
