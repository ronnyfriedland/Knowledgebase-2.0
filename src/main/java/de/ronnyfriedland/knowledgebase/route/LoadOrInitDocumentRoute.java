package de.ronnyfriedland.knowledgebase.route;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import spark.Request;
import spark.Response;
import de.ronnyfriedland.knowledgebase.entity.Document;
import de.ronnyfriedland.knowledgebase.exception.DataException;
import de.ronnyfriedland.knowledgebase.repository.IRepository;

public class LoadOrInitDocumentRoute extends AbstractRoute {
    private static final Logger LOG = LoggerFactory.getLogger(LoadOrInitDocumentRoute.class);

    private final IRepository repository;

    /**
     * Creates a new {@link LoadOrInitDocumentRoute} instance.
     *
     * @param repository the repository
     * @param resource the resource
     */
    public LoadOrInitDocumentRoute(final IRepository repository, final String resource) {
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
            String key = request.params(":key");
            if (null == key) { // if no key present -> prepare new document
                attributes.put("header", "");
                attributes.put("message", "");
                attributes.put("tags", "");

                return processResult("document.ftl", attributes).response;
            } else { // load existing document
                Document<String> document = repository.getTextDocument(key);
                if (null == document) {
                    response.status(404);
                    return "Not found";
                }
                attributes.put("header", document.getHeader());
                attributes.put("message", document.getMessage());
                attributes.put("tags", StringUtils.arrayToDelimitedString(document.getTags(), ","));

                return processResult("document.ftl", attributes).response;
            }

        } catch (DataException e) {
            LOG.error("Error getting document", e);
            response.status(500);
            return "Error getting document";
        }
    }
}