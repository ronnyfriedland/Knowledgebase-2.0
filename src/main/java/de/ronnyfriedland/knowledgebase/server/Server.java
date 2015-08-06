package de.ronnyfriedland.knowledgebase.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import spark.Spark;
import de.ronnyfriedland.knowledgebase.configuration.Configuration;
import de.ronnyfriedland.knowledgebase.route.AbstractRoute;

/**
 * @author ronnyfriedland
 */
@Component
public class Server implements Runnable {

    @Autowired
    private Configuration configuration;

    @Autowired
    private AbstractRoute addDocument;

    @Autowired
    private AbstractRoute loadDocument;

    @Autowired
    private AbstractRoute listDocuments;

    @Autowired
    private AbstractRoute exportDocuments;

    @Autowired
    private AbstractRoute importDocuments;

    @Autowired
    private AbstractRoute createDocument;

    @Autowired
    private AbstractRoute deleteDocument;

    /**
     * {@inheritDoc}
     *
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {
        Spark.setPort(configuration.getPort());
        Spark.staticFileLocation(configuration.getStaticContentLocation());

        Spark.get(addDocument);
        Spark.get(exportDocuments);
        Spark.get(loadDocument);
        Spark.get(listDocuments);
        Spark.post(importDocuments);
        Spark.post(createDocument);
        Spark.delete(deleteDocument);
    }
}
