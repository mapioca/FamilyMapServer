package service;

import data.DataAccessException;
import data.Database;
import data.PersonDAO;
import model.Person;
import response.PersonIDResult;
import java.sql.Connection;

/**
 * Returns the single Person object with the specified ID.
 */
public class PersonIDService {

    /**
     * Returns the single Person object with the specified ID.
     * @param ID Specified ID of person to retrieve info
     * @return person object or error
     */
    public PersonIDResult getPerson(String ID) throws DataAccessException {
        PersonIDResult personIDRslt;
        Database db = new Database();
        Connection conn = db.getConnection();
        PersonDAO pDAO = new PersonDAO(conn);
        try {
            Person newPerson = pDAO.retrieve(ID);

            if(newPerson != null){
                personIDRslt = new PersonIDResult(newPerson.getPersonID(), newPerson.getUsername(),
                        newPerson.getFirstName(), newPerson.getLastName(), newPerson.getGender(),
                        newPerson.getFatherID(), newPerson.getMotherID(), newPerson.getSpouseID(), true );
            } else {
                personIDRslt = new PersonIDResult("Error: Unable to find person associated to the given ID." ,
                        false);
            }
        } catch (DataAccessException e) {
            personIDRslt = new PersonIDResult("Error: Unable to find person associated to the given ID." ,
                    false);
            e.printStackTrace();
        }

        db.closeConnection(true);
        return personIDRslt;
    }
}
