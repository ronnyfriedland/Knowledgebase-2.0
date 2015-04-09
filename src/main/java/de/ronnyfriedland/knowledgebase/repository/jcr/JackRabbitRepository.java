package de.ronnyfriedland.knowledgebase.repository.jcr;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.jcr.LoginException;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.jcr.version.VersionException;

import org.apache.commons.lang.StringUtils;
import org.apache.jackrabbit.commons.JcrUtils;
import org.apache.jackrabbit.ocm.exception.ObjectContentManagerException;
import org.apache.jackrabbit.ocm.manager.ObjectContentManager;
import org.apache.jackrabbit.ocm.manager.impl.ObjectContentManagerImpl;
import org.apache.jackrabbit.ocm.mapper.Mapper;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.AnnotationMapperImpl;
import org.apache.jackrabbit.ocm.query.Filter;
import org.apache.jackrabbit.ocm.query.Query;
import org.apache.jackrabbit.ocm.query.QueryManager;

import de.ronnyfriedland.knowledgebase.entity.Document;
import de.ronnyfriedland.knowledgebase.exception.DataException;
import de.ronnyfriedland.knowledgebase.repository.IRepository;

/**
 * @author ronnyfriedland
 */
@org.springframework.stereotype.Repository
@org.springframework.beans.factory.annotation.Qualifier("jcr")
public class JackRabbitRepository implements IRepository {

    private ObjectContentManager ocm;

    @PostConstruct
    public void init() {
        try {
            Map<String, String> params = new HashMap<>();
            params.put("org.apache.jackrabbit.repository.home", "target/jackrabbit");
            params.put("org.apache.jackrabbit.repository.conf", "src/main/resources/repository.xml");
            Repository repository = JcrUtils.getRepository(params);

            ocm = getObjectContentManager(createSession(repository));
        } catch (RepositoryException e) {
            throw new de.ronnyfriedland.knowledgebase.exception.RepositoryException(e);
        }
    }

    @PreDestroy
    public void cleanup() {
        destroySession(ocm.getSession());
    }

    /**
     * {@inheritDoc}
     *
     * @see de.ronnyfriedland.knowledgebase.repository.IRepository#getTextDocument(java.lang.String)
     */
    @Override
    public Document<String> getTextDocument(final String key) {
        JCRTextDocument document = (JCRTextDocument) ocm.getObject("/" + key);
        if (null == document) {
            return null; // not found - wrong path?
        }
        return Document.fromJcrTextDocument(key, document);
    }

    /**
     * {@inheritDoc}
     *
     * @see de.ronnyfriedland.knowledgebase.repository.IRepository#saveTextDocument(de.ronnyfriedland.knowledgebase.entity.Document)
     */
    @Override
    public String saveTextDocument(final Document<String> message) throws DataException {
        JCRTextDocument jcrDocument = new JCRTextDocument(message.getKey(), message.getHeader(), message.getMessage(),
                StringUtils.join(message.getTags(), ","));
        try {
            if (ocm.objectExists(jcrDocument.getPath())) {
                ocm.checkout(jcrDocument.getPath());
                ocm.update(jcrDocument);
                ocm.save();
                ocm.checkin(jcrDocument.getPath());
            } else {
                ocm.insert(jcrDocument);
                ocm.save();
            }
        } catch (VersionException e) {
            throw new DataException(e);
        }
        return jcrDocument.getUuid();
    }

    /**
     * {@inheritDoc}
     *
     * @see de.ronnyfriedland.knowledgebase.repository.IRepository#listTextDocuments(int, int, java.lang.String)
     */
    @SuppressWarnings("unchecked")
    @Override
    public Collection<Document<String>> listTextDocuments(final int offset, final int max, final String tag)
            throws DataException {
        Collection<Document<String>> result = new ArrayList<>();

        QueryManager queryManager = ocm.getQueryManager();
        Filter filter = queryManager.createFilter(JCRTextDocument.class);
        if (null != tag) {
            filter.addLike("tags", tag);
        }
        Query query = queryManager.createQuery(filter);
        query.addOrderByDescending("creationDate");
        Collection<JCRTextDocument> objects = ocm.getObjects(query);

        int count = 0;
        for (JCRTextDocument object : objects) {
            if (count >= offset && count < max) { // should be replaced by query paging support
                String path = object.getPath().substring(1);
                result.add(Document.fromJcrTextDocument(path, object));
            }
            count++;
        }

        return result;
    }

    /**
     * {@inheritDoc}
     *
     * @see de.ronnyfriedland.knowledgebase.repository.IRepository#removeDocument(java.lang.String)
     */
    @Override
    public void removeDocument(final String key) throws DataException {
        try {
            ocm.remove("/" + key);
        } catch (ObjectContentManagerException e) {
            throw new DataException("Path does not exist,", e);
        }
        ocm.save();
    }

    @SuppressWarnings("rawtypes")
    private ObjectContentManager getObjectContentManager(final Session session) throws LoginException,
            RepositoryException {
        List<Class> classes = new ArrayList<>();
        classes.add(JCRTextDocument.class);
        Mapper mapper = new AnnotationMapperImpl(classes);
        return new ObjectContentManagerImpl(session, mapper);
    }

    private Session createSession(final Repository repository) throws LoginException, RepositoryException {
        Session session = repository.login(new SimpleCredentials("admin", "admin".toCharArray()));
        return session;
    }

    private void destroySession(final Session session) {
        session.logout();
    }

}