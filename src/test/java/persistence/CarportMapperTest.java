package persistence;
import app.entities.Carport;
import app.exceptions.DatabaseException;
import app.persistence.CarportMapper;
import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CarportMapperTest extends MapperTest {

    @Test
    void createCarportTest() throws DatabaseException {
        CarportMapper carportMapper = new CarportMapper();

        try (Connection connection = connectionPool.getConnection()) {
            try (Statement statement = connection.createStatement()) {
                statement.execute("INSERT INTO test.parts_lists DEFAULT VALUES; -- parts_list_id = 7 ");
                statement.execute("INSERT INTO test.products_parts_lists (product_id, parts_list_id, quantity) VALUES" +
                        "                                (1,  7, 6),\n" +
                        "                                (2,  7, 10),\n" +
                        "                                (3,  7, 2),\n" +
                        "                                (4,  7, 4),\n" +
                        "                                (5,  7, 1),\n" +
                        "                                (6,  7, 12),\n" +
                        "                                (7,  7, 10),\n" +
                        "                                (8,  7, 10),\n" +
                        "                                (9,  7, 100),\n" +
                        "                                (10, 7, 100),\n" +
                        "                                (11, 7, 4);");
                statement.execute("INSERT INTO test.addons (shed_id, roof_id) VALUES" +
                        "                                       (null, null);   -- shed + roof");

                Carport carport = new Carport(3.00, 2.30, 6.30, 20000.00, 7, 0, 0);

                int carportId = carportMapper.createCarport(carport, 7, 7);

                //We know the 7th entry will have id = 7
                assertEquals(7, carportId);

            }
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    @Test
    void getCarportByIdTest() throws DatabaseException {
        CarportMapper carportMapper = new CarportMapper();
        Carport expectedCarport = new Carport(1, 3.00, 2.08, 5.00, 24999.00, 1, 0, 0);

        Carport actualCarport = carportMapper.getCarportById(1, 1, 0, 0);

        assertEquals(expectedCarport.getCarportId(), actualCarport.getCarportId());
    }

    @Test
    void updateCarportByIdTest() throws DatabaseException {
        CarportMapper carportMapper = new CarportMapper();
        Carport expectedCarport = new Carport(1, 4.00, 2.50, 5.00, 23000, 1, 0, 0);

        carportMapper.updateCarportById(expectedCarport, 1);
        Carport actualCarport = carportMapper.getCarportById(1, 1, 0, 0);

        assertEquals(expectedCarport.getCarportId(), actualCarport.getCarportId());
    }

    @Test
    void deleteCarportByIdTest() throws DatabaseException {
        CarportMapper carportMapper = new CarportMapper();
        carportMapper.deleteCarportById(1);
        int size = carportMapper.getAllCarports().size();

        assertEquals(5,size);
    }

    @Test
    void getAllCarportTest() throws DatabaseException {
        CarportMapper carportMapper = new CarportMapper();
        int size = carportMapper.getAllCarports().size();

        assertEquals(6,size);
    }
}
