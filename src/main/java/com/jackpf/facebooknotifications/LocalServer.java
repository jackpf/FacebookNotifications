package com.jackpf.facebooknotifications;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

public class LocalServer
{
    public void listen() throws IOException
    {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/oauth_redirect/", new Handler());
        server.setExecutor(null);
        server.start();
    }

    static class Handler implements HttpHandler
    {
        public void handle(HttpExchange exchange) throws IOException
        {
            String response = readResource("/access_code.html");
            String code = parseQueryString(exchange).get("code");

            if (code != null) {
                response = String.format(response, code);
            } else {
                response = "Error!";
            }

            exchange.sendResponseHeaders(200, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }

        private Map<String, String> parseQueryString(HttpExchange exchange)
        {
            Map<String, String> result = new HashMap<String, String>();
            String query = exchange.getRequestURI().getQuery();

            if (query != null) {
                for (String param : query.split("&")) {
                    String pair[] = param.split("=");

                    if (pair.length > 1) {
                        result.put(pair[0], pair[1]);
                    } else {
                        result.put(pair[0], "");
                    }
                }
            }

            return result;
        }

        private String readResource(String filename) throws IOException
        {
            InputStream in = getClass().getResourceAsStream(filename);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            return IOUtils.toString(reader);
        }
    }
}
