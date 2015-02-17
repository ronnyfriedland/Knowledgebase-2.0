package de.ronnyfriedland.knowledgebase.route;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import spark.Route;
import de.ronnyfriedland.knowledgebase.server.Server;
import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;

public abstract class AbstractRoute extends Route {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractRoute.class);

    protected final Configuration config = new Configuration();
    {
        config.setTemplateLoader(new ClassTemplateLoader(Server.class, "/spark/template/freemarker"));
    }

    protected class Result {
        String response;
        Exception exception;

        public Result(final String response) {
            this.response = response;
        }

        public Result(final Exception exception) {
            this.exception = exception;
        }
    }

    public AbstractRoute(final String path) {
        super(path);
    }

    protected Result processResult(final String template, final Map<String, Object> attributes) {
        StringWriter writer = new StringWriter();
        try {
            config.getTemplate(template).process(attributes, writer);
        } catch (IOException e) {
            LOG.error("Error getting template {}", template);
            return new Result(e);
        } catch (TemplateException e) {
            LOG.error("Error processing template {}", template);
            return new Result(e);
        }
        return new Result(writer.toString());
    }
}
