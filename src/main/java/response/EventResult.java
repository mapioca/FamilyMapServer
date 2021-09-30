package response;

import model.Event;

public class EventResult {
    //DATA MEMBERS
    /**
     * An object containing all events
     */
    Event[] data;
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
     * Constructs object as a product of a successful transaction
     * @param data An object containing all events
     * @param success Shows whether or not the transaction was successful
     */
    public EventResult(Event[] data, Boolean success) {
        this.data = data;
        this.success = success;
    }

    /**
     * Constructs object as a product of a failed transaction
     * @param success Shows whether or not the transaction was successful
     * @param message Contains the description of the reason why the transaction was not successful
     */
    public EventResult(Boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public EventResult() {

    }

    //SETTERS AND GETTERS
    public Event[] getData() {
        return data;
    }

    public void setData(Event[] data) {
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
