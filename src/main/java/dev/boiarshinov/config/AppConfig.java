package dev.boiarshinov.config;

import dev.boiarshinov.Application;
import dev.boiarshinov.dto.GeoObject;
import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.xml.XmlConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

import java.net.URL;

@Configuration
@ComponentScan("dev.boiarshinov")
public class AppConfig {
    @Bean
    public Cache<String, GeoObject> cache(){
        URL cacheXML = Application.class.getResource("/ehcacheConfig.xml");
        org.ehcache.config.Configuration xmlConfig = new XmlConfiguration(cacheXML);
        CacheManager cacheManager = CacheManagerBuilder.newCacheManager(xmlConfig);
        cacheManager.init();
        return cacheManager.getCache("cache", String.class, GeoObject.class);
    }
}
