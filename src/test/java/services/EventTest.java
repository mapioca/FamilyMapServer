package services;

import data.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import request.RegisterRequest;
import response.EventResult;
import service.EventService;
import service.RegisterService;

import java.io.IOException;
import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class EventTest {
    private Database db;
    private PersonDAO pDAO;
    private UserDAO uDAO;
    private AuthTokenDAO aDAO;
    private EventDAO eDAO;
    Connection conn;
    private final EventService personService = new EventService();
    RegisterRequest registerReq;


    @BeforeEach
    @DisplayName("Setting up Database Objects")
    public void setUp() throws DataAccessException
    {
        db = new Database();
        registerReq = new RegisterRequest("carpioma", "carpioMM5427", "mcarpiolazo@gmail.com",
                "Moises", "Carpio", "m");

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
    @DisplayName("Event Positive Test Case")
    public void eventPass() throws DataAccessException, IOException {
        RegisterService registerSer = new RegisterService();
        registerSer.register(registerReq);
        db.closeConnection(true);
        EventResult eventResult = personService.getEvent("carpioma");
        assertNotNull(eventResult);
        assertEquals(true, eventResult.getSuccess(),
                "Event service failed. Success returned false");
    }

    @Test
    @DisplayName("Event Negative Test Case")
    public void eventFail() throws DataAccessException, IOException {
        RegisterService registerSer = new RegisterService();
        registerSer.register(registerReq);
        db.closeConnection(true);
        EventResult eventResult = personService.getEvent("wrongUsername");
        assertEquals(false, eventResult.getSuccess(),
                "Person Service should have not returned an object.");
    }
}
