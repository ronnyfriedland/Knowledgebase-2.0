package de.ronnyfriedland.knowledgebase.repository.fs;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;

import de.ronnyfriedland.knowledgebase.configuration.Configuration;
import de.ronnyfriedland.knowledgebase.exception.DataException;
import de.ronnyfriedland.knowledgebase.repository.IRepository;
import de.ronnyfriedland.knowledgebase.repository.fs.entity.FileDocument;
import de.ronnyfriedland.knowledgebase.resource.RepositoryMetadata;
import de.ronnyfriedland.knowledgebase.resource.RepositoryMetadata.MetadataKeyValue;

/**
 * @author ronnyfriedland
 *
 */
@org.springframework.stereotype.Repository
@org.springframework.beans.factory.annotation.Qualifier("fs")
public class FileSystemRepository implements IRepository<FileDocument<byte[]>> {

    @Autowired
    private Configuration configuration;

    /**
     * {@inheritDoc}
     *
     * @see de.ronnyfriedland.knowledgebase.repository.IRepository#getDocument(java.lang.String)
     */
    @Override
    public FileDocument<byte[]> getDocument(String id) throws DataException {
        try {
            File file = Paths.get(id).toFile();
            byte[] documentBytes = null;

            if (file.isFile()) {
                documentBytes = IOUtils.toByteArray(new FileInputStream(file));
            }

            FileDocument<byte[]> result = new FileDocument<byte[]>(file.getName(), file.getAbsolutePath().replaceAll(
                    "\\\\", "/"),
                    documentBytes, false);

            if (file.isDirectory()) {
                File[] files = file.listFiles();
                for (File child : files) {
                    result.getChildren().add(
                            new FileDocument<byte[]>(child.getName(), child.getAbsolutePath().replaceAll("\\\\", "/"),
                                    null, false));
                }
            }

            return result;
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
    public String saveDocument(FileDocument<byte[]> message) throws DataException {
        throw new IllegalStateException("Not yet implemented!");
    }

    /**
     * {@inheritDoc}
     *
     * @see de.ronnyfriedland.knowledgebase.repository.IRepository#listDocuments(int, int, java.lang.String)
     */
    @Override
    public Collection<FileDocument<byte[]>> listDocuments(final int offset, final int max, final String tag)
            throws DataException {
        try {
            String rootDirectory = configuration.getFilesRootDirectory();
            File root = Paths.get(rootDirectory).toFile();

            if (root.isFile()) {
                byte[] documentBytes = IOUtils.toByteArray(new FileInputStream(root));
                return Collections.singleton(new FileDocument<byte[]>(root.getName(), root.getAbsolutePath()
                        .replaceAll("\\\\", "/"), documentBytes, false));
            }

            FileDocument<byte[]> result = new FileDocument<byte[]>(root.getName(), root.getAbsolutePath().replaceAll(
                    "\\\\", "/"), null, false);

            if (root.isDirectory()) {
                final AtomicInteger count = new AtomicInteger(0);
                File[] files = root.listFiles(new FileFilter() {
                    /**
                     * {@inheritDoc}
                     *
                     * @see java.io.FileFilter#accept(java.io.File)
                     */
                    @Override
                    public boolean accept(File pathname) {
                        int current = count.getAndIncrement();
                        return current >= offset && current < max;
                    }
                });

                for (File file : files) {
                    result.addChild(new FileDocument<byte[]>(file.getName(), file.getAbsolutePath().replaceAll("\\\\",
                            "/"), null, false));
                }
            }

            return result.getChildren();
        } catch (IOException e) {
            throw new DataException(e);
        }
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
