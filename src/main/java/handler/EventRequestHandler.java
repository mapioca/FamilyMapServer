package handler;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import data.AuthTokenDAO;
import data.DataAccessException;
import data.Database;
import model.AuthToken;
import response.EventResult;
import service.EventService;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;

import static json.JSonSerializer.serialize;

public class EventRequestHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        EventResult eventRslt;

        try{
            //Check For Correct Request Type
            if(exchange.getRequestMethod().toUpperCase().equals("GET")){
                Headers reqHeaders = exchange.getRequestHeaders();

                //Checking for Authorization
                if(reqHeaders.containsKey("Authorization")){
                    String authToken = reqHeaders.getFirst("Authorization");
                    Database database = new Database();
                    AuthTokenDAO aDAO = new AuthTokenDAO(database.getConnection());
                    AuthToken userToken = aDAO.retrieve(authToken);
                    database.closeConnection(true);

                    if(userToken != null){
                        EventService eventService = new EventService();
                        eventRslt = eventService.getEvent(userToken.getUsername());
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);

                    } else{
                        //Invalid Auth Token Error
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                        eventRslt = new EventResult(false, "Error: Invalid authToken.");
                    }

                } else{
                    //Invalid Auth Token Error
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                    eventRslt = new EventResult(false, "Error: Request contains no authtoken");
                }
            } else{
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                eventRslt = new EventResult(false, "Error: Internal server error.");
            }

            //Serializing json
            String jsonRspns = serialize(eventRslt);
            System.out.println("Get all Events API Response: " + jsonRspns);
            OutputStream respBody = exchange.getResponseBody();
            writeString(jsonRspns, respBody);
            exchange.getResponseBody().close();

        } catch(IOException | DataAccessException e) {
            //Internal Server Error
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR, 0);
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
