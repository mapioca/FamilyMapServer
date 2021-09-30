package service;

import data.AuthTokenDAO;
import data.DataAccessException;
import data.Database;
import data.UserDAO;
import model.AuthToken;
import model.User;
import request.LoginRequest;
import response.LoginResult;
import java.sql.Connection;
import java.util.Random;

/**
 * Logs in the user and returns an auth token.
 */
public class LoginService {
    /**
     * Logs in the user and returns an auth token.
     * @param loginRequest object containing username and password
     * @return an object containing the auth token, username, personID, and message, OR
     * an error description message, plus a success/fail boolean if transaction failed
     */
    public LoginResult login(LoginRequest loginRequest) throws DataAccessException {
        LoginResult loginRslt = new LoginResult();
        Database db = new Database();
        Connection conn = db.getConnection();
        UserDAO uDAO = new UserDAO(conn);
        AuthTokenDAO aDAO = new AuthTokenDAO(conn);

        User dbUser = uDAO.retrieve(loginRequest.getUsername());

        if(dbUser != null){
            if(dbUser.getPassword().equals(loginRequest.getPassword())){
                try {
                    //Generate random auth token
                    String authCode = authTokenGenerator();
                    AuthToken authTok = new AuthToken(authCode, loginRequest.getUsername());

                    //Insert auth token to database
                    aDAO.insert(authTok);

                    //Close connection
                    db.closeConnection(true);

                    //Set auth token string in LoginResult deliverable
                    loginRslt = new LoginResult(authCode, loginRequest.getUsername(), dbUser.getPersonID(), true);

                } catch (DataAccessException e){
                    db.closeConnection(false);
                    e.printStackTrace();
                    loginRslt = new LoginResult("Error: Internal Server Error.", false );
                }
            } else {
                db.closeConnection(false);
                loginRslt = new LoginResult("Error: Invalid username or password.", false );
            }
        } else {
            db.closeConnection(false);
            loginRslt = new LoginResult("Error: Username does not exist.", false );
        }

        return loginRslt;
    }

    public String authTokenGenerator() {
        String str = "0123456789abcdefghijklmnopqrstuwxyz" +
                "ABCDEFGHIJKLMOPQRSTUVQXYZ";
        int length = 8;
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder(length);
        for(int i = 0; i < length; i++){
            sb.append(str.charAt(rnd.nextInt(str.length())));
        }
        return sb.toString();
    }

}
