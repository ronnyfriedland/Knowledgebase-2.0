package de.ronnyfriedland.knowledgebase.route.xml;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Collection;

import javax.xml.bind.JAXB;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import spark.Request;
import spark.Response;
import de.ronnyfriedland.knowledgebase.entity.Document;
import de.ronnyfriedland.knowledgebase.exception.DataException;
import de.ronnyfriedland.knowledgebase.repository.IRepository;
import de.ronnyfriedland.knowledgebase.route.ListDocumentsRoute;

public class ExportDocumentsRoute extends ListDocumentsRoute {

    private static final Logger LOG = LoggerFactory.getLogger(ExportDocumentsRoute.class);

    /**
     * Creates a new {@link ExportDocumentsRoute} instance.
     *
     * @param repository the repository
     * @param resource the resource
     */
    public ExportDocumentsRoute(final IRepository repository, final String resource) {
        super(repository, resource);
    }

    /**
     * {@inheritDoc}
     *
     * @see spark.Route#handle(spark.Request, spark.Response)
     */
    @Override
    public Object handle(final Request request, final Response response) {
        try {
            Collection<Document<String>> documents = retrieveDocuments(request);
            XmlDocumentList xmlDocuments = new XmlDocumentList();

            for (Document<String> document : documents) {
                xmlDocuments.add(new XmlDocument(document.getHeader(), document.getMessage(), document.getTags()));
            }

            try (StringWriter sw = new StringWriter()) {
                JAXB.marshal(xmlDocuments, sw);
                response.raw().setContentType("text/xml");

                return new Result(sw.toString()).response;
            } catch (IOException ioE) {
                throw new DataException(ioE);
            }

        } catch (DataException e) {
            LOG.error("Error getting content", e);

            response.status(500);
            return "error getting content";
        }
    }
}
