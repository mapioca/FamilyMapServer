package data;

import model.AuthToken;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthTokenDAO {
    private final Connection conn;
    public AuthTokenDAO(Connection conn) { this.conn = conn;}

    /**
     * Insert rows in database
     */
    public void insert(AuthToken authToken) throws DataAccessException{
        String sql = "INSERT INTO AuthToken (AuthToken, AssociatedUsername)" +
                "VALUES(?, ?)";
        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, authToken.getAuthToken());
            stmt.setString(2, authToken.getUsername());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while inserting into the database");
        }
    }

    public AuthToken retrieve(String authenticationToken) throws DataAccessException{
        AuthToken authToken;
        ResultSet rs = null;
        String sql = "SELECT * FROM AuthToken WHERE AuthToken = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, authenticationToken);
            rs = stmt.executeQuery();
            if(rs.next()) {
                authToken = new AuthToken(rs.getString("AuthToken"),
                        rs.getString("AssociatedUsername"));
                return authToken;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while retrieving the authToken " + authenticationToken);
        } finally {
            if(rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public void deleteUserData(String username) throws DataAccessException {
        String sql = "DELETE FROM AuthToken WHERE AssociatedUsername = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while deleting the authTokens associated with " +
                    username);
        }
    }

    /**
     * Delete rows in database
     */
    public void clear() throws DataAccessException{
        String sql = "DELETE FROM AuthToken;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("SQL Error encountered while clearing AuthToken Table");
        }
    }
}
