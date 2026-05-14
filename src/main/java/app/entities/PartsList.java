package app.entities;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor

public class PartsList {
    private int productPartsListsId;
    private int productId;
    private int partsListId;
    private double quantity;
}
