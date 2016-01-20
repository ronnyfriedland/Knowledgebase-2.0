package de.ronnyfriedland.knowledgebase.server;

import java.security.Principal;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
        final User user = authenticate(request);
        request.setSecurityContext(new SecurityContext() {
            private final Principal principal = new Principal() {
                /**
                 * {@inheritDoc}
                 *
                 * @see java.security.Principal#getName()
                 */
                @Override
                public String getName() {
                    return user.username;
                }
            };

            /**
             * {@inheritDoc}
             *
             * @see javax.ws.rs.core.SecurityContext#getUserPrincipal()
             */
            @Override
            public Principal getUserPrincipal() {
                return this.principal;
            }

            /**
             * {@inheritDoc}
             *
             * @see javax.ws.rs.core.SecurityContext#isUserInRole(java.lang.String)
             */
            @Override
            public boolean isUserInRole(final String role) {
                return role.equals(user.role);
            }

            /**
             * {@inheritDoc}
             *
             * @see javax.ws.rs.core.SecurityContext#isSecure()
             */
            @Override
            public boolean isSecure() {
                return configuration.isSslEnabled();
            }

            /**
             * {@inheritDoc}
             *
             * @see javax.ws.rs.core.SecurityContext#getAuthenticationScheme()
             */
            @Override
            public String getAuthenticationScheme() {
                return SecurityContext.BASIC_AUTH;
            }
        });
        return request;
    }

    private User authenticate(final ContainerRequest request) {
        // Extract authentication credentials
        String authentication = request.getHeaderValue(ContainerRequest.AUTHORIZATION);
        if (authentication == null) {
            throw new WebApplicationException(Response.status(400).entity("Authentication credentials are required")
                    .build());
        }
        // only basic auth is supported
        if (!authentication.startsWith("Basic ")) {
            throw new WebApplicationException(Response.status(405).entity("Authentication method not supported")
                    .build());
        }
        authentication = authentication.substring("Basic ".length());
        String[] values = new String(Base64.base64Decode(authentication)).split(":");
        if (values.length < 2) {
            throw new WebApplicationException(Response.status(400)
                    .entity("Invalid syntax, expected 'username:password'").build());
        }
        String username = values[0];
        String password = values[1];
        if (username == null || password == null) {
            throw new WebApplicationException(Response.status(400).entity("Username or password empty").build());
        }

        if (username.equals(configuration.getAuthUsername())
                && password.equals(String.valueOf(configuration.getAuthPassword()))) {
            return new User(configuration.getAuthUsername(), "user");
        } else {
            throw new WebApplicationException(Response.status(401).entity("Invalid username or password").build());
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
