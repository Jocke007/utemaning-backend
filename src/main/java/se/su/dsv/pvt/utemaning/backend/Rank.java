package se.su.dsv.pvt.utemaning.backend;

public class Rank {
    private int rank;
    private OutdoorGym gym;
    private User u;

    public Rank (OutdoorGym gym, User u){
        this.gym = gym;
        this.u = u;
    }
    public User getUser() {
        return u;
    }

    public OutdoorGym getGym(){
        return gym;
    }
    public int getRank(){
        return rank;
    }
}
