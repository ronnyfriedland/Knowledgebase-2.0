package de.ronnyfriedland.knowledgebase.repository.fs;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;

import org.apache.commons.io.IOUtils;

import de.ronnyfriedland.knowledgebase.entity.Document;
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
public class FileSystemRepository implements IRepository<byte[]> {

    /**
     * {@inheritDoc}
     *
     * @see de.ronnyfriedland.knowledgebase.repository.IRepository#getDocument(java.lang.String)
     */
    @Override
    public Document<byte[]> getDocument(String id) throws DataException {
        try {
            File file = Paths.get(id).toFile();
            byte[] documentBytes = IOUtils.toByteArray(new FileInputStream(file));

            return new Document<byte[]>(id, file.getName(), documentBytes, false);
        } catch (IOException e) {
            throw new DataException(e);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @see de.ronnyfriedland.knowledgebase.repository.IRepository#saveDocument(de.ronnyfriedland.knowledgebase.entity.Document)
     */
    @Override
    public String saveDocument(Document<byte[]> message) throws DataException {
        throw new IllegalStateException("Not yet implemented!");
    }

    /**
     * {@inheritDoc}
     *
     * @see de.ronnyfriedland.knowledgebase.repository.IRepository#listDocuments(int, int, java.lang.String)
     */
    @Override
    public Collection<Document<byte[]>> listDocuments(int offset, int max, String tag) throws DataException {
        throw new IllegalStateException("Not yet implemented!");
    }

    /**
     * {@inheritDoc}
     *
     * @see de.ronnyfriedland.knowledgebase.repository.IRepository#searchDocuments(int, int, java.lang.String)
     */
    @Override
    public Collection<Document<byte[]>> searchDocuments(int offset, int max, String search) throws DataException {
        throw new IllegalStateException("Not yet implemented!");
    }

    /**
     * {@inheritDoc}
     *
     * @see de.ronnyfriedland.knowledgebase.repository.IRepository#removeDocument(java.lang.String)
     */
    @Override
    public void removeDocument(String id) throws DataException {
        throw new IllegalStateException("Not yet implemented!");
    }

    /**
     * {@inheritDoc}
     *
     * @see de.ronnyfriedland.knowledgebase.repository.IRepository#getMetadata(java.lang.String)
     */
    @Override
    public RepositoryMetadata getMetadata(String id) throws DataException {
        Path root = Paths.get(id);

        RepositoryMetadata result = new RepositoryMetadata();
        result.setId(id);
        result.setName(id);
        File[] files = root.toFile().listFiles(new FileFilter() {

            @Override
            public boolean accept(File pathname) {
                return pathname.isFile();
            }
        });

        for (File file2 : files) {
            result.addMetadata(new MetadataKeyValue(file2.getName(), file2.getAbsolutePath().replaceAll("\\\\", "/")));
        }

        processNode(root.toFile(), result);
        return result;
    }

    private void processNode(File root, RepositoryMetadata result) {
        File[] children = root.listFiles(new FileFilter() {

            @Override
            public boolean accept(File pathname) {
                return pathname.isDirectory();
            }
        });
        if (null != children) {
            for (File file : children) {
                RepositoryMetadata child = new RepositoryMetadata();
                processNode(file, child);

                child.setId(file.getPath().replaceAll("\\\\", "/"));
                child.setName(file.getName());
                result.addChildren(child);
            }
        }
    }

}
