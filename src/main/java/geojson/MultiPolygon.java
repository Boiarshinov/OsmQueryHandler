package geojson;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import java.util.Arrays;

/**
 * Class realise GeoJSON type <a href=https://tools.ietf.org/html/rfc7946#section-3.1.7>MultiPolygon</a>.<p>
 * MultiPolygon is used to show some areas that can be represent only by group of Polygons.<p>
 * @see Polygon
 * @see GeoJson
 */
@JsonTypeName("MultiPolygon")
public class MultiPolygon implements GeoJson{
    @JsonProperty("coordinates")
    public Polygon[] polygons;

    /**
     * Blank constructor for Jackson
     */
    public MultiPolygon(){ }

    public MultiPolygon(Polygon[] polygons) {
        this.polygons = polygons;
    }

    /**
     * Method is used by Jackson. Convert four-dimensional double array to Polygon array.
     * @param polygons - four-dimensional array with coordinates of MultiPolygon
     * @see Polygon
     */
    public void setPolygons(double[][][][] polygons) {
        this.polygons = new Polygon[polygons.length];
        for (int i = 0; i < polygons.length; i++) {
            this.polygons[i] = new Polygon(polygons[i]);
        }
    }

    /**
     * Calculate center of multipolygon depending on polygons areas and centers.
     * Calculating is similar to calculating center of mass in physics.
     * @return MultiPolygon center Coordinate
     * @see Polygon
     * @see Coordinate
     */
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

    /**
     * @return Sum of all polygons areas.
     * @see Polygon
     */
    @Override
    public double getArea() {
        return Arrays.stream(polygons).mapToDouble(Polygon::getArea).sum();
    }

    /**
     * @return Coordinate array of outer linear ring of biggest polygon
     * @see Polygon
     * @see geojson.Polygon.LinearRing
     */
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
