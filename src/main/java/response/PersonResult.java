package response;

import model.Person;

public class PersonResult {
    /**
     *
     */
    Person[] data;

    /**
     * Shows whether or not the transaction was successful
     */
    Boolean success;

    /**
     * Contains the description of the reason why the transaction was not successful
     */
    String message;

    //CONSTRUCTOR

    /**
     * Constructs an object product of a failed transaction
     * @param message the message containing the reason why the transaction was not successful
     * @param success shows whether or not the transaction was successful
     */
    public PersonResult(String message, boolean success){
        this.message = message;
        this.success = success;
    }

    public PersonResult() {

    }

    //SETTERS AND GETTERS
    public Person[] getData() {
        return data;
    }

    public void setData(Person[] data) {
        this.data = data;
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
