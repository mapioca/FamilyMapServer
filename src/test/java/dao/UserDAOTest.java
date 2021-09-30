package dao;


import data.DataAccessException;
import data.Database;
import data.UserDAO;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class UserDAOTest {
    private Database db;
    private User goodUser;
    private User betterUser;
    private User bestUser;
    private UserDAO uDAO;

    @BeforeEach
    @DisplayName("Setting up Database Objects")
    public void setUp() throws DataAccessException
    {
        db = new Database();
        bestUser = new User("carpioma", "carpio123", "mcarpiolazo@gmail.com",
                "Moises", "Carpio", "M", "8018218793");

        betterUser = new User("carpiojesse", "jesse123", "jessecarpio@gmail.com",
                "Jesse", "Carpio", "F", "8018211234");

        goodUser = new User("carpiojoy", "joy123", "joycarpio@gmail.com",
                "Joy", "Carpio", "F", "8018215678");



        Connection conn = db.getConnection();
        uDAO = new UserDAO(conn);
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
        uDAO.insert(bestUser);
        User compareTest = uDAO.retrieve(bestUser.getUsername());
        assertNotNull(compareTest);
        assertEquals(bestUser, compareTest);
    }

    @Test
    @DisplayName("Insertion Negative Test Case")
    public void insertFail() throws DataAccessException {
        uDAO.insert(bestUser);
        assertThrows(DataAccessException.class, ()-> uDAO.insert(bestUser));
    }

    @Test
    @DisplayName("Retrieval Positive Test Case")
    public void retrievePass() throws DataAccessException {
        uDAO.insert(bestUser);
        uDAO.insert(goodUser);
        uDAO.insert(betterUser);
        User compareTest = uDAO.retrieve(betterUser.getUsername());
        assertNotNull(compareTest);
        assertEquals(betterUser, compareTest);
    }

    @Test
    @DisplayName("Retrieval Negative Test Case")
    public void retrieveFail() throws DataAccessException {
        uDAO.insert(bestUser);
        uDAO.insert(goodUser);
        uDAO.insert(betterUser);
        assertNull(uDAO.retrieve("8018010000"),
                "Found non-existing ID (should have returned null)");
    }

    @Test
    @DisplayName("Clear Test Case")
    public void clear() throws DataAccessException {
        uDAO.insert(bestUser);
        uDAO.insert(goodUser);
        uDAO.insert(betterUser);
        uDAO.clear();

        assertNull(uDAO.retrieve(bestUser.getUsername()),
                "Found user on database when database should be empty (should have returned null)");

        assertNull(uDAO.retrieve(goodUser.getUsername()),
                "Found user on database when database should be empty (should have returned null)");

        assertNull(uDAO.retrieve(betterUser.getUsername()),
                "Found user on database when database should be empty (should have returned null)");
    }

}
