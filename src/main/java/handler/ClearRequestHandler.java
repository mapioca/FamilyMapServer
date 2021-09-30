package handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import data.DataAccessException;
import response.ClearResult;
import service.ClearService;

import java.io.*;
import java.net.HttpURLConnection;
import java.sql.SQLException;

import static json.JSonSerializer.serialize;

public class ClearRequestHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {

            //Check For Correct Request Type
            if (exchange.getRequestMethod().toUpperCase().equals("POST")) {
                ClearService clearSrvc = new ClearService();
                ClearResult clearRslt = clearSrvc.clear();

                //Check if Clear Was Successful
                if(clearRslt.getSuccess()){
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                } else {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                }

                //Serialize json
                String jsonRspns = serialize(clearRslt);
                System.out.println("Clear API Response from the Service: " + jsonRspns);
                OutputStream respBody = exchange.getResponseBody();
                writeString(jsonRspns, respBody);
                exchange.getResponseBody().close();

            } else {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
            }
        } catch (IOException | DataAccessException e) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0);
            ClearResult cr = new ClearResult("Error: Internal Server Error.", false);
            String jsonRes = serialize(cr);
            System.out.println("Clear API Response from the Handler: " + jsonRes);
            OutputStream respBody = exchange.getResponseBody();
            writeString(jsonRes, respBody);
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
