package de.ronnyfriedland.knowledgebase.util;


/**
 * @author ronnyfriedland
 */
public final class TextUtils {
    public static String replaceInvalidChars(final String value) {
        return value.replaceAll("[\\W&&[^-]]", "_").toLowerCase();
    }

}
