package se.su.dsv.pvt.utemaning.backend;

import java.util.ArrayList;

/**
 * Abstract class
 */
public abstract class Place {
    Location location;
    String name, uniqueId;
    int id;
    ArrayList<Challenge> challengeList = new ArrayList<>();

    public Place(Location location, String name, int id, String uniqueId){
        this.location = location;
        this.name = name;
        this.id =  id;
        this.uniqueId = uniqueId;
    }

    public void addChallange(Challenge newChallenge){
        challengeList.add(newChallenge);
    }

    public ArrayList<Challenge> getChallengeList() {
        return challengeList;
    }
    public void removeChallenge(Challenge challenge){
        challengeList.remove(challenge);
    }
}