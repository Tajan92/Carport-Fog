package app.entities;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor

public class PartsList {
    private int partsListId;
    private List<ProductsPartsListEntry> partsListEntries;
}
