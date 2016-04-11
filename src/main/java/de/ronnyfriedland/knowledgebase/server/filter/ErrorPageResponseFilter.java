package de.ronnyfriedland.knowledgebase.server.filter;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerResponse;
import com.sun.jersey.spi.container.ContainerResponseFilter;

import de.ronnyfriedland.knowledgebase.freemarker.TemplateProcessor;

/**
 * @author rofr &lt;Ronny.Friedland@t-systems.com&gt;
 */
@Component
public class ErrorPageResponseFilter implements ContainerResponseFilter {

    @Autowired
    private TemplateProcessor templateProcessor;

    /**
     * {@inheritDoc}
     *
     * @see com.sun.jersey.spi.container.ContainerResponseFilter#filter(com.sun.jersey.spi.container.ContainerRequest,
     *      com.sun.jersey.spi.container.ContainerResponse)
     */
    @Override
    public ContainerResponse filter(final ContainerRequest request, final ContainerResponse response) {
        if (200 < response.getStatus()) {
            int status = response.getStatus();
            Object entity = response.getEntity();
            Map<String, Object> attributes = new HashMap<>();
            attributes.put("status", status);
            attributes.put("error", null == entity ? "" : entity);
            response.setEntity(templateProcessor.getProcessedTemplate("error.ftl", attributes));
        }
        return response;
    }
}
