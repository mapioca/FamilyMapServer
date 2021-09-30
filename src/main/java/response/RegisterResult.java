package response;

/**
 * Generates objects based on the success or failure of a Register transaction
 */
public class RegisterResult {
    //DATA MEMBERS
    /**
     * Unique authentication token for security purposes
     */
    String authtoken;

    /**
     * Unique name identifier for the user
     */
    String username;

    /**
     * Person identifier unique to an individual in the family tree
     */
    String personID;

    /**
     * Shows whether or not the transaction was successful
     */
    Boolean success;

    /**
     * Contains the description of the reason why the transaction was not successful
     */
    String message;

    //CONSTRUCTORS
    /**
     * Constructs an object product of a failed transaction
     * @param message the message containing the reason why the transaction was not successful
     * @param success shows whether or not the transaction was successful
     */
    public RegisterResult(String message, Boolean success) {
        this.message = message;
        this.success = success;
    }

    /**
     * Constructs an object product of a successful transaction
     * @param authtoken Unique authentication token for security purposes
     * @param username Unique name identifier for the user
     * @param personID Person identifier unique to an individual in the family tree
     * @param success Shows whether or not the transaction was successful
     */
    public RegisterResult(String authtoken, String username, String personID, boolean success){
        this.authtoken = authtoken;
        this.username = username;
        this.personID = personID;
        this.success = success;
    }

    public RegisterResult() {

    }

    //SETTERS AND GETTERS
    public String getAuthtoken() {
        return authtoken;
    }

    public void setAuthtoken(String authtoken) {
        this.authtoken = authtoken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
