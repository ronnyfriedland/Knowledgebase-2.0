/*
 * Copyright (c) 2016 T-Systems Multimedia Solutions GmbH
 * Riesaer Str. 5, 01129 Dresden, Germany
 * All rights reserved.
 */
package de.ronnyfriedland.knowledgebase.server;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerResponse;
import com.sun.jersey.spi.container.ContainerResponseFilter;

import de.ronnyfriedland.knowledgebase.resource.AbstractResource;

/**
 * @author rofr &lt;Ronny.Friedland@t-systems.com&gt;
 */
@Component
public class ErrorPageResponseFilter extends AbstractResource implements ContainerResponseFilter {

    @Override
    protected String processResult(final String template, final Map<String, Object> attributes) {
        return super.processResult(template, attributes);
    }

    /**
     * {@inheritDoc}
     *
     * @see com.sun.jersey.spi.container.ContainerResponseFilter#filter(com.sun.jersey.spi.container.ContainerRequest,
     *      com.sun.jersey.spi.container.ContainerResponse)
     */
    @Override
    public ContainerResponse filter(final ContainerRequest request, final ContainerResponse response) {
        if (200 < response.getStatus()) {
            Map<String, Object> attributes = new HashMap<>();
            attributes.put("error", response.getEntity());
            response.setEntity(processResult("error.ftl", attributes));
        }
        return response;
    }
}
