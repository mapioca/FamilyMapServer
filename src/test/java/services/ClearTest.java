package services;

import data.*;
import model.AuthToken;
import model.Event;
import model.Person;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import response.ClearResult;
import service.ClearService;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertNull;

public class ClearTest {
    private Database db;
    private UserDAO uDAO;
    private AuthTokenDAO aDAO;
    private EventDAO eDAO;
    private PersonDAO pDAO;

    private User user;
    private User bestUser;
    private Person person;
    private Event birthEvent;
    private AuthToken authToken;

    ClearService clearService = new ClearService();
    ClearResult clearResult;
    Connection conn;


    @BeforeEach
    @DisplayName("Setting up Database Objects")
    public void setUp() throws DataAccessException
    {
        db = new Database();
        user = new User("sheila", "parker", "sheila@parker.com", "Sheila",
                "Parker", "f", "Sheila_Parker");

        bestUser = new User("carpioma", "carpio123", "mcarpiolazo@gmail.com",
                "Moises", "Carpio", "M", "8018218793");

        person = new Person("Sheila_Parker", "sheila", "Sheila",
                "Parker", "f", "Blaine_McGary", "Betty_White",
                "Davis_Hyer");

        birthEvent = new Event("Sheila_Birth", "sheila", "Sheila_Parker",
                -36.1833f, 144.9667f,"Australia", "Melbourne",
                "birth", 1970);

        authToken = new AuthToken("cf7a368f", "carpioma");

        conn = db.openConnection();
        eDAO = new EventDAO(conn);
        pDAO = new PersonDAO(conn);
        uDAO = new UserDAO(conn);
        aDAO = new AuthTokenDAO(conn);
    }

    @AfterEach
    @DisplayName("Closing Database Connection")
    public void tearDown() throws DataAccessException {
        aDAO.clear();
        eDAO.clear();
        pDAO.clear();
        uDAO.clear();
        db.closeConnection(true);
    }

    @Test
    @DisplayName("Clear Test Case")
    public void clear() throws DataAccessException, SQLException {
        uDAO.insert(bestUser);
        uDAO.insert(user);
        pDAO.insert(person);
        eDAO.insert(birthEvent);
        aDAO.insert(authToken);
        db.closeConnection(true);
        clearResult = clearService.clear();

        conn = db.getConnection();
        eDAO = new EventDAO(conn);
        pDAO = new PersonDAO(conn);
        uDAO = new UserDAO(conn);
        aDAO = new AuthTokenDAO(conn);

        assertNull(uDAO.retrieve(bestUser.getUsername()),
                "Found user on database when database should be empty (should have returned null)");

        assertNull(pDAO.retrieve(person.getPersonID()),
                "Found person on database when database should be empty (should have returned null)");

        assertNull(eDAO.retrieve(birthEvent.getEventID()),
                "Found event on database when database should be empty (should have returned null)");

        assertNull(aDAO.retrieve(authToken.getAuthToken()),
                "Found authToken on database when database should be empty (should have returned null)");
    }



}
