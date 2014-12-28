package com.jackpf.facebooknotifications.Facebook;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.FacebookApi;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

import java.awt.Desktop;
import java.net.URI;
import java.util.Scanner;

public class Authenticator
{
    private Desktop desktop = Desktop.getDesktop();
    private static final String AUTHORISE_URL = "https://graph.facebook.com/oauth/authorize?client_id=%s&redirect_uri=http://www.facebook.com/connect/login_success.html&scope=manage_notifications";
    private String apiKey;
    private String apiSecret;

    public Authenticator(String apiKey, String apiSecret)
    {
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
    }

    public String getApiKey()
    {
        return apiKey;
    }

    public String getApiSecret()
    {
        return apiSecret;
    }

    public String getAccessToken()
    {
        try {
            //String uri = String.format(AUTHORISE_URL, apiKey);
            //desktop.browse(new URI(uri));

            OAuthService service = new ServiceBuilder()
                .provider(FacebookApi.class)
                .apiKey(apiKey)
                .apiSecret(apiSecret)
                .callback("http://localhost/oauth_redirect/")
                .build();

            String authorizationUrl = service.getAuthorizationUrl(null);

            Desktop.getDesktop().browse(new URI(authorizationUrl));

            Scanner in = new Scanner(System.in);
            System.out.println("Paste the authorization code here");
            System.out.print(">>");
            Verifier verifier = new Verifier(in.nextLine());
String a = service.getAccessToken(null, verifier).getToken();System.err.println(a);
            return a;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
