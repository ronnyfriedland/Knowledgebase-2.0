package de.ronnyfriedland.knowledgebase.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import spark.Spark;
import de.ronnyfriedland.knowledgebase.configuration.Configuration;
import de.ronnyfriedland.knowledgebase.repository.IRepository;
import de.ronnyfriedland.knowledgebase.route.ListDocumentsRoute;
import de.ronnyfriedland.knowledgebase.route.LoadOrInitDocumentRoute;
import de.ronnyfriedland.knowledgebase.route.CreateNewDocumentRoute;

/**
 * @author ronnyfriedland
 */
@Component
public class Server implements Runnable {

    @Autowired
    private IRepository repository;

    @Autowired
    private Configuration configuration;

    /**
     * {@inheritDoc}
     *
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {
        Spark.setPort(configuration.getPort());
        Spark.staticFileLocation(configuration.getStaticContentLocation());

        Spark.get(new LoadOrInitDocumentRoute(repository, "/data/add"));
        Spark.get(new LoadOrInitDocumentRoute(repository, "/data/:key"));
        Spark.get(new ListDocumentsRoute(repository, "/data"));
        Spark.post(new CreateNewDocumentRoute(repository, "/data"));
    }
}
