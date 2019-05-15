package se.su.dsv.pvt.utemaning.backend;

public class Participation {
    private User user;
    private int challengeID;

    public Participation (User user, int challengeID){
        this.challengeID=challengeID;
        this.user=user;
    }

    public int getChallengeID(){
        return challengeID;
    }
    public String getUserName(){
        String userName = user.getUserName();
        return userName;
    }
}
