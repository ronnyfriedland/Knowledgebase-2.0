package de.ronnyfriedland.knowledgebase.entity;

import java.util.Collection;
import java.util.LinkedHashSet;

public class FileDocument<T> extends Document<T> {

    /** the serialVersion uid */
    private static final long serialVersionUID = -4637079531110457673L;

    private FileDocument<T> parent;
    private Collection<FileDocument<T>> children = new LinkedHashSet<>();

    /**
     * Creates a new {@link FileDocument} instance.
     *
     * @param key the unique key
     * @param header the header value
     * @param message the message
     * @param tags the (optional) tags
     */
    public FileDocument(final String key, final String header, final T message, final boolean encrypted,
            final String... tags) {
        super(key, header, message, encrypted, tags);
    }

    public void setParent(FileDocument<T> parent) {
        this.parent = parent;
    }

    public FileDocument<T> getParent() {
        return parent;
    }

    public Collection<FileDocument<T>> getChildren() {
        return children;
    }

    public void addChild(final FileDocument<T> child) {
        getChildren().add(child);
    }
}
