package de.ronnyfriedland.knowledgebase.entity;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

import de.ronnyfriedland.knowledgebase.repository.jcr.JCRTextDocument;

/**
 * @author ronnyfriedland
 */
public class Document<T> implements Serializable {

    /** the serialVersionUID */
    private static final long serialVersionUID = -2596270543790245689L;

    private final String key;

    private final String header;
    private final T message;
    private final String[] tags;

    /**
     * Creates a new {@link Document} instance.
     *
     * @param key the unique key
     * @param header the header value
     * @param message the message
     * @param tags the (optional) tags
     */
    public Document(final String key, final String header, final T message, final String... tags) {
        this.key = key;
        this.header = header;
        this.message = message;

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

    public static Document<String> fromJcrTextDocument(final String path, final JCRTextDocument jcrTextDocument) {
        String[] tags;
        JCRTextDocument.ManageableStringCollectionImpl jcrTags = jcrTextDocument.getTags();
        if (null != jcrTags) {
            tags = new String[jcrTags.getSize()];
            int i = 0;
            for (String jcrTag : jcrTags.getObjects()) {
                tags[i] = jcrTag.trim();
                i++;
            }
        } else {
            tags = new String[0];
        }
        return new Document<String>(path, jcrTextDocument.getHeader(), jcrTextDocument.getMessage(), tags);
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

}
