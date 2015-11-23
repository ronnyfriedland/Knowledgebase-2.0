package de.ronnyfriedland.knowledgebase.resource.management;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
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
     * Loads the list of documents based on the input parameters
     *
     * @param offset the offset
     * @param limit the limit
     * @param tag the tags
     * @param search the search
     * @return the processed document list template
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/metadata")
    public Response getRepositoryMetadata() {
        try {
            RepositoryDocument metadata = repository.getMetadata();
            return Response.ok(metadata).build();
        } catch (DataException e) {
            LOG.error("Error getting content", e);
            throw new WebApplicationException(Response.status(500).entity("Error getting document").build());
        }
    }
}
