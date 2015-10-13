package de.ronnyfriedland.knowledgebase.resource.document;

import java.util.Collections;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import de.ronnyfriedland.knowledgebase.entity.Document;
import de.ronnyfriedland.knowledgebase.exception.DataException;
import de.ronnyfriedland.knowledgebase.freemarker.TemplateProcessor;
import de.ronnyfriedland.knowledgebase.repository.IRepository;

/**
 * Test {@link DocumentResource}
 *
 * @author ronnyfriedland
 */
public class DocumentResourceTest {

    @InjectMocks
    private DocumentResource subject;

    @Mock
    private IRepository repository;

    @Mock
    private TemplateProcessor templateProcessor;

    @Before
    public void init() throws Exception {
        MockitoAnnotations.initMocks(this);

        Mockito.when(repository.getTextDocument("valid-id")).thenReturn(
                new Document<String>("valid-id", "valid-id", "Hello World", "tag1", "tag2"));
        Mockito.when(repository.getTextDocument("invalid-id")).thenReturn(null);
        Mockito.when(repository.getTextDocument("exception-id")).thenThrow(new DataException(new RuntimeException()));

        Mockito.when(repository.listTextDocuments(0, 100, "tag1")).thenReturn(
                Collections.singletonList(new Document<String>("valid-id", "valid-id", "Hello World", "tag1", "tag2")));
        Mockito.when(repository.searchTextDocuments(0, 100, "valid-search")).thenReturn(
                Collections.singletonList(new Document<String>("valid-search", "valid-search", "Hello World", "tag1",
                        "tag2")));
    }

    @Test
    public void testLoadDocumentString() {
        Response document1 = subject.loadDocument("valid-id");
        Assert.assertNotNull(document1);
        Assert.assertEquals(200, document1.getStatus());

        Response document2 = subject.loadDocument("invalid-id");
        Assert.assertNotNull(document2);
        Assert.assertEquals(404, document2.getStatus());

        try {
            subject.loadDocument("exception-id");
            Assert.fail("Exception expected !");
        } catch (WebApplicationException e) {
            Assert.assertEquals(500, e.getResponse().getStatus());
        }
    }

    @Test
    public void testInitDocument() {
        Response document = subject.initDocument();
        Assert.assertNotNull(document);
        Assert.assertEquals(200, document.getStatus());
    }

    @Test
    public void testSaveDocument() {
        for (int i = 0; i < 2; i++) {
            Response document = subject.saveDocument("valid-id", "tag1,tag2", "Hello World");
            Assert.assertNotNull(document);
            Assert.assertEquals(301, document.getStatus());
        }
    }

    @Test
    public void testDeleteDocument() {
        for (String id : new String[] { "valid-id", "invalid-id", "exception-id", null }) {
            Response document = subject.deleteDocument(id);
            Assert.assertNotNull(document);
            Assert.assertEquals(200, document.getStatus());
        }
    }

    @Test
    public void testListDocuments() {
        Assert.assertNotNull(subject.loadDocument(0, 100, "", "valid-search"));
        Assert.assertEquals(200, subject.loadDocument(0, 100, "", "valid-search").getStatus());

        Assert.assertNotNull(subject.loadDocument(0, 100, "tag1", ""));
        Assert.assertEquals(200, subject.loadDocument(0, 100, "tag1", "").getStatus());
    }
}
