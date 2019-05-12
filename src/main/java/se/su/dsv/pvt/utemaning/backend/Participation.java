package se.su.dsv.pvt.utemaning.backend;

public class Participation {
    private String userName;
    private int challengeID;

    public Participation (String userName, int challengeID){
        this.challengeID=challengeID;
        this.userName=userName;
    }

    public int getChallengeID(){
        return challengeID;
    }
    public String getUserName(){
        return userName;
    }
}
