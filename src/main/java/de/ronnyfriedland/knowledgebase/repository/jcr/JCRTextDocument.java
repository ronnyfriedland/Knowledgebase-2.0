package de.ronnyfriedland.knowledgebase.repository.jcr;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
        if (path.startsWith("/")) {
        } else {
            this.path = new StringBuilder().append("/").append(path).toString();
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