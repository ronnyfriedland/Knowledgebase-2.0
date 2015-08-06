package de.ronnyfriedland.knowledgebase.route.xml;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "entries")
public class XmlDocumentList implements Iterable<XmlDocument> {
    @XmlElement(name = "entry")
    private final Collection<XmlDocument> entries = new ArrayList<>();

    public void add(final XmlDocument entry) {
        entries.add(entry);
    }

    @Override
    public Iterator<XmlDocument> iterator() {
        return entries.iterator();
    }

    public int getSize() {
        return entries.size();
    }
}