package persistence;

import app.entities.Product;
import app.exceptions.DatabaseException;
import app.persistence.ProductMapper;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProductMapperTest extends MapperTest{
    @Test
    void getProductByIdTest() throws DatabaseException {
        ProductMapper productMapper = new ProductMapper();

        Product expectedProduct = new Product(1,48,75,6.0,"stk","Træ & Tagplader","Stolpe 97x97 mm trykimp");
        Product actualProduct = productMapper.getProductById(1);

        assertEquals(expectedProduct.getProductId(), actualProduct.getProductId());
        assertEquals(expectedProduct.getProductGroup(), actualProduct.getProductGroup());
    }

    @Test
    void getAllProductsTest() throws DatabaseException {
        ProductMapper productMapper = new ProductMapper();
        int size = productMapper.getAllProducts().size();
        /*We know that we have 52 products in our DB*/
        assertEquals(52, size);
    }
}
