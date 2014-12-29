package com.jackpf.facebooknotifications;

import com.jackpf.facebooknotifications.Facebook.Notification;
import com.jackpf.facebooknotifications.Facebook.Notifications;
import com.jackpf.facebooknotifications.Helpers.JInteractiveIcon;
import com.jackpf.facebooknotifications.Helpers.JInteractiveMenuItem;
import com.jackpf.facebooknotifications.Helpers.JScrollPopupMenu;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
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
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class NotificationManager implements Observer
{
    private TrayIcon trayIcon;
    private JPopupMenu menu = new JScrollPopupMenu();
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

            trayIcon.addMouseListener(new MouseAdapter()
            {
                @Override
                public void mousePressed(MouseEvent e)
                {
                    if (!menu.isShowing()) {
                        menu.show(null, e.getX() - 150, e.getY());
                    } else {
                        menu.setVisible(false);
                    }
                }
            });

            addNoNotifications();
            addFooter(menu);

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
        final JMenuItem item = new JInteractiveMenuItem("<html><font size=-1><b>" + notification.getTitle(35) + "</b><br><font color=gray>" + notification.getPrettyDate() + "</font></font></html>", new ImageIcon(notification.image), new Color(0.93f, 0.96f, 0.98f));
        item.setVerticalTextPosition(SwingConstants.TOP);

        item.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent event)
            {
                notifications.remove(notification);
                menu.setVisible(false);

                try {
                    Desktop.getDesktop().browse(new URI(notification.link));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        menu.add(item);
        menu.addSeparator();
    }

    private void addNoNotifications()
    {
        JLabel label = new JLabel("NO NOTIFICATIONS", SwingConstants.CENTER);
        label.setForeground(Color.gray);
        label.setBorder(new EmptyBorder(5, 5, 5, 5));
        menu.add(label);
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
                addNoNotifications();

                trayIcon.setImage(ImageIO.read(getClass().getResource("/notification_light.png")));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        addFooter(menu);
    }

    /**
     * Add footer to the menu
     * Ideally this wouldn't be in the scrollview, and we wouldn't have to keep adding it again
     */
    private void addFooter(final JPopupMenu menu)
    {
        JPanel p = new JPanel();
        p.setPreferredSize(new Dimension(300, 25));

        JInteractiveIcon close = new JInteractiveIcon(
            getClass().getResource("/close.png"),
            getClass().getResource("/close_focus.png"),
            new JInteractiveIcon.Callback() {
                @Override
                public void action(MouseEvent e) {
                    menu.setVisible(false);
                }
            }
        );

        p.add(close);

        JInteractiveIcon facebook = new JInteractiveIcon(
            getClass().getResource("/facebook.png"),
            getClass().getResource("/facebook_focus.png"),
            new JInteractiveIcon.Callback() {
                @Override
                public void action(MouseEvent e) {
                    try {
                        Desktop.getDesktop().browse(new URI("https://www.facebook.com"));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    menu.setVisible(false);
                }
            }
        );

        p.add(facebook);

        JInteractiveIcon exit = new JInteractiveIcon(
            getClass().getResource("/exit.png"),
            getClass().getResource("/exit_focus.png"),
            new JInteractiveIcon.Callback() {
                @Override
                public void action(MouseEvent e) {
                    System.exit(0);
                }
            }
        );

        p.add(exit);

        menu.add(p);
    }
}
