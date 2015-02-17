package de.ronnyfriedland.knowledgebase.route;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import spark.Request;
import spark.Response;
import de.ronnyfriedland.knowledgebase.entity.Document;
import de.ronnyfriedland.knowledgebase.exception.DataException;
import de.ronnyfriedland.knowledgebase.repository.IRepository;

public class ListDocumentsRoute extends AbstractRoute {
    private static final Logger LOG = LoggerFactory.getLogger(ListDocumentsRoute.class);

    private final IRepository repository;

    /**
     * Creates a new {@link ListDocumentsRoute} instance.
     *
     * @param repository the repository
     * @param resource the resource
     */
    public ListDocumentsRoute(final IRepository repository, final String resource) {
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
        Map<String, Object> attributes = new HashMap<>();

        try {
            Collection<Document<String>> documents = repository.listTextDocuments(0, 0);
            attributes.put("messages", documents);

            return processResult("list.ftl", attributes).response;
        } catch (DataException e) {
            LOG.error("Error getting content", e);

            response.status(500);
            return "error getting content";
        }
    }
}