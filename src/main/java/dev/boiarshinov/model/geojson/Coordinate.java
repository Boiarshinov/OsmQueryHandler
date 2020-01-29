package dev.boiarshinov.model.geojson;

import java.util.Objects;

/**
 * Class is represent two-dimensional coordinate on earth surface.<p>
 * Coordinate have two parameters: longitude and latitude.<p>
 * Class very useful for GeoJSON objects
 * @see GeoJson
 */
public class Coordinate {
    public double latitude;
    public double longitude;

    public Coordinate(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    /**
     * Constructor is used by Jackson to convert double array with 2 values to Coordinate
     * @param coordinate - double array with 2 values: longitude and latitude
     */
    public Coordinate(double[] coordinate) {
        validateArgs(coordinate);
        this.longitude = coordinate[0];
        this.latitude = coordinate[1];
    }

    private void validateArgs(double[] coordinate){
        if (coordinate.length != 2) {
            throw new IllegalArgumentException("Should be two arguments. Actual amount: " + coordinate.length);
        }
    }

    public double[] convertToDoubleArray() {
        return new double[]{longitude, latitude};
    }

    @Override
    public String toString() {
        return "[" + longitude + ", " + latitude + "]\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinate that = (Coordinate) o;
        return Double.compare(that.latitude, latitude) == 0 &&
                Double.compare(that.longitude, longitude) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(latitude, longitude);
    }
}
