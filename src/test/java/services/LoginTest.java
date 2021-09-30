package services;

import data.*;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import request.LoginRequest;
import response.LoginResult;
import service.LoginService;
import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;


public class LoginTest {
    private Database db;
    private UserDAO uDAO;
    private AuthTokenDAO aDAO;
    private EventDAO eDAO;
    private PersonDAO pDAO;

    private User user;
    private User bestUser;

    LoginResult loginRes;
    LoginRequest loginReq;
    LoginService loginSer = new LoginService();
    Connection conn;


    @BeforeEach
    @DisplayName("Setting up Database Objects")
    public void setUp() throws DataAccessException
    {
        db = new Database();
        user = new User("sheila", "parker", "sheila@parker.com", "Sheila",
                "Parker", "f", "Sheila_Parker");

        bestUser = new User("carpioma", "carpio123", "mcarpiolazo@gmail.com",
                "Moises", "Carpio", "m", "Moises_Carpio");

        loginReq = new LoginRequest("carpioma", "carpio123");


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
    @DisplayName("Login Positive Test Case")
    public void loginPass() throws DataAccessException {
        uDAO.insert(bestUser);
        uDAO.insert(user);
        db.closeConnection(true);
        loginRes = loginSer.login(loginReq);
        assertNotNull(loginRes.getAuthtoken(), "Did not return an auth token for the user");
    }

    @Test
    @DisplayName("Login Negative Test Case")
    public void loginFail() throws DataAccessException {
        uDAO.insert(bestUser);
        uDAO.insert(user);
        db.closeConnection(true);
        loginReq.setUsername("Sheila");
        loginRes = loginSer.login(loginReq);
        assertNull(loginRes.getAuthtoken(), "Returned an AuthToken. Should have returned null");
    }
}
