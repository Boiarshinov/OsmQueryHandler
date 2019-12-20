public class CacheManagerTest {
    public static void main(String[] args) {
        CacheManager cacheManager = CacheManager.getInstance();
        System.out.println("Cache size: " + cacheManager.getCacheSize());
        System.out.println("Number of elements in cache: " + cacheManager.size());
    }
}
