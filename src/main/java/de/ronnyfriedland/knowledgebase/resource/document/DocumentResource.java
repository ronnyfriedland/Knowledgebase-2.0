package de.ronnyfriedland.knowledgebase.resource.document;

import java.net.URI;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
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
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import de.ronnyfriedland.knowledgebase.entity.Document;
import de.ronnyfriedland.knowledgebase.exception.DataException;
import de.ronnyfriedland.knowledgebase.repository.IRepository;
import de.ronnyfriedland.knowledgebase.resource.AbstractDocumentResource;
import de.ronnyfriedland.knowledgebase.util.TextUtils;

@Path("/")
@Component
public class DocumentResource extends AbstractDocumentResource {
    private static final Logger LOG = LoggerFactory.getLogger(DocumentResource.class);

    @Autowired
    private IRepository repository;

    /**
     * Retrieve an existing document for the given key.
     *
     * @param key the unique key of the document
     * @return the processed document template with filled in data
     */
    @GET
    @Path("/{key}")
    @Produces(MediaType.TEXT_HTML)
    public Response loadDocument(final @PathParam("key") String key) {
        Map<String, Object> attributes = new HashMap<>();
        try {
            Document<String> document = repository.getTextDocument(key);
            if (null == document) {
                return Response.status(404).entity("Not found").build();
            }
            attributes.put("header", document.getHeader());
            attributes.put("message", document.getMessage());
            attributes.put("tags", StringUtils.arrayToDelimitedString(document.getTags(), ","));

            return Response.ok(processResult("document.ftl", attributes)).build();
        } catch (DataException e) {
            LOG.error("Error getting document", e);
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
    @Path("/{key}/raw")
    @Produces(MediaType.TEXT_HTML)
    public Response rawDocument(final @PathParam("key") String key) {
        try {
            Document<String> document = repository.getTextDocument(key);
            if (null == document) {
                return Response.status(404).entity("Not found").build();
            }
            return Response.ok(document.getMessage()).build();
        } catch (DataException e) {
            LOG.error("Error getting document", e);
            throw new WebApplicationException(Response.status(500).entity("Error getting document").build());
        }
    }

    /**
     * Init a new document
     *
     * @return the processed document template (empty)
     */
    @GET
    @Path("/add")
    @Produces(MediaType.TEXT_HTML)
    public Response initDocument() {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("header", "");
        attributes.put("message", "");
        attributes.put("tags", "");

        return Response.ok(processResult("document.ftl", attributes)).build();
    }

    /**
     * Import data
     *
     * @return the processed document template (empty)
     */
    @GET
    @Path("/import")
    @Produces(MediaType.TEXT_HTML)
    public Response importDocuments() {
        return Response.ok(processResult("import.ftl", null)).build();
    }

    /**
     * Saves a new document
     *
     * @param header the header
     * @param tagString the tags
     * @param message the message
     * @return response object
     */
    @POST
    public Response saveDocument(final @FormParam("header") String header, final @FormParam("tags") String tagString,
            final @FormParam("message") String message) {
        try {

            // remove invalid chars
            String key = TextUtils.replaceInvalidChars(header);
            // prepare tags
            String[] tags = null;
            if (!"".equals(tagString)) {
                tags = tagString.split(",");
            }
            // save document
            repository.saveTextDocument(new Document<String>(key, header, message, tags));
            // redirect to overview
            return Response.status(301).location(URI.create("/")).build();
        } catch (DataException e) {
            LOG.error("Error saving content", e);
            throw new WebApplicationException(Response.status(500).entity("Error saving document").build());
        }
    }

    /**
     * Deletes an existing document
     *
     * @param key the unique key of the document
     * @return response object
     */
    @DELETE
    @Path("/{key}")
    public Response deleteDocument(final @PathParam("key") String key) {
        try {
            repository.removeDocument(key);

            return Response.ok().build();
        } catch (DataException e) {
            LOG.error("Error deleting document", e);
            throw new WebApplicationException(Response.status(500).entity("Error deleting document").build());
        }
    }

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
    @Produces(MediaType.TEXT_HTML)
    public Response loadDocument(@QueryParam("offset") final Integer offset, @QueryParam("limit") final Integer limit,
            final @QueryParam("tag") String tag, final @QueryParam("search") String search) {
        try {
            Map<String, Object> attributes = new HashMap<>();

            Collection<Document<String>> documents = retrieveData(offset, limit, tag, search);
            Collection<String> tags = new HashSet<>();
            for (Document<String> document : documents) {
                String[] documentTags = document.getTags();
                for (String documentTag : documentTags) {
                    tags.add(documentTag);
                }
            }

            attributes.put("messages", documents);
            attributes.put("tags", tags);

            return Response.ok(processResult("list.ftl", attributes)).build();
        } catch (DataException e) {
            LOG.error("Error getting content", e);
            throw new WebApplicationException(Response.status(500).entity("Error getting document").build());
        }
    }
}