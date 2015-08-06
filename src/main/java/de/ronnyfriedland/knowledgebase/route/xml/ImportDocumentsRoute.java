package de.ronnyfriedland.knowledgebase.route.xml;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.StringReader;

import javax.xml.bind.DataBindingException;
import javax.xml.bind.JAXB;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import de.ronnyfriedland.knowledgebase.entity.Document;
import de.ronnyfriedland.knowledgebase.exception.DataException;
import de.ronnyfriedland.knowledgebase.repository.IRepository;
import de.ronnyfriedland.knowledgebase.route.AbstractRoute;
import de.ronnyfriedland.knowledgebase.util.TextUtils;

public class ImportDocumentsRoute extends AbstractRoute {

    private static final Logger LOG = LoggerFactory.getLogger(ImportDocumentsRoute.class);

    private final IRepository repository;

    /**
     * Creates a new {@link ImportDocumentsRoute} instance.
     *
     * @param repository the repository
     * @param resource the resource
     */
    public ImportDocumentsRoute(final IRepository repository, final String resource) {
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
        XmlDocumentList xmlDocuments = null;

        try {
            QueryParamsMap qp = request.queryMap();
            String importFile = qp.get("importFile").value();
            if (null != importFile && !"".equals(importFile)) {
                try {
                    xmlDocuments = JAXB.unmarshal(new FileReader(importFile), XmlDocumentList.class);
                } catch (FileNotFoundException e) {
                    throw new DataException(e);
                }
            } else {
                String importXml = qp.get("importXml").value();
                if (null != importXml && !"".equals(importXml)) {
                    try {
                        xmlDocuments = JAXB.unmarshal(new StringReader(importXml), XmlDocumentList.class);
                    } catch (DataBindingException e) {
                        throw new DataException(e);
                    }
                }
            }

            if (null != xmlDocuments) {
                for (XmlDocument xmlDocument : xmlDocuments) {
                    String header = xmlDocument.header;
                    String message = xmlDocument.message;
                    String[] tags = xmlDocument.tags;

                    repository.saveTextDocument(new Document<String>(TextUtils.replaceInvalidChars(header), header,
                            message, tags));
                }
            }
            // redirect to overview
            response.redirect("/data");
            return null;
        } catch (DataException e) {
            LOG.error("Error saving content", e);

            response.status(500);
            return "error importing documents";
        }
    }
}
