package de.ronnyfriedland.knowledgebase.repository.jcr;

import java.util.Calendar;
import java.util.Date;

import org.apache.jackrabbit.ocm.mapper.impl.annotation.Field;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.Node;

@Node
public class JCRTextDocument {
    @Field(path = true)
    private String path;
    @Field
    private String header;
    @Field
    private String message;
    @Field
    private String tags;
    @Field
    private Date creationDate;

    public JCRTextDocument(final String path, final String header, final String message, final String tags) {
        this();
        if (path.startsWith("/")) {
        } else {
            this.path = new StringBuilder().append("/").append(path).toString();
        }
        this.header = header;
        this.message = message;
        this.tags = tags;
        this.creationDate = Calendar.getInstance().getTime();
    }

    public JCRTextDocument() {
        super();
    }

    public String getPath() {
        return path;
    }

    public void setPath(final String path) {
        this.path = path;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(final String header) {
        this.header = header;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(final String tags) {
        this.tags = tags;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(final Date creationDate) {
        this.creationDate = creationDate;
    }

}