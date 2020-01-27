import dev.boiarshinov.CacheManager;
import dev.boiarshinov.GeoObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CacheManagerTest {
    private static CacheManager cacheManager;
    private static GeoObject geoObject;
    private static String key;

    @BeforeAll
    static void setUpClass(){
        cacheManager = CacheManager.getInstance();
        geoObject = new GeoObject();
        key = "key";
    }

    @Test
    void put() {
        cacheManager.put(key, geoObject);
        assertTrue(cacheManager.containsKey(key), "Object not found in the cache");
    }

    @Test
    void get() {
        cacheManager.put(key, geoObject);
        assertEquals(geoObject, cacheManager.get(key), "Objects with same key should be equal");
    }
}
