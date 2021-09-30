package model;

public class User {
    /**
     * Unique username identifier of the user
     */
    String username;

    /**
     * User’s pass code
     */
    String password;

    /**
     * User’s email address
     */
    String email;

    /**
     * User’s first name
     */
    String firstName;

    /**
     * User’s last name
     */
    String lastName;

    /**
     * User's gender male or female
     */
    String gender;

    /**
     * User's unique person identifier
     */
    String personID;

    //CONSTRUCTOR

    /**
     * Constructs a User object
     * @param username Unique username identifier of the user
     * @param password User’s pass code
     * @param email User’s email address
     * @param firstName User’s first name
     * @param lastName User’s last name
     * @param gender User's gender male or female
     * @param personID User's unique person identifier
     */
    public User(String username, String password, String email, String firstName, String lastName, String gender, String personID) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.personID = personID;
    }

    //SETTERS AND GETTERS
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;
        if (o instanceof User) {
            User oUser = (User) o;
            return oUser.getUsername().equals(getUsername()) &&
                    oUser.getPassword().equals(getPassword()) &&
                    oUser.getEmail().equals(getEmail()) &&
                    oUser.getFirstName().equals(getFirstName()) &&
                    oUser.getLastName().equals(getLastName()) &&
                    oUser.getGender().equals(getGender()) &&
                    oUser.getPersonID().equals(getPersonID());
        } else {
            return false;
        }
    }
}
