package de.ronnyfriedland.knowledgebase.resource;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.collections.KeyValue;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * @author ronnyfriedland
 */
@XmlRootElement(name = "repository")
public class RepositoryNode implements Serializable {

    /** the serialVersionUID */
    private static final long serialVersionUID = 3898017953412626058L;

    @XmlElement
    private String id;

    @XmlElement
    private Collection<RepositoryNode> children = new HashSet<RepositoryNode>();

    @XmlElement(name = "text")
    private String name;

    @XmlElement
    private Collection<NodeAttributes> metadata = new HashSet<>();

    public void setId(final String id) {
        this.id = id;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public void setChildren(final Collection<RepositoryNode> children) {
        this.children = children;
    }

    public void addChildren(final RepositoryNode children) {
        this.children.add(children);
    }

    public void setMetadata(final Collection<NodeAttributes> metadata) {
        this.metadata = metadata;
    }

    public void addMetadata(final NodeAttributes value) {
        this.metadata.add(value);
    }

    /**
     * {@inheritDoc}
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    public static class NodeAttributes implements KeyValue {

        @XmlElement
        private String key;
        @XmlElement
        private String value;

        public NodeAttributes() {
        }

        public NodeAttributes(final String key, final String value) {
            this.key = key;
            this.value = value;
        }

        public void setKey(final String key) {
            this.key = key;
        }

        @Override
        public Object getKey() {
            return key;
        }

        public void setValue(final String value) {
            this.value = value;
        }

        @Override
        public Object getValue() {
            return value;
        }

    }
}
