package de.ronnyfriedland.knowledgebase.repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.PathNotFoundException;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.jcr.Value;
import javax.jcr.ValueFormatException;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;
import javax.jcr.query.qom.QueryObjectModel;
import javax.jcr.query.qom.QueryObjectModelFactory;
import javax.jcr.query.qom.Selector;

import org.apache.jackrabbit.commons.JcrUtils;

import de.ronnyfriedland.knowledgebase.entity.Document;
import de.ronnyfriedland.knowledgebase.exception.DataException;

/**
 * @author ronnyfriedland
 */
@org.springframework.stereotype.Repository
@org.springframework.beans.factory.annotation.Qualifier("jcr")
public class JackRabbitRepository implements IRepository {

    public static final String PROPERTY_HEADER = "header";
    public static final String PROPERTY_MESSAGE = "message";
    public static final String PROPERTY_TAGS = "tags";

    private Repository repository;

    @PostConstruct
    public void init() {
        try {
            repository = JcrUtils.getRepository();
        } catch (RepositoryException e) {
            throw new de.ronnyfriedland.knowledgebase.exception.RepositoryException(e);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @see de.ronnyfriedland.knowledgebase.repository.IRepository#getTextDocument(java.lang.String)
     */
    @Override
    public Document<String> getTextDocument(final String key) throws DataException {
        try {
            Session session = repository.login(new SimpleCredentials("admin", "admin".toCharArray()));
            try {
                Node root = session.getRootNode();
                Node node = root.getNode(key);

                return convertToDocument(node);
            } finally {
                session.logout();
            }
        } catch (RepositoryException e) {
            throw new DataException(e);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @see de.ronnyfriedland.knowledgebase.repository.IRepository#saveTextDocument(de.ronnyfriedland.knowledgebase.entity.Document)
     */
    @Override
    public String saveTextDocument(final Document<String> message) throws DataException {
        try {
            Session session = repository.login(new SimpleCredentials("admin", "admin".toCharArray()));
            Node node = null;
            try {
                Node root = session.getRootNode();

                if (!root.hasNode(message.getKey())) {
                    node = root.addNode(message.getKey());
                } else {
                    node = root.getNode(message.getKey());
                }

                node.setProperty(PROPERTY_HEADER, message.getHeader());
                node.setProperty(PROPERTY_MESSAGE, message.getMessage());
                node.setProperty(PROPERTY_TAGS, message.getTags());
                session.save();
            } finally {
                session.logout();
            }
            return node.getIdentifier();
        } catch (RepositoryException e) {
            throw new DataException(e);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @see de.ronnyfriedland.knowledgebase.repository.IRepository#listTextDocuments(int, int)
     */
    @Override
    public Collection<Document<String>> listTextDocuments(final int offset, final int max) throws DataException {
        Collection<Document<String>> result = new ArrayList<>();
        try {
            Session session = repository.login(new SimpleCredentials("admin", "admin".toCharArray()));
            try {
                final QueryManager qm = session.getWorkspace().getQueryManager();
                final QueryObjectModelFactory qomf = qm.getQOMFactory();
                final Selector nodeTypeSelector = qomf.selector("{http://www.jcp.org/jcr/nt/1.0}unstructured",
                        "unstructured");

                // final Ordering order = qomf.ascending(new PropertyValue() {
                //
                // @Override
                // public String getSelectorName() {
                // return nodeTypeSelector.getSelectorName();
                // }
                //
                // @Override
                // public String getPropertyName() {
                // return "jcr:created";
                // }
                // });

                final QueryObjectModel qom = qomf.createQuery(nodeTypeSelector, null, null, null);

                if (0 < max) {
                    qom.setLimit(max);
                }
                if (0 < offset) {
                    qom.setOffset(offset);
                }
                qom.getOrderings();
                QueryResult qr = qom.execute();

                NodeIterator nodes = qr.getNodes();
                while (nodes.hasNext()) {
                    Node node = (Node) nodes.next();
                    if (node.hasProperty(PROPERTY_HEADER)) {
                        result.add(convertToDocument(node));
                    }
                }
            } finally {
                session.logout();
            }
        } catch (RepositoryException e) {
            throw new DataException(e);
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
            Session session = repository.login(new SimpleCredentials("admin", "admin".toCharArray()));
            try {

                Node root = session.getRootNode();
                Node node = root.getNode(key);

                if (null != node) {
                    node.remove();
                    session.save();
                }
            } finally {
                session.logout();
            }
        } catch (RepositoryException e) {
            throw new DataException(e);
        }
    }

    private Document<String> convertToDocument(final Node node) throws ValueFormatException, RepositoryException,
            PathNotFoundException {
        String header = node.getProperty(PROPERTY_HEADER).getString();
        String message = node.getProperty(PROPERTY_MESSAGE).getString();

        List<String> tags = new ArrayList<>();
        Value[] tagValues = node.getProperty(PROPERTY_TAGS).getValues();
        for (Value tagValue : tagValues) {
            tags.add(tagValue.getString());
        }

        return new Document<String>(node.getName(), header, message, tags.toArray(new String[tags.size()]));
    }
}
