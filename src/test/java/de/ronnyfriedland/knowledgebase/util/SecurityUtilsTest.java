package de.ronnyfriedland.knowledgebase.util;

import java.security.Security;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author ronnyfriedland
 */
public class SecurityUtilsTest {

    @Test
    public void testEncryptDecrypt() throws Exception {
        if (null == Security.getProvider(BouncyCastleProvider.PROVIDER_NAME)) {
            Security.addProvider(new BouncyCastleProvider());
        }

        String testvalue = "Hello Knowledgebase 2.0";
        String encrypted = SecurityUtils.encryptStringSymmetric(testvalue);
        Assert.assertNotNull(encrypted);
        String decrypted = SecurityUtils.decryptStringSymmetric(encrypted);
        Assert.assertNotNull(decrypted);
        Assert.assertEquals(testvalue, decrypted);
    }

}
