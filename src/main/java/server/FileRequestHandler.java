package server;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.HttpURLConnection;
import java.nio.file.Files;

public class FileRequestHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {

        try{
            //Ignores everything except GET requests
            if(exchange.getRequestMethod().toUpperCase().equals("GET")){
                String urlPath = exchange.getRequestURI().toString();

                if((urlPath.equals("/")) || (urlPath == null)){
                    urlPath = "/index.html";
                }

                String filePath = "web" + urlPath;
                File file = new File(filePath);
                if(file.exists()){
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                    OutputStream respBody = exchange.getResponseBody();
                    Files.copy(file.toPath(), respBody);
                    respBody.close();
                } else {
                    //404 Error
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_NOT_FOUND, 0);
                    OutputStream respBody = exchange.getResponseBody();
                    File notFoundFile = new File("web/HTML/404.html");
                    Files.copy(notFoundFile.toPath(), respBody);
                    exchange.getResponseBody().close();
                }

            } else{
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_METHOD, 0);
                exchange.getResponseBody().close();
            }
        } catch(IOException e) {
            //Internal Server Error
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR, 0);
            exchange.getResponseBody().close();
            e.printStackTrace();
        }
    }
}
