package dev.boiarshinov.service;

import dev.boiarshinov.model.GeoObject;
import dev.boiarshinov.util.UrlPreparer;
import org.ehcache.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;

@Component
@Scope("singleton")
public class MainService {
    private Cache<URL, GeoObject> cache;
    private OsmSearcher osmSearcher;
    private GeoObjectParser geoObjectParser;
    private Logger logger = LoggerFactory.getLogger(MainService.class);

    public GeoObject service(String name) throws IOException {
        return service(name, null);
    }

    public GeoObject service(String name, String geoType) throws IOException {
        logger.info("search query came with name \"{}\" and type \"{}\"", name, geoType);
        URL urlQuery = UrlPreparer.prepareURL(name, geoType);
        if (alreadyHaveInCache(urlQuery)) {
            GeoObject geoObject = cache.get(urlQuery);
            logger.info("Taking geoObject from cache");
            return geoObject;
        } else {
            String jsonString = osmSearcher.search(urlQuery);
            GeoObject geoObject = geoObjectParser.parseJson(jsonString);
            putInCache(urlQuery, geoObject);
            return geoObject;
        }
    }

    private boolean alreadyHaveInCache(URL query){
        if (cache == null) return false;
        return cache.containsKey(query);
    }

    private void putInCache(URL urlQuery, GeoObject geoObject) {
        if (cache != null) cache.put(urlQuery, geoObject);
    }

    @Autowired
    public void setCache(@Qualifier("cache") Cache<URL, GeoObject> cache) {
        this.cache = cache;
    }

    @Autowired
    public void setOsmSearcher(OsmSearcher osmSearcher) {
        this.osmSearcher = osmSearcher;
    }

    @Autowired
    public void setGeoObjectParser(GeoObjectParser geoObjectParser) {
        this.geoObjectParser = geoObjectParser;
    }
}
