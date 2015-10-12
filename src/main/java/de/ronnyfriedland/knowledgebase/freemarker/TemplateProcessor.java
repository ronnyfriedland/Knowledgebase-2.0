package de.ronnyfriedland.knowledgebase.freemarker;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

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

    @Autowired
    private MessageResolverMethod messageResolver;

    protected final Configuration config = new Configuration(Configuration.VERSION_2_3_23);
    {
        config.setTemplateLoader(new ClassTemplateLoader(Server.class, "/templates"));
    }

    @PostConstruct
    private void init() throws TemplateModelException {
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("locale", messageResolver);
        config.setSharedVaribles(model);
    }

    public String getProcessedTemplate(final String template, final Map<String, Object> attributes, final Writer writer)
            throws TemplateNotFoundException, MalformedTemplateNameException, ParseException, IOException,
            TemplateException {
        Template tpl = config.getTemplate(template);
        tpl.process(attributes, writer);

        return writer.toString();
    }
}
