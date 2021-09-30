package handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import data.DataAccessException;
import request.LoadRequest;
import response.LoadResult;
import service.LoadService;

import java.io.*;
import java.net.HttpURLConnection;
import java.sql.SQLException;

import static json.JSonSerializer.*;

public class LoadRequestHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            if(exchange.getRequestMethod().toUpperCase().equals("POST")){

                //Get Request Body in JSON from exchange
                InputStream reqBody = exchange.getRequestBody();
                String reqData = readString(reqBody);
                System.out.println("Load Request Data: " + reqData);

                //Convert JSON request to Java Objects
                LoadRequest loadRqst = deserialize(reqData, LoadRequest.class);

                //Process the request
                LoadService loadSrvc = new LoadService();
                LoadResult loadRslt = loadSrvc.load(loadRqst);

                //Check if Load was successful
                if (loadRslt.getSuccess()) {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                } else {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0);
                }

                //Serializing json
                String jsonRes = serialize(loadRslt);
                System.out.println("Load API Response: " + jsonRes);
                OutputStream respBody = exchange.getResponseBody();
                writeString(jsonRes, respBody);
                exchange.getResponseBody().close();

            } else {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
            }
        } catch (IOException | DataAccessException | SQLException e){
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0);
            LoadResult loadRes = new LoadResult("Error: Internal Server Error.", false);
            String jsonRes = serialize(loadRes);
            System.out.println("Load API Response: " + jsonRes);
            OutputStream respBody = exchange.getResponseBody();
            writeString(jsonRes, respBody);
            exchange.getResponseBody().close();
            e.printStackTrace();
        }
    }

    private String readString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        InputStreamReader sr = new InputStreamReader(is);
        char[] buf = new char[1024];
        int len;
        while((len  = sr.read(buf)) > 0) {
            sb.append(buf, 0, len);
        }
        return sb.toString();
    }

    private void writeString(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        BufferedWriter bw = new BufferedWriter(sw);
        bw.write(str);
        bw.flush();
    }
}
