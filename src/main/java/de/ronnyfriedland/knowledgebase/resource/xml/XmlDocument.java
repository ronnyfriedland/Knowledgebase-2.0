package de.ronnyfriedland.knowledgebase.resource.xml;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "entry")
public class XmlDocument {
    @XmlElement(name = "header")
    public String header;
    @XmlElement(name = "message")
    public String message;
    @XmlElementWrapper
    @XmlElement(name = "tag")
    public String[] tags;

    public XmlDocument() {
    }

    public XmlDocument(final String header, final String message, final String[] tags) {
        this.header = header;
        this.message = message;
        this.tags = tags;
    }
}