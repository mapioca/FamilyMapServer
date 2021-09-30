package service;

import data.*;
import response.ClearResult;
import java.sql.Connection;

/**
 * Deletes ALL data from the database, including user accounts, auth tokens, and
 * generated person and event data.
 */
public class ClearService {

    /**
     * Deletes ALL data from the database (user accounts, auth tokens, and
     * generated person; all data)
     */
    public ClearResult clear() throws DataAccessException {
        ClearResult clearRslt = new ClearResult();
        Database db = new Database();
        Connection conn = db.getConnection();

        try {   //Closing connection
            //Clearing User Table
            UserDAO uDAO = new UserDAO(conn);
            uDAO.clear();

            //Clearing Person Table
            PersonDAO pDAO = new PersonDAO(conn);
            pDAO.clear();

            //Clearing Event Table
            EventDAO eDAO = new EventDAO(conn);
            eDAO.clear();

            //Clearing AuthToken Table
            AuthTokenDAO aDAO = new AuthTokenDAO(conn);
            aDAO.clear();
            db.closeConnection(true);
            clearRslt.setMessage("Clear succeeded.");
            clearRslt.setSuccess(true);

        } catch (DataAccessException throwables) {
            throwables.printStackTrace();
            clearRslt.setMessage("Error: Internal Server Error while trying to access the Database.");
            db.closeConnection(false);
            clearRslt.setSuccess(false);
        }
        return clearRslt;
    }
}
