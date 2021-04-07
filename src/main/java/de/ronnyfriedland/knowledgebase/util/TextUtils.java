package de.ronnyfriedland.knowledgebase.util;


/**
 * @author ronnyfriedland
 */
public final class TextUtils {

    /**
     * Remove invalid chars from string
     *
     * @param value the value to clean
     * @return the cleaned string
     */
    public static String replaceInvalidChars(final String value) {
        return value.replaceAll("[\\W&&[^-]]", "_").toLowerCase();
    }

}
