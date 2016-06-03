package de.ronnyfriedland.knowledgebase.resource.file;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import de.ronnyfriedland.knowledgebase.configuration.Configuration;
import de.ronnyfriedland.knowledgebase.entity.FileDocument;
import de.ronnyfriedland.knowledgebase.exception.DataException;
import de.ronnyfriedland.knowledgebase.freemarker.TemplateProcessor;
import de.ronnyfriedland.knowledgebase.repository.IRepository;
import de.ronnyfriedland.knowledgebase.resource.AbstractDocumentResource;
import de.ronnyfriedland.knowledgebase.resource.RepositoryMetadata;

@Path("/files")
@Component
@RolesAllowed("user")
public class FileResource extends AbstractDocumentResource<FileDocument<byte[]>> {
    private static final Logger LOG = LoggerFactory.getLogger(FileResource.class);

    @Autowired
    @Qualifier("fs")
    private IRepository<FileDocument<byte[]>> repository;

    @Autowired
    private TemplateProcessor templateProcessor;

    @Autowired
    private Configuration configuration;

    /**
     * Retrieve an existing document for the given key.
     *
     * @param key the unique key of the document
     * @return the processed document template with filled in data
     */
    @GET
    @Path("/{key:.+}")
    @Produces(MediaType.TEXT_HTML)
    public Response loadDocument(final @PathParam("key") String key) {
        Map<String, Object> attributes = new HashMap<>();
        try {
            FileDocument<byte[]> document = repository.getDocument(key);
            if (null == document) {
                return Response.status(404).entity("Document not found").build();
            }
            attributes.put("header", document.getHeader());
            attributes.put("message", document.getMessage());
            attributes.put("encrypted", document.isEncrypted());
            attributes.put("files", document.getChildren());
            attributes.put("parent", document.getParent());

            return Response.ok(templateProcessor.getProcessedTemplate("file.ftl", attributes)).build();
        } catch (DataException e) {
            LOG.error("Error getting document", e);
            throw new WebApplicationException(Response.status(500).entity("Error getting document").build());
        }
    }

    /**
     * Loads the list of documents based on the input parameters
     *
     * @param offset the offset
     * @param limit the limit
     * @param tag the tags
     * @return the processed document list template
     */
    @GET
    @Produces(MediaType.TEXT_HTML)
    public Response loadDocument(@QueryParam("offset") final Integer offset, @QueryParam("limit") final Integer limit,
            final @QueryParam("tag") String tag) {
        try {
            Map<String, Object> attributes = new HashMap<>();

            Collection<FileDocument<byte[]>> documents = retrieveData(offset, limit, tag, null);

            attributes.put("files", documents);

            return Response.ok(templateProcessor.getProcessedTemplate("file.ftl", attributes)).build();
        } catch (DataException e) {
            LOG.error("Error getting content", e);
            throw new WebApplicationException(Response.status(500).entity("Error getting document").build());
        }
    }

    /**
     * Retrieve an existing document for the given key.
     *
     * @param key the unique key of the document
     * @return the plain document stored in backend
     */
    @GET
    @Path("/{key:.+}/raw")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response rawFile(final @PathParam("key") String key) {
        try {
            FileDocument<byte[]> document = repository.getDocument(key);
            if (null == document) {
                return Response.status(404).entity("Document not found").build();
            }
            return Response.ok(document.getMessage())
                    .header("Content-Disposition", "attachment; filename=\"" + document.getHeader() + "\"").build();
        } catch (DataException e) {
            LOG.error("Error getting document", e);
            throw new WebApplicationException(Response.status(500).entity("Error getting document").build());
        }
    }

    /**
     * Returns the content of the repository
     *
     * @param key the id of the repository element
     * @return the content of the repository
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/metadata")
    public Response getRepositoryMetadata() {
        return getRepositoryMetadata(configuration.getFilesRootDirectory());
    }

    /**
     * Returns the content of the repository
     *
     * @param key the id of the repository element
     * @return the content of the repository
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/metadata/{key:.+}")
    public Response getRepositoryMetadata(final @PathParam("key") String key) {
        try {
            RepositoryMetadata metadata = repository.getMetadata(key);
            return Response.ok(metadata).build();
        } catch (DataException e) {
            LOG.error("Error getting content", e);
            throw new WebApplicationException(Response.status(500).entity("Error getting document").build());
        }
    }

    /**
     * {@inheritDoc}
     *
     * @see de.ronnyfriedland.knowledgebase.resource.AbstractDocumentResource#getRepository()
     */
    @Override
    protected IRepository<FileDocument<byte[]>> getRepository() {
        return repository;
    }
}