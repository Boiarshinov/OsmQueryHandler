package dev.boiarshinov.config;

import dev.boiarshinov.Application;
import dev.boiarshinov.model.GeoObject;
import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.xml.XmlConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.net.URL;

@Configuration
@ComponentScan("dev.boiarshinov")
public class AppConfig {
    @Bean
    public Cache<URL, GeoObject> cache(){
        URL cacheXML = Application.class.getResource("/ehcacheConfig.xml");
        org.ehcache.config.Configuration xmlConfig = new XmlConfiguration(cacheXML);
        CacheManager cacheManager = CacheManagerBuilder.newCacheManager(xmlConfig);
        cacheManager.init();
        return cacheManager.getCache("cache", URL.class, GeoObject.class);
    }
}
