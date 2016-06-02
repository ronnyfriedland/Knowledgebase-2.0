package de.ronnyfriedland.knowledgebase.resource.management;

import java.util.HashMap;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import de.ronnyfriedland.knowledgebase.configuration.Configuration;
import de.ronnyfriedland.knowledgebase.entity.Document;
import de.ronnyfriedland.knowledgebase.exception.DataException;
import de.ronnyfriedland.knowledgebase.freemarker.TemplateProcessor;
import de.ronnyfriedland.knowledgebase.repository.IRepository;
import de.ronnyfriedland.knowledgebase.resource.AbstractResource;
import de.ronnyfriedland.knowledgebase.resource.RepositoryMetadata;

/**
 * @author ronnyfriedland
 */
@Path("/documents/management")
@Component
@RolesAllowed("admin")
public class ManagementResource extends AbstractResource {

    private static final Logger LOG = LoggerFactory.getLogger(ManagementResource.class);

    @Autowired
    @Qualifier("jcr")
    private IRepository<Document<String>> repository;

    @Autowired
    private TemplateProcessor templateProcessor;

    @Autowired
    private Configuration configuration;

    /**
     * Init
     *
     * @return the processed document template (empty)
     */
    @GET
    @Produces(MediaType.TEXT_HTML)
    public Response initManagement() {
        return Response.ok(templateProcessor.getProcessedTemplate("management.ftl", new HashMap<String, Object>()))
                .build();
    }

    /**
     * Returns the content of the repositoy
     *
     * @return the content of the repository
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/metadata")
    public Response getRepositoryMetadata() {
        return getRepositoryMetadata(configuration.getDocumentsRoot());
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

}
