package de.ronnyfriedland.knowledgebase.freemarker;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import de.ronnyfriedland.knowledgebase.configuration.Configuration;
import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;

/**
 * @author ronnyfriedland
 */
@Component
@SuppressWarnings("rawtypes")
class MessageResolverMethod implements TemplateMethodModelEx {

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private Configuration configuration;

    /**
     * {@inheritDoc}
     * 
     * @see freemarker.template.TemplateMethodModelEx#exec(java.util.List)
     */
    @Override
    public Object exec(final List arguments) throws TemplateModelException {
        if (arguments.size() != 1) {
            throw new TemplateModelException("Wrong number of arguments");
        }
        SimpleScalar code = (SimpleScalar) arguments.get(0);
        if (code == null) {
            throw new TemplateModelException("Invalid code value '" + code + "'");
        }
        return messageSource.getMessage(code.getAsString(), null, configuration.getLocale());
    }
}