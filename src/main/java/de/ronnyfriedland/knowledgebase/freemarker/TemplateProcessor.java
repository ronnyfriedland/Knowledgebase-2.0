package de.ronnyfriedland.knowledgebase.freemarker;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.ronnyfriedland.knowledgebase.server.Server;
import freemarker.cache.ClassTemplateLoader;
import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateNotFoundException;

/**
 * @author ronnyfriedland
 */
@Component
public class TemplateProcessor {

    private static final Logger LOG = LoggerFactory.getLogger(TemplateProcessor.class);

    @Autowired
    private MessageResolverMethod messageResolver;

    protected final Configuration config = new Configuration(Configuration.VERSION_2_3_23);

    {
        config.setTemplateLoader(new ClassTemplateLoader(Server.class, "/templates"));
    }

    @PostConstruct
    private void init() throws TemplateModelException {
        Map<String, Object> model = new HashMap<>();
        model.put("locale", messageResolver);
        config.setSharedVariables(model);
    }

    public String getProcessedTemplate(final String template, final Map<String, Object> attributes, final Writer writer)
            throws IOException, TemplateException {
        Template tpl = config.getTemplate(template);
        tpl.process(attributes, writer);

        return writer.toString();
    }

    public String getProcessedTemplate(final String template, final Map<String, Object> attributes) {
        try (StringWriter writer = new StringWriter()) {
            return getProcessedTemplate(template, attributes, writer);
        } catch (IOException e) {
            LOG.error("Error getting template {}", template);
            return null;
        } catch (TemplateException e) {
            LOG.error("Error processing template {}", template);
            return null;
        }
    }
}
