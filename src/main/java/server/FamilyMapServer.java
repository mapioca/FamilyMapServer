package server;

import handler.ClearRequestHandler;
import com.sun.net.httpserver.HttpServer;
import handler.EventRequestHandler;
import handler.EventIDRequestHandler;
import handler.FillRequestHandler;
import handler.LoadRequestHandler;
import handler.LoginRequestHandler;
import handler.PersonRequestHandler;
import handler.PersonIDRequestHandler;
import handler.RegisterRequestHandler;

import java.io.IOException;
import java.net.InetSocketAddress;

public class FamilyMapServer {

    public static void main(String[] args) throws IOException {
        startServer(8080);
    }

    private static void startServer(int port) throws IOException {
        InetSocketAddress serverAddress = new InetSocketAddress(port);
        HttpServer server = HttpServer.create(serverAddress, 10);
        registerHandlers(server);
        server.start();
        System.out.println("FamilyMapServer listening on port " + port);
    }

    private static void registerHandlers(HttpServer server) {
        server.createContext("/", new FileRequestHandler());
        server.createContext("/user/register", new RegisterRequestHandler());
        server.createContext("/user/login", new LoginRequestHandler());
        server.createContext("/clear", new ClearRequestHandler());
        server.createContext("/fill", new FillRequestHandler());
        server.createContext("/load", new LoadRequestHandler());
        server.createContext("/person/", new PersonIDRequestHandler());
        server.createContext("/person", new PersonRequestHandler());
        server.createContext("/event/", new EventIDRequestHandler());
        server.createContext("/event", new EventRequestHandler());
    }
}
