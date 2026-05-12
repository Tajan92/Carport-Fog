package app.entities;
import java.util.List;

public class PartsList {
    List<Product> partsList;

    public PartsList(List<Product> partsList) {
        this.partsList = partsList;
    }

    public void addToPartList(Product product){
        partsList.add(product);
    }
}
