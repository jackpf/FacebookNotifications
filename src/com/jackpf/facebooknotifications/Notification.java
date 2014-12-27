package com.jackpf.facebooknotifications;

import java.awt.AWTException;
import java.awt.Desktop;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

public class Notification
{
    private TrayIcon trayIcon;
    private PopupMenu menu = new PopupMenu();
    private Map<String, MenuItem> menuItems = new HashMap<String, MenuItem>();

    protected void createMenuItems(ActionListener actionListener)
    {
        menuItems.put("open", new MenuItem("Open Facebook"));
        menuItems.put("quit", new MenuItem("Quit"));

        for (Map.Entry<String, MenuItem> item : menuItems.entrySet()) {
            item.getValue().addActionListener(actionListener);
            menu.add(item.getValue());
        }
    }

    public void createSystemTrayIcon() throws Exception
    {
        if (java.awt.SystemTray.isSupported()) {
            createMenuItems(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent event) {
                    if (event.getSource() == menuItems.get("quit")) {
                        System.exit(0);
                    } else if (event.getSource() == menuItems.get("open")) {
                        try {
                            Desktop.getDesktop().browse(new URI("https://wwww.facebook.com"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

            trayIcon = new TrayIcon(
                ImageIO.read(getClass().getResource("resources/notification_dark.png")),
                "Facebook Notifications",
                menu
            );

            try {
                java.awt.SystemTray.getSystemTray().add(trayIcon);
            } catch (AWTException e) {
                throw new RuntimeException("Unable to add system tray icon", e);
            }

        } else {
            throw new RuntimeException("System tray is not supported");
        }

        // ...
        // some time later
        // the application state has changed - update the image
        if (trayIcon != null) {
            //trayIcon.setImage(updatedImage);
        }
    }
}
