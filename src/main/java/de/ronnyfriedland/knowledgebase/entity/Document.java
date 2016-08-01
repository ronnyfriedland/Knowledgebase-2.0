package de.ronnyfriedland.knowledgebase.entity;

import java.io.Serializable;

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
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    /**
     * {@inheritDoc}
     *
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(final Document<T> o) {
        return getKey().compareTo(o.getKey());
    }

}
