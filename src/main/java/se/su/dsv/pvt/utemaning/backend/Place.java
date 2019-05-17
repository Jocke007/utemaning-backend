package se.su.dsv.pvt.utemaning.backend;

import java.util.ArrayList;

/**
 * Abstract class
 */
public abstract class Place {
    private Location location;
    private String name, uniqueId;
    private int id;
    private ArrayList<Challenge> challengeList = new ArrayList<>();
    private double avrageRating;

    public Place(Location location, String name, int id, String uniqueId, double avrageRating){
        this.location = location;
        this.name = name;
        this.id =  id;
        this.uniqueId = uniqueId;
        this.avrageRating = avrageRating;

    }

    public void addChallange(Challenge newChallenge){
        challengeList.add(newChallenge);
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setChallengeList(ArrayList<Challenge> challengeList) {
        this.challengeList = challengeList;
    }

    public ArrayList<Challenge> getChallengeList() {
        return challengeList;
    }
    public void removeChallenge(Challenge challenge){
        challengeList.remove(challenge);
    }
    public void addChallenge(Challenge c){
        challengeList.add(c);
    }

    public double getAvrageRating(){
        return avrageRating;
    }

    public void setAvrageRating(double avrageRating) {
        this.avrageRating = avrageRating;
    }
}