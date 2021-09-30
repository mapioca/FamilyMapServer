package model;

import java.util.Objects;

public class Person {
    /**
     * Unique identifier for this person
     */
    String personID;

    /**
     * User (Username) to which this person belongs
     */
    String associatedUsername;

    /**
     * Person’s first name
     */
    String firstName;

    /**
     * Person’s last name
     */
    String lastName;

    /**
     * Person’s gender, male or female
     */
    String gender;

    /**
     * Unique person's father identifier
     */
    String fatherID;

    /**
     * Unique person's moter identifier
     */
    String motherID;

    /**
     * Unique person's spouse identifier
     */
    String spouseID;

    //CONSTRUCTOR

    /**
     * Complete constructor
     * @param personID Unique identifier for this person
     * @param associatedUsername Unique username identifier of the user
     * @param firstName User’s first name
     * @param lastName Person’s last name
     * @param gender Person’s gender, male or female
     * @param fatherID Person's father unique identifier
     * @param motherID Person's mother unique identifier
     * @param spouseID Person's spouse unique identifier
     */
    public Person(String personID, String associatedUsername, String firstName, String lastName, String gender,
                  String fatherID, String motherID, String spouseID) {
        this.personID = personID;
        this.associatedUsername = associatedUsername;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.fatherID = fatherID;
        this.motherID = motherID;
        this.spouseID = spouseID;
    }



    //SETTERS AND GETTERS
    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public String getUsername() {
        return associatedUsername;
    }

    public void setUsername(String associatedUsername) {
        this.associatedUsername = associatedUsername;
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

    public String getFatherID() {
        return fatherID;
    }

    public void setFatherID(String fatherID) {
        this.fatherID = fatherID;
    }

    public String getMotherID() {
        return motherID;
    }

    public void setMotherID(String motherID) {
        this.motherID = motherID;
    }

    public String getSpouseID() {
        return spouseID;
    }

    public void setSpouseID(String spouseID) {
        this.spouseID = spouseID;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;
        if (o instanceof Person) {
            Person oPerson = (Person) o;
            return oPerson.getPersonID().equals(getPersonID()) &&
                    oPerson.getUsername().equals(getUsername()) &&
                    oPerson.getFirstName().equals(getFirstName()) &&
                    oPerson.getLastName().equals(getLastName()) &&
                    oPerson.getGender().equals(getGender()) &&
                    Objects.equals(oPerson.getFatherID(), getFatherID()) &&
                    Objects.equals(oPerson.getMotherID(), getMotherID()) &&
                    Objects.equals(oPerson.getSpouseID(), getSpouseID());
        } else {
            return false;
        }
    }
}
