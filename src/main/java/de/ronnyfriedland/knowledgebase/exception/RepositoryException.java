package de.ronnyfriedland.knowledgebase.exception;

/**
 * Error accessing data repository
 *
 * @author ronnyfriedland
 */
public class RepositoryException extends RuntimeException {

    /**
     * the serialVersionUID
     */
    private static final long serialVersionUID = 6740211887521004068L;

    public RepositoryException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public RepositoryException(final Throwable cause) {
        super(cause);
    }

}
