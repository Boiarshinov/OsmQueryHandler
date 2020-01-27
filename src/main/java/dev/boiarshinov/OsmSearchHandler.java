package dev.boiarshinov;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * <p>OSMSearchHandler used to search some subjects of russian federation in
 * <a href=https://www.openstreetmap.org/>OpenStreetMap</a></p>
 * Class is a singleton.<p>
 */
public class OsmSearchHandler {
    private CacheManager cacheManager;
    private static OsmSearchHandler instance = new OsmSearchHandler();

    private OsmSearchHandler(){
        System.out.println("Configuring OSM Search Handler");
        cacheManager = CacheManager.getInstance();
        System.out.println("OSM Search Handler configured!");
    }

    /**
     * Class is a singleton.
     * @return Return only instance of OSMSearchHandler
     */
    public static OsmSearchHandler getInstance(){
        return instance;
    }

    /**
     * For first check query in the cache. If cache have object with same query, than object return from cache.
     * In another way query is searching in OSM.
     * @param searchQuery - query that would be searching in OSM.
     * @return Object that deserialize from incoming JSON.
     * @throws IOException
     * @see GeoObject
     * @see CacheManager
     */
    public GeoObject search(String searchQuery) throws IOException {
        System.out.println("\n=================================");
        if (alreadyHaveInCache(searchQuery)){
            System.out.print(searchQuery + " is in the cache. Taking it out... ");
            GeoObject geoObject = cacheManager.get(searchQuery);
            System.out.println("Success!");
            return geoObject;
        } else {
            System.out.println(searchQuery + " is not in the cache. Connecting to OSM server...");
            URL url = prepareURL(searchQuery);
            System.out.println("Searching by URL: " + url);
            String jsonString = readJsonStringFromURL(url);
            System.out.println("Reading JSON from OSM complete");
            GeoObject geoObject = parseJsonString(jsonString);
            cacheManager.put(searchQuery, geoObject);
            return geoObject;
        }
    }

    /**
     * Check cache for object with same query
     * @param query query for OSM
     * @return true if cache already have object with same query
     * @see CacheManager
     */
    private boolean alreadyHaveInCache(String query){
        return cacheManager.containsKey(query);
    }

    /**
     * Encode cyrillic characters to unicode string
     * @param searchQuery query to OSM
     * @return encoded String
     * @see URLEncoder
     */
    private String encodeSearchQuery(String searchQuery) {
        return URLEncoder.encode(searchQuery, StandardCharsets.UTF_8);
    }

    /**
     * Prepare URL for opening HTTP connection
     * @param query - query for OSM
     * @return fabricate URL
     * @throws MalformedURLException
     */
    private URL prepareURL(String query) throws MalformedURLException{
        String SOURCE = "https://nominatim.openstreetmap.org/";
        return new URL(SOURCE + "search?q=" +
                encodeSearchQuery(query) + "&" +
                "country=russia" + "&" +
                "format=json" + "&" +
                "polygon_geojson=1" + "&" +
                "limit=1");
    }

    /**
     * Open HTTP connection with OSM and pull GET-query.
     * @param url - searching URL fabricated from query
     * @return JSON string
     * @throws IOException
     * @see HttpURLConnection
     */
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

    /**
     * @param jsonString - JSON string come from OSM service
     * @return Always return first answer of OSM, because it's the most correct answer in larger part of queries.
     * @throws IOException
     * @see GeoObject
     */
    private GeoObject parseJsonString(String jsonString) throws IOException{
        ObjectMapper mapper = new ObjectMapper();
        GeoObject[] array = mapper.readValue(jsonString, GeoObject[].class);
        System.out.println("Parsing complete successfully!");
        return array[0];
    }
}
