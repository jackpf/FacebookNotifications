package com.jackpf.facebooknotifications.Facebook;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class ImageCache
{
    private final String CACHE_DIR = System.getProperty("user.home") + "/.FacebookNotifications/cache";
    private final int EXPIRE_TIME = 86400;
    private Authenticator authenticator;

    public ImageCache(Authenticator authenticator)
    {
        this.authenticator = authenticator;

        // Make sure cache dir has been created
        new File(CACHE_DIR).mkdirs();
    }

    public ImageIcon getImage(String userId) throws IOException
    {
        File file = new File(CACHE_DIR + "/" + userId + ".jpg");

        if (isCached(file)) {
            return new ImageIcon(file.toURI().toURL());
        } else {
            return retrieveImage(userId);
        }
    }

    public boolean isCached(File file)
    {
        return file.exists() && (System.currentTimeMillis() - file.lastModified()) / 1000 < EXPIRE_TIME;
    }

    public ImageIcon retrieveImage(String userId) throws IOException
    {
        String url = String.format(
            "https://graph.facebook.com/%s/picture?access_token=%s&width=128&height=128",
            userId,
            authenticator.getAccessToken()
        );

        Image image = ImageIO.read(new URL(url));
        cacheImage(userId, image);

        // Read from disk since it will handle retina icons automatically
        return getImage(userId);
    }

    public void cacheImage(String userId, Image image) throws IOException
    {
        ImageIO.write((RenderedImage) image, "jpg", new File(CACHE_DIR + "/" + userId + "@2x.jpg"));

        // Scale image down for non retina
        BufferedImage buf = new BufferedImage(image.getWidth(null) / 2, image.getHeight(null) / 2, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g = buf.createGraphics();
        g.scale(0.5f, 0.5f);
        g.drawImage(image, 0, 0, null);
        g.dispose();

        ImageIO.write((RenderedImage) buf, "jpg", new File(CACHE_DIR + "/" + userId + ".jpg"));
    }
}
