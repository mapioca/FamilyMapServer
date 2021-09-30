package service;

import data.DataAccessException;
import data.Database;
import data.EventDAO;
import response.EventResult;
import java.sql.Connection;

/**
 * Returns ALL events for ALL family members of the current user. The current
 * user is determined from the provided auth token.
 */
public class EventService {

    /**
     * Returns ALL events for ALL family members of the current user. The current
     * user is determined from the provided auth token.
     * @param username associated to events to be retrieved
     * @return All events in a single object
     */
    public EventResult getEvent(String username) throws DataAccessException {
        EventResult eventRslt = new EventResult();
        Database db = new Database();
        Connection conn = db.getConnection();
        EventDAO eDAO = new EventDAO(conn);

        try {
            eventRslt.setData(eDAO.retrieveByUsername(username));

            if(eventRslt.getData() == null){
                eventRslt = new EventResult(false,
                        "Error: Unable to find events associated to the user.");
            } else {
                eventRslt.setSuccess(true);
            }
        } catch (DataAccessException e) {
            eventRslt = new EventResult(false,
                    "Error: Unable to find events associated to the given user.");

            e.printStackTrace();
        }

        db.closeConnection(true);
        return eventRslt;
    }
}
