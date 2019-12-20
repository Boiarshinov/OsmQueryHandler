package geojson;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("LineString")
public class LineString implements GeoJson {
    private Coordinate[] coordinates;


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

    @Override
    public double getArea() {
        return 0;
    }

    public void setCoordinates(double[][] coordinates) {
        this.coordinates = new Coordinate[coordinates.length];
        for (int i = 0; i < coordinates.length; i++) {
            this.coordinates[i] = new Coordinate(coordinates[i]);
        }
    }
}
