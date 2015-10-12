package de.ronnyfriedland.knowledgebase.resource;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import de.ronnyfriedland.knowledgebase.freemarker.TemplateProcessor;
import freemarker.template.TemplateException;

/**
 * @author ronnyfriedland
 */
public abstract class AbstractResource {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractResource.class);

    @Autowired
    private TemplateProcessor templateProcessor;

    protected String processResult(final String template, final Map<String, Object> attributes) {
        try (StringWriter writer = new StringWriter()) {
            return templateProcessor.getProcessedTemplate(template, attributes, writer);
        } catch (IOException e) {
            LOG.error("Error getting template {}", template);
            return null;
        } catch (TemplateException e) {
            LOG.error("Error processing template {}", template);
            return null;
        }
    }
}