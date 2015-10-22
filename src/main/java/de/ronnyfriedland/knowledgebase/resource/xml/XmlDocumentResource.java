package de.ronnyfriedland.knowledgebase.resource.xml;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URI;
import java.security.DigestOutputStream;
import java.security.MessageDigest;
import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.output.WriterOutputStream;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.ronnyfriedland.knowledgebase.entity.Document;
import de.ronnyfriedland.knowledgebase.exception.DataException;
import de.ronnyfriedland.knowledgebase.repository.IRepository;
import de.ronnyfriedland.knowledgebase.resource.AbstractDocumentResource;
import de.ronnyfriedland.knowledgebase.util.TextUtils;

@Path("/xml")
@Component
public class XmlDocumentResource extends AbstractDocumentResource {

    private static final Logger LOG = LoggerFactory.getLogger(XmlDocumentResource.class);

    @Autowired
    private IRepository repository;

    private MessageDigest digest;
    private Marshaller marshaller;
    private Unmarshaller unmarshaller;

    @PostConstruct
    private void init() throws Exception {
        digest = MessageDigest.getInstance("SHA-256");

        JAXBContext jaxbContext = JAXBContext.newInstance(XmlDocumentList.class, XmlDocument.class);
        {
            marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        }
        {
            unmarshaller = jaxbContext.createUnmarshaller();
        }
    }

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
    public Response exportXml(@QueryParam("offset") final Integer offset, @QueryParam("limit") final Integer limit,
            final @QueryParam("tag") String tag, final @QueryParam("search") String search) {
        try {
            Collection<Document<String>> documents = retrieveData(offset, limit, tag, search);

            XmlDocumentList xmlDocuments = new XmlDocumentList();

            for (Document<String> document : documents) {
                xmlDocuments.add(new XmlDocument(document.getHeader(), document.getMessage(), document.getTags()));
            }

            String hash;
            StringWriter sw = new StringWriter();
            try (DigestOutputStream dos = new DigestOutputStream(new WriterOutputStream(sw, "UTF-8"), digest)) {
                marshaller.marshal(xmlDocuments, dos);
                hash = Base64.encodeBase64String(dos.getMessageDigest().digest());
            } catch (IOException | JAXBException ex) {
                throw new DataException(ex);
            }

            StringBuilder filename = new StringBuilder("export");
            if (!StringUtils.isBlank(tag)) {
                filename.append("_" + StringUtils.lowerCase(tag));
            }
            if (!StringUtils.isBlank(search)) {
                filename.append("_" + StringUtils.lowerCase(search));
            }
            filename.append(".xml");

            return Response.ok(sw.toString()).header("Content-Disposition", "attachment; filename=" + filename)
                    .header("Content-Type", "text/xml").header("Content-SHA256", hash).build();
        } catch (DataException e) {
            LOG.error("Error getting content", e);
            throw new WebApplicationException(Response.status(500).entity("Error exporting document").build());
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
                    xmlDocuments = (XmlDocumentList) unmarshaller.unmarshal(new File(importFile));
                } catch (JAXBException e) {
                    throw new DataException(e);
                }
            } else {
                if (null != importXml && !"".equals(importXml)) {
                    try {
                        xmlDocuments = (XmlDocumentList) unmarshaller.unmarshal(new StringReader(importXml));
                    } catch (JAXBException e) {
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
            throw new WebApplicationException(Response.status(500).entity("Error importing document").build());
        }
    }

}
