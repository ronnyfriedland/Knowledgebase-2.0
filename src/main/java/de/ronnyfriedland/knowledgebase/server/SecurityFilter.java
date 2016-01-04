package de.ronnyfriedland.knowledgebase.server;

import java.security.Principal;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.SecurityContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sun.jersey.api.container.MappableContainerException;
import com.sun.jersey.core.util.Base64;
import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;

import de.ronnyfriedland.knowledgebase.configuration.Configuration;

/**
 * Basic Auth security filter.
 *
 * @author ronnyfriedland
 */
@Component
public class SecurityFilter implements ContainerRequestFilter {

    @Autowired
    private Configuration configuration;

    /**
     * {@inheritDoc}
     *
     * @see com.sun.jersey.spi.container.ContainerRequestFilter#filter(com.sun.jersey.spi.container.ContainerRequest)
     */
    @Override
    public ContainerRequest filter(final ContainerRequest request) {
        User user = authenticate(request);
        request.setSecurityContext(new Authorizer(user));
        return request;
    }

    private User authenticate(final ContainerRequest request) {
        // Extract authentication credentials
        String authentication = request.getHeaderValue(ContainerRequest.AUTHORIZATION);
        if (authentication == null) {
            throw new MappableContainerException(new AuthenticationException("Authentication credentials are required"));
        }
        // only basic auth is supported
        if (!authentication.startsWith("Basic ")) {
            throw new MappableContainerException(new AuthenticationException("Authentication method not supported"));
        }
        authentication = authentication.substring("Basic ".length());
        String[] values = new String(Base64.base64Decode(authentication)).split(":");
        if (values.length < 2) {
            throw new WebApplicationException(400); // Invalid syntax, expected "username:password"
        }
        String username = values[0];
        String password = values[1];
        if (username == null || password == null) {
            throw new WebApplicationException(400);
        }

        if (username.equals(configuration.getAuthUsername())
                && password.equals(String.valueOf(configuration.getAuthPassword()))) {
            return new User(configuration.getAuthUsername(), "user");
        } else {
            throw new MappableContainerException(new AuthenticationException("Invalid username or password"));
        }
    }

    public class Authorizer implements SecurityContext {

        private User user;
        private Principal principal;

        public Authorizer(final User user) {
            this.user = user;
            this.principal = new Principal() {

                @Override
                public String getName() {
                    return user.username;
                }
            };
        }

        @Override
        public Principal getUserPrincipal() {
            return this.principal;
        }

        @Override
        public boolean isUserInRole(final String role) {
            return role.equals(user.role);
        }

        @Override
        public boolean isSecure() {
            return configuration.isSslEnabled();
        }

        @Override
        public String getAuthenticationScheme() {
            return SecurityContext.BASIC_AUTH;
        }
    }

    public class User {

        public String username;
        public String role;

        public User(final String username, final String role) {
            this.username = username;
            this.role = role;
        }
    }
}
