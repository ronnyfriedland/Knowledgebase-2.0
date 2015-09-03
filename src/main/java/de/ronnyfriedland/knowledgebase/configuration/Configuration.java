package de.ronnyfriedland.knowledgebase.configuration;

import org.springframework.beans.factory.annotation.Value;

@org.springframework.context.annotation.Configuration
public class Configuration {

    @Value("${server.port}")
    private int port;
    @Value("${server.staticcontent.location}")
    private String staticContentLocation;

    public int getPort() {
        return port;
    }

    public String getStaticContentLocation() {
        return staticContentLocation;
    }
}
