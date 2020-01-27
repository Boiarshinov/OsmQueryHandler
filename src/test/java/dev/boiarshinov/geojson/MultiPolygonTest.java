package dev.boiarshinov.geojson;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MultiPolygonTest {
    private static MultiPolygon testMultiPolygon;

    @BeforeAll
    static void setUpClass(){
        double[][][] firstPolygonArray = {{{0.0, 0.0},{20.0, 0.0},{20.0, 20.0},{0.0, 20.0},{0.0, 0.0}}};
        double[][][] secondPolygonArray = {{{0.0, 0.0}, {-20.0, 0.0}, {-20.0, -20.0}, {0.0, -20.0}, {0.0, 0.0}}};
        double[][][][] arrForMultiPolygon = {firstPolygonArray, secondPolygonArray};
        testMultiPolygon = new MultiPolygon();
        testMultiPolygon.setPolygons(arrForMultiPolygon);
    }

    @Test
    void getCenter() {
        Coordinate expected = new Coordinate(0.0, 0.0);
        Coordinate actual = testMultiPolygon.getCenter();
        assertEquals(expected, actual);
    }

    @Test
    void getArea() {
        double expected = 800.0;
        double actual = testMultiPolygon.getArea();
        assertEquals(expected, actual);
    }
}