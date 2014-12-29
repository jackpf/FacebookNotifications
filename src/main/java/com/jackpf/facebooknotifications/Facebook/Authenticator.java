package com.jackpf.facebooknotifications.Facebook;

import org.apache.commons.io.IOUtils;
import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.FacebookApi;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URI;

import javax.swing.JOptionPane;

public class Authenticator
{
    private final String ACCESS_TOKEN_FILE = System.getProperty("user.home") + "/.FacebookNotifications/access_token";
    private String apiKey;
    private String apiSecret;
    private static String accessToken;

    public Authenticator(String apiKey, String apiSecret)
    {
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
        reloadAccessToken();
    }

    private void reloadAccessToken()
    {
        File file = new File(ACCESS_TOKEN_FILE);
        file.getParentFile().mkdirs();

        InputStream in = null;

        try {
            in = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            accessToken = IOUtils.toString(reader);
            System.out.println("Reloaded access token");
        } catch (Exception e) {

        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {

            }
        }
    }

    private void persistAccessToken()
    {
        Writer writer = null;

        try {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(ACCESS_TOKEN_FILE)));
            writer.write(accessToken);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                writer.close();
            } catch (Exception e) {

            }
        }
    }

    public String getAccessToken()
    {
        return getAccessToken(false);
    }

    public String getAccessToken(boolean forceReload)
    {
        if (!forceReload && accessToken != null) {
            return accessToken;
        }

        try {
            OAuthService service = new ServiceBuilder()
                .provider(FacebookApi.class)
                .apiKey(apiKey)
                .apiSecret(apiSecret)
                .scope("manage_notifications")
                .callback("http://localhost:8080/oauth_redirect/")
                .build();

            Desktop.getDesktop().browse(new URI(service.getAuthorizationUrl(null)));
            String authorisationCode = receiveAuthorisationCode();

            if (authorisationCode == null) {
                System.exit(-1);
            }

            Verifier verifier = new Verifier(authorisationCode);
            accessToken = service.getAccessToken(null, verifier).getToken();

            persistAccessToken();

            return accessToken;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String receiveAuthorisationCode()
    {
        return JOptionPane.showInputDialog(
            null,
            "Please grant permissions on Facebook and paste the authorisation code here:",
            "Authorisation Code",
            JOptionPane.INFORMATION_MESSAGE
        );
    }
}
