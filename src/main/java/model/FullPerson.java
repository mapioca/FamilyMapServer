package model;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class FullPerson {
    Person person;
    Map<String, Event> events = new HashMap<>();

    public FullPerson() { }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Map<String, Event> getEvents() {
        return events;
    }

    public void setEvents(Map<String, Event> events) {
        this.events = events;
    }

    public void addEvent(String eventType, Event event){
        events.put(eventType, event);
    }
}
