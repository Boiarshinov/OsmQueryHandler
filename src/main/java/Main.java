import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        OsmSearchHandler osmSearchHandler = OsmSearchHandler.getInstance();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))){
            while (true){
                System.out.print("Input new query (type 'exit' to close app): ");
                String query = reader.readLine();
                if (query.equals("exit")) System.exit(0);

                GeoObject geoObject = osmSearchHandler.search(query);

                System.out.println("Coordinates: " + Arrays.toString(geoObject.getCoordinateArray()) + "\n");
                System.out.println("Center: " + geoObject.getCenter());
            }

        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
