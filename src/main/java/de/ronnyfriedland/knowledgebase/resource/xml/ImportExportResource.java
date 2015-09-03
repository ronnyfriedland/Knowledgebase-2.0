package de.ronnyfriedland.knowledgebase.resource.xml;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URI;
import java.util.Collection;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.DataBindingException;
import javax.xml.bind.JAXB;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.ronnyfriedland.knowledgebase.entity.Document;
import de.ronnyfriedland.knowledgebase.exception.DataException;
import de.ronnyfriedland.knowledgebase.repository.IRepository;
import de.ronnyfriedland.knowledgebase.resource.AbstractResource;
import de.ronnyfriedland.knowledgebase.util.TextUtils;

@Path("/xml/")
@Component
@SuppressWarnings("restriction")
public class ImportExportResource extends AbstractResource {

    private static final Logger LOG = LoggerFactory.getLogger(ImportExportResource.class);

    @Autowired
    private IRepository repository;

    /**
     * Exports the list of documents based on the input parameters
     *
     * @param offset the offset
     * @param limit the limit
     * @param tag the tags
     * @param search the search
     * @return xml data
     */
    @GET
    @Path("/export")
    @Produces(MediaType.TEXT_XML)
    public Response exportXml(@QueryParam("offset") Integer offset, @QueryParam("limit") Integer limit,
            final @QueryParam("tag") String tag, final @QueryParam("search") String search) {
        try {
            if (null == offset) {
                offset = 0;
            }
            if (null == limit) {
                limit = 10;
            }

            Collection<Document<String>> documents;
            if (null != search) {
                documents = repository.searchTextDocuments(offset, limit, search);
            } else {
                documents = repository.listTextDocuments(offset, limit, tag);
            }

            XmlDocumentList xmlDocuments = new XmlDocumentList();

            for (Document<String> document : documents) {
                xmlDocuments.add(new XmlDocument(document.getHeader(), document.getMessage(), document.getTags()));
            }

            try (StringWriter sw = new StringWriter()) {
                JAXB.marshal(xmlDocuments, sw);

                return Response.ok(sw.toString()).build();
            } catch (IOException ioE) {
                throw new DataException(ioE);
            }

        } catch (DataException e) {
            LOG.error("Error getting content", e);
            return Response.status(500).entity("Error getting document").build();
        }
    }

    /**
     * Imports the data
     * 
     * @param importFile not empty if source is a xml file
     * @param importXml not empty if source is xml string
     * @return
     */
    @POST
    @Path("/import")
    @Produces(MediaType.TEXT_XML)
    public Response importXml(final @FormParam("importFile") String importFile,
            final @FormParam("importXml") String importXml) {
        XmlDocumentList xmlDocuments = null;

        try {
            if (null != importFile && !"".equals(importFile)) {
                try {
                    xmlDocuments = JAXB.unmarshal(new FileReader(importFile), XmlDocumentList.class);
                } catch (FileNotFoundException e) {
                    throw new DataException(e);
                }
            } else {
                if (null != importXml && !"".equals(importXml)) {
                    try {
                        xmlDocuments = JAXB.unmarshal(new StringReader(importXml), XmlDocumentList.class);
                    } catch (DataBindingException e) {
                        throw new DataException(e);
                    }
                }
            }

            if (null != xmlDocuments) {
                xmlDocuments.reverseOrder();
                for (XmlDocument xmlDocument : xmlDocuments) {
                    String header = xmlDocument.header;
                    String message = xmlDocument.message;
                    String[] tags = xmlDocument.tags;

                    repository.saveTextDocument(new Document<String>(TextUtils.replaceInvalidChars(header), header,
                            message, tags));
                }
            }
            // redirect to overview
            return Response.status(301).location(URI.create("/")).build();
        } catch (DataException e) {
            LOG.error("Error saving content", e);
            return Response.status(500).entity("Error getting document").build();
        }
    }

}
