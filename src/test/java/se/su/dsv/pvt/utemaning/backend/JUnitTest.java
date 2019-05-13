package se.su.dsv.pvt.utemaning.backend;

import org.junit.Assert;
import org.junit.Test;

public class JUnitTest {
    @Test
    public void createUserTest() {
        User newUser = new User("Test", 123, "Test", "Testsson", "testaren@haringen.stansattgå");
        Assert.assertEquals("Test", newUser.getUserName());
        Assert.assertEquals(123, newUser.getUserID());
    }

    @Test
    public void createOutdoorGymTest(){
        Location newLocation = new Location(1000, 2000);
        OutdoorGym testGym = new OutdoorGym(newLocation, "DSVs Utegym", 007, "007", "Här kan du träna allt");
        Assert.assertFalse(testGym.getHasChallenge());
        Assert.assertEquals("Här kan du träna allt", testGym.getDescription());

        testGym.setDescription("Gymmet försvann");
        Assert.assertEquals("Gymmet försvann", testGym.getDescription());
    }
// Tillfälligt utkommenterad då jag inte vet ifall den kommer förstöra databasen
//
//    @Test
//    public void createAddChallengeToGymTest(){
//        Date date = new GregorianCalendar(2014, Calendar.FEBRUARY, 11).getTime();
//        Challenge newChallenge = new Challenge("100 Burpees", 50, date, "Do 100 burpees", 007, 007);
//
//        Location newLocation = new Location(1000, 2000);
//        OutdoorGym testGym = new OutdoorGym(newLocation, "DSVs Utegym", 007, "007", "Här kan du träna allt");
//
//        testGym.addChallange(newChallenge);
//    }
}
