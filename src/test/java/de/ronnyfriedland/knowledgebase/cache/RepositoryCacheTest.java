package de.ronnyfriedland.knowledgebase.cache;

import org.junit.Assert;
import org.junit.Test;

import de.ronnyfriedland.knowledgebase.entity.Document;

/**
 * @author ronnyfriedland
 */
public class RepositoryCacheTest {

    @Test
    public void testCache() throws Exception {
        for (String region : new String[] { "documents", "files" }) {
            RepositoryCache<Document<String>> subject = new RepositoryCache<>(region);

            Assert.assertNull(subject.get("test"));

            subject.put("test", new Document<String>("1", "2", "3", false, new String[0]));
            Assert.assertNotNull(subject.get("test"));
            Assert.assertEquals("1", subject.get("test").getKey());
            Assert.assertEquals("2", subject.get("test").getHeader());
            Assert.assertEquals("3", subject.get("test").getMessage());
            Assert.assertArrayEquals(new String[0], subject.get("test").getTags());

            subject.put("test", new Document<String>("a", "b", "c", false, new String[0]));
            Assert.assertNotNull(subject.get("test"));
            Assert.assertEquals("a", subject.get("test").getKey());
            Assert.assertEquals("b", subject.get("test").getHeader());
            Assert.assertEquals("c", subject.get("test").getMessage());
            Assert.assertArrayEquals(new String[0], subject.get("test").getTags());
        }
    }

}
