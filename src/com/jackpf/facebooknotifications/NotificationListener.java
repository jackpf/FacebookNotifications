package com.jackpf.facebooknotifications;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class NotificationListener
{
    public void listen() throws IOException
    {
        HttpServer server = HttpServer.create(new InetSocketAddress(1337), 0);
        server.createContext("/", new MyHandler());
        server.setExecutor(null);
        server.start();
    }

    static class MyHandler implements HttpHandler
    {
        public void handle(HttpExchange exchange) throws IOException
        {
            String response = "Working!";
            exchange.sendResponseHeaders(200, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
}
