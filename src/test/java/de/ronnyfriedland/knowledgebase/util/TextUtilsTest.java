package de.ronnyfriedland.knowledgebase.util;

import java.security.Security;

import org.apache.commons.lang.StringUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
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
        String valid = "abcdefghijklmnopqrstuvwxyz1234567890ABCDEFGHIJKLMNOPQRSTUVWQYZ-_";
        String invalid = "!\"§$%&/()=?`@*':;,.#+<>|äöüß";

        String result = TextUtils.replaceInvalidChars(valid);
        Assert.assertEquals(valid, result);

        result = TextUtils.replaceInvalidChars(invalid);
        Assert.assertEquals(StringUtils.repeat("_", invalid.length()), result);

        result = TextUtils.replaceInvalidChars(valid + invalid);
        Assert.assertEquals(valid + StringUtils.repeat("_", invalid.length()), result);
    }

    @Test
    public void testEncryptDecrypt() throws Exception {
        if (null == Security.getProvider(BouncyCastleProvider.PROVIDER_NAME)) {
            Security.addProvider(new BouncyCastleProvider());
        }

        String testvalue = "Hello Knowledgebase 2.0";
        String encrypted = TextUtils.encryptStringSymmetric(testvalue);
        Assert.assertNotNull(encrypted);
        String decrypted = TextUtils.decryptStringSymmetric(encrypted);
        Assert.assertNotNull(decrypted);
        Assert.assertEquals(testvalue, decrypted);
    }

}
