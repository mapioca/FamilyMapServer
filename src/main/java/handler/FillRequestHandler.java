package handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import data.DataAccessException;
import data.PersonDAO;
import response.FillResult;
import service.FillService;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;

import static json.JSonSerializer.serialize;

public class FillRequestHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {

        try {
            //Check for Correct Request Type
            if(exchange.getRequestMethod().toUpperCase().equals("POST")){
                FillService fillSrvc = new FillService();
                String urlPath = exchange.getRequestURI().toString();
                String[] urlSegments = urlPath.split("/");

                int generations = 4; //Default
                String username;

                if(urlSegments[urlSegments.length - 2].equals("fill")){
                    username = urlSegments[urlSegments.length - 1];
                } else {
                    generations = Integer.parseInt(urlSegments[urlSegments.length - 1]);
                    username = urlSegments[urlSegments.length - 2];
                }

                FillResult fillRslt = fillSrvc.fill(username, generations);

                if(fillRslt.getSuccess()){
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                } else {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0);
                }

                String jsonRes = serialize(fillRslt);
                System.out.println("Fill API Response: " + jsonRes);
                OutputStream respBody = exchange.getResponseBody();
                writeString(jsonRes, respBody);
                exchange.getResponseBody().close();

            } else {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
            }

        } catch (DataAccessException e) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0);
            FillResult fillResult = new FillResult("Error: Internal Server Error.", false);
            String jsonResult = serialize(fillResult);
            System.out.println("Fill API Response: " + jsonResult);
            OutputStream respBody = exchange.getResponseBody();
            writeString(jsonResult, respBody);
            exchange.getResponseBody().close();
            e.printStackTrace();
        }
    }

    private void writeString(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        BufferedWriter bw = new BufferedWriter(sw);
        bw.write(str);
        bw.flush();
    }
}
