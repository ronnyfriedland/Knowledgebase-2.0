package de.ronnyfriedland.knowledgebase.exception;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author rofr &lt;Ronny.Friedland@t-systems.com&gt;
 */
public class RepositoryExceptionTest {

    @Test
    public void test() {
        RepositoryException ex = new RepositoryException(new RuntimeException());
        Assert.assertNotNull(ex);
        Assert.assertTrue(ex.getCause() instanceof RuntimeException);

        ex = new RepositoryException("testexception", new RuntimeException());
        Assert.assertNotNull(ex);
        Assert.assertEquals("testexception", ex.getMessage());
        Assert.assertTrue(ex.getCause() instanceof RuntimeException);
    }

}
