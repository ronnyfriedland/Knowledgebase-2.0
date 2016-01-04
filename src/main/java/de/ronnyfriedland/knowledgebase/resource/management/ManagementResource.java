package de.ronnyfriedland.knowledgebase.resource.management;

import java.util.HashMap;

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
import org.springframework.stereotype.Component;

import de.ronnyfriedland.knowledgebase.exception.DataException;
import de.ronnyfriedland.knowledgebase.repository.IRepository;
import de.ronnyfriedland.knowledgebase.resource.AbstractResource;

/**
 * @author ronnyfriedland
 */
@Path("/management")
@Component
public class ManagementResource extends AbstractResource {

    private static final Logger LOG = LoggerFactory.getLogger(ManagementResource.class);

    @Autowired
    private IRepository repository;

    /**
     * Init
     *
     * @return the processed document template (empty)
     */
    @GET
    @Produces(MediaType.TEXT_HTML)
    public Response initManagement() {
        return Response.ok(processResult("management.ftl", new HashMap<String, Object>())).build();
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
        return getRepositoryMetadata("/");
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
            RepositoryDocument metadata = repository.getMetadata(key);
            return Response.ok(metadata).build();
        } catch (DataException e) {
            LOG.error("Error getting content", e);
            throw new WebApplicationException(Response.status(500).entity("Error getting document").build());
        }
    }

}
