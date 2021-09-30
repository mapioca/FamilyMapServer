package services;

import data.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import request.RegisterRequest;
import response.PersonResult;
import service.PersonService;
import service.RegisterService;

import java.io.IOException;
import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class PersonTest {
    private Database db;
    private PersonDAO pDAO;
    private UserDAO uDAO;
    private AuthTokenDAO aDAO;
    private EventDAO eDAO;
    Connection conn;
    private final PersonService personService = new PersonService();
    RegisterRequest registerReq;


    @BeforeEach
    @DisplayName("Setting up Database Objects")
    public void setUp() throws DataAccessException
    {
        db = new Database();
        registerReq = new RegisterRequest("carpioma", "carpioMM5427", "mcarpiolazo@gmail.com",
                "Moises", "Carpio", "m");

        Connection conn = db.getConnection();
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
    @DisplayName("Person Positive Test Case")
    public void personPass() throws DataAccessException, IOException {
        RegisterService registerSer = new RegisterService();
        db.closeConnection(true);
        registerSer.register(registerReq);
        PersonResult personResult = personService.getPersonGroup("carpioma");
        assertNotNull(personResult);
        assertEquals(true, personResult.getSuccess(),
                "Person service failed. Success returned false");
    }

    @Test
    @DisplayName("Person Negative Test Case")
    public void personFail() throws DataAccessException, IOException {
        RegisterService registerSer = new RegisterService();
        registerSer.register(registerReq);
        db.closeConnection(true);
        PersonResult personResult = personService.getPersonGroup("wrongUsername");
        assertEquals(false, personResult.getSuccess(),
                "Person Service should have not returned an object.");
    }
}
