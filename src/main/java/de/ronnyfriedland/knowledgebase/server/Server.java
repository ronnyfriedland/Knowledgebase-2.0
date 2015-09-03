package de.ronnyfriedland.knowledgebase.server;

import java.io.IOException;

import org.glassfish.grizzly.http.server.CLStaticHttpHandler;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.NetworkListener;
import org.glassfish.grizzly.servlet.ServletRegistration;
import org.glassfish.grizzly.servlet.WebappContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sun.jersey.spi.spring.container.servlet.SpringServlet;

import de.ronnyfriedland.knowledgebase.configuration.Configuration;

/**
 * @author ronnyfriedland
 */
@Component
public class Server implements Runnable {

    @Autowired
    private Configuration configuration;

    private final HttpServer server = new HttpServer();

    /**
     * {@inheritDoc}
     *
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {
        try {
            NetworkListener listener = new NetworkListener("grizzly2", "localhost", configuration.getPort());
            server.addListener(listener);
            server.getServerConfiguration().addHttpHandler(
                    new CLStaticHttpHandler(Thread.currentThread().getContextClassLoader(),
                            configuration.getStaticContentLocation()), "/");
            WebappContext ctx = new WebappContext("ctx", "/data");
            final ServletRegistration reg = ctx.addServlet("spring", new SpringServlet());
            reg.addMapping("/*");
            ctx.addContextInitParameter("contextConfigLocation", "classpath:context.xml");
            ctx.addListener("org.springframework.web.context.ContextLoaderListener");
            ctx.addListener("org.springframework.web.context.request.RequestContextListener");
            ctx.deploy(server);

            server.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void shutdown() {
        server.shutdown();
    }
}
