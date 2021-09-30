package data;

import model.Person;
import oracle.jdbc.proxy.annotation.Pre;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class PersonDAO {
    private final Connection conn;
    public PersonDAO(Connection conn) { this.conn = conn;}

    /**
     * Insert rows in database
     */
    public void insert(Person person) throws DataAccessException{
        String sql = "INSERT INTO Person (PersonID, AssociatedUsername, FirstName, LastName," +
                "Gender, FatherID, MotherID, SpouseID)" + "VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, person.getPersonID());
            stmt.setString(2, person.getUsername());
            stmt.setString(3, person.getFirstName());
            stmt.setString(4, person.getLastName());
            stmt.setString(5, person.getGender());
            stmt.setString(6, person.getFatherID());
            stmt.setString(7, person.getMotherID());
            stmt.setString(8, person.getSpouseID());

            stmt.executeUpdate();
        } catch(SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while inserting a person into the database");
        }
    }

    /**
     *
     * @param personID Person's unique identifier, every person in the database has one
     * @return a person object
     * @throws DataAccessException error related to accessing the database
     */
    public Person retrieve(String personID) throws DataAccessException {
        Person person;
        ResultSet rs = null;
        String sql = "SELECT * FROM Person WHERE PersonID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, personID);
            rs = stmt.executeQuery();
            if(rs.next()) {
                person = new Person(rs.getString("PersonID"), rs.getString("AssociatedUsername"),
                        rs.getString("FirstName"), rs.getString("LastName"),
                        rs.getString("Gender"), rs.getString("FatherID"),
                        rs.getString("MotherID"), rs.getString("SpouseID"));
                return person;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while retrieving all persons associated with" + personID);
        } finally {
            if(rs != null) {
                try{
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public Person[] retrieveByUsername(String username) throws DataAccessException {
        Person[] personArray = null;
        Set<Person> tempList = new HashSet<>();
        Person person;
        ResultSet rs = null;
        String sql = "SELECT * FROM Person WHERE AssociatedUsername = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            rs = stmt.executeQuery();

            while(rs.next()) {
                person = new Person(rs.getString("PersonID"), rs.getString("AssociatedUsername"),
                        rs.getString("FirstName"), rs.getString("LastName"),
                        rs.getString("Gender"), rs.getString("FatherID"),
                        rs.getString("MotherID"), rs.getString("SpouseID"));
                tempList.add(person);
            }

            int i = 0;
            if(tempList.size() != 0){
                personArray = new Person[tempList.size()];
                for(Person p : tempList){
                    personArray[i] = p;
                    i++;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while retrieving all persons associated with" + username);
        } finally {
            if(rs != null) {
                try{
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return personArray;
    }

    public void deleteUserData(String username) throws DataAccessException {
        String sql = "DELETE FROM Person WHERE AssociatedUsername = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while deleting all persons associated with" + username);
        }
    }

    /**
     * Clears the table
     */
    public void clear() throws DataAccessException{
        String sql = "DELETE FROM Person;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("SQL Error encountered while clearing Person Table");
        }
    }
}
