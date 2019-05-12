package se.su.dsv.pvt.utemaning.backend;


import java.util.ArrayList;

public class User {

    private String userName, password, firstName, lastName, email;
    private int userID;
    //private String password; Modified by Michel
    private ArrayList<Challenge> challangeParticipationList = new ArrayList<>();

    public User (String userName, int userID, String firstName, String lastName, String email){
        this.userName = userName;
        this.userID = userID;
        // this.password = password; modified by Michel
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
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


}