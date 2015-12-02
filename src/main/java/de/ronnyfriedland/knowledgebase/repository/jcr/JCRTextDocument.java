package de.ronnyfriedland.knowledgebase.repository.jcr;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.jackrabbit.ocm.manager.collectionconverter.impl.ManageableCollectionImpl;
import org.apache.jackrabbit.ocm.manager.collectionconverter.impl.MultiValueCollectionConverterImpl;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.Collection;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.Field;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.Node;

/**
 * Node implementation to store text based documents.
 *
 * @author ronnyfriedland
 */
@Node(jcrMixinTypes = "mix:versionable, mix:referenceable, mix:lockable")
public class JCRTextDocument {
    public static final class ManageableStringCollectionImpl extends ManageableCollectionImpl {

        /**
         * Creates a new {@link ManageableStringCollectionImpl} instance.
         *
         * @param collection the initial collection
         */
        public ManageableStringCollectionImpl(final java.util.Collection<String> collection) {
            super(collection);
        }

        /**
         * Creates a new {@link ManageableStringCollectionImpl} instance.
         */
        public ManageableStringCollectionImpl() {
            super(new ArrayList<String>());
        }

        /**
         * {@inheritDoc}
         *
         * @see org.apache.jackrabbit.ocm.manager.collectionconverter.impl.ManageableCollectionImpl#getObjects()
         */
        @SuppressWarnings("unchecked")
        @Override
        public java.util.Collection<String> getObjects() {
            return super.getObjects();
        }
    }

    @Field(uuid = true)
    private String uuid;
    @Field(path = true)
    private String path;
    @Field
    private String header;
    @Field
    private String message;
    @Collection(collectionConverter = MultiValueCollectionConverterImpl.class)
    private ManageableStringCollectionImpl tags;
    @Field
    private Date creationDate;

    /**
     * Creates a new {@link JCRTextDocument} instance.
     *
     * @param path the path of the document
     * @param header the header
     * @param message the message
     * @param tags the (optional) tags
     */
    public JCRTextDocument(final String path, final String header, final String message, final String[] tags) {
        this(path, header, message, Arrays.asList(tags));
    }

    /**
     * Creates a new {@link JCRTextDocument} instance.
     *
     * @param path the path of the document
     * @param header the header
     * @param message the message
     * @param tags the (optional) tags
     */
    public JCRTextDocument(final String path, final String header, final String message, final List<String> tags) {
        this();
        if (!path.startsWith("/")) {
            this.path = "/" + path;
        }
        this.header = header;
        this.message = message;
        this.tags = new ManageableStringCollectionImpl(tags);
        this.creationDate = Calendar.getInstance().getTime();
    }

    /**
     * Creates a new {@link JCRTextDocument} instance.
     */
    public JCRTextDocument() {
        super();
    }

    public String getPath() {
        return path;
    }

    public void setPath(final String path) {
        this.path = path;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(final String header) {
        this.header = header;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    public ManageableStringCollectionImpl getTags() {
        return tags;
    }

    public void setTags(final ManageableStringCollectionImpl tags) {
        this.tags = tags;
    }

    public void setTags(final String[] tags) {
        setTags(Arrays.asList(tags));
    }

    public void setTags(final List<String> tags) {
        this.tags = new ManageableStringCollectionImpl(tags);
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(final Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(final String uuid) {
        this.uuid = uuid;
    }
}