package se.su.dsv.pvt.utemaning.backend;

public class Participation {
    private User user;
    private int challengeID, participationID;
    private boolean completed;

    public Participation (User user, int challengeID, boolean completed, int participationID){
        this.challengeID=challengeID;
        this.user=user;
        this.completed = completed;
        this.participationID = participationID;
    }

    public int getChallengeID(){
        return challengeID;
    }
    public String getUserName(){
        String userName = user.getUserName();
        return userName;
    }
    public boolean getCompleted(){
        return completed;
    }

    public int getParticipationID() {
        return participationID;
    }

    @Override
    public String toString() {
        return "Participation{" +
                "user=" + user +
                ", challengeID=" + challengeID +
                ", participationID=" + participationID +
                ", completed=" + completed +
                '}';
    }
}
