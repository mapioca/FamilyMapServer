package services;

import data.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import request.LoadRequest;
import response.LoadResult;
import service.LoadService;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import static json.JSonConverter.JSConvert;
import static org.junit.jupiter.api.Assertions.*;

public class LoadTest {
    private Database db;
    private UserDAO uDAO;
    private AuthTokenDAO aDAO;
    private EventDAO eDAO;
    private PersonDAO pDAO;

    LoadRequest loadReq;
    LoadRequest incompleteLoadReq;
    LoadResult loadRes;
    LoadResult incompleteLoadRes;
    LoadService loadSer = new LoadService();
    Connection conn;


    @BeforeEach
    @DisplayName("Setting up Database Objects")
    public void setUp() throws DataAccessException, FileNotFoundException {
        db = new Database();
        final String loadDataPath = "passoffFiles/LoadData.json";
        loadReq = JSConvert(loadDataPath, LoadRequest.class);
        final String incompleteLoadDataPath = "passoffFiles/LoadIncompleteData.json";
        incompleteLoadReq = JSConvert(incompleteLoadDataPath, LoadRequest.class);

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
    @DisplayName("Load Positive Test Case")
    public void loadPass() throws DataAccessException, IOException, SQLException {
        loadRes = loadSer.load(loadReq);
        assertEquals(true, loadRes.getSuccess());

        conn = db.getConnection();
        eDAO = new EventDAO(conn);
        pDAO = new PersonDAO(conn);
        uDAO = new UserDAO(conn);

        assertEquals("sheila", pDAO.retrieve("Sheila_Parker").getUsername());
        assertEquals("Jones", pDAO.retrieve("Frank_Jones").getLastName());
        assertEquals("Patrick_Spencer", pDAO.retrieve("Patrick_Spencer").getPersonID());

    }

    @Test
    @DisplayName("Load Negative Test Case")
    public void loadFail() throws DataAccessException, IOException, SQLException {

        conn = db.getConnection();
        eDAO = new EventDAO(conn);
        pDAO = new PersonDAO(conn);
        uDAO = new UserDAO(conn);
        aDAO = new AuthTokenDAO(conn);

        incompleteLoadRes = loadSer.load(incompleteLoadReq);
        assertNull(uDAO.retrieve("patrick"),
                "Found user when user is not in database.");

        assertNull(pDAO.retrieve("Happy_Birthday"),
                "Found person on database when person is not in database.");

        assertNull(eDAO.retrieve("True_Love"),
                "Found event on database when event is not");
    }

}
