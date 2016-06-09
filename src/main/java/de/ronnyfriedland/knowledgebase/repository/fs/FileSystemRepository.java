package de.ronnyfriedland.knowledgebase.repository.fs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import de.ronnyfriedland.knowledgebase.cache.RepositoryCache;
import de.ronnyfriedland.knowledgebase.configuration.Configuration;
import de.ronnyfriedland.knowledgebase.entity.FileDocument;
import de.ronnyfriedland.knowledgebase.exception.DataException;
import de.ronnyfriedland.knowledgebase.repository.IRepository;
import de.ronnyfriedland.knowledgebase.resource.RepositoryMetadata;
import de.ronnyfriedland.knowledgebase.resource.RepositoryMetadata.MetadataKeyValue;

/**
 * @author ronnyfriedland
 *
 */
@org.springframework.stereotype.Repository
@org.springframework.beans.factory.annotation.Qualifier("fs")
public class FileSystemRepository implements IRepository<FileDocument<byte[]>> {

    private static final Logger LOG = LoggerFactory.getLogger(FileSystemRepository.class);

    private SimpleDateFormat df = new SimpleDateFormat("MM.dd.yyyy HH:mm:ss");

    @Autowired
    private Configuration configuration;

    @Autowired
    private RepositoryCache<FileDocument<byte[]>> fileCache;

    /**
     * {@inheritDoc}
     *
     * @see de.ronnyfriedland.knowledgebase.repository.IRepository#getDocument(java.lang.String)
     */
    @Override
    public FileDocument<byte[]> getDocument(String key) throws DataException {
        FileDocument<byte[]> cachedDocument = fileCache.get(key);
        if (null == cachedDocument) {
            if (LOG.isTraceEnabled()) {
                LOG.trace("Not found in cache: '{}'.", key);
            }
            try {
                File file = Paths.get(key).toFile();
                byte[] documentBytes = null;

                if (file.isFile()) {
                    documentBytes = IOUtils.toByteArray(new FileInputStream(file));
                }

                FileDocument<byte[]> result = new FileDocument<byte[]>(key, file.getAbsolutePath()
                        .replaceAll("\\\\", "/"), documentBytes, false);
                if (!configuration.getFilesRootDirectory().equalsIgnoreCase(key)) {
                    result.setParent(new FileDocument<byte[]>(file.getParent().replaceAll("\\\\", "/"), null, null,
                            false));
                }

                if (file.isDirectory()) {
                    File[] files = file.listFiles();
                    for (File child : files) {
                        result.getChildren().add(
                                new FileDocument<byte[]>(child.getName(), child.getAbsolutePath().replaceAll("\\\\",
                                        "/"), null, false));
                    }
                    Collections.sort(new ArrayList<FileDocument<byte[]>>(result.getChildren()));
                }
                if (LOG.isTraceEnabled()) {
                    LOG.trace("Put entry to cache: '{}'.", result);
                }
                fileCache.put(key, result);
                return result;
            } catch (IOException e) {
                throw new DataException(e);
            }
        } else {
            if (LOG.isTraceEnabled()) {
                LOG.trace("using cached entry for key: '{}' -> '{}'.", key, cachedDocument);
            }
            return cachedDocument;
        }

    }

    /**
     * {@inheritDoc}
     *
     * @see de.ronnyfriedland.knowledgebase.repository.IRepository#saveDocument(de.ronnyfriedland.knowledgebase.entity.Document)
     */
    @Override
    public String saveDocument(FileDocument<byte[]> message) throws DataException {
        try {
            File file = Paths.get(message.getHeader()).toFile();
            if (file.getParentFile().exists()) {
                try (FileOutputStream fos = new FileOutputStream(file)) {
                    fos.write(message.getMessage());
                }
            }
        } catch (IOException e) {
            throw new DataException(e);
        }
        return message.getKey();
    }

    /**
     * {@inheritDoc}
     *
     * @see de.ronnyfriedland.knowledgebase.repository.IRepository#listDocuments(int, int, java.lang.String)
     */
    @Override
    public Collection<FileDocument<byte[]>> listDocuments(final int offset, final int max, final String tag)
            throws DataException {
        throw new IllegalStateException("Not yet implemented!");
    }

    /**
     * {@inheritDoc}
     *
     * @see de.ronnyfriedland.knowledgebase.repository.IRepository#searchDocuments(int, int, java.lang.String)
     */
    @Override
    public Collection<FileDocument<byte[]>> searchDocuments(int offset, int max, String search) throws DataException {
        throw new IllegalStateException("Not yet implemented!");
    }

    /**
     * {@inheritDoc}
     *
     * @see de.ronnyfriedland.knowledgebase.repository.IRepository#removeDocument(java.lang.String)
     */
    @Override
    public void removeDocument(String key) throws DataException {
        File file = Paths.get(key).toFile();
        if (file.exists()) {
            file.delete();
            if (LOG.isTraceEnabled()) {
                LOG.trace("Remove entry with parent from cache: '{}'.", key);
            }
        }
        fileCache.remove(key);
        fileCache.remove(key.substring(0, key.lastIndexOf("/")));
    }

    /**
     * {@inheritDoc}
     *
     * @see de.ronnyfriedland.knowledgebase.repository.IRepository#getMetadata(java.lang.String)
     */
    @Override
    public RepositoryMetadata getMetadata(String id) throws DataException {
        RepositoryMetadata result = new RepositoryMetadata();
        try {
            Path root = Paths.get(id);
            BasicFileAttributes attr = Files.readAttributes(root, BasicFileAttributes.class);

            result.setId(id);
            result.setName(root.toFile().getAbsolutePath());

            result.addMetadata(new MetadataKeyValue("creationdate", df.format(attr.creationTime().toMillis())));
            result.addMetadata(new MetadataKeyValue("lastmodifieddate", df.format(attr.lastModifiedTime().toMillis())));
            result.addMetadata(new MetadataKeyValue("size", String.valueOf(attr.size() / 1024) + " kb"));

        } catch (IOException e) {
            throw new DataException(e);
        }

        return result;
    }

}
