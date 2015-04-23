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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

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
                // todo
            }
        });
    }

    private void init() {
        LOG.info("Starting Knowledgebase 2.0 ...");

        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("context.xml");
        // register shutdown hook to close context
        context.registerShutdownHook();

        ExecutorService es = Executors.newFixedThreadPool(1);
        try {
            Server server = context.getBean("server", Server.class);
            es.execute(server);

            if (SystemTray.isSupported()) {
                try {
                    SystemTray tray = SystemTray.getSystemTray();
                    TrayIcon trayIcon = new TrayIcon(new ImageIcon(Thread.currentThread().getContextClassLoader()
                            .getResource("public/images/icon.gif")).getImage());

                    final Configuration config = context.getBean("configuration", Configuration.class);

                    trayIcon.setToolTip("Knowledgebase 2.0 - listen on port " + config.getPort());
                    trayIcon.setPopupMenu(new PopupMenu());
                    trayIcon.addActionListener(new ActionListener() {

                        @Override
                        public void actionPerformed(final ActionEvent e) {
                            try {
                                Desktop.getDesktop().browse(URI.create("http://localhost:" + config.getPort()));
                            } catch (IOException e1) {
                                JOptionPane.showMessageDialog(null, "Kann Broswer mit Anwendung nicht öffnen.");
                            }
                        }
                    });

                    MenuItem menuItemExit = new MenuItem("Exit");
                    menuItemExit.addActionListener(new ActionListener() {

                        @Override
                        public void actionPerformed(final ActionEvent e) {
                            System.exit(1);
                        }
                    });
                    PopupMenu popup = new PopupMenu();
                    popup.add(menuItemExit);

                    trayIcon.setPopupMenu(popup);

                    tray.add(trayIcon);
                } catch (Exception e) {
                    LOG.error("Error adding to tray", e);
                    // ignore
                }
            }
        } finally {
            es.shutdown();
        }
    }
}
