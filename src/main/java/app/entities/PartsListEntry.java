package app.entities;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PartsListEntry {
    Product product;
    double quantity;
}
