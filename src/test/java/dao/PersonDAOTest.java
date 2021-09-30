package dao;

import data.DataAccessException;
import data.Database;
import data.PersonDAO;
import model.Person;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class PersonDAOTest {
    private Database db;
    private Person bestPerson;
    private Person goodPerson;
    private Person betterPerson;
    private PersonDAO pDAO;

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
        pDAO = new PersonDAO(conn);
        pDAO.clear();
    }

    @AfterEach
    @DisplayName("Closing Database Connection")
    public void tearDown() throws DataAccessException {
        db.closeConnection(false);
    }

    @Test
    @DisplayName("Insertion Positive Test Case")
    public void insertPass() throws DataAccessException {
        pDAO.insert(bestPerson);
        Person compareTest = pDAO.retrieve(bestPerson.getPersonID());
        assertNotNull(compareTest);
        assertEquals(bestPerson, compareTest);
    }

    @Test
    @DisplayName("Insertion Negative Test Case")
    public void insertFail() throws DataAccessException {
        pDAO.insert(bestPerson);
        assertThrows(DataAccessException.class, ()-> pDAO.insert(bestPerson));
    }

    @Test
    @DisplayName("Retrieval Positive Test Case")
    public void retrievePass() throws DataAccessException {
        pDAO.insert(bestPerson);
        pDAO.insert(goodPerson);
        pDAO.insert(betterPerson);
        Person compareTest = pDAO.retrieve(betterPerson.getPersonID());
        assertNotNull(compareTest);
        assertEquals(betterPerson, compareTest);
    }

    @Test
    @DisplayName("Retrieval Negative Test Case")
    public void retrieveFail() throws DataAccessException {
        pDAO.insert(bestPerson);
        pDAO.insert(goodPerson);
        pDAO.insert(betterPerson);
        assertNull(pDAO.retrieve("8018010000"),
                "Found non-existing ID (should have returned null)");
    }

    @Test
    @DisplayName("Clear Test Case")
    public void clear() throws DataAccessException {
        pDAO.insert(bestPerson);
        pDAO.insert(goodPerson);
        pDAO.insert(betterPerson);
        pDAO.clear();

        assertNull(pDAO.retrieve(bestPerson.getPersonID()),
                "Found person on database when database should be empty (should have returned null)");

        assertNull(pDAO.retrieve(goodPerson.getPersonID()),
                "Found person on database when database should be empty (should have returned null)");

        assertNull(pDAO.retrieve(betterPerson.getPersonID()),
                "Found person on database when database should be empty (should have returned null)");
    }

}
