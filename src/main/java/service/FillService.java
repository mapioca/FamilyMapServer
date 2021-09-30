package service;

import data.*;
import genericData.Locations;
import genericData.Names;
import model.*;
import response.FillResult;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import static json.JSonConverter.JSConvert;

/**
 * Populates the server's database with generated data for the specified user name.
 * The required "username" parameter must be a user already registered with the server. If there is
 * any data in the database already associated with the given user name, it is deleted. The
 * optional generations parameter lets the caller specify the number of generations of ancestors
 * to be generated, and must be a non-negative integer (the default is 4, which results in 31 new
 * persons each with associated events).
 */
public class FillService {
    String mainUsername = null;
    int completedGenerations = 0;
    int generationsNum = 0;
    int eventCount = 0;
    int personCount = 0;

    /**
     *Populates the server's database with generated data for the specified user name.
     * Number of generations is decided by user
     * @param username unique user identifier
     * @param generations number of generations to generate database with
     * @return object containing success/failure information
     */
    public FillResult fill(String username, int generations) throws DataAccessException, FileNotFoundException {
        this.mainUsername = username;
        this.generationsNum = generations;

        Database db = new Database();
        Connection conn = db.getConnection();
        UserDAO uDAO = new UserDAO(conn);
        EventDAO eDAO = new EventDAO(conn);
        AuthTokenDAO aDAO = new AuthTokenDAO(conn);
        PersonDAO pDAO = new PersonDAO(conn);
        FillResult fillRslt = new FillResult();

            try {
                if(generations >= 0){
                //Check if username exists in database
                User dbUser = uDAO.retrieve(username);
                if(dbUser != null){

                    //Delete all data related to the user (if any)
                    eDAO.deleteUserData(username);
                    aDAO.deleteUserData(username);
                    pDAO.deleteUserData(username);

                    //Generate a person for the user
                    Person userPerson = new Person(dbUser.getPersonID(), dbUser.getUsername(), dbUser.getFirstName(),
                            dbUser.getLastName(), dbUser.getGender(), null, null, null);
                    FullPerson personAndEvent = new FullPerson();
                    personAndEvent.setPerson(userPerson);

                    //Generate event for user
                    Event birthEvent = CreateEvent(userPerson);
                    eDAO.insert(birthEvent);
                    eventCount++;
                    completedGenerations++;

                    //Closing connection
                    conn = null;
                    db.closeConnection(true);

                    personAndEvent.addEvent("Birth", birthEvent);

                    //Generate new data based on # of gen
                    StartFamily(personAndEvent);

                    fillRslt = new FillResult();
                    fillRslt.setMessage("Successfully added " + personCount + " persons and " +
                            eventCount + " events to the database.");
                    fillRslt.setSuccess(true);

                } else {
                    db.closeConnection(true);
                    fillRslt.setMessage("Error: Username not found in database");
                    fillRslt.setSuccess(false);
                }
            } else {
                    db.closeConnection(true);
                    fillRslt.setMessage("Error: Generations need to be a positive value.");
                    fillRslt.setSuccess(false);
                }


            } catch (Throwable e) {
                if(conn != null) {
                    db.closeConnection(true);
                }
                e.printStackTrace();
                fillRslt.setMessage("Error: Internal Server Error.");
                fillRslt.setSuccess(false);
        }
        return fillRslt;
    }



    private void StartFamily(FullPerson person) throws IOException, DataAccessException {
        Set<FullPerson> motherSet = new HashSet<>();
        Set<FullPerson> fatherSet = new HashSet<>();
        //Create Parents
        FullPerson[] parentsArray = CreateParents(person);

        //Finish up last user person's information
        person.getPerson().setMotherID(parentsArray[0].getPerson().getPersonID());
        person.getPerson().setFatherID(parentsArray[1].getPerson().getPersonID());

        completedGenerations++;

        motherSet.add(parentsArray[0]);
        fatherSet.add(parentsArray[1]);

        //Add User's person to Database
        AddPersonToDb(person.getPerson());
        personCount++;

        //Generate 3 more generations
        CreateFamilyRecursively(motherSet, fatherSet);

    }

    private void CreateFamilyRecursively(Set<FullPerson> motherSet, Set<FullPerson> fatherSet) throws IOException, DataAccessException {
        Set<FullPerson> motherFamilySet;
        Set<FullPerson> fatherFamilySet;

        motherFamilySet = FamilyRecursiveHelper(motherSet);
        fatherFamilySet = FamilyRecursiveHelper(fatherSet);

        completedGenerations++;

        if (completedGenerations <= generationsNum) {
            CreateFamilyRecursively(motherFamilySet, fatherFamilySet);
        } else if(completedGenerations == generationsNum + 1) {
            for(FullPerson person : motherFamilySet) {
                personCount++;
                AddPersonToDb(person.getPerson());
            }
            for(FullPerson person : fatherFamilySet) {
                personCount++;
                AddPersonToDb(person.getPerson());
            }
        }

    }

    private Set<FullPerson> FamilyRecursiveHelper(Set<FullPerson> parentSet) throws IOException, DataAccessException {
        Set<FullPerson> parentFamilySet = new HashSet<>();

        for (FullPerson person : parentSet) {
            //Create Parents
            FullPerson[] parents = CreateParents(person);

            //Set Parent's IDs
            person.getPerson().setMotherID(parents[0].getPerson().getPersonID());
            person.getPerson().setFatherID(parents[1].getPerson().getPersonID());

            //Add person to Database
            AddPersonToDb(person.getPerson());
            personCount++;

            //Add parents to return object
            parentFamilySet.add(parents[0]);
            parentFamilySet.add(parents[1]);
        }

        return parentFamilySet;
    }

    private FullPerson[] CreateParents(FullPerson person) throws IOException, DataAccessException {
        FullPerson[] parentPair = new FullPerson[2];
        //Get Generic Data from json
        final String femaleNamesPath = "json/fnames.json";
        final String maleNamesPath = "json/mnames.json";
        final String lastNamesPath = "json/snames.json";


        Names femaleNames = JSConvert(femaleNamesPath, Names.class);
        Names maleNames = JSConvert(maleNamesPath, Names.class);
        Names lastNames = JSConvert(lastNamesPath, Names.class);

        //Create Mother (Person)
        String momFirstName = RndNameGenerator(femaleNames);
        String momLastName = RndNameGenerator(lastNames);
        String momPersonID = UUID.randomUUID().toString();
        Person mom = new Person(momPersonID, mainUsername, momFirstName, momLastName,
                "f", null, null, null);
        FullPerson mother = new FullPerson();
        mother.setPerson(mom);

        //Create Father (Person)
        String dadFirstName = RndNameGenerator(maleNames);
        String dadLastName = person.getPerson().getLastName();
        String dadPersonID = UUID.randomUUID().toString();
        Person dad = new Person(dadPersonID, mainUsername, dadFirstName, dadLastName,
                "m", null, null, momPersonID);
        mom.setSpouseID(dadPersonID);
        FullPerson father = new FullPerson();
        father.setPerson(dad);

        //Create Mother Events
        mother.addEvent("Birth", CreateEvent("Birth", mother, person));
        mother.addEvent("Marriage", CreateEvent("Marriage", mother, person));
        mother.addEvent("Death", CreateEvent("Death", mother, person));

        //Create Father Events
        father.addEvent("Birth", CreateEvent("Birth", father, person));
        father.addEvent("Marriage", CreateEvent("Marriage", father, mother));
        father.addEvent("Death", CreateEvent("Death", father, person));

        parentPair[0] = mother;
        parentPair[1] = father;

        return parentPair;
    }

    private Event CreateEvent(Person person) throws FileNotFoundException {
        final String locationsPath = "json/locations.json";
        Locations locations = JSConvert(locationsPath, Locations.class);
        Location location = RndLocationGenerator(locations);

        String aUsername = person.getUsername();
        String personID = person.getPersonID();
        float latitude = location.getLatitude();
        float longitude = location.getLongitude();
        String country = location.getCountry();
        String city = location.getCity();
        int year = RndNumberGenerator((2021 - 35), (2021 - 15));
        String eventID = UUID.randomUUID().toString();

        return new Event(eventID, aUsername, personID, latitude, longitude, country,
                city, "Birth", year);
    }

    private Event CreateEvent(String eventType, FullPerson person, FullPerson associatedPerson) throws FileNotFoundException, DataAccessException {
        Event event = null;

        final String locationsPath = "json/locations.json";
        Locations locations = JSConvert(locationsPath, Locations.class);
        Location location = RndLocationGenerator(locations);
        String aUsername = person.getPerson().getUsername();    //Associated Username - mainUser
        String personID = person.getPerson().getPersonID(); //PersonID - passed by person
        float latitude;
        float longitude;
        String country;
        String city;
        int year;
        String eventID;

        switch (eventType) {
            case "Birth":
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                country = location.getCountry();
                city = location.getCity();
                year = RndNumberGenerator((associatedPerson.getEvents().get("Birth").getYear() - 35),
                        (associatedPerson.getEvents().get("Birth").getYear() - 20));
                eventID = UUID.randomUUID().toString();
                event = new Event(eventID, aUsername, personID, latitude, longitude, country, city,
                        eventType, year);
                break;


            case "Marriage":
                if (person.getPerson().getGender().equals("m")) {
                    latitude = associatedPerson.getEvents().get("Marriage").getLatitude();
                    longitude = associatedPerson.getEvents().get("Marriage").getLongitude();
                    country = associatedPerson.getEvents().get("Marriage").getCountry();
                    city = associatedPerson.getEvents().get("Marriage").getCity();
                    year = associatedPerson.getEvents().get("Marriage").getYear();
                    eventID = UUID.randomUUID().toString();
                    event = new Event(eventID, aUsername, personID, latitude, longitude, country, city,
                            eventType, year);
                    break;

                } else {    //Mom falls here
                    latitude = location.getLatitude();    //Latitude - json(random)
                    longitude = location.getLongitude();  //Longitude- json(random)
                    country = location.getCountry(); //Country - json(random)
                    city = location.getCity();   //City - json(random)
                    year = RndNumberGenerator(associatedPerson.getEvents().get("Birth").getYear() - 3,
                            associatedPerson.getEvents().get("Birth").getYear());
                    eventID = UUID.randomUUID().toString();
                    event = new Event(eventID, aUsername, personID, latitude, longitude, country, city,
                            eventType, year);
                    break;
                }

            case "Death":
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                country = location.getCountry();
                city = location.getCity();
                if(person.getEvents().get("Birth").getYear() < 1910 ) {
                    year = RndNumberGenerator(person.getEvents().get("Marriage").getYear() + 1,
                            person.getEvents().get("Birth").getYear() + 110);
                } else {
                    year = RndNumberGenerator(person.getEvents().get("Marriage").getYear() + 1,
                            2021);
                }
                eventID = UUID.randomUUID().toString();
                event = new Event(eventID, aUsername, personID, latitude, longitude, country, city,
                        eventType, year);
                break;
        }
        AddEventToDB(event);
        eventCount++;
        return event;
    }

    private String RndNameGenerator(Names nameList) {
        int rnd = new Random().nextInt(nameList.getData().length);
        return nameList.getData()[rnd];
    }

    private int RndNumberGenerator(int yearStart, int yearEnd) {
        return new Random().nextInt(yearEnd - yearStart) + yearStart;
    }

    private Location RndLocationGenerator(Locations locationList) {
        int rnd = new Random().nextInt(locationList.getData().length);
        return locationList.getData()[rnd];
    }

    private void AddEventToDB(Event event) throws DataAccessException {
        Database db = new Database();
        Connection conn = db.getConnection();
        EventDAO eDAO = new EventDAO(conn);
        eDAO.insert(event);
        db.closeConnection(true);
    }

    private void AddPersonToDb(Person person) throws DataAccessException {
        Database db = new Database();
        Connection conn = db.getConnection();
        PersonDAO pDAO = new PersonDAO(conn);
        pDAO.insert(person);
        db.closeConnection(true);
    }
}
