package se.su.dsv.pvt.utemaning.backend;

import javax.sql.rowset.CachedRowSet;
import java.sql.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * this is a class to handle all database management. instead of having to know every class that does one thing to the database
 * we got 1 class that has metods that does those things. one object to use methods on instead of 20 different classes to handle.
 *
 * The SQL code written in this class is not very safe and is vulnerable to SQLinjection, we do however not have the time to make
 * it safe.
 * <p>
 * Comments are above the code it refers to
 * <p>
 * METHODS SO FAR
 * getAllChallenge
 * getOneChallenge
 * challengeBuilder
 * getAllOutdoorGyms
 * getOneOutdoorGym
 * outdoorGymBuilder
 * getAllUsers
 * getOneUser
 * getParticipations
 * participationBuilder
 * addNewUser
 * userBuilder
 * addNewOutdoorGym
 * addNewChallenge
 * addNewParticipation
 * <p>
 * TO FIX
 *
 *
 * TO DO
 *
 * @author Michel
 */


public class DBManagement {
    private String errorMessage;
    private CachedRowSet crs;
    private ConnectionToPasiDB ctpdb = new ConnectionToPasiDB();


    /**
     * a method to retrive all challenges from the database and return those challenges in the form of a collection
     * will return empthy collection if no challenges exsists
     *
     * @return a collection in the form of an arraylist
     */
    public Collection getAllChallenge() {

        String sqlQuery = ("SELECT * FROM `Challenge`");
        Collection<Challenge> challengeCollection = new ArrayList<>();
        try {
            crs = ctpdb.getData(sqlQuery);
            while (crs.next()) {
                Challenge challenge = challengeBuilder(crs);
                challengeCollection.add(challenge);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return challengeCollection;
    }

    /**
     * a method to collect a single challenge from the database based on the challenge id nummer, creates that challenge and
     * returns it
     * will return null if challenge does not exsists
     *
     * @param challengeIDInput the ID of the challenge to be retrived.
     * @return returns a challenge object if found in database otherwise an empty object
     */
    public Challenge getSpecificChallenge(int challengeIDInput) {
        String sqlQuery = ("SELECT * FROM `Challenge` WHERE ChallengeID = '"+challengeIDInput+"' ");
        Challenge challenge = null;
        try {
            crs = ctpdb.getData(sqlQuery);
            while (crs.next()) {
                challenge = challengeBuilder(crs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (challenge != null) {
            return challenge;
        } else return null;
    }

    /**
     * repurposed this method. it will not take an outdoorgym and get all challanges at that outdoorgym and add them to
     * that outdoorgyms list and then return the outdoorgym.
     *
     * @param outdoorGym the object whos challanges to retrive
     * @return the same object but with all challanges related to it
     */
    private OutdoorGym getAllChallengeAtSpot(OutdoorGym outdoorGym) {
        int workoutspotID = outdoorGym.getId();
        String sqlQuery = ("SELECT * FROM `Challenge` WHERE WorkoutSpotId = '" + workoutspotID + "' ");

        try {
            CachedRowSet crsChallenge = ctpdb.getData(sqlQuery);
            while (crsChallenge.next()) {
                Challenge challenge = challengeBuilder(crsChallenge);
                outdoorGym.challengeList.add(challenge);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }return outdoorGym;
    }
    /**
     * Support method for building challenge objects.
     *
     * @param crsChallenge a row from the table.
     * @return challenge object
     */
    private Challenge challengeBuilder(CachedRowSet crsChallenge) {
        Challenge challenge = null;
        try {
            String challengeName = crsChallenge.getString("ChallengeName");
            String challengeDesc = crsChallenge.getString("Description");
            int workoutSpotID = crsChallenge.getInt("WorkoutSpotID");
            int challengeID = crsChallenge.getInt("ChallengeID");
            int numberOfParticipants = crsChallenge.getInt("Participants");
            java.util.Date date = new Date(crsChallenge.getDate("Date").getTime());
            challenge = new Challenge(challengeName, numberOfParticipants, date, challengeDesc, challengeID, workoutSpotID);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return challenge;
    }

    /**
     * a method to collect all outdoorGyms from the database and returns those objects as a collection
     * will return empthy list if no outdoorgyms exsists
     *
     * @return collection of outdoorgyms
     */
    public ArrayList getAllOutdoorGyms() {
        ArrayList<OutdoorGym> outdoorGymCollection = new ArrayList<>();
        String sqlQuery = ("SELECT * FROM OutdoorGym, Workoutspot, Location WHERE " +
                "Workoutspot.WorkoutSpotId = OutdoorGym.WorkoutSpotId AND " +
                "Workoutspot.WorkoutSpotId = Location.WorkoutSpotID");
        try {
            crs = ctpdb.getData(sqlQuery);
            while (crs.next()) {
                OutdoorGym outdoorGym = buildOfOutdoorGym();
                outdoorGymCollection.add(outdoorGym);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return outdoorGymCollection;
    }

    /**
     * this is a method to collect a single outdoorGym from the database and return that object
     * will return null if outdoorgym does not exsists
     *
     * @param workoutSpotIdInput the primarykey of the workoutSpot to be found
     * @return returns the outdoorgym object
     */
    public OutdoorGym getOneOutdoorGym(int workoutSpotIdInput) {
        String sqlQuery = ("SELECT * FROM OutdoorGym, Workoutspot, Location " +
                "WHERE Workoutspot.WorkoutSpotId = OutdoorGym.WorkoutSpotId AND " +
                "Workoutspot.WorkoutSpotId = Location.WorkoutSpotID AND " +
                "OutdoorGym.WorkoutSpotId = ' "+workoutSpotIdInput+" ' ");
        OutdoorGym outdoorGym = null;
        try {
            crs = ctpdb.getData(sqlQuery);
            while (crs.next()) {
                outdoorGym = buildOfOutdoorGym();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (outdoorGym != null) {
            return outdoorGym;
        } else {
            System.out.println("gym is null");
            return null;
        }
    }

    /**
     * private method to create outdoorgyms in an attempt to redude code
     * @return outdoorgym
     */
    private OutdoorGym buildOfOutdoorGym() {
        OutdoorGym outdoorGym = null;
        try {
            int workoutSpotId = crs.getInt("WorkoutSpotId");
            String gymName = crs.getString("WorkoutSpotName");
            double longitude = crs.getDouble("GLongitude");
            double latitude = crs.getDouble("GLatitude");
            boolean hasChallenge = crs.getBoolean("HasChallenge");
            String gymDesctiption = crs.getString("outdoorGymDesc");
            String uniqueId = crs.getString("StockholmStadAPIKey");
            Location location = new Location(longitude, latitude);
            outdoorGym = new OutdoorGym(location, gymName, workoutSpotId, uniqueId, gymDesctiption);
            outdoorGym = getAllChallengeAtSpot(outdoorGym);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return outdoorGym;
    }

    /**
     * method to get all users from database and create user objects, and returns those objects in a colletion
     * will return empthy collection if no users in database
     *
     * @return returns a collection of all users.
     */
    public Collection getAllUsers() {

        String sqlQuery = ("SELECT * FROM `User`");
        Collection<User> userCollection = new ArrayList<>();
        userCollection.clear();
        try {
            crs = ctpdb.getData(sqlQuery);
            while (crs.next()) {
                User user = userBuilder(crs);
                userCollection.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userCollection;
    }

    /**
     * a method to get a singel user from the database
     * will return null if user does not exsists
     *
     * @param userNameInput name of the user to find
     * @return returns that user
     */
    public User getOneUser(String userNameInput) {

        User user = null;
        String sqlQuery = ("SELECT * FROM User WHERE UserName = ' " + userNameInput + " ' ");
        try {
            crs = ctpdb.getData(sqlQuery);
            while (crs.next()) {
                user = userBuilder(crs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (user != null) {
            return user;
        } else return null;
    }

    /**
     * method for creating user. takes 1 row from table and transforms that into a ser object.
     *
     * @param crs 1 row from the table
     * @return a user object
     */
    public User userBuilder(CachedRowSet crs) {
        User user = null;
        try {
            int userId = crs.getInt("UserID");
            String userName = crs.getString("UserName");
            String firstName = crs.getString("FirstName");
            String lastName = crs.getString("LastName");
            String email = crs.getString("E-Mail");
            user = new User(userName, userId, firstName, lastName, email);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    /**
     * this method retrives participations from the database, if null on username and 0 on challengeID it will retrive
     * ALL participations, otherwise it will get either all participations connected to a user or all participations
     * connected to a challenge. returns participation objects in a collection
     *
     * @param userName    name of user
     * @param challengeID ID of the challenge
     * @return a list of the participations
     */
    public Collection getParticipations(String userName, int challengeID) {
        Participation participation = null;
        String sqlQuery = null;
        Collection<Participation> participationCollection = new ArrayList<>();
        if (challengeID != 0) {
            sqlQuery = ("SELECT * FROM participation WHERE ChallengeID = '" + challengeID+"' ");
        } else if (!userName.equals(null)) {
            sqlQuery = ("SELECT * FROM participation WHERE UserName = '" + userName+"' ");
        } else {
            sqlQuery = ("SELECT * FROM Participation");
        }
        crs = ctpdb.getData(sqlQuery);
        try {
            while (crs.next()) {
                participation = participationBuilder(crs);
                participationCollection.add(participation);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            errorMessage = e.getMessage();
        }
        return participationCollection;
    }


    private Participation participationBuilder(CachedRowSet crs) {
        Participation participation = null;
        try {
            String userName = crs.getString("UserName");
            int challengeID = crs.getInt("ChallengeID");
            User user = getOneUser(userName);
            participation = new Participation(user, challengeID);
        } catch (SQLException e) {
            e.printStackTrace();
            errorMessage = e.getMessage();
        }
        return participation;
    }

    /**
     * this is a mthod used for the spring demo. can be used again if another demo is needed.
     */
    public String sprintDemo() {
        String sqlQuery = ("SELECT * FROM Challenge WHERE ChallengeID = 1");
        String message = null;
        try {
            crs = ctpdb.getData(sqlQuery);
            while (crs.next()) {
                String challengeName = crs.getString("ChallengeName");
                String challengeDesc = crs.getString("Description");
                int atLocationId = crs.getInt("WorkoutSpotID");
                int challengeID = crs.getInt("ChallengeID");
                int participants = crs.getInt("Participants");
                Date date = crs.getDate("Date");
                message = (challengeName + "\n" + challengeDesc);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return message;
    }

    /**
     * this is a method for adding a new user to the database. will return true and false depending on if the adding was
     * successfull, if a fail it will automaticly retrive the errormessage and hold it if its needed.
     *
     * @param userName  the username of the user to be added
     * @param firstName firstname of the user
     * @param lastName  lastname of the user
     * @param Email     email adress of the user
     * @return boolean true or false depending on result
     */
    public boolean addUser(String userName, String firstName, String lastName, String Email) {
        String sqlQuery = ("INSERT INTO User SET UserName = '" + userName + "', FirstName = '"
                + firstName + "', LastName = '" + lastName + "', Email ='" + Email + "'");
        boolean success = ctpdb.insertData(sqlQuery);
        if (!success) {
            errorMessage = ctpdb.getErrorMessage();
        }
        return success;
    }

    /**
     * this is a method for storing a new outdoorGym in the database. it inserts into 3 different tables and uses
     * the primary key generated in the first table as primary key on the other 2
     *
     * @param name        the name of the outdoorGym, taken from stockholm API
     * @param description the description of the outdoorGym, taken from stockholm API
     * @param longitude   the longitude of the outdoorGym, taken from stockholm API
     * @param latitude    the latitude of the outdoorGym, taken from stockholm API
     * @return returns a boolean true if all went fine, will return false if not.
     */

    public boolean addOutdoorGym(String name, String description, double longitude, double latitude, String apiKey) {
        String sqlQuery = ("INSERT INTO Workoutspot SET WorkoutSpotName = '" + name + "' , HasChallenge = false");
        int workoutSpotID = ctpdb.addAndReturnIncrementValue(sqlQuery);
        sqlQuery = ("INSERT INTO OutdoorGym SET WorkoutSpotId = '"
                + workoutSpotID + "', outdoorGymDesc = '" + description + "' ," +
                "StockholmStadAPIKey = '" + apiKey + "'");
        boolean successOnOutdoorgym = ctpdb.insertData(sqlQuery);
        if (!successOnOutdoorgym) {
            errorMessage = ctpdb.getErrorMessage();
            return false;
        }
        sqlQuery = ("INSERT INTO Location SET WorkoutSpotId = '" + workoutSpotID + "' , Longitude = '"
                + longitude + "', Latitude = '" + latitude + "' ");
        Boolean successOnLocation = ctpdb.insertData(sqlQuery);
        if (!successOnLocation) {
            errorMessage = ctpdb.getErrorMessage();
            return false;
        }
        return true;
    }

    /**
     * @param desc          decription of challenge
     * @param name          name of challenge
     * @param time          time of challange
     * @param date          date of challege
     * @param workoutSpotID at what location is the challenge
     * @return boolean true of false if successfly stored in database
     */

    public boolean addChallenge(String desc, String name, Time time, Date date, int workoutSpotID) {
        String sqlQuery = ("INSERT INTO Challenge SET WorkoutSpotid = '" + workoutSpotID
                + "' , ChallengeName = '" + name + "' , Time = '" + time + "' , Date = '" + date +
                "' , Description = '" + desc + "' , Participants = '0'");
        boolean success = ctpdb.insertData(sqlQuery);
        if (!success) {
            errorMessage = ctpdb.getErrorMessage();
        }
        return success;
    }

    /**
     * Method for adding a new participation to the database
     *
     * @param challengeID the primarykey of the challenge, and the identifier of the challenge
     * @param userName    a unique identifier for a user
     * @return will return true if all went well, otherwise get the error message and store it and return false
     */
    public boolean addParticipation(int challengeID, String userName) {
        String sqlQuery = ("INSERT INTO Participation SET ChallengeID ='" + challengeID + "' , UserName = '" + userName + "'");
        boolean success = ctpdb.insertData(sqlQuery);
        if (!success) {
            errorMessage = ctpdb.getErrorMessage();
            return false;
        }
        return true;
    }

    /**
     * a method for forwarding the error message so it can be used if needed
     *
     * @return the error message if SQL exception has been thrown when inserting to database
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    public boolean removeChallenge(int challengeId){
        String sqlQuery = ("DELETE FROM `Challenge` WHERE Challenge.ChallengeID = '"+challengeId+"' ");
            boolean success = ctpdb.insertData(sqlQuery);
            if(!success){
                errorMessage = ctpdb.getErrorMessage();
                return false;
            }return success;
    }
    public boolean removeParticipation (int challengeID, String userName){
        String sqlQuery = ("DELETE FROM  Participation WHERE Participation.ChallengeID = '"+challengeID+"' AND " +
                "Participation.UserName = '"+userName+"' ");
        boolean success = ctpdb.insertData(sqlQuery);
        if(!success){
            errorMessage = ctpdb.getErrorMessage();
            return false;
        }return success;
    }
}
