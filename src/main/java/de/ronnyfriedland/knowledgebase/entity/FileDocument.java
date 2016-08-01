package de.ronnyfriedland.knowledgebase.entity;

import java.util.ArrayList;
import java.util.List;

public class FileDocument<T> extends Document<T> {

    /** the serialVersion uid */
    private static final long serialVersionUID = -4637079531110457673L;

    private FileDocument<T> parent;
    private final List<FileDocument<T>> children = new ArrayList<>();

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

    public void setParent(final FileDocument<T> parent) {
        this.parent = parent;
    }

    public FileDocument<T> getParent() {
        return parent;
    }

    public List<FileDocument<T>> getChildren() {
        return children;
    }

    public void addChild(final FileDocument<T> child) {
        getChildren().add(child);
    }
}
