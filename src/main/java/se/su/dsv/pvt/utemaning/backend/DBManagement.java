package se.su.dsv.pvt.utemaning.backend;

import javax.sql.rowset.CachedRowSet;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * this is a class to handle all database management. instead of having to know every class that does one thing to the database
 * we got 1 class that has metods that does those things. one object to use methods on instead of 20 different classes to handle.
 * <p>
 * The SQL code written in this class is not very safe and is vulnerable to SQLinjection, we do however not have the time to make
 * it safe.
 * <p>
 * Comments are above the code it refers to
 * Comments are above the code it refers to
 * <p>
 * METHODS SO FAR
 * getAllChallenge              TESTED
 * getSpecificChallenge         TESTED
 * getAllChallengeAtSpot        TESTED
 * challengeBuilder             TESTED
 * getAllOutdoorGyms            TESTED
 * getOneOutdoorGym             TESTED
 * outdoorGymBuilder            TESTED
 * getAllUsers                  TESTED
 * getOneUser                   TESTED
 * userBuilder                  TESTED
 * getParticipations            TESTED
 * participationBuilder         TESTED
 * addNewUser                   TESTED
 * addNewOutdoorGym             TESTED
 * addNewChallenge
 * addNewParticipation          TESTED
 * addRank                      TESTED
 * removeChallenge              TESTED
 * removeParticipation          TESTED
 * completeChallenge            TESTED
 * alterRank                    TESTED
 * setAvrageRank                TESTED
 *
 * <p>
 * TO FIX
 *
 * <p>
 * TO DO
 * alter outdoorgym table
 * alter workspot table
 *
 * @author Michel
 */


public class DBManagement {
    private String errorMessage;
    private ConnectionToPasiDB ctpdb = new ConnectionToPasiDB();


    /**
     * a method to retrive all challenges from the database and return those challenges in the form of an ArrayList
     * will return an empthy Arraylist if no challenges exsists
     * will use challengeBuilder method to build the objects
     *
     * @return an arraylist
     * <p>
     * TESTED 15/5 AND WORKS AS INTENDED.
     */
    public ArrayList getAllChallenge() {

        String sqlQuery = ("SELECT * FROM `Challenge`");
        ArrayList<Challenge> challengeCollection = new ArrayList<>();
        try {
            CachedRowSet crs = ctpdb.getData(sqlQuery);
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
     * a method to collect a single challenge from the database based on the challenge id number.
     * will return null if challenge does not exsists
     * will use challengeBuilder method to create the challenge
     *
     * @param challengeIDInput the ID of the challenge to be retrived.
     * @return returns a challenge object if found in database otherwise an empty object
     * <p>
     * TESTED 15/5 AND WORKS AS INTENDED
     */
    public Challenge getSpecificChallenge(int challengeIDInput) {
        String sqlQuery = ("SELECT * FROM `Challenge` WHERE ChallengeID = '" + challengeIDInput + "' ");
        Challenge challenge = null;
        try {
            CachedRowSet crs = ctpdb.getData(sqlQuery);
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
     * <p>
     * TESTED 15/5 AND WORKS AS INTENDED
     */
    private OutdoorGym getAllChallengeAtSpot(OutdoorGym outdoorGym) {
        int workoutspotID = outdoorGym.getId();
        String sqlQuery = ("SELECT * FROM `Challenge` WHERE WorkoutSpotId = '" + workoutspotID + "' ");

        try {
            CachedRowSet crs = ctpdb.getData(sqlQuery);
            while (crs.next()) {
                Challenge challenge = challengeBuilder(crs);
                outdoorGym.challengeList.add(challenge);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return outdoorGym;
    }

    /**
     * Support method for building challenge objects.
     *
     * @param crs a row from the table.
     * @return challenge object
     * <p>
     * TESTED 15/5 AND WORKS AS INTENDED
     */
    private Challenge challengeBuilder(CachedRowSet crs) {
        Challenge challenge = null;
        try {
            String challengeName = crs.getString("ChallengeName");
            String challengeDesc = crs.getString("Description");
            int workoutSpotID = crs.getInt("WorkoutSpotID");
            int challengeID = crs.getInt("ChallengeID");
            int numberOfParticipants = crs.getInt("Participants");
            java.util.Date date = new Date(crs.getDate("Date").getTime());
            boolean expired = crs.getBoolean("Expired");
            challenge = new Challenge(challengeName, numberOfParticipants, date, challengeDesc, challengeID, workoutSpotID);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return challenge;
    }

    /**
     * a method to collect all outdoorGyms from the database and returns those objects as an ArrayList
     * will return empthy list if no outdoorgyms exsists
     *
     * @return arraylist of outdoorgyms
     * <p>
     * TESTED 15/5 AND WORKS AS INTENDED
     */
    public ArrayList getAllOutdoorGyms() {
        ArrayList<OutdoorGym> outdoorGymCollection = new ArrayList<>();
        String sqlQuery = ("SELECT * FROM OutdoorGym, Workoutspot, Location WHERE " +
                "Workoutspot.WorkoutSpotId = OutdoorGym.WorkoutSpotId AND " +
                "Workoutspot.WorkoutSpotId = Location.WorkoutSpotID");
        try {
            CachedRowSet crs = ctpdb.getData(sqlQuery);
            while (crs.next()) {
                OutdoorGym outdoorGym = buildOfOutdoorGym(crs);
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
     * <p>
     * TESTED 15/5 AND WORKS AS INTENDED
     */
    public OutdoorGym getOneOutdoorGym(int workoutSpotIdInput) {
        String sqlQuery = ("SELECT * FROM OutdoorGym, Workoutspot, Location " +
                "WHERE Workoutspot.WorkoutSpotId = OutdoorGym.WorkoutSpotId AND " +
                "Workoutspot.WorkoutSpotId = Location.WorkoutSpotID AND " +
                "OutdoorGym.WorkoutSpotId = ' " + workoutSpotIdInput + " ' ");
        OutdoorGym outdoorGym = null;
        try {
            CachedRowSet crs = ctpdb.getData(sqlQuery);
            while (crs.next()) {
                outdoorGym = buildOfOutdoorGym(crs);
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
     *
     * @return outdoorgym
     * <p>
     * TESTED 15/5 AND WOKRS AS INTENDED
     */
    private OutdoorGym buildOfOutdoorGym(CachedRowSet crs) {
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
            double rank = crs.getDouble("Rank");
            outdoorGym = new OutdoorGym(location, gymName, workoutSpotId, uniqueId, gymDesctiption);
            outdoorGym = getAllChallengeAtSpot(outdoorGym);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return outdoorGym;
    }

    /**
     * method to get all users from database and create user objects, and returns those objects in an arraylist
     * will return empthy arraylist if no users in database
     *
     * @return returns an Arraylist of all users.
     * <p>
     * TESTED 15/5 AND WORKS AS INTENDED
     */
    public ArrayList getAllUsers() {

        String sqlQuery = ("SELECT * FROM `User`");
        ArrayList<User> userCollection = new ArrayList<>();
        userCollection.clear();
        try {
            CachedRowSet crs = ctpdb.getData(sqlQuery);
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
     * <p>
     * TESTED 15/5 AND WOKRS AS INTENDED
     */
    public User getOneUser(String userNameInput) {
        User user = null;
        String sqlQuery = ("SELECT * FROM User WHERE `UserName` = '" + userNameInput + "' ");
        CachedRowSet crs = ctpdb.getData(sqlQuery);
        try {
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
     * <p>
     * TESTED 15/5 AND WOKRS AS INTENDED
     */
    public User userBuilder(CachedRowSet crs) {
        User user = null;
        try {
            int userId = crs.getInt("UserID");
            String userName = crs.getString("UserName");
            String firstName = crs.getString("FirstName");
            String lastName = crs.getString("LastName");
            String email = crs.getString("EMail");
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
     * <p>
     * TESTED 15/5 AND WOKRS AS INTENDED
     */
    public ArrayList getParticipations(String userName, int challengeID) {
        Participation participation = null;
        String sqlQuery = null;
        ArrayList<Participation> participationCollection = new ArrayList<>();
        if (challengeID != 0 && userName.equals(null)) {
            sqlQuery = ("SELECT * FROM Participation WHERE ChallengeID = '" + challengeID + "' ");
        } else if (challengeID == 0 && !userName.equals(null)) {
            sqlQuery = ("SELECT * FROM Participation WHERE UserName = '" + userName + "' ");
        } else {
            sqlQuery = ("SELECT * FROM Participation WHERE UserName = '" + userName + "' AND ChallengeID = '" + challengeID + "' ");
        }
        CachedRowSet crs = ctpdb.getData(sqlQuery);
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

    /**
     * method for building participation objects, will support the other methods that need it
     *
     * @param crs a table with the information needed
     * @return a participation object
     * <p>
     * TESTED 15/5 AND WOKRS AS INTENDED
     */
    private Participation participationBuilder(CachedRowSet crs) {
        Participation participation = null;
        try {
            String userName = crs.getString("UserName");
            int challengeID = crs.getInt("ChallengeID");
            boolean completed = crs.getBoolean("Completed");
            User user = getOneUser(userName);
            participation = new Participation(user, challengeID);
        } catch (SQLException e) {
            e.printStackTrace();
            errorMessage = e.getMessage();
        }
        return participation;
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
     * <p>
     * TESTED 15/5 AND WOKRS AS INTENDED
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
     * <p>
     * tested 15/5 and works as intended
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
     * @param challenge object.
     * @return boolean true of false if successfly stored in database
     * <p>
     * tested 15/5 and works as intended
     */

    public boolean addChallenge(Challenge challenge) {
        int workoutSpotID = challenge.getWorkoutSpotID();
        String name = challenge.getName();
        java.util.Date date = challenge.getEventTimeAndDate();
        String desc = challenge.getDescription();
        String sqlQuery = ("INSERT INTO Challenge SET WorkoutSpotid = '" + workoutSpotID
                + "' , ChallengeName = '" + name + "' , Date = '" + date +
                "' , Description = '" + desc + "' , Participants = '0' , Expired = '" + 0 + "' ");
        boolean success = ctpdb.insertData(sqlQuery);
        if (!success) {
            errorMessage = ctpdb.getErrorMessage();
            return false;
        }
        return success;
    }

    /**
     * Method for adding a new participation to the database
     * will also get the number of participants from the challenge that the person just joined and incresse that number with one
     * <p>
     * WARNING! if it fails on nested method that also use sql the first will still have been added. should be removed if fail.
     * TODO make it commit all or nothing on a failed query.
     *
     * @param challengeID the primarykey of the challenge, and the identifier of the challenge
     * @param userName    a unique identifier for a user
     * @return will return true if all went well, otherwise get the error message and store it and return false
     * <p>
     * tested 16/5 and works
     */
    public boolean addParticipation(int challengeID, String userName) {
        String sqlQuery = ("INSERT INTO Participation SET ChallengeID ='" + challengeID + "' , UserName = '" + userName + "'" +
                " , Completed = '" + 0 + "' ");
        boolean success = ctpdb.insertData(sqlQuery);
        if (success) {
            Challenge c = getSpecificChallenge(challengeID);
            int i = c.getNumberOfParticipants();
            i++;
            sqlQuery = ("UPDATE Challenge SET Participants = '" + i + "' WHERE ChallengeID = '" + challengeID + "' ");
            success = ctpdb.insertData(sqlQuery);
        }
        if (!success) {
            errorMessage = ctpdb.getErrorMessage();
            return false;
        }
        return true;
    }

    /**
     * method for adding a rank to an outdoorgym.
     * will also trigger the change of the avrage rank value for that gym
     *
     * @param workoutSpotID identifier of the gym
     * @param userName      identifier of the person who ranks it
     * @param rank          rank value in the form of an int
     * @return true or false depending on success.
     *
     * tested 16/5 and works as intended
     */
    public boolean addRank(int workoutSpotID, String userName, int rank) {
        String sqlQuery = ("INSERT INTO WorkoutSpotRanks SET WorkoutSpotId = '" + workoutSpotID + "' " +
                ", UserName = '" + userName + "' , Rank = '" + rank + "' ");
        boolean success = ctpdb.insertData(sqlQuery);
        if (!success) {
            errorMessage = getErrorMessage();
            return false;
        }
        success = setAvrageRank(workoutSpotID);
        if(!success){
            errorMessage = ctpdb.getErrorMessage();
            return false;
        }
        return success;
    }


    /**
     * a method for forwarding the error message so it can be used if needed
     *
     * @return the error message if SQL exception has been thrown when inserting to database
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * method for removing a challenge from the database. will return true if all went well false is it did not.
     * will also retrive error message if something went wrong.
     *
     * @param challengeId the ID number of the challenge to be deleted
     * @return boolean true or false
     * <p>
     * tested 15/5 and works
     */
    public boolean removeChallenge(int challengeId) {
        String sqlQuery = ("DELETE FROM `Challenge` WHERE Challenge.ChallengeID = '" + challengeId + "' ");
        boolean success = ctpdb.insertData(sqlQuery);
        if (!success) {
            errorMessage = ctpdb.getErrorMessage();
            return false;
        }
        return success;
    }

    /**
     * method for removing a paticipation from the database, takes a challenge id and a username, returns true if deleted, false
     * if not. will also retrive error message and store in errormessage
     *
     * @param challengeID the identifier of the challenge
     * @param userName    the identifier of the user
     * @return boolean true or false
     * tested 16/5 and works
     */
    public boolean removeParticipation(int challengeID, String userName) {
        String sqlQuery = ("DELETE FROM  Participation WHERE Participation.ChallengeID = '" + challengeID + "' AND " +
                "Participation.UserName = '" + userName + "' ");
        boolean success = ctpdb.insertData(sqlQuery);
        if (success) {
            Challenge c = getSpecificChallenge(challengeID);
            int i = c.getNumberOfParticipants();
            i--;
            sqlQuery = ("UPDATE Challenge SET Participants = '" + i + "' WHERE ChallengeID = '" + challengeID + "' ");
            success = ctpdb.insertData(sqlQuery);
        }
        if (!success) {
            errorMessage = ctpdb.getErrorMessage();
            return false;
        }
        return success;
    }

    /**
     * method for completing a challenge, it will alter the participation table and flip the boolean completed to 1.
     *
     * @param userName    the user
     * @param challengeID the challenge
     * @return returns true if ok false if not ok
     * <p>
     * tested 15/5 and works
     */
    public boolean completeChallenge(String userName, int challengeID) {
        String sqlQuery = ("UPDATE Participation SET Completed = '" + 1 + "' " +
                "WHERE ChallengeID = '" + challengeID + "' AND UserName = '" + userName + "' ");
        boolean success = ctpdb.insertData(sqlQuery);
        if (!success) {
            errorMessage = ctpdb.getErrorMessage();
            return false;
        }
        return success;
    }

    /**
     * a method for changing the rank if the user has already set it once.
     * will also trigger method for adjusting avrage rank
     * @param workoutSpotId the outdoorgym identifier
     * @param userName the user identifier
     * @param rank the new rank
     * @return returns true and false depending on success
     *
     * tested 16/5 and works as intended
     */
    public boolean alterRank(int workoutSpotId, String userName, int rank) {
        String sqlQuery = ("UPDATE WorkoutSpotRanks SET Rank = '" + rank + "' WHERE WorkoutSpotId = '"+workoutSpotId+"' " +
                "AND userName = '"+userName+"' ");
        boolean success = ctpdb.insertData(sqlQuery);
        if (!success) {
            errorMessage = getErrorMessage();
            return false;
        }
        success = setAvrageRank(workoutSpotId);
        if(!success){
            errorMessage = ctpdb.getErrorMessage();
            return false;
        }
        return success;
    }

    /**
     * a method for adjusting the avrage rank of the gym, will trigger when a new rank is either added or altered.
     * @param workoutSpotID the gym that needs its avrage rank adjusted
     * @return true or false depending on success
     *
     * tested 16/5 and works as intended
     */
    private boolean setAvrageRank(int workoutSpotID) {
        String sqlQuery = ("SELECT * FROM WorkoutSpotRanks WHERE WorkoutSpotId = '" + workoutSpotID + "' ");
        CachedRowSet crs = ctpdb.getData(sqlQuery);
        int  count = 0;
        double result = 0,avrageRank = 0, i = 0;
        try {
            while (crs.next()) {
                i = crs.getDouble("Rank");
                count++;
                avrageRank += i;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            errorMessage = ctpdb.getErrorMessage();
        }
        result = avrageRank/count;
        sqlQuery = ("UPDATE Workoutspot SET Rank = '"+result+"' WHERE workoutSpotId = " +
               " '"+workoutSpotID+"'" );
        boolean success = ctpdb.insertData(sqlQuery);
        if(!success){
            errorMessage = ctpdb.getErrorMessage();
            return false;
        }return success;
    }
}
