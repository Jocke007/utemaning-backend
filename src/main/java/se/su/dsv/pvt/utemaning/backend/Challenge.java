package se.su.dsv.pvt.utemaning.backend;
import java.util.*;

/**
 * The Challenge that a user can create
 */
public class Challenge {
    private String name;
    private int numberOfParticipants, challengeID, workoutSpotID;
    //private String level; //TODO bestämma ifall det här ska vara en String eller kanske en egen klass? //Modified by michel
    //private String workoutType; //Modified by michel
    private Date eventTimeAndDate = new Date(); //Valde att enbart göra en instans av datum och inte en separat för tid eftersom att tid finns inbyggt i datum-klassen
    private String description; //TODO fixa den här
    //private ArrayList<User> participantList = new ArrayList<>(); //modified by Michel

    public Challenge(String name, int numberOfParticipants, Date eventTimeAndDate, String description,
                     int challengeID, int workoutspotID){
        this.name = name;
        this.numberOfParticipants = numberOfParticipants;
        this.challengeID = challengeID;
        this.workoutSpotID = workoutspotID;
        //this.level = level; //modified by Michel
        //this.workoutType = workoutType; //Modified by michel
        this.eventTimeAndDate = eventTimeAndDate;
        this.description = description;
    }

    public int getChallengeID(){
        return challengeID;
    }
    @Override
    public String toString(){
        return "\nChallenge toString: "+
                "\nName: "+ name +
                "\nDate: "+ eventTimeAndDate;
    }
}