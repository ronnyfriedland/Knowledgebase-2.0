package de.ronnyfriedland.knowledgebase.server;

/**
 * @author ronnyfriedland
 */
public class AuthenticationException extends RuntimeException {
    /** the serialVersionUID */
    private static final long serialVersionUID = -5917946696003517687L;

    public AuthenticationException(final String message) {
        super(message);
    }
}
