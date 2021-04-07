package de.ronnyfriedland.knowledgebase.entity;

import java.io.Serializable;
import java.util.Arrays;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * @author ronnyfriedland
 */
public class Document<T> implements Serializable, Comparable<Document<T>> {

    /** the serialVersionUID */
    private static final long serialVersionUID = -2596270543790245689L;

    private final String key;

    private final String header;
    private final T message;
    private final String[] tags;
    private final boolean encrypted;

    /**
     * Creates a new {@link Document} instance.
     *
     * @param key the unique key
     * @param header the header value
     * @param message the message
     * @param tags the (optional) tags
     */
    public Document(final String key, final String header, final T message, final boolean encrypted,
            final String... tags) {
        this.key = key;
        this.header = header;
        this.message = message;
        this.encrypted = encrypted;

        if (null != tags) {
            this.tags = new String[tags.length];
            int i = 0;
            for (String tag : tags) {
                this.tags[i] = tag.trim();
                i++;
            }
        } else {
            this.tags = new String[0];
        }
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

    public boolean isEncrypted() {
        return encrypted;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(final Document<T> o) {
        return getKey().compareTo(o.getKey());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Document<?> document = (Document<?>) o;

        if (encrypted != document.encrypted) return false;
        if (key != null ? !key.equals(document.key) : document.key != null) return false;
        if (header != null ? !header.equals(document.header) : document.header != null) return false;
        if (message != null ? !message.equals(document.message) : document.message != null) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        return Arrays.equals(tags, document.tags);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        int result = key != null ? key.hashCode() : 0;
        result = 31 * result + (header != null ? header.hashCode() : 0);
        result = 31 * result + (message != null ? message.hashCode() : 0);
        result = 31 * result + Arrays.hashCode(tags);
        result = 31 * result + (encrypted ? 1 : 0);
        return result;
    }
}
