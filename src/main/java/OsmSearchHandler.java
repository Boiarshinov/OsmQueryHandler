import com.fasterxml.jackson.databind.ObjectMapper;

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
    public final String SOURCE = "https://nominatim.openstreetmap.org/";
    private CacheManager cacheManager;
    private static OsmSearchHandler instance;

    private OsmSearchHandler(){
        cacheManager = CacheManager.getInstance();
    }

    public static OsmSearchHandler getInstance(){
        if (instance == null) instance = new OsmSearchHandler();
        return instance;
    }

    public RFSubject search(String searchQuery) throws IOException {
        System.out.println("\n=================================");
        if (alreadyHaveInCache(searchQuery)){
            System.out.print(searchQuery + " is in the cache. Taking it out... ");
            RFSubject rfSubject = cacheManager.get(searchQuery);
            System.out.println("Success!");
            return rfSubject;
        } else {
            System.out.println(searchQuery + " is not in the cache. Connecting to OSM server...");
            URL url = prepareURL(searchQuery);
            System.out.println("Searching by URL: " + url);
            String jsonString = readJsonStringFromURL(url);
            System.out.println("Reading JSON from OSM complete");
            RFSubject rfSubject = parseJsonString(jsonString);
            cacheManager.put(searchQuery, rfSubject);
            return rfSubject;
        }
    }

    private RFSubject getFromCache(String query){
        return cacheManager.get(query);
    }

    private boolean alreadyHaveInCache(String query){
        return cacheManager.containsKey(query);
    }

    private String encodeSearchQuery(String searchQuery) {
        return URLEncoder.encode(searchQuery, StandardCharsets.UTF_8);
    }

    private URL prepareURL(String query) throws MalformedURLException{
        return new URL(SOURCE + "search?q=" +
                encodeSearchQuery(query) + "&" +
                "country=russia" + "&" +
                "format=json" + "&" +
                "polygon_geojson=1" + "&" +
                "limit=1");
    }

    private String readJsonStringFromURL(URL url) throws IOException {
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

        connection.disconnect();
        return result;
    }

    private RFSubject parseJsonString(String jsonString) throws IOException{
        ObjectMapper mapper = new ObjectMapper();
        RFSubject[] array = mapper.readValue(jsonString, RFSubject[].class);
        System.out.println("Parsing complete successfully!");
        return array[0];
    }
}
