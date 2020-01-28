package dev.boiarshinov.util;

import dev.boiarshinov.Application;
import dev.boiarshinov.dto.GeoObject;
import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.Configuration;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.xml.XmlConfiguration;

import java.net.URL;

public class CacheSupplier {
    static CacheManager cacheManager;

    static {
        URL cacheXML = Application.class.getResource("/ehcacheConfig.xml");
        Configuration xmlConfig = new XmlConfiguration(cacheXML);
        cacheManager = CacheManagerBuilder.newCacheManager(xmlConfig);
        cacheManager.init();
    }

    private CacheSupplier(){}

    public static Cache<String, GeoObject> getCache(){
        return cacheManager.getCache("cache", String.class, GeoObject.class);
    }

}
