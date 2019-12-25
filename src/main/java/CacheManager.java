import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

/**
 * CacheManager is a class that manage operations with cache.<p>
 * Cache size is set by .properties file.<p>
 * Cache was realized by the LinkedHashMap. So if cache is full when first object in cache would remove
 * and new object will set to the end of the map.<p>
 * Class is a singleton.
 * @see LinkedHashMap
 */
public class CacheManager {
    private static CacheManager instance = new CacheManager();
    private LinkedHashMap<String, GeoObject> querySubjectMap;
    private final int CACHE_SIZE;

    /**
     * Cache size is set by .properties file.<p>
     * If some problem occurs while reading .properties file when cache size is set to 100.
     */
    private CacheManager() {
        System.out.println("Configuring Cache Manager");
        int tempCacheSize;
        int defaultCacheSize = 100;
        try {
            Properties properties = new Properties();
            properties.load(new FileReader("./src/main/resources/.properties"));
            tempCacheSize = Integer.parseInt(properties.getProperty("Cache_size"));
        } catch (FileNotFoundException e){
            System.out.println("Property file not find. Cache size is set to " + defaultCacheSize);
            tempCacheSize = defaultCacheSize;
        } catch (IOException e) {
            System.out.println("Error while reading property file. Cache size is set to " + defaultCacheSize);
            tempCacheSize = defaultCacheSize;
        }
        CACHE_SIZE = tempCacheSize;
        querySubjectMap = new LinkedHashMap<>(CACHE_SIZE);
        System.out.println("Cache Manager configured!");
    }

    /**
     * Class is a singleton.
     * @return Return only instance of CacheManager
     */
    public static CacheManager getInstance() {
        return instance;
    }

    /**
     * Put into cache query and object, that returned as a result of query.<p>
     * If cache is full when first object in cache would remove
     * and new object will set to the end of the map.<p>
     * @param query query for OSM
     * @param geoObject object that return as a result of searching in OSM
     * @see OsmSearchHandler
     */
    public void put(String query, GeoObject geoObject){
        if (querySubjectMap.size() < CACHE_SIZE){
            querySubjectMap.put(query, geoObject);
        } else {
            Iterator<Map.Entry<String, GeoObject>> iterator = querySubjectMap.entrySet().iterator();
            iterator.next();
            iterator.remove();
            querySubjectMap.put(query, geoObject);
        }
    }

    /**
     * Get object from the cache and replace entry in inner map to the end.
     * @param key query for OSM
     * @return object which one was returned by key query
     */
    public GeoObject get(String key){
        if (querySubjectMap.containsKey(key)){
            GeoObject answer = querySubjectMap.get(key);
            replaceSubjectToTheEndOfMap(key, answer);
            return answer;
        } else return null;
    }

    /**
     * @return number of entries in cache
     */
    public int size(){
        return querySubjectMap.size();
    }

    /**
     * Clear cache from objects in it.
     */
    public void clear(){
        querySubjectMap.clear();
    }

    private void replaceSubjectToTheEndOfMap(String key, GeoObject geoObject){
        querySubjectMap.remove(key);
        querySubjectMap.put(key, geoObject);
    }

    /**
     * @return max cache size
     */
    public int getCacheSize() {
        return CACHE_SIZE;
    }

    /**
     * Returns true if this cache contains an object for search query.
     * @param key query for OSM
     * @return true if this cache contains an object for the specified key.
     */
    public boolean containsKey(String key){
        return querySubjectMap.containsKey(key);
    }
}
