package se.su.dsv.pvt.utemaning.backend;
import java.util.*;

/**
 *  fixed this class with proper getters and a few setters, not challengeID or workoutspotID
 *
 */
public class Challenge {
    private String name, description;
    private int numberOfParticipants, challengeID, workoutSpotID;
    private Date eventTimeAndDate;

    public Challenge(String name, int numberOfParticipants, Date eventTimeAndDate, String description,
                     int challengeID, int workoutspotID){
        this.name = name;
        this.numberOfParticipants = numberOfParticipants;
        this.challengeID = challengeID;
        this.workoutSpotID = workoutspotID;
        this.eventTimeAndDate = eventTimeAndDate;
        this.description = description;
    }

    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getDescription(){
        return description;
    }
    public void setDescription(String desc){
        description = desc;
    }

    public int getNumberOfParticipants() {
        return numberOfParticipants;
    }
    public void setNumberOfParticipants(int i){
        numberOfParticipants = i;
    }

    public int getChallengeID(){
        return challengeID;
    }
    public int getWorkoutSpotID(){
        return workoutSpotID;
    }

    public Date getEventTimeAndDate() {
        return eventTimeAndDate;
    }
    public long getTime(){
        return eventTimeAndDate.getTime();
    }
    public void setEventTimeAndDate(Date d){
        eventTimeAndDate = d;
    }

    @Override
    public String toString(){
        return "\nChallenge toString: "+
                "\nName: "+ name +
                "\nDate: "+ eventTimeAndDate;
    }
}