import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;

public class TestAllSubjects {
    public static void main(String[] args) throws IOException {
        ArrayList<String> subjectQueries = readRFSubjects("./src/main/resources/RFSubjects.txt");
        ArrayList<String> geoJsonTypes = new ArrayList<>();
        for (String query : subjectQueries) {
            try {
                String jsonString = OsmSearchHandler.search(query);
                RFSubject rfSubject = OsmSearchHandler.parseJsonString(jsonString);
                String geoJsonType = rfSubject.geoJson.getClass().getSimpleName();
                geoJsonTypes.add(geoJsonType);
                System.out.println("Type of GeoJSON: " + geoJsonType);
            } catch (IOException e){
                geoJsonTypes.add(null);
                System.out.println("Something went wrong with query: " + query);
                e.printStackTrace();
            }
        }

        for (int i = 0; i < subjectQueries.size(); i++) {
            System.out.println("Query: " + subjectQueries.get(i) + " - GeoJSON type: " + geoJsonTypes.get(i));
        }
    }

    public static ArrayList<String> readRFSubjects(String path){
        ArrayList<String> subjectQueries = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(path, StandardCharsets.UTF_8))){
            while (reader.ready()){
                subjectQueries.add(reader.readLine());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return subjectQueries;
    }
}
