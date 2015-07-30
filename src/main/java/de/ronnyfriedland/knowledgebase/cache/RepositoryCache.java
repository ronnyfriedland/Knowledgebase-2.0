package de.ronnyfriedland.knowledgebase.cache;

import javax.annotation.PostConstruct;

import org.apache.jcs.JCS;
import org.apache.jcs.access.CacheAccess;
import org.apache.jcs.access.exception.CacheException;
import org.springframework.stereotype.Component;

import de.ronnyfriedland.knowledgebase.entity.Document;

/**
 * @author ronnyfriedland
 */
@Component
public class RepositoryCache<T> {

    private CacheAccess cache = null;

    @PostConstruct
    public void init() throws Exception {
        cache = JCS.getInstance("default");
    }

    @SuppressWarnings("unchecked")
    public Document<T> get(final String key) {
        return (Document<T>) cache.get(key);
    }

    public void put(final String key, final Document<T> document) {
        try {
            cache.put(key, document);
        } catch (CacheException e) {
            // ignore
        }
    }
}
