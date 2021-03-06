package se.su.dsv.pvt.utemaning.backend;

import javax.sql.rowset.CachedRowSet;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;

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
 * modifyChallengeExpired       TESTED
 * getDateToCompare             TESTED
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

        String sqlQuery = ("SELECT * FROM `Challenge` WHERE Expired = '" + false + "' ");
        ArrayList<Challenge> challengeCollection = new ArrayList<>();
        java.util.Date date = getDateToCompare();
        try {
            CachedRowSet crs = ctpdb.getData(sqlQuery);
            while (crs.next()) {
                Challenge challenge = challengeBuilder(crs);
                int i = date.compareTo(challenge.getDate());
                if (i == -1) {
                    challengeCollection.add(challenge);
                } else {
                    boolean success = modifyChallengeExpired(challenge);
                }

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
     * repurposed this method. it will take an outdoorgym and get all challanges at that outdoorgym and add them to
     * that outdoorgyms list and then return the outdoorgym.
     *
     * @param outdoorGym the object whos challanges to retrive
     * @return the same object but with all challanges related to it
     * <p>
     * TESTED 15/5 AND WORKS AS INTENDED
     */
    private OutdoorGym getAllChallengeAtSpot(OutdoorGym outdoorGym) {
        int workoutspotID = outdoorGym.getId();
        String sqlQuery = ("SELECT * FROM `Challenge` WHERE WorkoutSpotId = '" + workoutspotID + "' " +
                "AND  Expired = '" + 0 + "' ");
        java.util.Date date = getDateToCompare();
        try {
            CachedRowSet crs = ctpdb.getData(sqlQuery);
            while (crs.next()) {
                Challenge challenge = challengeBuilder(crs);
                int i = date.compareTo(challenge.getDate());
                if (i == -1) {
                    outdoorGym.addChallange(challenge);
                } else {
                    boolean success = modifyChallengeExpired(challenge);
                }
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
            String gymDesctiption = crs.getString("outdoorGymDesc");
            String uniqueId = crs.getString("StockholmStadAPIKey");
            Location location = new Location(longitude, latitude);
            double rating = crs.getDouble("Rating");
            double r = Math.round(rating * 100.0) / 100.0;
            outdoorGym = new OutdoorGym(location, gymName, workoutSpotId, uniqueId, gymDesctiption, r);
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
     * method for retriving one user on a userID from the database
     * @param userId the is of the user
     * @return user object
     */
    public User getOneUserOnId(int userId){
        String sqlQuery = ("SELECT * FROM User WHERE UserID = '"+ userId + "' ");
        CachedRowSet crs = ctpdb.getData(sqlQuery);
        User user = null;
        try{
            while(crs.next()){
                user = userBuilder(crs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
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
            String password = crs.getString("Password");
            user = new User(userName, userId, password);
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
            int participationID = crs.getInt("ParticipationId");
            User user = getOneUser(userName);
            participation = new Participation(user, challengeID, completed, participationID);
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
     * @param password user password
     * @return boolean true or false depending on result
     * <p>
     * TESTED 15/5 AND WOKRS AS INTENDED
     */
    public boolean addUser(String userName, String password) {
        String sqlQuery = ("INSERT INTO User SET UserName = '" + userName + "' , Password = '"+ password + "' ");
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

    public int addChallenge(Challenge challenge) {
        int workoutSpotID = challenge.getWorkoutSpotID();
        String name = challenge.getName();
        java.util.Date date2 = challenge.getDate();
        date2.setTime(date2.getTime()+7200000);
        String desc = challenge.getDescription();
        String sqlQuery = ("INSERT INTO Challenge SET WorkoutSpotid = '" + workoutSpotID
                + "' , ChallengeName = '" + name + "' , Date = '" + date2.toInstant() +
                "' , Description = '" + desc + "' , Participants = '0' , Expired = '" + 0 + "' ");
        int challengeID = ctpdb.addAndReturnIncrementValueChallenge(sqlQuery);
        if (challengeID == 0) {
            errorMessage = ctpdb.getErrorMessage();
            return 0;
        }
        return challengeID;
    }

    /**
     * Method for adding a new participation to the database
     * will also get the number of participants from the challenge that the person just joined and incresse that number with one
     * <p>
     * WARNING! if it fails on nested method that also use sql the first will still have been added. should be removed if fail.
     * TODO make it commit all or nothing on a failed query.
     *
     * @param challenge the challenge to join
     * @param user      the user to join the challenge
     * @return will return true if all went well, otherwise get the error message and store it and return false
     * <p>
     * tested 16/5 and works
     */
    public boolean addParticipation(Challenge challenge, User user) {
        int challengeID = challenge.getChallengeID();
        String userName = user.getUserName();
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
        return success;
    }

    /**
     * method for rating a gym
     * @param workoutSpotID the gym
     * @param userName the user
     * @param rating the rating
     * @return true or false
     */
    public boolean addRating(int workoutSpotID, String userName, int rating) {

        String sqlQuery = ("INSERT INTO WorkoutSpotRanks SET WorkoutSpotId = '" + workoutSpotID + "' " +
                ", UserName = '" + userName + "' , Rating = '" + rating + "' ");
        boolean success = ctpdb.insertData(sqlQuery);
        if (!success) {
            errorMessage = getErrorMessage();
            return false;
        }
        success = setAvrageRank(workoutSpotID);
        if (!success) {
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
     * @param c Challenge to be deleted.
     * @return boolean true or false
     * <p>
     * tested 15/5 and works
     */
    public boolean removeChallenge(Challenge c) {
        int challengeId = c.getChallengeID();
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
     * @param participationId the unique ID number of the participation
     * @return boolean true or false
     * tested 16/5 and works
     */
    public boolean removeParticipation(int participationId) {
        int challengeID = 0;
        //String userName = p.getUserName();
        String sqlQueryForChallenge = ("SELECT * FROM Participation WHERE ParticipationId = '" + participationId + "' ");

        CachedRowSet crs = ctpdb.getData(sqlQueryForChallenge);
        try{
            while(crs.next()){
                challengeID = crs.getInt("ChallengeID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String sqlQuery = ("DELETE FROM  Participation WHERE Participation.ParticipationId = '" + participationId + "' ");
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
     * @param participationId the Id of the participation on the a challenge to be completed
     * @return returns true if ok false if not ok
     * <p>
     * tested 15/5 and works
     */
    public boolean completeChallenge(int participationId) {
        String sqlQuery = ("UPDATE Participation SET Completed = '" + 1 + "' " +
                "WHERE ParticipationId = '" + participationId + "' ");
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
     *
     * @param workoutSpotId the outdoorgym identifier
     * @param userName      the user identifier
     * @param rating          the new rank
     * @return returns true and false depending on success
     * <p>
     * tested 16/5 and works as intended
     */
    public boolean alterRating(int workoutSpotId, String userName, int rating) {
        String sqlQuery = ("UPDATE WorkoutSpotRanks SET Rating = '" + rating + "' WHERE WorkoutSpotId = '" + workoutSpotId + "' " +
                "AND userName = '" + userName + "' ");
        boolean success = ctpdb.insertData(sqlQuery);
        if (!success) {
            errorMessage = getErrorMessage();
            return false;
        }
        success = setAvrageRank(workoutSpotId);
        if (!success) {
            errorMessage = ctpdb.getErrorMessage();
            return false;
        }
        return success;
    }

    /**
     * a method for adjusting the avrage rank of the gym, will trigger when a new rank is either added or altered.
     *
     * @param workoutSpotID the gym that needs its avrage rank adjusted
     * @return true or false depending on success
     * <p>
     * tested 16/5 and works as intended
     */
    private boolean setAvrageRank(int workoutSpotID) {
        String sqlQuery = ("SELECT * FROM WorkoutSpotRanks WHERE WorkoutSpotId = '" + workoutSpotID + "' ");
        CachedRowSet crs = ctpdb.getData(sqlQuery);
        int count = 0;
        double result = 0, avrageRank = 0, i = 0;
        try {
            while (crs.next()) {
                i = crs.getDouble("Rating");
                count++;
                avrageRank += i;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            errorMessage = ctpdb.getErrorMessage();
        }
        result = avrageRank / count;
        sqlQuery = ("UPDATE Workoutspot SET Rating = '" + result + "' WHERE workoutSpotId = " +
                " '" + workoutSpotID + "'");
        boolean success = ctpdb.insertData(sqlQuery);
        if (!success) {
            errorMessage = ctpdb.getErrorMessage();
            return false;
        }
        return success;
    }

    /**
     * changes the boolean in expired to show if the challenge has expired or not
     *
     * @param c challene object to modify
     * @return true or false
     */
    private boolean modifyChallengeExpired(Challenge c) {
        int challengeID = c.getChallengeID();
        String sqlQuery = ("UPDATE Challenge SET Expired = '" + 1 + "' WHERE ChallengeID = '" + challengeID + "' ");
        boolean success = ctpdb.insertData(sqlQuery);
        if (!success) {
            errorMessage = ctpdb.getErrorMessage();
            return false;
        }
        return success;
    }

    /**
     * creates an instance of a current system time and date, add 2 hours to that date so we can ignore challenges that
     * expired more then 2 hours ago.
     *
     * @return returns the date to compare with
     */
    private java.util.Date getDateToCompare() {
        java.util.Date date = new java.util.Date(Calendar.getInstance().getTimeInMillis());
        long time = date.getTime();
        long newTime = time - 7200000;
        date.setTime(newTime);
        return date;
    }
}
