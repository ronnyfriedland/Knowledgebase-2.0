package de.ronnyfriedland.knowledgebase.resource.xml;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "entries")
public class XmlDocumentList implements Iterable<XmlDocument> {
    @XmlElement(name = "entry")
    private final List<XmlDocument> entries = new ArrayList<>();

    public void add(final XmlDocument entry) {
        entries.add(entry);
    }

    @Override
    public Iterator<XmlDocument> iterator() {
        return entries.iterator();
    }

    public void reverseOrder() {
        Collections.reverse(entries);
    }
}