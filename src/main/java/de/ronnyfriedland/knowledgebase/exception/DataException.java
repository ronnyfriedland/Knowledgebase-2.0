package de.ronnyfriedland.knowledgebase.exception;

/**
 * Error handling data persistence
 *
 * @author ronnyfriedland
 */
public class DataException extends Exception {

    /**
     * the serialVersionUID
     */
    private static final long serialVersionUID = 196747232387730657L;

    public DataException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public DataException(final Throwable cause) {
        super(cause);
    }

}
