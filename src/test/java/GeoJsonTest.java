import com.fasterxml.jackson.databind.ObjectMapper;
import geojson.GeoJson;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class GeoJsonTest {
    public static void main(String[] args) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader("./src/main/resources/geojson_Multipolygon.json"))){
            while (reader.ready()){
                stringBuilder.append(reader.readLine());
            }
        }
        String jsonString = stringBuilder.toString();

        ObjectMapper mapper = new ObjectMapper();
        GeoJson geoJson = mapper.readValue(jsonString, GeoJson.class);

        System.out.println(geoJson);
    }
}
