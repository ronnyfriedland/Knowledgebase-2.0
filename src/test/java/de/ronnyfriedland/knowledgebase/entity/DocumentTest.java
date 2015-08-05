package de.ronnyfriedland.knowledgebase.entity;

import java.util.Collections;

import org.junit.Assert;
import org.junit.Test;

import de.ronnyfriedland.knowledgebase.repository.jcr.JCRTextDocument;

/**
 * @author ronnyfriedland
 */
public class DocumentTest {

    @Test
    public void testConstructor() {
        Document<String> subject = new Document<>("key", "header", "message", "tag1", "tag2");
        Assert.assertNotNull(subject);

        Assert.assertEquals("key", subject.getKey());
        Assert.assertEquals("header", subject.getHeader());
        Assert.assertEquals("message", subject.getMessage());
        Assert.assertArrayEquals(new String[] { "tag1", "tag2" }, subject.getTags());

        subject = new Document<>("key", "header", "message");
        Assert.assertArrayEquals(new String[0], subject.getTags());
    }

    @Test
    public void testFromJcrDocument() {
        Document<String> subject = Document.fromJcrTextDocument("path", new JCRTextDocument());
        Assert.assertNotNull(subject);
        Assert.assertNotNull(subject.getKey());
        Assert.assertEquals("path", subject.getKey());
        Assert.assertNull(subject.getHeader());
        Assert.assertNull(subject.getMessage());
        Assert.assertNotNull(subject.getTags());
        Assert.assertArrayEquals(new String[0], subject.getTags());

        subject = Document.fromJcrTextDocument("path",
                new JCRTextDocument("path", "header", "message", Collections.singletonList("  tag1 withwithspaces ")));
        Assert.assertNotNull(subject);
        Assert.assertNotNull(subject.getKey());
        Assert.assertEquals("path", subject.getKey());
        Assert.assertNotNull(subject.getHeader());
        Assert.assertEquals("header", subject.getHeader());
        Assert.assertNotNull(subject.getMessage());
        Assert.assertEquals("message", subject.getMessage());
        Assert.assertNotNull(subject.getTags());
        Assert.assertArrayEquals(new String[] { "tag1 withwithspaces" }, subject.getTags()); // trimmed tags
    }
}
