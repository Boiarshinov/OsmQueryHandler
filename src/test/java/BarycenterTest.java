import com.fasterxml.jackson.databind.ObjectMapper;
import geojson.GeoJson;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class BarycenterTest {
    public static void main(String[] args) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
                new FileReader("./src/main/resources/geojson_Multipolygon.json"))){
            while (reader.ready()){
                stringBuilder.append(reader.readLine());
            }
        }
        String jsonString = stringBuilder.toString();

        ObjectMapper mapper = new ObjectMapper();
        GeoJson geoJson = mapper.readValue(jsonString, GeoJson.class);

        System.out.println(geoJson);
        System.out.println();
        /*for (int i = 0; i < geoJson.polygonable.polygons.length; i++) {
            System.out.println("Polygon " + i);
            for (int j = 0; j < geoJson.polygonable.polygons[i].linearRings.length; j++) {
                System.out.println("\tLinearRing " + j);
                System.out.println("\t\tArea: " + geoJson.polygonable.polygons[i].linearRings[j].getArea());
                System.out.println("\t\tBarycenter: " + geoJson.polygonable.polygons[i].linearRings[j].getBarycenter());
            }
            System.out.println("\tCenter: " + geoJson.polygonable.polygons[i].getCenter());
            System.out.println("\tPolygon area: " + geoJson.polygonable.polygons[i].getArea());
        }*/

    }
}
