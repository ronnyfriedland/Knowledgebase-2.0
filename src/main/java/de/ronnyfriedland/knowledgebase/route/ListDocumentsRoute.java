package de.ronnyfriedland.knowledgebase.route;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import spark.QueryParamsMap;
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
        try {
            Map<String, Object> attributes = new HashMap<>();

            Collection<Document<String>> documents = retrieveDocuments(request);
            attributes.put("messages", documents);

            return processResult("list.ftl", attributes).response;
        } catch (DataException e) {
            LOG.error("Error getting content", e);

            response.status(500);
            return "error getting content";
        }
    }

    /**
     * Maps the request attributes and retrieves the data.
     */
    protected Collection<Document<String>> retrieveDocuments(final Request request) throws DataException {
        QueryParamsMap qp = request.queryMap();
        Integer offset = qp.get("offset").integerValue();
        if (null == offset) {
            offset = 0;
        }
        Integer limit = qp.get("limit").integerValue();
        if (null == limit) {
            limit = 10;
        }
        String tag = qp.get("tag").value();
        String search = qp.get("search").value();

        Collection<Document<String>> documents;
        if (null != search) {
            documents = repository.searchTextDocuments(offset, limit, search);
        } else {
            documents = repository.listTextDocuments(offset, limit, tag);
        }
        return documents;
    }
}