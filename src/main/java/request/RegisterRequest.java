package request;

/**
 * Generates an object needed to execute a Register transaction
 */
public class RegisterRequest {
    //DATA MEMBERS
    /**
     * The unique name by which the user will be recognized
     */
    String username;

    /**
     * Combination of characters intended to protect the user's data
     */
    String password;

    /**
     * Email address used as the user's email
     */
    String email;

    /**
     * First name of user
     */
    String firstName;

    /**
     * Last name of the user
     */
    String lastName;

    /**
     * User's gender - Either male or female
     */
    String gender;

    //CONSTRUCTOR

    /**
     * Constructs the object containing the Register request body.
     * @param username The unique name by which the user will be recognized
     * @param password Combination of characters intended to protect the user's data
     * @param email Email address used as the user's email
     * @param firstName First name of user
     * @param lastName Last name of the user
     * @param gender User's gender - Either male or female
     */
    public RegisterRequest(String username, String password, String email, String firstName, String lastName, String gender){
        this.username = username;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
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

}
