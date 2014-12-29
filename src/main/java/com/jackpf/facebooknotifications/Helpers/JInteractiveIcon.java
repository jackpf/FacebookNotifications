package com.jackpf.facebooknotifications.Helpers;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class JInteractiveIcon extends JLabel
{
    private ImageIcon normal, focused;

    public JInteractiveIcon(final URL normalResource, final URL focusedResource, final Callback callback)
    {
        super(new ImageIcon(normalResource));

        normal = new ImageIcon(normalResource);
        focused = new ImageIcon(focusedResource);

        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) { }

            @Override
            public void mousePressed(MouseEvent e) { }

            @Override
            public void mouseReleased(MouseEvent e) {
                callback.action(e);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                setIcon(focused);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setIcon(normal);
            }
        });
    }

    public interface Callback
    {
        public void action(MouseEvent e);
    }
}
