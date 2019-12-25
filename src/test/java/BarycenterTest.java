import com.fasterxml.jackson.databind.ObjectMapper;
import geojson.GeoJson;
import geojson.MultiPolygon;
import geojson.Polygon;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class BarycenterTest {
    public static void main(String[] args) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
                new FileReader("./src/main/resources/barycenter.json"))){
            while (reader.ready()){
                stringBuilder.append(reader.readLine());
            }
        }
        String jsonString = stringBuilder.toString();

        ObjectMapper mapper = new ObjectMapper();
        GeoJson geoJson = mapper.readValue(jsonString, GeoJson.class);

        System.out.println(geoJson);
        System.out.println();
        if (geoJson instanceof MultiPolygon){
            MultiPolygon multiPolygon = (MultiPolygon) geoJson;
            for (int i = 0; i < multiPolygon.polygons.length; i++) {
                System.out.println("Polygon " + i);
                for (int j = 0; j < multiPolygon.polygons[i].linearRings.length; j++) {
                    System.out.println("\tLinearRing " + j);
                    System.out.println("\t\tArea: " + multiPolygon.polygons[i].linearRings[j].getArea());
                    System.out.println("\t\tBarycenter: " + multiPolygon.polygons[i].linearRings[j].getBarycenter());
                }
                System.out.println("\tPolygon center: " + multiPolygon.polygons[i].getCenter());
                System.out.println("\tPolygon area: " + multiPolygon.polygons[i].getArea() + "\n");
            }
            System.out.println("\nMultipolygon center: " + multiPolygon.getCenter());
            System.out.println("Multipolygon area: " + multiPolygon.getArea());
        } else if (geoJson instanceof Polygon){
            Polygon polygon = (Polygon) geoJson;
            for (int j = 0; j < polygon.linearRings.length; j++) {
                System.out.println("LinearRing " + j);
                System.out.println("\tArea: " + polygon.linearRings[j].getArea());
                System.out.println("\tBarycenter: " + polygon.linearRings[j].getBarycenter());
            }
            System.out.println("Polygon center: " + polygon.getCenter());
            System.out.println("Polygon area: " + polygon.getArea());
        } else {
            System.out.println("ваш geoJson - неведома зверушка");
        }


    }
}
