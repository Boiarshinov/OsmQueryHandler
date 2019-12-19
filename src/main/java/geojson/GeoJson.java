package geojson;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonAutoDetect
public class GeoJson {
    public String type;

    @JsonProperty("coordinates")
    public Polygonable polygonable;

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("GeoJson{\n");
        result.append("\ttype='").append(type).append("\',\n");
        result.append("\tcoordinates=\n").append(polygonable);
        result.append("}");

        return result.toString();
    }

    //TODO: if you want to serialize objects of this class, than you should write this method and similar methods in inner classes
    /*public double[][][][] getMultiPolygons() {
        return multiPolygons;
    }*/

    public void setPolygonable(double[][][][] coordinates) {
        polygonable = new MultiPolygon(coordinates);
    }

    /*public void setPolygonable(double[][][] coordinates) {
        polygonable = new Polygon(coordinates);
    }*/
}
