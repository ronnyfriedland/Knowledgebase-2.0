package de.ronnyfriedland.knowledgebase.route;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import spark.Request;
import spark.Response;
import de.ronnyfriedland.knowledgebase.exception.DataException;
import de.ronnyfriedland.knowledgebase.repository.IRepository;

public class DeleteDocumentRoute extends AbstractRoute {
    private static final Logger LOG = LoggerFactory.getLogger(DeleteDocumentRoute.class);

    private final IRepository repository;

    /**
     * Creates a new {@link DeleteDocumentRoute} instance.
     *
     * @param repository the repository
     * @param resource the resource
     */
    public DeleteDocumentRoute(final IRepository repository, final String resource) {
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
            String key = request.params(":key");
            repository.removeDocument(key);

            return "";
        } catch (DataException e) {
            LOG.error("Error deleting document", e);
            response.status(500);
            return "Error deleting document";
        }
    }
}