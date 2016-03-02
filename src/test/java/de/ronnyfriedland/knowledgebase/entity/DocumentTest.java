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
    public void testConstructor() throws Exception {
        Document<String> subject = new Document<>("key", "header", "message", false, "tag1", "tag2");
        Assert.assertNotNull(subject);

        Assert.assertEquals("key", subject.getKey());
        Assert.assertEquals("header", subject.getHeader());
        Assert.assertEquals("message", subject.getMessage());
        Assert.assertArrayEquals(new String[] { "tag1", "tag2" }, subject.getTags());

        subject = new Document<>("key", "header", "message", false);
        Assert.assertArrayEquals(new String[0], subject.getTags());
    }

    @Test
    public void testFromJcrDocument() throws Exception {
        Document<String> subject = new JCRTextDocument("path", "header", "message", false,
                Collections.singletonList("  tag1 withwithspaces ")).toDocument();
        Assert.assertNotNull(subject);
        Assert.assertNotNull(subject.getKey());
        Assert.assertEquals("/path", subject.getKey());
        Assert.assertNotNull(subject.getHeader());
        Assert.assertEquals("header", subject.getHeader());
        Assert.assertNotNull(subject.getMessage());
        Assert.assertEquals("message", subject.getMessage());
        Assert.assertNotNull(subject.getTags());
        Assert.assertArrayEquals(new String[] { "tag1 withwithspaces" }, subject.getTags()); // trimmed tags
    }
}
