package service;

import data.DataAccessException;
import data.Database;
import data.EventDAO;
import data.PersonDAO;
import model.Event;
import response.EventIDResult;
import response.PersonIDResult;

import java.sql.Connection;

/**
 * Returns the single Event object with the specified ID.
 */
public class EventIDService {
    /**
     * Returns the single Event object with the specified ID.
     * @param ID Identifier associated with event
     * @return Event object with all its objects
     */
    public EventIDResult getEvent(String ID) throws DataAccessException {
        EventIDResult eventIDResult = new EventIDResult();
        Database db = new Database();
        Connection conn = db.getConnection();
        EventDAO eDAO = new EventDAO(conn);
        try {
            Event newEvent = eDAO.retrieve(ID);
            if(newEvent != null){
                eventIDResult.setSuccess(true);
                eventIDResult.setAssociatedUsername(newEvent.getUsername());
                eventIDResult.setEventID(newEvent.getEventID());
                eventIDResult.setPersonID(newEvent.getPersonID());
                eventIDResult.setLatitude(newEvent.getLatitude());
                eventIDResult.setLongitude(newEvent.getLongitude());
                eventIDResult.setCountry(newEvent.getCountry());
                eventIDResult.setCity(newEvent.getCity());
                eventIDResult.setEventType(newEvent.getEventType());
                eventIDResult.setYear(newEvent.getYear());
            } else {
                eventIDResult.setSuccess(false);
                eventIDResult.setMessage("Error: Unable to find event associated to the given ID.");
            }

        } catch (DataAccessException e) {
            eventIDResult.setSuccess(false);
            eventIDResult.setMessage("Error: Unable to find event associated to the given ID.");
            e.printStackTrace();
        }
        db.closeConnection(true);
        return eventIDResult;
    }
}
