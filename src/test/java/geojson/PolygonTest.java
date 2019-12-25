package geojson;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class PolygonTest {
    private static Polygon testPolygon;

    @BeforeAll
    static void setUpClass(){
        double[][] arrForOuterRing = {{0.0, 0.0},{20.0, 0.0},{20.0, 20.0},{0.0, 20.0},{0.0, 0.0}};
        double[][] arrForInnerRing = {{5.0, 5.0}, {5.0, 15.0}, {15.0, 15.0}, {15.0, 5.0}, {5.0, 5.0}};
        double[][][] arrForPolygon = {arrForOuterRing, arrForInnerRing};
        testPolygon = new Polygon(arrForPolygon);
    }

    @Test
    void validateConstructorArgs() {
        double[][][] notEnoughCoordinatesArray = {{{0.0, 0.0},{20.0, 0.0},{0.0, 0.0}}};

        double[][][] firstAndLastCoordNotTheSameArray = {{{0.0, 0.0},{20.0, 0.0},{20.0, 20.0}, {0.0, 20.0}}};

        double[][] holeLinearRingArray = {{5.0, 5.0}, {5.0, 15.0}, {15.0, 15.0}, {15.0, 5.0}, {5.0, 5.0}};
        double[][] outerLinearRingArray = {{0.0, 0.0},{20.0, 0.0},{20.0, 20.0},{0.0, 20.0},{0.0, 0.0}};
        double[][][] firstLinearRingIsHoleArray = {holeLinearRingArray, outerLinearRingArray};

        assertThrows(IllegalArgumentException.class, () -> new Polygon(notEnoughCoordinatesArray),
                "LinearRing should have at least 4 coordinates");
        assertThrows(IllegalArgumentException.class, () -> new Polygon(firstAndLastCoordNotTheSameArray),
                "First and Last coordinates of LinearRing should be the same");
        assertThrows(IllegalArgumentException.class, () -> new Polygon(firstLinearRingIsHoleArray),
                "First LinearRing should be outer and others should be holes");
    }

    @Test
    void getCenter() {
        Coordinate expected = new Coordinate(10.0, 10.0);
        Coordinate actual = testPolygon.getCenter();
        assertEquals(expected, actual);
    }

    @Test
    void getArea() {
        double expected = 300d;
        double actual = testPolygon.getArea();
        assertEquals(expected, actual);
    }
}