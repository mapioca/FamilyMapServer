package services;

import data.*;
import model.Person;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import response.PersonIDResult;
import service.PersonIDService;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class PersonIDTest {
    private Database db;
    private PersonDAO pDAO;
    private UserDAO uDAO;
    private AuthTokenDAO aDAO;
    private EventDAO eDAO;
    Connection conn;
    private Person bestPerson;
    private Person goodPerson;
    private Person betterPerson;
    private final PersonIDService personIDService = new PersonIDService();


    @BeforeEach
    @DisplayName("Setting up Database Objects")
    public void setUp() throws DataAccessException
    {
        db = new Database();
        bestPerson = new Person("8018218793", "carpioma", "Moises",
                "Carpio", "M", "papa123", "mama123", "Vicky123");

        goodPerson = new Person("8018211234", "jessedog", "Jesse",
                "Carpio", "F", "dogpapa123", "dogmama123", null);

        betterPerson = new Person("8018215678", "joydog", "Joy",
                "Carpio", "F", null, null, null);

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
        pDAO.insert(bestPerson);
        Person compareTest = pDAO.retrieve(bestPerson.getPersonID());
        db.closeConnection(true);
        PersonIDResult personIDResult = personIDService.getPerson("8018218793");
        assertNotNull(compareTest);
        assertEquals(bestPerson, compareTest);
        assertNotNull(personIDResult);
        assertEquals(true, personIDResult.getSuccess());

    }

    @Test
    @DisplayName("PersonID Negative Test Case")
    public void personIDFail() throws DataAccessException {
        pDAO.insert(bestPerson);
        pDAO.insert(goodPerson);
        pDAO.insert(betterPerson);
        assertEquals(false, personIDService.getPerson("nonExistingID").getSuccess());
    }

}
