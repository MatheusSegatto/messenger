package Server;

import java.io.IOException;

import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpServer;

public class HTTPServerRequests {
    public static void startServer(int port) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/stablishConection", new HandleRequests.SetClientOnServer());
        server.createContext("/getOnlineClients", new HandleRequests.GetOnlineClients());
        server.createContext("/ping", new HandleRequests.PingHandler());
        server.createContext("/sendMessage", new HandleRequests.MessageReceiver());
        server.createContext("/authenticate", new HandleRequests.AuthenticateUser());
        server.createContext("/changePassword", new HandleRequests.ChangePassword());
        server.setExecutor(null);
        server.start();
        HandleRequests.checkClientActivity();
    }
}
