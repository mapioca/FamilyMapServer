package dao;

import data.*;
import model.Event;
import model.Person;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class EventDAOTest {
    private Database db;
    private EventDAO eDAO;
    private PersonDAO pDAO;
    private UserDAO uDAO;

    private Event birthEvent;
    private Event marriageEvent;
    private Event deathEvent;
    private User user;
    private Person person;


    @BeforeEach
    @DisplayName("Setting up Database Objects")
    public void setUp() throws DataAccessException
    {
        db = new Database();
        user = new User("sheila", "parker", "sheila@parker.com", "Sheila",
            "Parker", "f", "Sheila_Parker");

        person = new Person("Sheila_Parker", "sheila", "Sheila",
                "Parker", "f", "Blaine_McGary", "Betty_White",
                "Davis_Hyer");

        birthEvent = new Event("Sheila_Birth", "sheila", "Sheila_Parker",
                -36.1833f, 144.9667f,"Australia", "Melbourne",
                "birth", 1970);

        marriageEvent = new Event("Sheila_Marriage", "sheila", "Sheila_Parker",
                34.0500f, -117.7500f, "United States", "Los Angeles", "marriage",
                2012);

        deathEvent = new Event("Sheila_Death", "sheila", "Sheila_Parker",
                40.2444f, 111.6608f, "United States", "Provo", "death",
                2015);

        Connection conn = db.getConnection();
        eDAO = new EventDAO(conn);
        pDAO = new PersonDAO(conn);
        uDAO = new UserDAO(conn);
        eDAO.clear();
        pDAO.clear();
        uDAO.clear();
    }

    @AfterEach
    @DisplayName("Closing Database Connection")
    public void tearDown() throws DataAccessException {
        db.closeConnection(false);
    }

    @Test
    @DisplayName("Insertion Positive Test Case")
    public void insertPass() throws DataAccessException {
        uDAO.insert(user);
        pDAO.insert(person);
        eDAO.insert(birthEvent);
        Event compareTest = eDAO.retrieve(birthEvent.getEventID());
        assertNotNull(compareTest);
        assertEquals(birthEvent, compareTest);
    }

    @Test
    @DisplayName("Insertion Negative Test Case")
    public void insertFail() throws DataAccessException {
        uDAO.insert(user);
        pDAO.insert(person);
        eDAO.insert(birthEvent);
        assertThrows(DataAccessException.class, ()-> eDAO.insert(birthEvent));
    }

    @Test
    @DisplayName("Retrieval Positive Test Case")
    public void retrievePass() throws DataAccessException {
        uDAO.insert(user);
        pDAO.insert(person);
        eDAO.insert(birthEvent);
        eDAO.insert(marriageEvent);
        eDAO.insert(deathEvent);
        Event compareTest = eDAO.retrieve(birthEvent.getEventID());
        assertNotNull(compareTest);
        assertEquals(birthEvent, compareTest);
    }

    @Test
    @DisplayName("Retrieval Negative Test Case")
    public void retrieveFail() throws DataAccessException {
        uDAO.insert(user);
        pDAO.insert(person);
        eDAO.insert(birthEvent);
        eDAO.insert(marriageEvent);
        eDAO.insert(deathEvent);
        assertNull(eDAO.retrieve("Moises_Marriage"),
                "Found non-existing ID (should have returned null)");
    }

    @Test
    @DisplayName("Clear Test Case")
    public void clear() throws DataAccessException {
        uDAO.insert(user);
        pDAO.insert(person);
        eDAO.insert(birthEvent);
        eDAO.insert(marriageEvent);
        eDAO.insert(deathEvent);
        eDAO.clear();

        assertNull(eDAO.retrieve(birthEvent.getEventID()),
                "Found person on database when database should be empty (should have returned null)");

        assertNull(eDAO.retrieve(marriageEvent.getEventID()),
                "Found person on database when database should be empty (should have returned null)");

        assertNull(eDAO.retrieve(deathEvent.getEventID()),
                "Found person on database when database should be empty (should have returned null)");
    }

}
