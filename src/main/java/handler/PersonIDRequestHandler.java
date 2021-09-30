package handler;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import data.AuthTokenDAO;
import data.DataAccessException;
import data.Database;
import model.AuthToken;
import model.Person;
import response.PersonIDResult;
import service.PersonIDService;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.sql.Connection;

import static json.JSonSerializer.serialize;

public class PersonIDRequestHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        PersonIDResult personIDRslt;
        PersonIDService personIDSrvc = new PersonIDService();

        try {
            //Check for Correct Request Type
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

                    //Check for Authentication
                    if(userAuthToken != null) {
                        String personIDToRetrieve = null;
                        String urlPath = exchange.getRequestURI().toString();
                        String[] urlSegments = urlPath.split("/");

                        if(urlSegments[urlSegments.length - 2].equals("person")){
                            personIDToRetrieve = urlSegments[urlSegments.length - 1];
                        }

                        personIDRslt = personIDSrvc.getPerson(personIDToRetrieve);
                        if(personIDRslt.getAssociatedUsername().equals(userAuthToken.getUsername())){
                            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                        } else {
                            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                            personIDRslt = new PersonIDResult("Error: The PersonID does not match the " +
                                    "authtoken user.",false);
                        }

                    } else {
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                         personIDRslt = new PersonIDResult("Error: The authtoken was invalid " +
                                "or user was not found in database.", false);
                    }

                } else {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_UNAUTHORIZED, 0);
                    personIDRslt = new PersonIDResult("Error: No authtoken was found in request header.",
                            false);
                }


            } else {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                personIDRslt = new PersonIDResult("Error: Internal Server Erorr. Wrong Request.", false);
            }

            //Serializing json
            String jsonRspns = serialize(personIDRslt);
            System.out.println("PersonID API Response: " + jsonRspns);
            OutputStream respBody = exchange.getResponseBody();
            writeString(jsonRspns, respBody);
            exchange.getResponseBody().close();

        } catch (DataAccessException e) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0);
            personIDRslt = new PersonIDResult("Error: Internal Server Error.", false);
            String jsonRes = serialize(personIDRslt);
            System.out.println("PersonID API Response: " + jsonRes);
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
