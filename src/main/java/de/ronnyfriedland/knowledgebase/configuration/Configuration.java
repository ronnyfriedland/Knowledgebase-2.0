package de.ronnyfriedland.knowledgebase.configuration;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Value;

/**
 * The application configuration
 *
 * @author ronnyfriedland
 */
@org.springframework.context.annotation.Configuration
public class Configuration {
    @Value("${server.interface}")
    private String serverInterface;
    @Value("${server.port}")
    private int serverPort;

    @Value("${server.auth.enabled}")
    private boolean authEnabled;
    @Value("${server.auth.username}")
    private String authUsername;
    @Value("${server.auth.password}")
    private char[] authPassword;
    @Value("${server.auth.admin}")
    private boolean authAdmin;

    @Value("${server.ssl.enabled}")
    private boolean sslEnabled;
    @Value("${server.ssl.keystore}")
    private String keystore;
    @Value("${server.ssl.keystore.password}")
    private char[] keystorePassword;

    @Value("${server.ssl.tls.version}")
    private String[] sslProtocolVersion;
    @Value("${server.ssl.ciphersuites}")
    private String[] sslCiphersuites;

    @Value("${server.staticcontent.location}")
    private String staticContentLocation;

    @Value("${server.files.root}")
    private String filesRootDirectory;

    @Value("${server.documents.root}")
    private String documentsRoot;

    @Value("${server.locale}")
    private String locale;

    public int getPort() {
        return serverPort;
    }

    public String getInterface() {
        return serverInterface;
    }

    public Locale getLocale() {
        switch (locale) {
        case "de":
            return Locale.GERMAN;
        case "en":
        default:
            return Locale.ENGLISH;
        }
    }

    public boolean isAuthEnabled() {
        return authEnabled;
    }

    public String getAuthUsername() {
        return authUsername;
    }

    public char[] getAuthPassword() {
        return authPassword;
    }

    public boolean isAuthAdmin() {
        return authAdmin;
    }

    public boolean isSslEnabled() {
        return sslEnabled;
    }

    public String getKeystore() {
        return keystore;
    }

    public char[] getKeystorePassword() {
        return keystorePassword;
    }

    public String[] getSslProtocolVersion() {
        return sslProtocolVersion;
    }

    public String[] getSslCiphersuites() {
        return sslCiphersuites;
    }

    public String getStaticContentLocation() {
        return staticContentLocation;
    }

    public String getFilesRootDirectory() {
        return filesRootDirectory;
    }

    public String getDocumentsRoot() {
        return documentsRoot;
    }
}
