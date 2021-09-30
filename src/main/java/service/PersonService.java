package service;

import data.DataAccessException;
import data.Database;
import data.PersonDAO;
import response.PersonResult;
import java.sql.Connection;

/**
 * Returns ALL family members of the current user. The current user is
 * determined from the provided auth token.
 */
public class PersonService {

    /**
     * Returns ALL family members of the current user. The current user is
     * determined from the provided auth token.
     * @param username auth token associated to a user session
     * @return an object containing all family members of the current user
     */
    public PersonResult getPersonGroup(String username) throws DataAccessException {
        PersonResult personRslt = new PersonResult();
        Database db = new Database();
        Connection conn = db.getConnection();
        PersonDAO pDAO = new PersonDAO(conn);

        try {
            personRslt.setData(pDAO.retrieveByUsername(username));

            if(personRslt.getData() == null){
                personRslt = new PersonResult("Error: Unable to find persons associated to the user.",
                        false);
            } else {
                personRslt.setSuccess(true);
            }
        } catch (DataAccessException e) {
            personRslt = new PersonResult("Error: Unable to find persons associated to the user.",
                    false);
            e.printStackTrace();
        }

        db.closeConnection(true);
        return personRslt;
    }
}
