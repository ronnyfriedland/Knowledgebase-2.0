package de.ronnyfriedland.knowledgebase.route;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import spark.Request;
import spark.Response;
import de.ronnyfriedland.knowledgebase.entity.Document;
import de.ronnyfriedland.knowledgebase.exception.DataException;
import de.ronnyfriedland.knowledgebase.repository.IRepository;

public class CreateNewDocumentRoute extends AbstractRoute {
    private static final Logger LOG = LoggerFactory.getLogger(LoadOrInitDocumentRoute.class);

    private final IRepository repository;

    /**
     * Creates a new {@link CreateNewDocumentRoute} instance.
     *
     * @param repository the repository
     * @param resource the resource
     */
    public CreateNewDocumentRoute(final IRepository repository, final String resource) {
        super(resource);
        this.repository = repository;
    }

    /**
     * {@inheritDoc}
     *
     * @see spark.Route#handle(spark.Request, spark.Response)
     */
    @Override
    public Object handle(final Request request, final Response response) {
        try {
            // remove invalid chars
            String header = request.queryParams("header");
            String key = header.replaceAll("[\\W&&[^-]]", "_");
            // prepare tags
            String[] tags = null;
            if (request.queryParams("tags") != "") {
                tags = request.queryParams("tags").split(",");
            }
            // get message
            String message = request.queryParams("message");
            // save document
            repository.saveTextDocument(new Document<String>(key, header, message, tags));
            // redirect to overview
            response.redirect("/data");
            return null;
        } catch (DataException e) {
            LOG.error("Error saving content", e);

            response.status(500);
            return "error saving document";
        }
    }
}