package de.ronnyfriedland.knowledgebase.entity;

import java.io.Serializable;

/**
 * @author ronnyfriedland
 */
public class Document<T> implements Serializable {

    /**
     * the serialVersionUID
     */
    private static final long serialVersionUID = -2596270543790245689L;

    private final String key;

    private final String header;
    private final T message;
    private final String[] tags;

    public Document(final String key, final String header, final T message, final String... tags) {
        this.key = key;
        this.header = header;
        this.message = message;
        this.tags = tags;
    }

    public String getKey() {
        return key;
    }

    public String getHeader() {
        return header;
    }

    public T getMessage() {
        return message;
    }

    public String[] getTags() {
        return tags;
    }
}
