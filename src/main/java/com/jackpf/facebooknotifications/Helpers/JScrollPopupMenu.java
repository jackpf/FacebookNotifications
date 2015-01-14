package com.jackpf.facebooknotifications.Helpers;

import java.awt.Window;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

public class JScrollPopupMenu extends JPopupMenu
{
    private boolean fading = false;

    public JScrollPopupMenu()
    {
        this(null);
    }

    public JScrollPopupMenu(String label)
    {
        super(label);
        setBorder(new EmptyBorder(5, 0, 0, 0));
        setLightWeightPopupEnabled(false);
    }

    @Override
    public void setVisible(boolean visible)
    {
        if (fading) {
            return;
        }

        if (isVisible() && !visible) {
            fadeOut();
        } else {
            super.setVisible(visible);
        }
    }

    public void fadeOut()
    {
        final Window w = SwingUtilities.getWindowAncestor(this);
        final ScheduledExecutorService s = Executors.newScheduledThreadPool(1);
        s.scheduleAtFixedRate(new Runnable()
        {
            private float opacity = 1.0f;

            @Override
            public void run()
            {
                fading = true;
                w.setOpacity(opacity);
                opacity -= 0.01f;

                if (opacity <= 0.0f) {
                    JScrollPopupMenu.super.setVisible(false);
                    fading = false;
                    s.shutdown();
                }
            }
        }, 0, 1, TimeUnit.MILLISECONDS);
    }
}
