package handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import data.DataAccessException;
import request.RegisterRequest;
import response.RegisterResult;
import service.RegisterService;

import java.io.*;
import java.net.HttpURLConnection;

import static json.JSonSerializer.*;

public class RegisterRequestHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {

        try {

            //Check for Correct Request Type
            if (exchange.getRequestMethod().toUpperCase().equals("POST")) {

                // Extract the JSON string from the HTTP request body
                // Get the request body input stream - GETTING JSON
                InputStream reqBody = exchange.getRequestBody();

                // Read JSON string from the input stream
                String reqData = readString(reqBody);

                // Display/log the request JSON data
                System.out.println("Register Request Data: " + reqData);

                //TURN INTO registerRequest  object
                RegisterRequest registerRqst = deserialize(reqData, RegisterRequest.class);
                RegisterService registerSrvc = new RegisterService();
                RegisterResult registerRslt = registerSrvc.register(registerRqst);

                //Check if Register was successful
                if (registerRslt.getSuccess()) {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                } else {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                }

                //Serializing json
                String jsonRes = serialize(registerRslt);
                System.out.println("Register API Response:" + jsonRes);
                OutputStream respBody = exchange.getResponseBody();
                writeString(jsonRes, respBody);
                exchange.getResponseBody().close();

            } else {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
            }
        } catch (DataAccessException | IOException e) {
            // Internal Server Error
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
            RegisterResult rRes = new RegisterResult("Error: Internal Server Error.", false);
            String jsonRes = serialize(rRes);
            System.out.println("Register API Response:" + jsonRes);
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
