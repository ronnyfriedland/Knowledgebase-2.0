package de.ronnyfriedland.knowledgebase.cache;

import org.apache.jcs.JCS;
import org.apache.jcs.access.CacheAccess;
import org.apache.jcs.access.exception.CacheException;

/**
 * @author ronnyfriedland
 */
public class RepositoryCache<T> {

    private CacheAccess cache = null;

    public RepositoryCache(final String region) throws Exception {
        cache = JCS.getInstance(region);
    }

    @SuppressWarnings("unchecked")
    public T get(final String key) {
        return (T) cache.get(key);
    }

    public void put(final String key, final T document) {
        try {
            cache.put(key, document);
        } catch (CacheException e) {
            // ignore
        }
    }

    public void remove(final String key) {
        try {
            cache.remove(key);
        } catch (CacheException e) {
            // ignore
        }
    }
}
