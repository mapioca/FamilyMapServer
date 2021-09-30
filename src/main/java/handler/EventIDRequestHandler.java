package handler;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import data.AuthTokenDAO;
import data.DataAccessException;
import data.Database;
import model.AuthToken;
import response.EventIDResult;
import service.EventIDService;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.sql.Connection;

import static json.JSonSerializer.serialize;

public class EventIDRequestHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        EventIDResult eventIDRslt;
        EventIDService eventIDSrvc = new EventIDService();

        try {
            //Check for correct request type
            if (exchange.getRequestMethod().toUpperCase().equals("GET")) {
                //Get request header
                Headers reqHeaders = exchange.getRequestHeaders();

                //Check if request header has authtoken
                if (reqHeaders.containsKey("Authorization")) {

                    //Get authtoken and establish connection to retrieve user associated to authtoken
                    String authToken = reqHeaders.getFirst("Authorization");
                    Database db = new Database();
                    Connection conn = db.getConnection();
                    AuthTokenDAO aDAO = new AuthTokenDAO(conn);
                    AuthToken userAuthToken = aDAO.retrieve(authToken);
                    db.closeConnection(true);

                    if(userAuthToken != null) {
                        String eventIDToRetrieve = null;
                        String urlPath = exchange.getRequestURI().toString();
                        String[] urlSegments = urlPath.split("/");

                        if(urlSegments[urlSegments.length - 2].equals("event")){
                            eventIDToRetrieve = urlSegments[urlSegments.length - 1];
                        }

                        eventIDRslt = eventIDSrvc.getEvent(eventIDToRetrieve);
                        if(eventIDRslt.getAssociatedUsername().equals(userAuthToken.getUsername())){
                            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                        } else {
                            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                            eventIDRslt = new EventIDResult(false, "Error: The eventID does " +
                                    "not match the authtoken user");
                        }

                    } else {
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                        eventIDRslt = new EventIDResult(false, "Error: The authtoken was invalid " +
                                "or user was not found in database.");
                    }

                } else {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_UNAUTHORIZED, 0);
                    eventIDRslt = new EventIDResult(false, "Error: No authtoken was found in request header.");
                }

                //Serialize json
                String jsonRspns = serialize(eventIDRslt);
                System.out.println("EventID API Response: " + eventIDRslt);
                OutputStream respBody = exchange.getResponseBody();
                writeString(jsonRspns, respBody);
                exchange.getResponseBody().close();

            } else {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
            }

        } catch (DataAccessException e) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0);
            eventIDRslt = new EventIDResult(false, "Error: Internal Server Error.");
            String jsonRes = serialize(eventIDRslt);
            System.out.println("EventID API Response: " + eventIDRslt);
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
