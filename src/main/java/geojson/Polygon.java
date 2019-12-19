package geojson;

import java.util.Arrays;

public class Polygon implements Polygonable{
    public LinearRing[] linearRings;
    private double area;
    private Coordinate barycenter;

    public Polygon(LinearRing[] linearRings) {
        this.linearRings = linearRings;
    }

    public Polygon(double[][][] linearRings) {
        this.linearRings = new LinearRing[linearRings.length];
        for (int i = 0; i < linearRings.length; i++) {
            this.linearRings[i] = new LinearRing(linearRings[i]);
        }
    }

    @Override
    public Coordinate getCenter() {
        double sumArea = 0.0;
        double longitude = 0.0;
        double latitude = 0.0;
        for (LinearRing linearRing : linearRings) {
            double area = linearRing.getArea();
            Coordinate barycenter = linearRing.getBarycenter();
            longitude += barycenter.longitude * area;
            latitude += barycenter.latitude * area;
            sumArea += area;
        }

        longitude /= sumArea;
        latitude /= sumArea;

        return new Coordinate(longitude, latitude);
    }

    @Override
    public double getArea(){
        return Arrays.stream(linearRings).mapToDouble(LinearRing::getArea).sum();
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("\t\t\t[\n");
        for (LinearRing linearRing : linearRings) {
            result.append(linearRing);
        }
        result.append("\t\t\t]\n");
        return result.toString();
    }

    public class LinearRing {
        Coordinate[] coordinates;
        private double area;
        private Coordinate barycenter;

        public LinearRing(Coordinate[] coordinates) {
            this.coordinates = coordinates;
        }

        public LinearRing(double[][] coordinates) {
            this.coordinates = new Coordinate[coordinates.length];
            for (int i = 0; i < coordinates.length; i++) {
                this.coordinates[i] = new Coordinate(coordinates[i]);
            }
        }

        public double getArea() {
            if (coordinates.length < 3) return 0.0;
            area = 0.0;
            for (int i = 0; i < coordinates.length - 1; i++) {
                area += coordinates[i].longitude * coordinates[i + 1].latitude -
                        coordinates[i + 1].longitude * coordinates[i].latitude;
            }
            area /= 2;
            return area;
        }

        /**
         * <p>Calculate barycenter of polygon by <a href="https://ru.wikipedia.org/wiki/%D0%91%D0%B0%D1%80%D0%B8%D1%86%D0%B5%D0%BD%D1%82%D1%80#%D0%91%D0%B0%D1%80%D0%B8%D1%86%D0%B5%D0%BD%D1%82%D1%80_%D0%BC%D0%BD%D0%BE%D0%B3%D0%BE%D1%83%D0%B3%D0%BE%D0%BB%D1%8C%D0%BD%D0%B8%D0%BA%D0%B0">this</a>
         * formula</p>
         */
        public Coordinate getBarycenter() {
            getArea();
            double longitude = 0.0;
            double latitude = 0.0;
            for (int i = 0; i < coordinates.length - 1; i++) {
                longitude += (coordinates[i].longitude + coordinates[i + 1].longitude) *
                        (coordinates[i].longitude * coordinates[i + 1].latitude -
                                coordinates[i + 1].longitude * coordinates[i].latitude);
                latitude += (coordinates[i].latitude + coordinates[i + 1].latitude) *
                        (coordinates[i].longitude * coordinates[i + 1].latitude -
                                coordinates[i + 1].longitude * coordinates[i].latitude);
            }
            longitude /= 6 * area;
            latitude /= 6 * area;
            barycenter = new Coordinate(longitude, latitude);
            return barycenter;
        }

        public double[][] convertToDoubleArray() {
            double[][] result = new double[coordinates.length][2];
            for (int i = 0; i < result.length; i++) {
                result[i] = coordinates[i].convertToDoubleArray();
            }
            return result;
        }

        @Override
        public String toString() {
            StringBuilder result = new StringBuilder();
            result.append("\t\t\t\t[\n");
            for (Coordinate coordinate : coordinates) {
                result.append("\t\t\t\t\t").append(coordinate);
            }
            result.append("\t\t\t\t]\n");
            return result.toString();
        }
    }
}
