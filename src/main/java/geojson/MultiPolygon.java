package geojson;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import java.util.Arrays;

@JsonTypeName("MultiPolygon")
public class MultiPolygon implements GeoJson{
    @JsonProperty("coordinates")
    public Polygon[] polygons;

    public MultiPolygon(){ }

    public MultiPolygon(Polygon[] polygons) {
        this.polygons = polygons;
    }

    public void setPolygons(double[][][][] polygons) {
        this.polygons = new Polygon[polygons.length];
        for (int i = 0; i < polygons.length; i++) {
            this.polygons[i] = new Polygon(polygons[i]);
        }
    }

    @Override
    public Coordinate getCenter() {
        double sumArea = 0.0;
        double longitude = 0.0;
        double latitude = 0.0;
        for (Polygon polygon : polygons){
            double area = polygon.getArea();
            Coordinate barycenter = polygon.getCenter();
            longitude += barycenter.longitude * area;
            latitude += barycenter.latitude * area;
            sumArea += area;
        }

        longitude /= sumArea;
        latitude /= sumArea;

        return new Coordinate(longitude, latitude);
    }

    @Override
    public double getArea() {
        return Arrays.stream(polygons).mapToDouble(Polygon::getArea).sum();
    }

    @Override
    public Coordinate[] getCoordinateArray() {
        return findBiggestPolygon().getCoordinateArray();
    }

    private Polygon findBiggestPolygon(){
        int id = 0;
        double maxArea = 0.0;
        for (int i = 0; i < polygons.length; i++) {
            if (polygons[i].getArea() > maxArea) id = i;
        }
        return polygons[id];
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("GeoJson: {\n").append("\ttype: Multipolygon,\n").append("\tcoordinates:\n");
        result.append("\t\t[\n");
        for (Polygon polygon : polygons) {
            result.append(polygon);
        }
        result.append("\t\t]\n");
        result.append("}\n");
        return result.toString();
    }
}
