package handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import data.DataAccessException;
import request.LoginRequest;
import response.LoginResult;
import service.LoginService;

import java.io.*;
import java.net.HttpURLConnection;

import static json.JSonSerializer.*;

public class LoginRequestHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            //Check for Correct Request Type
            if (exchange.getRequestMethod().toUpperCase().equals("POST")) {

                //Get JSON object
                InputStream reqBody = exchange.getRequestBody();
                String reqData = readString(reqBody);
                System.out.println("Login Request Data: " + reqData);

                //ConvertJSON into a string
                LoginRequest loginRqst = deserialize(reqData, LoginRequest.class);
                LoginService loginSrvc = new LoginService();

                //Process login request
                LoginResult loginRslt = loginSrvc.login(loginRqst);

                //Check if Login was successful
                if(loginRslt.getSuccess()){
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                } else {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                }

                //Serializing json
                String jsonRspns = serialize(loginRslt);
                System.out.println("Login API Response: " + jsonRspns);
                OutputStream respBody = exchange.getResponseBody();
                writeString(jsonRspns, respBody);
                exchange.getResponseBody().close();

            } else {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
            }
        } catch (IOException | DataAccessException e) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0);
            LoginResult loginRes = new LoginResult("Error: Internal Server Error.", false);
            String jsonRes = serialize(loginRes);
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
