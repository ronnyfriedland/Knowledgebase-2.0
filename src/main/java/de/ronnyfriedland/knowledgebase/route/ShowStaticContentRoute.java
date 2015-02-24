package de.ronnyfriedland.knowledgebase.route;

import java.io.IOException;

import org.apache.commons.io.IOUtils;

import spark.Request;
import spark.Response;
import spark.Route;

public class ShowStaticContentRoute extends Route {
    private final String resource;

    public ShowStaticContentRoute(final String resource, final String path) {
        super(path);
        this.resource = resource;
    }

    /**
     * {@inheritDoc}
     *
     * @see spark.Route#handle(spark.Request, spark.Response)
     */
    @Override
    public Object handle(final Request request, final Response response) {
        try {
            byte[] html = IOUtils.toByteArray(Thread.currentThread().getContextClassLoader()
                    .getResourceAsStream(resource));
            return new String(html);
        } catch (IOException e) {
            // should never happen ...
        }
        return null;
    }
}