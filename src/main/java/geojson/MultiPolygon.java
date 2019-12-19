package geojson;

import java.util.Arrays;

public class MultiPolygon implements Polygonable{
    public Polygon[] polygons;

    public MultiPolygon(Polygon[] polygons) {
        this.polygons = polygons;
    }

    public MultiPolygon(double[][][][] polygons) {
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
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("\t\t[\n");
        for (Polygon polygon : polygons) {
            result.append(polygon);
        }
        result.append("\t\t]\n");

        return result.toString();
    }
}
