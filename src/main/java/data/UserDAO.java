package data;

import model.User;
import java.sql.*;

public class UserDAO {
    private final Connection conn;
    public UserDAO(Connection conn){
        this.conn = conn;
    }

    /**
     * Insert rows in the database
     */
    public void insert(User user) throws DataAccessException{
        String sql = "INSERT INTO User (Username, Password, Email, FirstName, LastName, Gender, PersonID)" +
                "VALUES(?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getFirstName());
            stmt.setString(5, user.getLastName());
            stmt.setString(6, user.getGender());
            stmt.setString(7, user.getPersonID());

            stmt.executeUpdate();
        }   catch(SQLException e)   {
            throw new DataAccessException("Error encountered while inserting into the database");
        }
    }

    /**
     *
     * @param username unique username for the user
     * @return a User object
     * @throws DataAccessException Error related to accessing the database
     */
    public User retrieve(String username) throws DataAccessException {
        User user;
        ResultSet rs = null;
        String sql = "SELECT * FROM User WHERE Username = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, username);
            rs = stmt.executeQuery();
            if(rs.next())   {
                user = new User(rs.getString("Username"), rs.getString("Password"),
                        rs.getString("Email"), rs.getString("FirstName"),
                        rs.getString("LastName"), rs.getString(String.valueOf("Gender")),
                        rs.getString("PersonID"));
                return user;
            }
        }   catch (SQLException e)  {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while retrieving the user associated with " + username);
        }finally    {
            if(rs != null)  {
                try{
                    rs.close();
                }   catch   (SQLException e)    {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * Clears the table
     */
    public void clear() throws DataAccessException {
        String sql = "DELETE FROM User;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("SQL Error encountered while clearing User Table");
        }
    }
}
