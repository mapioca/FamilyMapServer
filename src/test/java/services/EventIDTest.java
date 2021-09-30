package services;

import data.*;
import model.Event;
import model.Person;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import response.EventIDResult;
import service.EventIDService;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class EventIDTest {
    private Database db;
    private PersonDAO pDAO;
    private UserDAO uDAO;
    private AuthTokenDAO aDAO;
    private EventDAO eDAO;
    Connection conn;
    private Person bestPerson;
    Event birthEvent;
    Event marriageEvent;
    Event deathEvent;
    private Person betterPerson;
    private final EventIDService eventIDResult = new EventIDService();


    @BeforeEach
    @DisplayName("Setting up Database Objects")
    public void setUp() throws DataAccessException
    {
        db = new Database();
        bestPerson = new Person("8018218793", "carpioma", "Moises",
                "Carpio", "M", "papa123", "mama123", "Vicky123");

        birthEvent = new Event("Sheila_Birth", "sheila", "Sheila_Parker",
                -36.1833f, 144.9667f, "Australia", "Melbourne",
                "birth", 1970);

        marriageEvent = new Event("Sheila_Marriage", "sheila", "Sheila_Parker",
                34.0500f, -117.7500f, "United States", "Los Angeles", "marriage",
                2012);

        deathEvent = new Event("Sheila_Death", "sheila", "Sheila_Parker",
                40.2444f, 111.6608f, "United States", "Provo", "death",
                2015);

        Connection conn = db.getConnection();
        conn = db.openConnection();
        eDAO = new EventDAO(conn);
        pDAO = new PersonDAO(conn);
        uDAO = new UserDAO(conn);
        aDAO = new AuthTokenDAO(conn);
    }

    @AfterEach
    @DisplayName("Closing Database Connection")
    public void tearDown() throws DataAccessException {
        conn = db.getConnection();
        eDAO = new EventDAO(conn);
        pDAO = new PersonDAO(conn);
        uDAO = new UserDAO(conn);
        aDAO = new AuthTokenDAO(conn);
        aDAO.clear();
        eDAO.clear();
        pDAO.clear();
        uDAO.clear();
        db.closeConnection(true);
    }

    @Test
    @DisplayName("PersonID Positive Test Case")
    public void personIDPass() throws DataAccessException {
        eDAO.insert(birthEvent);
        Event compareTest = eDAO.retrieve(birthEvent.getEventID());
        db.closeConnection(true);
        EventIDResult eventIDResult = this.eventIDResult.getEvent("Sheila_Birth");
        assertNotNull(compareTest);
        assertEquals(birthEvent, compareTest);
        assertNotNull(eventIDResult);
        assertEquals(true, eventIDResult.getSuccess());

    }

    @Test
    @DisplayName("PersonID Negative Test Case")
    public void personIDFail() throws DataAccessException {
        eDAO.insert(birthEvent);
        eDAO.insert(marriageEvent);
        eDAO.insert(deathEvent);
        assertEquals(false, eventIDResult.getEvent("nonExistingID").getSuccess());
    }
}
