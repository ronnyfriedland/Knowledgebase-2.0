package de.ronnyfriedland.knowledgebase.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyManagementException;
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
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.request.RequestContextListener;

import com.sun.jersey.api.core.ResourceConfig;
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
            NetworkListener listener = new NetworkListener("grizzly2", configuration.getInterface(),
                    configuration.getPort());
            if (configuration.isSslEnabled()) {
                listener.setSSLEngineConfig(getSslConfiguration());
                listener.setSecure(true);
            }
            server.addListener(listener);
            server.getServerConfiguration().addHttpHandler(
                    new CLStaticHttpHandler(Thread.currentThread().getContextClassLoader(),
                            configuration.getStaticContentLocation()), "/");

            WebappContext ctx = new WebappContext("ctx", "/data");
            final ServletRegistration reg = ctx.addServlet("spring", new SpringServlet());
            reg.addMapping("/*");

            reg.setInitParameter(ResourceConfig.PROPERTY_CONTAINER_REQUEST_FILTERS,
                    "com.sun.jersey.api.container.filter.LoggingFilter,de.ronnyfriedland.knowledgebase.server.filter.SecurityFilter");
            reg.setInitParameter(
                    ResourceConfig.PROPERTY_CONTAINER_RESPONSE_FILTERS,
                    "com.sun.jersey.api.container.filter.LoggingFilter,de.ronnyfriedland.knowledgebase.server.filter.ErrorPageResponseFilter");

            reg.setInitParameter(ResourceConfig.PROPERTY_RESOURCE_FILTER_FACTORIES,
                    "com.sun.jersey.api.container.filter.RolesAllowedResourceFilterFactory");

            ctx.addContextInitParameter("contextConfigLocation", "classpath:context.xml");
            ctx.addListener(ContextLoaderListener.class.getCanonicalName());
            ctx.addListener(RequestContextListener.class.getCanonicalName());
            ctx.deploy(server);

            server.start();
        } catch (IOException | GeneralSecurityException e) {
            if (server.isStarted()) {
                server.shutdown();
            }
            throw new RuntimeException(e);
        }
    }

    private SSLEngineConfigurator getSslConfiguration() throws NoSuchAlgorithmException, KeyManagementException,
    GeneralSecurityException, IOException, KeyStoreException, UnrecoverableKeyException {
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(getKeyManagers(), null, new SecureRandom());

        SSLEngineConfigurator sslEngineConfigurator = new SSLEngineConfigurator(sslContext);
        sslEngineConfigurator.setClientMode(false);
        sslEngineConfigurator.setCipherConfigured(true);
        sslEngineConfigurator.setNeedClientAuth(false);
        sslEngineConfigurator.setProtocolConfigured(true);
        sslEngineConfigurator.setEnabledProtocols(configuration.getSslProtocolVersion());
        sslEngineConfigurator.setEnabledCipherSuites(configuration.getSslCiphersuites());
        return sslEngineConfigurator;
    }

    private KeyManager[] getKeyManagers() throws GeneralSecurityException, IOException, NoSuchAlgorithmException,
    KeyStoreException, UnrecoverableKeyException {
        KeyStore ks = KeyStore.getInstance("JKS");
        ks.load(new FileInputStream(new File(configuration.getKeystore())), configuration.getKeystorePassword());
        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(ks, configuration.getKeystorePassword());
        return kmf.getKeyManagers();
    }

}
