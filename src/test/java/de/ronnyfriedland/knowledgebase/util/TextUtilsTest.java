package de.ronnyfriedland.knowledgebase.util;

import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author ronnyfriedland
 */
public class TextUtilsTest {

    /**
     * Test method for {@link de.ronnyfriedland.knowledgebase.util.TextUtils#replaceInvalidChars(java.lang.String)}.
     */
    @Test
    public void testReplaceInvalidChars() {
        String valid = "abcdefghijklmnopqrstuvwxyz1234567890-_";
        String invalid = "!\"§$%&/()=?`@*':;,.#+<>|äöüß";

        String result = TextUtils.replaceInvalidChars(valid);
        Assert.assertEquals(valid, result);

        result = TextUtils.replaceInvalidChars(invalid);
        Assert.assertEquals(StringUtils.repeat("_", invalid.length()), result);

        result = TextUtils.replaceInvalidChars(valid + invalid);
        Assert.assertEquals(valid + StringUtils.repeat("_", invalid.length()), result);
    }


}
