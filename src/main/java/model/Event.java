package model;

public class Event {
    //DATA MEMBERS
    /**
     * Unique event identifier
     */
    String eventID;

    /**
     * Username associated with event
     */
    String associatedUsername;

    /**
     * Unique person identifier to the event
     */
    String personID;

    /**
     * Position related object
     */
    float latitude;

    /**
     * Position related object
     */
    float longitude;

    /**
     * Country where event happen
     */
    String country;

    /**
     * City where event happen
     */
    String city;

    /**
     * Type of event
     */
    String eventType;

    /**
     * Year event happen
     */
    Integer year;

    //CONSTRUCTOR

    /**
     *
     * @param eventID Unique event identifier
     * @param associatedUsername Username associated with event
     * @param personID Unique person identifier to the event
     * @param latitude Position related object
     * @param longitude Position related object
     * @param country Country where event happen
     * @param city City where event happen
     * @param eventType Type of event
     * @param year Year event happen
     */
    public Event(String eventID, String associatedUsername, String personID, float latitude, float longitude, String country, String city, String eventType, Integer year) {
        this.eventID = eventID;
        this.associatedUsername = associatedUsername;
        this.personID = personID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.country = country;
        this.city = city;
        this.eventType = eventType;
        this.year = year;
    }

    public Event() {

    }

    //SETTERS AND GETTERS

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public String getUsername() {
        return associatedUsername;
    }

    public void setUsername(String associatedUsername) {
        this.associatedUsername = associatedUsername;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;
        if (o instanceof Event) {
            Event oEvent = (Event) o;
            return oEvent.getEventID().equals(getEventID()) &&
                    oEvent.getUsername().equals(getUsername()) &&
                    oEvent.getPersonID().equals(getPersonID()) &&
                    oEvent.getLatitude() == (getLatitude()) &&
                    oEvent.getLongitude() == (getLongitude()) &&
                    oEvent.getCountry().equals(getCountry()) &&
                    oEvent.getCity().equals(getCity()) &&
                    oEvent.getEventType().equals(getEventType()) &&
                    oEvent.getYear().equals(getYear());
        } else {
            return false;
        }
    }
}
