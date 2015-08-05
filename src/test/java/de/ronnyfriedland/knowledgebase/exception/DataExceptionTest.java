package de.ronnyfriedland.knowledgebase.exception;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author rofr &lt;Ronny.Friedland@t-systems.com&gt;
 */
public class DataExceptionTest {

    @Test
    public void test() {
        DataException ex = new DataException(new RuntimeException());
        Assert.assertNotNull(ex);
        Assert.assertTrue(ex.getCause() instanceof RuntimeException);

        ex = new DataException("testexception", new RuntimeException());
        Assert.assertNotNull(ex);
        Assert.assertEquals("testexception", ex.getMessage());
        Assert.assertTrue(ex.getCause() instanceof RuntimeException);
    }

}
