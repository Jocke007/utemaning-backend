package se.su.dsv.pvt.utemaning.backend;


import java.util.ArrayList;

public class User {

    private String userName, password;
    private int userID;

    private ArrayList<Challenge> challangeParticipationList = new ArrayList<>();

    public User (String userName, int userID, String password){
        this.userName = userName;
        this.userID = userID;
        this.password = password;

    }

    //public void changePassword(String newPassword){ Modified by Michel
    //    this.password = newPassword;
    //}

    public String getUserName() {
        return userName;
    }

    public int getUserID() {
        return userID;
    }
    public String getPassword(){
        return password;
    }

    @Override
    public String toString() {
        return "User{" +
                "userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", userID=" + userID +
                ", challangeParticipationList=" + challangeParticipationList +
                '}';
    }
}