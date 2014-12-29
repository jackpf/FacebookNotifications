package com.jackpf.facebooknotifications;

import com.jackpf.facebooknotifications.Facebook.Notification;
import com.jackpf.facebooknotifications.Facebook.Notifications;
import com.jackpf.facebooknotifications.Helpers.JScrollPopupMenu;

import java.awt.AWTException;
import java.awt.Desktop;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.util.Observable;
import java.util.Observer;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.SwingConstants;

public class NotificationManager implements Observer
{
    private TrayIcon trayIcon;
    private JScrollPopupMenu menu = new JScrollPopupMenu();
    private Notifications notifications;

    public NotificationManager(Notifications notifications)
    {
        this.notifications = notifications;
        notifications.addObserver(this);
    }

    public void createSystemTrayIcon() throws Exception
    {
        if (SystemTray.isSupported()) {
            trayIcon = new TrayIcon(
                ImageIO.read(getClass().getResource("/notification_light.png")),
                "Facebook Notifications",
                null
            );

            trayIcon.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseReleased(MouseEvent e) {
                    if (!menu.isShowing() && notifications.size() > 0) {
                        menu.show(null, e.getX() - 150, e.getY());
                    } else {
                        menu.setVisible(false);
                    }
                }
            });

            try {
                SystemTray.getSystemTray().add(trayIcon);
            } catch (AWTException e) {
                throw new RuntimeException("Unable to add system tray icon", e);
            }

        } else {
            throw new RuntimeException("System tray is not supported");
        }
    }

    private void addNotification(final Notification notification)
    {
        final JMenuItem item = new JMenuItem("<html><font size=-1><b>" + notification.getTitle(25) + "</b><br>" + notification.getPrettyDate() + "</font></html>", new ImageIcon(notification.image));
        item.setVerticalTextPosition(SwingConstants.TOP);

        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                try {
                    Desktop.getDesktop().browse(new URI(notification.link));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                notifications.remove(notification);
                menu.setVisible(false);
            }
        });

        menu.add(item);

        if (notification.position > 1) {
            menu.addSeparator();
        }
    }

    @Override
    public void update(Observable observable, Object data)
    {
        menu.removeAll();

        if (notifications.size() > 0) {
            for (Object o : notifications) {
                addNotification((Notification) o);
            }

            try {
                trayIcon.setImage(ImageIO.read(getClass().getResource("/notification_dark.png")));
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(getClass().getResource("/notification.wav"));
                Clip clip = AudioSystem.getClip();
                clip.open(audioInputStream);
                clip.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                trayIcon.setImage(ImageIO.read(getClass().getResource("/notification_light.png")));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
