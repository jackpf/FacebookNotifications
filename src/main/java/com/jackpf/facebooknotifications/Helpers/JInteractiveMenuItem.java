package com.jackpf.facebooknotifications.Helpers;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.border.EmptyBorder;

public class JInteractiveMenuItem extends JMenuItem
{
    public JInteractiveMenuItem(String text, ImageIcon icon, final Color hoverColour)
    {
        super(text, icon);

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
                setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(null);
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });

        setBorder(new EmptyBorder(0, 0, 5, 0));
    }
}
