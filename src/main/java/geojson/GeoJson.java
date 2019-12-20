package geojson;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;


/**
 * <p>GeoJson is the interface that realises by GeoJSON objects</p>
 * <p>There is a plenty of GeoJSON objects, you can find all of them in
 * <a href=https://tools.ietf.org/html/rfc7946>official documentation</a> but here only 4 types:
 * <strong>MultiPolygon, Polygon, LineString, Point</strong></p>
 *
 * @see MultiPolygon
 * @see Polygon
 * @see LineString
 * @see Point
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Polygon.class, name = "Polygon"),
        @JsonSubTypes.Type(value = MultiPolygon.class, name = "MultiPolygon"),
        @JsonSubTypes.Type(value = LineString.class, name = "LineString"),
        @JsonSubTypes.Type(value = Point.class, name = "Point")
})
public interface GeoJson {
    /**
     * @return Coordinate that is the center of GeoJSON object.
     * @see Coordinate
     */
    Coordinate getCenter();

    /**
     * @return Area of GeoJSON object in conventional units.
     * It can be used only to compare GeoJSON objects between each other.
     */
    double getArea();

    /**
     * @return Array of Coordinates. What kind of array depends on GeoJSON object type.
     * @see Coordinate
     */
    Coordinate[] getCoordinateArray();
}
