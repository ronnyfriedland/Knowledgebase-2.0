package de.ronnyfriedland.knowledgebase.repository.jcr;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.jackrabbit.ocm.manager.collectionconverter.impl.ManageableCollectionImpl;
import org.apache.jackrabbit.ocm.manager.collectionconverter.impl.MultiValueCollectionConverterImpl;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.Collection;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.Field;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.Node;

import de.ronnyfriedland.knowledgebase.entity.Document;
import de.ronnyfriedland.knowledgebase.exception.DataException;
import de.ronnyfriedland.knowledgebase.util.SecurityUtils;

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
    @Field(jcrDefaultValue = "false")
    private boolean encrypted;

    /**
     * Creates a new {@link JCRTextDocument} instance.
     *
     * @param path the path of the document
     * @param header the header
     * @param message the message
     * @param tags the (optional) tags
     * @throws DataException
     */
    public JCRTextDocument(final String path, final String header, final String message, final boolean encrypted,
            final String[] tags) throws DataException {
        this(path, header, message, encrypted, Arrays.asList(tags));
    }

    /**
     * Creates a new {@link JCRTextDocument} instance.
     *
     * @param path the path of the document
     * @param header the header
     * @param message the message
     * @param tags the (optional) tags
     * @throws DataException
     */
    public JCRTextDocument(final String path, final String header, final String message, final boolean encrypted,
            final List<String> tags) throws DataException {
        this();
        if (!path.startsWith("/")) {
            this.path = "/" + path;
        }
        this.header = header;
        this.tags = new ManageableStringCollectionImpl(tags);
        this.creationDate = Calendar.getInstance().getTime();
        this.encrypted = encrypted;

        try {
            if (this.encrypted) {
                this.message = SecurityUtils.encryptStringSymmetric(message);
            } else {
                this.message = message;
            }
        } catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | NoSuchProviderException
                | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
            throw new DataException("Error encrypting message", e);
        }
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

    public void setEncrypted(final boolean encrypted) {
        this.encrypted = encrypted;
    }

    public boolean isEncrypted() {
        return encrypted;
    }

    /**
     * Creates a {@link Document} based on the current data of this {@link JCRTextDocument}.
     *
     * @return the {@link Document}
     * @throws DataException Error when decrypting an encrypted message
     */
    public Document<String> toDocument() throws DataException {
        String[] tags;
        JCRTextDocument.ManageableStringCollectionImpl jcrTags = getTags();
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

        String message = getMessage();
        try {
            if (isEncrypted()) {
                message = SecurityUtils.decryptStringSymmetric(message);
            }
        } catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | NoSuchProviderException
                | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
            throw new DataException("Error decrypting message", e);
        }

        return new Document<String>(getPath(), getHeader(), message, isEncrypted(), tags);
    }

    public void update(Document<String> update) throws DataException {
        setHeader(update.getHeader());
        try {
            if (update.isEncrypted()) {
                setMessage(SecurityUtils.encryptStringSymmetric(update.getMessage()));
            } else {
                setMessage(update.getMessage());
            }
        } catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException
                | BadPaddingException | NoSuchProviderException | InvalidAlgorithmParameterException e) {
            throw new DataException("Error encrypting message", e);
        }
        setEncrypted(update.isEncrypted());
        setTags(update.getTags());
    }
}