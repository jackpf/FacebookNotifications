package com.jackpf.facebooknotifications.Helpers;

import com.jackpf.facebooknotifications.Facebook.Notification;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JMenuItem;
import javax.swing.border.EmptyBorder;

public class JInteractiveMenuItem extends JMenuItem
{
    private Notification notification;
    private static final String text = "<html><font size=-1><b>%s</b><br><font color=gray>%s</font></font></html>";
    private long lastUpdate = 0;

    public JInteractiveMenuItem(Notification notification, final Color hoverColour)
    {
        super(String.format(text, notification.getTitle(35), notification.getPrettyDate()), notification.image);

        this.notification = notification;

        addMouseListener(new MouseListener()
        {
            @Override
            public void mouseClicked(MouseEvent e) { }

            @Override
            public void mousePressed(MouseEvent e) { }

            @Override
            public void mouseReleased(MouseEvent e) { }

            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(hoverColour);
                //setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(null);
                //setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });

        setBorder(new EmptyBorder(0, 0, 5, 0));
    }

    @Override
    public void paint(Graphics g)
    {
        long t = System.currentTimeMillis() / 1000;
        if (t - lastUpdate > 3) {
            setText(String.format(text, notification.getTitle(35), notification.getPrettyDate()));
            lastUpdate = t;
        }

        super.paint(g);
    }
}
