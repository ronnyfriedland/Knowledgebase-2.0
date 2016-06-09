package de.ronnyfriedland.knowledgebase.launcher;

import java.awt.Desktop;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.security.Security;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import de.ronnyfriedland.knowledgebase.configuration.Configuration;
import de.ronnyfriedland.knowledgebase.server.Server;

/**
 * The main class to start the application
 *
 * @author ronnyfriedland
 */
public class Launcher {

    private static final Logger LOG = LoggerFactory.getLogger(Launcher.class);

    private static final ExecutorService es = Executors.newFixedThreadPool(1);

    /**
     * The main method
     *
     * @param args application parameters
     */
    public static void main(final String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            /**
             * {@inheritDoc}
             *
             * @see java.lang.Runnable#run()
             */
            @Override
            public void run() {
                new Launcher().init();
            }
        });
        Runtime.getRuntime().addShutdownHook(new Thread() {
            /**
             * {@inheritDoc}
             *
             * @see java.lang.Thread#run()
             */
            @Override
            public void run() {
                if (null != es) {
                    es.shutdown();
                }
            }
        });
    }

    private void init() {
        LOG.info("Starting Knowledgebase 2.0 ...");

        if (null == Security.getProvider(BouncyCastleProvider.PROVIDER_NAME)) {
            Security.addProvider(new BouncyCastleProvider());
        }

        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("context.xml");
        // register shutdown hook to close context
        context.registerShutdownHook();

        try {
            Server server = context.getBean("server", Server.class);
            es.execute(server);

            if (SystemTray.isSupported()) {
                try {
                    SystemTray tray = SystemTray.getSystemTray();

                    final Configuration config = context.getBean("configuration", Configuration.class);

                    final String baseurl = String.format("http%s://localhost:%d", config.isSslEnabled() ? "s" : "",
                            config.getPort());

                    final TrayIcon trayIcon = new TrayIcon(new ImageIcon(Thread.currentThread().getContextClassLoader()
                            .getResource("public/images/icon.gif")).getImage());
                    trayIcon.setToolTip("Knowledgebase 2.0 - listen on port " + config.getPort());
                    trayIcon.setPopupMenu(new PopupMenu());
                    trayIcon.addActionListener(new ActionListener() {

                        @Override
                        public void actionPerformed(final ActionEvent e) {
                            try {
                                Desktop.getDesktop().browse(URI.create(baseurl + "/documents"));
                            } catch (IOException e1) {
                                JOptionPane.showMessageDialog(null, "Kann Browser mit Anwendung nicht öffnen.");
                            }
                        }
                    });

                    MenuItem menuItemExit = new MenuItem("Exit");
                    menuItemExit.addActionListener(new ActionListener() {

                        @Override
                        public void actionPerformed(final ActionEvent e) {
                            es.shutdown();
                            System.exit(1);
                        }
                    });
                    MenuItem menuItemBase = new MenuItem("Knowledgebase");
                    menuItemBase.addActionListener(new ActionListener() {

                        @Override
                        public void actionPerformed(final ActionEvent e) {
                            try {
                                Desktop.getDesktop().browse(URI.create(baseurl + "/documents"));
                            } catch (IOException e1) {
                                JOptionPane.showMessageDialog(null, "Kann Browser mit Anwendung nicht öffnen.");
                            }
                        }
                    });
                    PopupMenu popup = new PopupMenu();
                    popup.add(menuItemBase);
                    popup.addSeparator();
                    popup.add(menuItemExit);

                    trayIcon.setPopupMenu(popup);

                    tray.add(trayIcon);
                } catch (Exception e) {
                    LOG.error("Error adding to tray", e);
                    // ignore
                }
            }
        } finally {
            context.close();
        }
    }

}
