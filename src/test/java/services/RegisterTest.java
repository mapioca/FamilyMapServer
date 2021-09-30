package services;

import data.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import request.RegisterRequest;
import response.RegisterResult;
import service.RegisterService;

import java.io.IOException;
import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class RegisterTest {
    private Database db;

    RegisterRequest registerReq;
    RegisterRequest registerBadReq;
    RegisterRequest duplicateReq;
    RegisterResult registerRes;
    RegisterResult registerBadRes;
    RegisterResult duplicateRes;
    RegisterService registerSer = new RegisterService();
    Connection conn;


    @BeforeEach
    @DisplayName("Setting up Database Objects")
    public void setUp() throws DataAccessException
    {
        db = new Database();
        registerReq = new RegisterRequest("carpioma", "carpioMM5427", "mcarpiolazo@gmail.com",
                "Moises", "Carpio", "m");

        registerBadReq = new RegisterRequest(null, null, null, null, null,
                null);

        duplicateReq = registerReq;


    }

    @AfterEach
    @DisplayName("Closing Database Connection")
    public void tearDown() throws DataAccessException {
        conn = db.getConnection();
        EventDAO eDAO = new EventDAO(conn);
        PersonDAO pDAO = new PersonDAO(conn);
        UserDAO uDAO = new UserDAO(conn);
        AuthTokenDAO aDAO = new AuthTokenDAO(conn);
        aDAO.clear();
        eDAO.clear();
        pDAO.clear();
        uDAO.clear();
        db.closeConnection(true);
    }

    @Test
    @DisplayName("Register Positive Test Case")
    public void registerPass() throws DataAccessException, IOException {
        registerRes = registerSer.register(registerReq);
        assertEquals(true, registerRes.getSuccess(),
                " Register Service failed.");
        assertNotNull(registerRes.getAuthtoken(),
                "Did not return an auth token for the user");

    }

    @Test
    @DisplayName("Register Negative Test Case")
    public void registerFail() throws DataAccessException, IOException {

        registerRes = registerSer.register(registerReq);
        assertEquals(true, registerRes.getSuccess());

        registerBadRes = registerSer.register(registerBadReq);
        assertNotEquals(true, registerBadRes.getSuccess());
    }
}
