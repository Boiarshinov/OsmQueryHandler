package dev.boiarshinov.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.boiarshinov.dto.GeoObject;
import org.ehcache.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

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
@Component
@Scope("singleton")
public class OsmSearchHandler {
    private Cache<String, GeoObject> cache;
    private Logger logger = LoggerFactory.getLogger(OsmSearchHandler.class);
    private static OsmSearchHandler instance = new OsmSearchHandler();

    private OsmSearchHandler(){}

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
     */
    public GeoObject search(String searchQuery) throws IOException {
        return search(searchQuery, null);
    }

    public GeoObject search(String searchQuery, String geoType) throws IOException{
        logger.info("search query \"{}\" came with geoType \"{}\"", searchQuery, geoType);
        if (alreadyHaveInCache(searchQuery)){
            logger.info("Taking result from cache");
            return cache.get(searchQuery);
        } else {
            logger.info("Connecting to OSM server to solve query");
            URL url = prepareURL(searchQuery, geoType);
            logger.debug("Searching by URL: " + url);
            String jsonString = readJsonStringFromURL(url);
            logger.debug("Reading JSON from OSM complete");
            GeoObject geoObject = parseJsonString(jsonString);
            putInCache(searchQuery, geoObject);
            return geoObject;
        }
    }

    /**
     * Check cache for object with same query
     * @param query query for OSM
     * @return true if cache already have object with same query
     */
    private boolean alreadyHaveInCache(String query){
        if (cache == null) return false;
        return cache.containsKey(query);
    }

    private void putInCache(String searchQuery, GeoObject geoObject){
        if (cache != null) cache.put(searchQuery, geoObject);
    }

    private String encodeSearchQuery(String searchQuery) {
        return URLEncoder.encode(searchQuery, StandardCharsets.UTF_8);
    }

    private URL prepareURL(String query, String geoType) throws MalformedURLException{
        query = encodeSearchQuery(query);
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance();
        uriBuilder.scheme("https").
                host("nominatim.openstreetmap.org").
                path("search").
                queryParam("format", "json").
                queryParam("polygon_geojson", 1).
                queryParam("limit", 1);

        if (geoType != null){
            uriBuilder.queryParam(geoType, query);
            if(!geoType.equals("country")){
                uriBuilder.queryParam("country", "russia");
            }
        }
        return new URL(uriBuilder.build().toUriString());
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
        logger.debug("Parsing complete successfully!");
        return array[0];
    }

    @Autowired
    public void setCache(@Qualifier("cache") Cache<String, GeoObject> cache) {
        this.cache = cache;
    }
}
