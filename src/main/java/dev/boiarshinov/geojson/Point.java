package dev.boiarshinov.geojson;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * Class realise GeoJSON type <a href=https://tools.ietf.org/html/rfc7946#section-3.1.2>Point</a>.<p>
 * Point have only one Coordinate and have no area (set to zero).
 * @see Coordinate
 * @see GeoJson
 */
@JsonTypeName("Point")
public class Point implements GeoJson{
    @JsonProperty("coordinates")
    private Coordinate coordinate;

    public Point() {
    }

    /**
     * Method is used by Jackson. Convert double array to Coordinate.
     * @param coordinate - array of two doubles
     * @see Coordinate
     */
    public void setCoordinate(double[] coordinate) {
        this.coordinate = new Coordinate(coordinate);
    }

    /**
     * @return coordinates of this point
     */
    @Override
    public Coordinate getCenter() {
        return coordinate;
    }

    /**
     * @return 0
     */
    @Override
    public double getArea() {
        return 0;
    }

    /**
     * @return Coordinate array with only one element - coordinates of point
     */
    @Override
    public Coordinate[] getCoordinateArray() {
        return new Coordinate[]{coordinate};
    }
}
