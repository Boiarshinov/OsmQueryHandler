import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class OsmSearchHandler {
    public static final String SOURCE = "https://nominatim.openstreetmap.org/";

    public static String search(String searchQuery) throws IOException {
        URL url = new URL(SOURCE + "search/" +
                encodeSearchQuery(searchQuery) + "?" +
                "country=russia" + "&" +
                "format=json" + "&" +
                "polygon_geojson=1" + "&" +
                "limit=1");

        System.out.println("Searching by URL: " + url + "\n");

        String jsonString = readJsonStringFromURL(url);

        System.out.println(jsonString);

        System.out.println("\nReading JSON from OSM complete");

        return jsonString;
    }

    public static String encodeSearchQuery(String searchQuery) {
        return URLEncoder.encode(searchQuery, StandardCharsets.UTF_8);
    }

    public static String readJsonStringFromURL(URL url) throws IOException {
        String result = "something went wrong!";
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        try (InputStream in = connection.getInputStream();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int readBytes = 0;
            while ((readBytes = in.read(buffer)) != -1){
                outputStream.write(buffer, 0, readBytes);
            }
            result = outputStream.toString(StandardCharsets.UTF_8);
        }

        System.out.println("result of reading: " + result + "\n");

        connection.disconnect();

        return result;
    }
}
