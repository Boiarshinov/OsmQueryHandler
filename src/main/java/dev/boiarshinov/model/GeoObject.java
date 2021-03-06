package dev.boiarshinov.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import dev.boiarshinov.model.geojson.Coordinate;
import dev.boiarshinov.model.geojson.GeoJson;

import java.util.Arrays;

/**
 * Class represent objects that comes from OSM service. It calls RFSubject only because of test statement.<p>
 * Class is parsing by Jackson.<p>
 * The most interesting part of this class is a geoJson field.
 * @see GeoJson
 */
@JsonAutoDetect
public class GeoObject {
    @JsonProperty("place_id")
    public int placeId;
    public String licence;
    @JsonProperty("osm_type")
    public String osmType;
    @JsonProperty("osm_id")
    public long osmId;
    @JsonProperty("boundingbox")
    private Coordinate[] boundingBox;
    @JsonProperty("lat")
    private double latitude;
    @JsonProperty("lon")
    private double longitude;
    @JsonProperty("display_name")
    public String displayName;
    @JsonProperty("class")
    public String className;
    public String type;
    public double importance;
    public String icon;
    @JsonProperty("geojson")
    public GeoJson geoJson;

    @Override
    public String toString() {
        return "RFSubject{" + "\n" +
                "placeId=" + placeId + ",\n" +
                "licence='" + licence + '\'' + ",\n" +
                "osmType='" + osmType + '\'' + ",\n" +
                "osmId=" + osmId + ",\n" +
                "boundingBox=" + Arrays.toString(boundingBox) + ",\n" +
                "lat=" + latitude + ",\n" +
                "lon=" + longitude + ",\n" +
                "display_name='" + displayName + '\'' + ",\n" +
                "className='" + className + '\'' + ",\n" +
                "type='" + type + '\'' + ",\n" +
                "importance=" + importance + ",\n" +
                "icon='" + icon + '\'' + ",\n" +
                "geoJson=" + geoJson + "\n" +
                '}';
    }

    //Methods for Jackson parser

    public String getLatitude() {
        return String.valueOf(latitude);
    }

    public void setLatitude(String latitude) {
        this.latitude = Double.parseDouble(latitude);
    }

    public String getLongitude() {
        return String.valueOf(longitude);
    }

    public void setLongitude(String longitude) {
        this.longitude = Double.parseDouble(longitude);
    }

    public Coordinate[] getBoundingBox() {
        return boundingBox;
    }

    public void setBoundingBox(double[] boundingBox) {
        Coordinate oneCorner = new Coordinate(boundingBox[2], boundingBox[0]);
        Coordinate secondCorner = new Coordinate(boundingBox[3], boundingBox[1]);
        this.boundingBox = new Coordinate[]{oneCorner, secondCorner};
    }
}
