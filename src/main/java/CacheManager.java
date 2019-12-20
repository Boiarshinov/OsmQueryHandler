import javax.swing.text.html.parser.Entity;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

public class CacheManager {
    private static CacheManager instance;
    private LinkedHashMap<String, RFSubject> querySubjectMap;
    private final int CACHE_SIZE;

    private CacheManager() {
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
    }

    public static CacheManager getInstance() {
        if (instance == null) instance = new CacheManager();
        return instance;
    }

    public void put(String query, RFSubject rfSubject){
        if (querySubjectMap.size() < CACHE_SIZE){
            querySubjectMap.put(query, rfSubject);
        } else {
            Iterator<Map.Entry<String, RFSubject>> iterator = querySubjectMap.entrySet().iterator();
            iterator.next();
            iterator.remove();
            querySubjectMap.put(query, rfSubject);
        }
    }

    public RFSubject get(String key){
        if (querySubjectMap.containsKey(key)){
            RFSubject answer = querySubjectMap.get(key);
            replaceSubjectToTheEndOfMap(key, answer);
            return answer;
        } else return null;
    }

    public int size(){
        return querySubjectMap.size();
    }

    public void clear(){
        querySubjectMap.clear();
    }

    private void replaceSubjectToTheEndOfMap(String key, RFSubject rfSubject){
        querySubjectMap.remove(key);
        querySubjectMap.put(key, rfSubject);
    }

    public int getCacheSize() {
        return CACHE_SIZE;
    }

    public boolean containsKey(String key){
        return querySubjectMap.containsKey(key);
    }
}
