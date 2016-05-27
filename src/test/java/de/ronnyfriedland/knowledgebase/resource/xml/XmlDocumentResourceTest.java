package de.ronnyfriedland.knowledgebase.resource.xml;

import java.security.MessageDigest;
import java.util.Collections;

import javax.ws.rs.core.Response;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import de.ronnyfriedland.knowledgebase.entity.Document;
import de.ronnyfriedland.knowledgebase.repository.IRepository;

public class XmlDocumentResourceTest {

    @InjectMocks
    private XmlDocumentResource subject;

    @Mock
    private IRepository repository;

    @Mock
    private MessageDigest digest;

    @Mock
    private Marshaller marshaller;

    @Mock
    private Unmarshaller unmarshaller;

    @Before
    public void init() throws Exception {
        MockitoAnnotations.initMocks(this);

        Mockito.when(repository.listDocuments(0, 100, "tag1")).thenReturn(
                Collections.singletonList(new Document<String>("valid-id", "valid-id", "Hello World", false, "tag1",
                        "tag2")));
        Mockito.when(repository.searchDocuments(0, 100, "valid-search")).thenReturn(
                Collections.singletonList(new Document<String>("valid-search", "valid-search", "Hello World", false,
                        "tag1", "tag2")));
    }

    @Test
    public void testExportXml() {
        Response result1 = subject.exportXml(0, 100, "tag1", "");
        Assert.assertNotNull(result1);
        Assert.assertEquals(200, result1.getStatus());
        Assert.assertNotNull(result1.getMetadata().get("Content-Disposition"));
        Assert.assertNotNull(result1.getMetadata().get("Content-Type"));
        Assert.assertNull(result1.getMetadata().get("unknown"));

        Response result2 = subject.exportXml(0, 100, "", "valid-search");
        Assert.assertNotNull(result2);
        Assert.assertEquals(200, result1.getStatus());
    }

    @Test
    public void testImportXml() {
        for (String param1 : new String[] { null, "", "test.xml" }) {
            for (String param2 : new String[] { null, "", "<entries/>" }) {
                Response result1 = subject.importXml(param1, param2);
                Assert.assertNotNull(result1);
                Assert.assertEquals(301, result1.getStatus());
            }
        }
    }

}
