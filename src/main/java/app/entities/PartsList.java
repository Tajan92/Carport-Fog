package app.entities;
import app.persistence.PartsListMapper;

import java.util.List;

public class PartsList {
    List<PartsListEntry> partsList;

    public PartsList(List<PartsListEntry> productList) {
        this.partsList = productList;
    }

    public void addToPartList(PartsListEntry product){
        partsList.add(product);
    }
}
