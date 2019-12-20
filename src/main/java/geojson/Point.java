package geojson;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("Point")
public class Point implements GeoJson{
    @JsonProperty("coordinates")
    private Coordinate coordinate;

    public Point() {
    }

    public void setCoordinate(double[] coordinate) {
        this.coordinate = new Coordinate(coordinate);
    }

    @Override
    public Coordinate getCenter() {
        return coordinate;
    }

    @Override
    public double getArea() {
        return 0;
    }

    @Override
    public Coordinate[] getCoordinateArray() {
        return new Coordinate[]{coordinate};
    }
}
