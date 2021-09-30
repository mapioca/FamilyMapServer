package handler;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import data.AuthTokenDAO;
import data.DataAccessException;
import data.Database;
import model.AuthToken;
import response.PersonResult;
import service.PersonService;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;

import static json.JSonSerializer.serialize;

public class PersonRequestHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        PersonResult personRslt;
        try{
            //Check for Correct Request Type
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
                        PersonService personSrvc = new PersonService();
                        personRslt = personSrvc.getPersonGroup(userToken.getUsername());
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);

                    } else{
                        //Invalid Auth Token Error
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                        personRslt = new PersonResult("Error: Invalid authToken", false);
                    }

                } else{
                    //Invalid Auth Token Error
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                    personRslt = new PersonResult("Error: Request contains no authtoken", false);
                }
        } else{
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                personRslt = new PersonResult("Error: Internal server error.", false);
            }

            //Serializing json
            String jsonRes = serialize(personRslt);
            System.out.println("Person API Response: " + jsonRes);
            OutputStream respBody = exchange.getResponseBody();
            writeString(jsonRes, respBody);
            exchange.getResponseBody().close();

        } catch(IOException | DataAccessException e) {
            //Internal Server Error
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
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
