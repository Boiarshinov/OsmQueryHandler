package dev.boiarshinov.util;

import org.springframework.web.util.UriComponentsBuilder;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class UrlPreparer {
    private UrlPreparer(){}

    public static URL prepareURL(String name, String geoType) throws MalformedURLException {
        name = URLEncoder.encode(name, StandardCharsets.UTF_8);
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance();
        uriBuilder.scheme("https").
                host("nominatim.openstreetmap.org").
                path("search").
                queryParam("format", "json").
                queryParam("polygon_geojson", 1).
                queryParam("limit", 1);

        if (geoType != null){
            uriBuilder.queryParam(geoType, name);
            if(!geoType.equals("country")){
                uriBuilder.queryParam("country", "russia");
            }
        } else {
            uriBuilder.
                    queryParam("q", name).
                    queryParam("country", "russia");
        }
        return new URL(uriBuilder.build().toUriString());
    }
}
