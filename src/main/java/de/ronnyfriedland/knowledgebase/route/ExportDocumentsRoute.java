package de.ronnyfriedland.knowledgebase.route;

import java.io.StringWriter;
import java.util.Collection;

import javax.xml.bind.JAXB;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlRootElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import spark.Request;
import spark.Response;
import de.ronnyfriedland.knowledgebase.entity.Document;
import de.ronnyfriedland.knowledgebase.exception.DataException;
import de.ronnyfriedland.knowledgebase.repository.IRepository;

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

            StringWriter sw = new StringWriter();

            for (Document<String> document : documents) {
                JAXB.marshal(new XmlDocument(document.getKey(), document.getMessage(), document.getTags()), sw);
            }

            response.raw().setContentType("text/xml");

            return new Result(sw.toString()).response;
        } catch (DataException e) {
            LOG.error("Error getting content", e);

            response.status(500);
            return "error getting content";
        }
    }

    @XmlRootElement
    private static class XmlDocument {
        @XmlElement
        private String key;
        @XmlElement
        private String message;
        @XmlList
        private String[] tags;

        public XmlDocument() {
        }

        public XmlDocument(final String key, final String message, final String[] tags) {
            this.key = key;
            this.message = message;
            this.tags = tags;
        }
    }

}
