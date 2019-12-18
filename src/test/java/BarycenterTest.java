import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class BarycenterTest {
    public static void main(String[] args) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
                new FileReader("./src/main/resources/geojson.json"))){
            while (reader.ready()){
                stringBuilder.append(reader.readLine());
            }
        }
        String jsonString = stringBuilder.toString();

        ObjectMapper mapper = new ObjectMapper();
        GeoJson geoJson = mapper.readValue(jsonString, GeoJson.class);

        System.out.println(geoJson);
        System.out.println();
        for (int i = 0; i < geoJson.multiPolygon.polygons.length; i++) {
            System.out.println("Polygon " + i);
            for (int j = 0; j < geoJson.multiPolygon.polygons[i].linearRings.length; j++) {
                System.out.println("\tLinearRing " + j);
                System.out.println("\t\tArea: " + geoJson.multiPolygon.polygons[i].linearRings[j].getArea());
                System.out.println("\t\tBarycenter: " + geoJson.multiPolygon.polygons[i].linearRings[j].getBarycenter());
            }
            System.out.println("\tCenter: " + geoJson.multiPolygon.polygons[i].getCenter());
            System.out.println("\tPolygon area: " + geoJson.multiPolygon.polygons[i].getArea());
        }

    }
}
