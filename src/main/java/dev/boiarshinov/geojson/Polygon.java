package dev.boiarshinov.geojson;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import java.util.Arrays;

/**
 * Class realise GeoJSON type <a href=https://tools.ietf.org/html/rfc7946#section-3.1.6>Polygon</a>.<p>
 * Polygon is used to show some areas. These areas can be with holes inside them.<p>
 * dev.boiarshinov.Main area and holes submitted by Linear Rings.
 * @see LinearRing
 * @see Coordinate
 * @see GeoJson
 */
@JsonTypeName("Polygon")
public class Polygon implements GeoJson{
    @JsonProperty("coordinates")
    public LinearRing[] linearRings;

    /**
     * Blank constructor for Jackson
     */
    public Polygon(){ }

    public Polygon(LinearRing[] linearRings) {
        this.linearRings = linearRings;
    }

    public Polygon(double[][][] linearRings) {
        this.linearRings = new LinearRing[linearRings.length];
        for (int i = 0; i < linearRings.length; i++) {
            this.linearRings[i] = new LinearRing(linearRings[i]);
        }

        if (this.linearRings[0].getArea() < 0) {
            throw new IllegalArgumentException("First LinearRing should be outer");
        }
        for (int i = 1; i < this.linearRings.length; i++) {
            if (this.linearRings[i].getArea() > 0) {
                throw new IllegalArgumentException("All LinearRings after first should be holes");
            }
        }
    }


    /**
     * Method is used by Jackson. Convert tree-dimensional double array to LinearRing array.
     * @param linearRings - tree-dimensional double array with coordinates
     * @see LinearRing
     */
    public void setLinearRings(double[][][] linearRings) {
        this.linearRings = new LinearRing[linearRings.length];
        for (int i = 0; i < linearRings.length; i++) {
            this.linearRings[i] = new LinearRing(linearRings[i]);
        }
    }

    /**
     * Calculate center of polygon depending on linear rings areas and centers.
     * Calculating is similar to calculating center of mass in physics.
     * @return Polygon center coordinate.
     * @see LinearRing
     */
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

    /**
     * @return Sum of linear rings areas.
     * @see LinearRing#getArea()
     */
    @Override
    public double getArea(){
        return Arrays.stream(linearRings).mapToDouble(LinearRing::getArea).sum();
    }

    /**
     * @return Coordinate array of outer linear ring (index = 0, because first linear ring in polygon always outer)
     * @see LinearRing
     */
    @Override
    public Coordinate[] getCoordinateArray() {
        return linearRings[0].coordinates;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("\tGeoJson: {\n").append("\t\ttype: Polygon,\n").append("\t\tcoordinates:\n");
        result.append("\t\t\t[\n");
        for (LinearRing linearRing : linearRings) {
            result.append(linearRing);
        }
        result.append("\t\t\t]\n");
        result.append("\t}\n");
        return result.toString();
    }


    /**
     * Class realise <a href=https://tools.ietf.org/html/rfc7946#section-3.1.6>LinearRing</a>.<p>
     * LinearRing used to represent surface and holes in Polygon.<p>
     * A linear ring is a closed LineString with four or more positions.<p>
     * The first and last positions are equivalent.<p>
     * A linear ring is the boundary of a surface or the boundary of a hole in a surface.<p>
     * @see Coordinate
     * @see GeoJson
     * @see Polygon
     */
    public class LinearRing {
        public Coordinate[] coordinates;
        private double area;

        public LinearRing(Coordinate[] coordinates) {
            this.coordinates = coordinates;
        }

        /**
         * Constructor for Jackson deserialization of GeoJSON objects.
         * @param coordinates - two-dimensional double array with coordinates (longitude and latitude)
         */
        public LinearRing(double[][] coordinates) {
            validateArgs(coordinates);
            this.coordinates = new Coordinate[coordinates.length];
            for (int i = 0; i < coordinates.length; i++) {
                this.coordinates[i] = new Coordinate(coordinates[i]);
            }
        }

        private void validateArgs(double[][] coordinates){
            if (coordinates.length < 4) {
                throw new IllegalArgumentException("Should be at least 3 coordinates");
            }
            if (!Arrays.equals(coordinates[0], coordinates[coordinates.length - 1])) {
                throw new IllegalArgumentException("First and last Coordinate in LinearRing should be the same");
            }
        }

        /**
         * @return If linear ring is a surface - return positive value. If linear ring is a hole - return negative value.
         * Value can be used only to compare linear rings between each other.
         */
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
         * @return barycenter of polygon by <a href="https://ru.wikipedia.org/wiki/%D0%91%D0%B0%D1%80%D0%B8%D1%86%D0%B5%D0%BD%D1%82%D1%80#%D0%91%D0%B0%D1%80%D0%B8%D1%86%D0%B5%D0%BD%D1%82%D1%80_%D0%BC%D0%BD%D0%BE%D0%B3%D0%BE%D1%83%D0%B3%D0%BE%D0%BB%D1%8C%D0%BD%D0%B8%D0%BA%D0%B0">this</a>
         * formula
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
            return new Coordinate(longitude, latitude);
        }

        /**
         * Need for Jackson serialization
         */
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
