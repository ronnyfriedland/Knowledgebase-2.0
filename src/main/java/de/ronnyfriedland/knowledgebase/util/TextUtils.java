package de.ronnyfriedland.knowledgebase.util;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 * @author ronnyfriedland
 */
public final class TextUtils {

    private static byte[] keyBytes = new byte[] { 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a,
            0x0b, 0x0c, 0x0d, 0x0e, 0x0f, 0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17 };

    public static String replaceInvalidChars(final String value) {
        return value.replaceAll("[\\W&&[^-]]", "_");
    }

    public static String encryptString(final String decrypted) throws NoSuchAlgorithmException, NoSuchPaddingException,
    NoSuchProviderException, InvalidKeyException, InvalidAlgorithmParameterException,
    IllegalBlockSizeException, BadPaddingException {
        SecretKey key = getSecretKey();
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding", BouncyCastleProvider.PROVIDER_NAME);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return Base64.encodeBase64String(cipher.doFinal(decrypted.getBytes()));
    }

    public static String decryptString(final String encrypted) throws NoSuchAlgorithmException, NoSuchPaddingException,
            IllegalBlockSizeException, BadPaddingException, NoSuchProviderException,
            InvalidAlgorithmParameterException, InvalidKeyException {
        SecretKey key = getSecretKey();
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding", BouncyCastleProvider.PROVIDER_NAME);
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decorVal = Base64.decodeBase64(encrypted);
        byte[] decValue = cipher.doFinal(decorVal);
        return new String(decValue);
    }

    private static SecretKey getSecretKey() throws NoSuchAlgorithmException, NoSuchProviderException {
        return new SecretKeySpec(keyBytes, "AES");
    }
}
