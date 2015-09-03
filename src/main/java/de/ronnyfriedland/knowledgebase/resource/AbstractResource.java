package de.ronnyfriedland.knowledgebase.resource;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.ronnyfriedland.knowledgebase.server.Server;
import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;

public abstract class AbstractResource {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractResource.class);

    protected final Configuration config = new Configuration();
    {
        config.setTemplateLoader(new ClassTemplateLoader(Server.class, "/spark/template/freemarker"));
    }

    protected String processResult(final String template, final Map<String, Object> attributes) {
        try (StringWriter writer = new StringWriter()) {
            config.getTemplate(template).process(attributes, writer);
            return writer.toString();
        } catch (IOException e) {
            LOG.error("Error getting template {}", template);
            return null;
        } catch (TemplateException e) {
            LOG.error("Error processing template {}", template);
            return null;
        }
    }
}
