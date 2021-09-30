package service;

import data.*;
import model.*;
import request.LoginRequest;
import request.RegisterRequest;
import response.FillResult;
import response.LoginResult;
import response.RegisterResult;
import java.io.*;
import java.sql.Connection;


/**
 * Creates a new user account, generates 4 generations of ancestor data for the new
 * user, logs the user in, and returns an auth token.
 */
public class RegisterService {
    String mainUser = null;
    int completedGenerations = 0;
    int generationsToComplete = 4;

    /**
     * Creates a new user account, generates 4 generations of ancestor data for the new
     * user, logs the user in, and returns an auth token.
     *
     * @param registerRequest object containing needed information to register a new user
     * @return an object containing the auth token and/or other objects based on the success or failure of the
     * transaction
     */
    public RegisterResult register(RegisterRequest registerRequest) throws DataAccessException, IOException {
        Database db = new Database();
        Connection conn = db.getConnection();
        UserDAO uDAO = new UserDAO(conn);
        RegisterResult registerRslt = new RegisterResult();
        FillService fillSrvc = new FillService();
        FillResult fillRslt;

        try {

            //Create the User
            //Check if user exists
            if(uDAO.retrieve(registerRequest.getUsername()) == null) {
                //Check if request body is complete and valid
                if (registerRequest.getUsername() != null &&
                        registerRequest.getPassword() != null &&
                        registerRequest.getFirstName() != null &&
                        registerRequest.getLastName() != null &&
                        registerRequest.getEmail() != null &&
                        registerRequest.getGender() != null) {

                    //Create user
                    uDAO.insert(CreateUser(registerRequest));
                    db.closeConnection(true);

                    //Fill in with generations
                    fillRslt = fillSrvc.fill(registerRequest.getUsername(), 4);
                    if(fillRslt.getSuccess()){

                        //Log the user in
                        LoginRequest loginRqst = new LoginRequest(registerRequest.getUsername(),
                                registerRequest.getPassword());

                        LoginService loginSrvc = new LoginService();
                        LoginResult loginRslt;
                        loginRslt = loginSrvc.login(loginRqst);

                        registerRslt = new RegisterResult(loginRslt.getAuthtoken(), loginRslt.getUsername(),
                                loginRslt.getPersonID(), true);

                    } else {
                        registerRslt = new RegisterResult("Error: Database could not be filled with generations.",
                                false);
                    }

                } else {
                    db.closeConnection(false);
                    registerRslt = new RegisterResult("Error: Request is invalid or incomplete.",
                            false);
                }
            } else {
                db.closeConnection(false);
            registerRslt = new RegisterResult("Error: Username not valid. It already exists.", false);
            }

        } catch (Throwable e) {
            db.closeConnection(false);
            registerRslt = new RegisterResult("Error: Internal Server Error", false);
            e.printStackTrace();
        }

        return registerRslt;
    }

    private User CreateUser(RegisterRequest registerRequest) {
        String personID = registerRequest.getFirstName() + "_" + registerRequest.getLastName();
        mainUser = registerRequest.getUsername();
        return new User(registerRequest.getUsername(), registerRequest.getPassword(), registerRequest.getEmail(),
                registerRequest.getFirstName(), registerRequest.getLastName(), registerRequest.getGender(),
                personID);
    }

}
