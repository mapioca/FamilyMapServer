package services;

import data.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import request.RegisterRequest;
import response.ClearResult;
import response.FillResult;
import response.RegisterResult;
import service.ClearService;
import service.FillService;
import service.RegisterService;

import java.io.IOException;
import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class FillTest {
    private Database db;
    private UserDAO uDAO;
    private AuthTokenDAO aDAO;
    private EventDAO eDAO;
    private PersonDAO pDAO;

    ClearService clearSer = new ClearService();
    ClearService clearSerLong = new ClearService();
    ClearResult clearRes;
    ClearResult clearResLong;
    RegisterRequest registerReq;
    RegisterRequest registerReqLong;
    RegisterResult registerRes;
    RegisterResult registerResLong;
    FillResult fillRes;
    FillResult fillResLong;
    FillService fillSer = new FillService();
    FillService fillSerLong = new FillService();
    RegisterService registerSer = new RegisterService();
    RegisterService registerSerLong = new RegisterService();

    String username;
    String usernameLong;
    int generations;
    int generationsLong;
    Connection conn;


    @BeforeEach
    @DisplayName("Setting up Database Objects")
    public void setUp() throws DataAccessException
    {
        db = new Database();
        username = "carpioma";
        generations = 4;
        usernameLong = "vickycarpio";
        generationsLong = 10;
        registerReq = new RegisterRequest("carpioma", "carpioMM5427", "mcarpiolazo@gmail.com",
                "Moises", "Carpio", "m");
        registerReqLong = new RegisterRequest("vickycarpio", "civky542734", "victoria,carpio@gmail.com",
                "Victoria", "Carpio", "f");

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
    @DisplayName("Short Fill Positive Test Case")
    public void fillPass() throws DataAccessException, IOException {
        clearRes = clearSer.clear();
        registerRes = registerSer.register(registerReq);
        fillRes = fillSer.fill(username, generations);
        assertEquals(true, clearRes.getSuccess());
        assertEquals(true, registerRes.getSuccess());
        assertEquals(true, fillRes.getSuccess());
    }

    @Test
    @DisplayName("Long Fill Positive Test Case")
    public void longFillPass() throws DataAccessException, IOException {
        clearResLong = clearSerLong.clear();
        registerResLong = registerSerLong.register(registerReqLong);
        fillResLong = fillSerLong.fill(usernameLong, generationsLong);
        assertEquals(true, clearResLong.getSuccess());
        assertEquals(true, registerResLong.getSuccess());
        assertEquals(true, fillResLong.getSuccess());
    }

    @Test
    @DisplayName("Fill Negative Test Case")
    public void fillFail() throws DataAccessException, IOException {
        fillResLong = fillSerLong.fill(usernameLong, generationsLong);
        assertEquals(false, fillResLong.getSuccess());
    }
}
