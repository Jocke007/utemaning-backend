package se.su.dsv.pvt.utemaning.backend;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * fixed this class with proper getters and a few setters, not challengeID or workoutspotID
 */
public class Challenge {
    private String name, description, date;
    private int numberOfParticipants, challengeID, workoutSpotID;
    private Date timeAndDate;
    private long time;

    public Challenge(String name, int numberOfParticipants, Date date, String description,
                     int challengeID, int workoutspotID) {
        this.name = name;
        this.numberOfParticipants = numberOfParticipants;
        this.challengeID = challengeID;
        this.workoutSpotID = workoutspotID;
        this.timeAndDate = date;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String desc) {
        description = desc;
    }

    public int getNumberOfParticipants() {
        return numberOfParticipants;
    }

    public void setNumberOfParticipants(int i) {
        numberOfParticipants = i;
    }

    public int getChallengeID() {
        return challengeID;
    }

    public int getWorkoutSpotID() {
        return workoutSpotID;
    }

    public Date getDate() {
        return timeAndDate;
    }

    public long getTime() {
        return timeAndDate.getTime();
    }

    public void setDate(Date d) {
        timeAndDate = d;
    }

    public void setTimeAndDate() {
        try {

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
            timeAndDate = dateFormat.parse(date);
            timeAndDate.setTime(time);
        } catch (Exception e) {

        }
    }



                /*
    @Override
    public String toString(){
        return "\nChallenge toString: "+
                "\nName: "+ name +
                "\nDate: "+ eventTimeAndDate;
    }

 */
}