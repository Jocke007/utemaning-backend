package se.su.dsv.pvt.utemaning.backend;

import org.junit.Assert;
import org.junit.Test;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class JUnitTest {
    @Test
    public void createUserTest() {
        User newUser = new User("Test", 123, "Test");
        Assert.assertEquals("Test", newUser.getUserName());
        Assert.assertEquals(123, newUser.getUserID());
    }

    @Test
    public void createOutdoorGymTest(){
        Location newLocation = new Location(1000, 2000);
        OutdoorGym testGym = new OutdoorGym(newLocation, "DSVs Utegym", 007, "007", "Här kan du träna allt", 5.0);
        Assert.assertFalse(testGym.getHasChallenge());
        Assert.assertEquals("Här kan du träna allt", testGym.getDescription());

        testGym.setDescription("Gymmet försvann");
        Assert.assertEquals("Gymmet försvann", testGym.getDescription());
    }
// Tillfälligt utkommenterad då jag inte vet ifall den kommer förstöra databasen

    @Test
    public void createAddChallengeToGymTest(){
        Date date = new GregorianCalendar(2014, Calendar.FEBRUARY, 11).getTime();
        Challenge newChallenge = new Challenge("100 Burpees", 50, date, "Do 100 burpees", 007, 007);

        Location newLocation = new Location(1000, 2000);
        OutdoorGym testGym = new OutdoorGym(newLocation, "DSVs Utegym", 007, "007", "Här kan du träna allt", 5.0);

        testGym.addChallange(newChallenge);

        Assert.assertEquals("[Challenge{name='100 Burpees', description='Do 100 burpees'}]", testGym.getChallengeList().toString());
    }

    @Test
    public void addUserToChallenge(){
        Date date = new GregorianCalendar(2014, Calendar.FEBRUARY, 11).getTime();
        Challenge newChallenge = new Challenge("100 Burpees", 50, date, "Do 100 burpees", 007, 007);

        Location newLocation = new Location(1000, 2000);
        OutdoorGym testGym = new OutdoorGym(newLocation, "DSVs Utegym", 007, "007", "Här kan du träna allt", 5.0);

        testGym.addChallange(newChallenge);

        User newUser = new User("Test", 123, "Test");


        Participation p = new Participation(newUser, newChallenge.getChallengeID(), false, 007);


        Assert.assertEquals("Participation{user=User{userName='Test', password='Test', userID=123, challangeParticipationList=[]}, challengeID=7, participationID=7, completed=false}", p.toString());
    }


}
