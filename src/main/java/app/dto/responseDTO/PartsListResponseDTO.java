package app.dto.responseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
public class PartsListResponseDTO {
    private int partsListId;
    private List<ProductsPartsListEntryResponseDTO> partsListEntries;

    public PartsListResponseDTO(int partsListId){
        this.partsListId = partsListId;
    }
}
