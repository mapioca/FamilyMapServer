package dao;

import data.AuthTokenDAO;
import data.DataAccessException;
import data.Database;
import data.UserDAO;
import model.AuthToken;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class AuthTokenDAOTest {
    private Database db;
    private UserDAO uDAO;
    private AuthTokenDAO aDAO;
    private User bestUser;
    private User goodUser;
    private User betterUser;
    private AuthToken authToken;

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

        authToken = new AuthToken("cf7a368f", "carpioma");

        Connection conn = db.getConnection();
        uDAO = new UserDAO(conn);
        aDAO = new AuthTokenDAO(conn);
        uDAO.clear();
        aDAO.clear();
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
        aDAO.insert(authToken);
        AuthToken compareTest = aDAO.retrieve(authToken.getAuthToken());
        assertNotNull(compareTest);
        assertEquals(authToken, compareTest);
    }

    @Test
    @DisplayName("Insertion Negative Test Case")
    public void insertFail() throws DataAccessException {
        uDAO.insert(bestUser);
        aDAO.insert(authToken);
        assertThrows(DataAccessException.class, ()-> aDAO.insert(authToken));
    }

    @Test
    @DisplayName("Retrieval Positive Test Case")
    public void retrievePass() throws DataAccessException {
        uDAO.insert(bestUser);
        uDAO.insert(goodUser);
        uDAO.insert(betterUser);
        aDAO.insert(authToken);
        AuthToken compareTest = aDAO.retrieve(authToken.getAuthToken());
        assertNotNull(compareTest);
        assertEquals(authToken, compareTest);
    }

    @Test
    @DisplayName("Retrieval Negative Test Case")
    public void retrieveFail() throws DataAccessException {
        uDAO.insert(bestUser);
        uDAO.insert(goodUser);
        uDAO.insert(betterUser);
        aDAO.insert(authToken);
        assertNull(aDAO.retrieve("5fjcmnf5"),
                "Found non-existing authCode(should have returned null)");
    }

    @Test
    @DisplayName("Clear Test Case")
    public void clear() throws DataAccessException {
        uDAO.insert(bestUser);
        uDAO.insert(goodUser);
        uDAO.insert(betterUser);
        aDAO.insert(authToken);
        aDAO.clear();

        assertNull(aDAO.retrieve(authToken.getAuthToken()),
                "Found authToken on database when database should be empty (should have returned null)");
    }

}
