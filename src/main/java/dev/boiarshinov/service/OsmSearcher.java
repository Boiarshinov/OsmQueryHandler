package dev.boiarshinov.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * <p>OSMSearchHandler used to search some subjects of russian federation in
 * <a href=https://www.openstreetmap.org/>OpenStreetMap</a></p>
 * Class is a singleton.<p>
 */
@Component
@Scope("singleton")
public class OsmSearcher {
    private Logger logger = LoggerFactory.getLogger(OsmSearcher.class);
    private static OsmSearcher instance = new OsmSearcher();

    public String search(URL searchQuery) throws IOException {
        logger.debug("Searching by URL: " + searchQuery);
        String jsonString = readJsonStringFromURL(searchQuery);
        logger.debug("Reading JSON from OSM complete");
        return jsonString;
    }

    private String readJsonStringFromURL(URL url) throws IOException {
        String result;
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        try (InputStream in = connection.getInputStream();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int readBytes = 0;
            while ((readBytes = in.read(buffer)) != -1) {
                outputStream.write(buffer, 0, readBytes);
            }
            result = outputStream.toString(StandardCharsets.UTF_8);
        }

        connection.disconnect();
        return result;
    }
}
