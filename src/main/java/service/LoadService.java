package service;

import data.*;
import model.Event;
import model.Person;
import model.User;
import request.LoadRequest;
import response.ClearResult;
import response.LoadResult;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Clears all data from the database (just like the /clear API), and then loads the
 * posted user, person, and event data into the database.
 */
public class LoadService {
    /**
     * Clears all data from the database (just like the /clear API), and then loads the
     * posted user, person, and event data into the database.
     * @param loadRequest object containing users, persons, and events
     * @return object containing success/failure information
     */
    public LoadResult load(LoadRequest loadRequest) throws DataAccessException, SQLException {
        LoadResult loadRslt = new LoadResult();
        ClearResult clearRslt;
        ClearService clearSrvc = new ClearService();
        clearRslt = clearSrvc.clear();

        if(clearRslt.getSuccess()){
            Database db = new Database();

            try {
                Connection conn = db.getConnection();
                EventDAO eDAO = new EventDAO(conn);
                PersonDAO pDAO = new PersonDAO(conn);
                UserDAO uDAO = new UserDAO(conn);
                int eCount = 0;
                int pCount = 0;
                int uCount = 0;

                try {
                    for(Person p : loadRequest.getPersons()){
                        pCount++;
                        pDAO.insert(p);
                    }
                } catch (DataAccessException e) {
                    e.printStackTrace();
                    loadRslt.setSuccess(false);
                    loadRslt.setMessage("Error: Encountered error while adding a person to the database.");
                }
                try {
                    for(User u : loadRequest.getUsers()){
                        uCount++;
                        uDAO.insert(u);
                    }
                } catch (DataAccessException e) {
                    e.printStackTrace();
                    loadRslt.setSuccess(false);
                    loadRslt.setMessage("Error: Encountered error while adding a user to the database.");
                }
                try {
                    for(Event e : loadRequest.getEvents()){
                        eCount++;
                        eDAO.insert(e);
                    }
                } catch (DataAccessException e){
                    e.printStackTrace();
                    loadRslt.setSuccess(false);
                    loadRslt.setMessage("Error: Encountered error while adding an event to the database.");
                }
                db.closeConnection(true);
                 loadRslt.setMessage("Successfully added " + uCount + " users, " + pCount + " persons, and " +
                        eCount + " events to the database.");
                loadRslt.setSuccess(true);

            } catch(DataAccessException e) {
                db.closeConnection(false);
                e.printStackTrace();
                loadRslt.setSuccess(false);
                loadRslt.setMessage("Error: Encountered error while adding an event to the database.");
            }

        }
        return loadRslt;
    }
}
