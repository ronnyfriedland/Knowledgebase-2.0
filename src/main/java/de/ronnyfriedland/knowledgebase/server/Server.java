package de.ronnyfriedland.knowledgebase.server;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;

import org.glassfish.grizzly.http.server.CLStaticHttpHandler;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.NetworkListener;
import org.glassfish.grizzly.servlet.ServletRegistration;
import org.glassfish.grizzly.servlet.WebappContext;
import org.glassfish.grizzly.ssl.SSLEngineConfigurator;
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
            if (configuration.isSslEnabled()) {
                listener.setSecure(true);
                SSLContext sslContext = SSLContext.getInstance("TLS");
                sslContext.init(getKeyManagers(), null, new SecureRandom());

                SSLEngineConfigurator sslEngineConfigurator = new SSLEngineConfigurator(sslContext);
                sslEngineConfigurator.setClientMode(false);
                sslEngineConfigurator.setCipherConfigured(true);
                sslEngineConfigurator.setNeedClientAuth(false);
                sslEngineConfigurator.setProtocolConfigured(true);
                sslEngineConfigurator.setEnabledProtocols(configuration.getSslProtocolVersion());
                sslEngineConfigurator.setEnabledCipherSuites(configuration.getSslCiphersuites());
                listener.setSSLEngineConfig(sslEngineConfigurator);
            }
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
        } catch (IOException | GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
    }

    private KeyManager[] getKeyManagers() throws GeneralSecurityException, IOException, NoSuchAlgorithmException,
            KeyStoreException, UnrecoverableKeyException {
        KeyStore ks = KeyStore.getInstance("JKS");
        ks.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(configuration.getKeystore()),
                configuration.getKeystorePassword());
        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(ks, configuration.getKeystorePassword());
        return kmf.getKeyManagers();
    }

    public void shutdown() {
        server.shutdown();
    }
}
