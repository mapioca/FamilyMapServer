package request;

import model.Event;
import model.Person;
import model.User;

/**
 * The users property in the request body contains an array of users to be
 * created. The persons and events properties contain family history information for these
 * users. The objects contained in the persons and events arrays should be added to the
 * server’s database. The objects in the users array have the same format as those passed to
 * the /user/register API with the addition of the personID.  The objects in the events
 * array have the same format as those returned by the /event/[eventID] API.
 */
public class LoadRequest {
    /**
     * The users property in the request body contains an array of users to be
     * created.
     */
    User[] users;

    /**
     * The objects in the users array have the same format as those passed to
     * the /user/register API with the addition of the personID.
     */
    Person[] persons;

    /**
     * The objects in the events
     * array have the same format as those returned by the /event/[eventID] API.
     */
    Event[] events;

    /**
     * Contains all needed objects to run the load function
     * @param users The users property in the request body contains an array of users to be created.
     * @param persons The objects in the users array have the same format as those passed to
     *                the /user/register API with the addition of the personID.
     * @param events The objects in the events array have the same format as
     *               those returned by the /event/[eventID] API.
     */
    public LoadRequest(User[] users,Person[] persons, Event[] events){
        this.users = users;
        this.persons = persons;
        this.events = events;
    }

    public LoadRequest() {
    }

    public User[] getUsers() {
        return users;
    }

    public void setUsers(User[] users) {
        this.users = users;
    }

    public Person[] getPersons() {
        return persons;
    }

    public void setPersons(Person[] persons) {
        this.persons = persons;
    }

    public Event[] getEvents() {
        return events;
    }

    public void setEvents(Event[] events) {
        this.events = events;
    }
}
