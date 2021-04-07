package de.ronnyfriedland.knowledgebase.cache;

import org.apache.jcs.JCS;
import org.apache.jcs.access.CacheAccess;
import org.apache.jcs.access.exception.CacheException;

/**
 * @author ronnyfriedland
 */
public class RepositoryCache<T> {

    private CacheAccess cache;

    /**
     * Creates a new instance of {@link RepositoryCache}
     *
     * @param region jcs region
     * @throws CacheException error initializing cache
     */
    public RepositoryCache(final String region) throws CacheException {
        cache = JCS.getInstance(region);
    }

    /**
     * Get value by key
     *
     * @param key the cache key
     * @return the value
     */
    @SuppressWarnings("unchecked")
    public T get(final String key) {
        return (T) cache.get(key);
    }

    /**
     * Put value to cache
     *
     * @param key      the cache key
     * @param document the value to cache
     */
    public void put(final String key, final T document) {
        try {
            cache.put(key, document);
        } catch (CacheException e) {
            // ignore
        }
    }

    /**
     * Remove value from cache
     *
     * @param key the key
     */
    public void remove(final String key) {
        try {
            cache.remove(key);
        } catch (CacheException e) {
            // ignore
        }
    }
}
