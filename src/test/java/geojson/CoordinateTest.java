package geojson;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CoordinateTest {
    @Test
    void validateConstructorArgs(){
        double[] notEnoughArgs = {1.0};
        double[] tooMuchArgs = {1.0, 2.0, 3.0};

        assertThrows(IllegalArgumentException.class, () -> new Coordinate(notEnoughArgs));
        assertThrows(IllegalArgumentException.class, () -> new Coordinate(tooMuchArgs));
    }
}