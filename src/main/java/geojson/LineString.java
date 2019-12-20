package geojson;

import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * Class realise GeoJSON type <a href=https://tools.ietf.org/html/rfc7946#section-3.1.4>LineString</a>.<p>
 * LineString usually use for streets and roads.<p>
 * LineString is a array of coordinates that form broken line.
 * @see Coordinate
 * @see GeoJson
 */
@JsonTypeName("LineString")
public class LineString implements GeoJson {
    private Coordinate[] coordinates;

    /**
     * @return Coordinate with average longitude and latitude
     */
    @Override
    public Coordinate getCenter() {
        double longitude = 0.0;
        double latitude = 0.0;
        for (Coordinate coordinate: coordinates) {
            longitude += coordinate.longitude;
            latitude += coordinate.latitude;
        }
        longitude /= coordinates.length;
        latitude /= coordinates.length;
        return new Coordinate(longitude, latitude);
    }

    /**
     * @return 0
     */
    @Override
    public double getArea() {
        return 0;
    }

    /**
     * @return Inner coordinate array
     */
    @Override
    public Coordinate[] getCoordinateArray() {
        return coordinates;
    }

    /**
     * Method is used by Jackson. Convert two-dimensional double array to Coordinate array.
     * @param coordinates - two-dimensional double array
     */
    public void setCoordinates(double[][] coordinates) {
        this.coordinates = new Coordinate[coordinates.length];
        for (int i = 0; i < coordinates.length; i++) {
            this.coordinates[i] = new Coordinate(coordinates[i]);
        }
    }
}
