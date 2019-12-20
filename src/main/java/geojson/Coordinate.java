package geojson;

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
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * Constructor is used by Jackson to convert double array with 2 values to Coordinate
     * @param coordinate - double array with 2 values: longitude and latitude
     */
    public Coordinate(double[] coordinate) {
        this(coordinate[0], coordinate[1]);
    }

    public double[] convertToDoubleArray() {
        return new double[]{longitude, latitude};
    }

    @Override
    public String toString() {
        return "[" + longitude + ", " + latitude + "]\n";
    }
}
