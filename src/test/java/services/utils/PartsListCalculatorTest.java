package services.utils;

import app.exceptions.CalculatorException;
import app.services.utils.PartsListCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import persistence.MapperTest;

public class PartsListCalculatorTest extends MapperTest {
    private PartsListCalculator partsListCalculator;

    @BeforeEach
    public void setUp() {
        partsListCalculator = new PartsListCalculator();
    }

    @Test
    public void calculatePartsList() throws CalculatorException {
        partsListCalculator.createProductsPartsList()

    }
}
