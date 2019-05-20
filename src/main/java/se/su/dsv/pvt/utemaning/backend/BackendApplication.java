package se.su.dsv.pvt.utemaning.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;



@SpringBootApplication
public class BackendApplication {
    DBManagement dbm = new DBManagement();


    public static void main(String[] args) {

        SpringApplication.run(BackendApplication.class, args);

/*

        DBManagement dbm = new DBManagement();
        dbm.alterRank(60, "nillls", 5);



 */
/*
  Date date = new Date();
        boolean success = dbm.addChallenge("testing if boolean will flip","fitnessULTIMATE!",date, 61);
        if (success){
            System.out.println("yey");
        }

 */
/*


        ArrayList<OutdoorGym> list = dbm.getAllOutdoorGyms();
        for(OutdoorGym challenge:list ){
            System.out.println(challenge.getName());
        }
 User user = dbm.getOneUser("nils");
        System.out.println(user.getUserID());


 */




/*
        ArrayList<Challenge> list = dbm.getAllChallenge();

        if(list.isEmpty()) {
            System.out.println("tom");
        }
         for (Challenge participation: list ){

            System.out.println(participation.getDescription());
        }


 */


    }

    //TEST FetchJSONFromAPI (Print all gyms)
    //new FetchJSONFromAPI().parseFromAllOutdoorGyms();


    @RestController
    public class getAllGyms {
        @RequestMapping(value = "/allGyms", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
        public @ResponseBody
        ArrayList<OutdoorGym> getAllGymsmethod() {
            ArrayList<OutdoorGym> allGyms;
            allGyms = dbm.getAllOutdoorGyms();
            return allGyms;
        }
    }

    public class createNewChallenge {
        //Behövs det ResponseEntity<Challenge> här?
        @RequestMapping(value = "/createChallenge", method = RequestMethod.POST)
        public void createNewChallengeMethod(@RequestBody Challenge c) {
            OutdoorGym gym = dbm.getOneOutdoorGym(c.getWorkoutSpotID());
            dbm.addChallenge(c);
        }
    }

    public class createParticipation{
        @RequestMapping(value = "/createParticipation", method = RequestMethod.POST)
        public void createNewChallengeMethod(@RequestBody Challenge c, User u){
            dbm.addParticipation(c, u);
        }
    }

    public class removeChallenge {
        @RequestMapping(value = "/removeChallenge/{id}", method = RequestMethod.PUT)
        public Challenge removeChallengemethod(@PathVariable("id") Challenge c){
            dbm.removeChallenge(c);
            return c;
        }
    }

    public class removeParticipation{
        @RequestMapping(value = "/removeParticipation/{id}", method = RequestMethod.PUT)
        public Participation removeParticipationMethod(@PathVariable("id") Participation p){
            dbm.removeParticipation(p);
            return p;
        }
    }

    @RestController
    public class rankGym{
        @RequestMapping (value = "/rankGym", method = RequestMethod.POST)
        public void createNewRank(@RequestBody Rating r){
            dbm.addRank(r);
        }
    }







    //Den här får inget error men den verkar inte skicka tillbaka rätt informamtion. Vi borde testa att den
    //faktiskt hämtar korrekt data från databasen
    @RestController
    public class HelloJSONRestController2 {
        @CrossOrigin
        @RequestMapping(value = "outdoorgymtest", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
        public @ResponseBody
        OutdoorGym response(@RequestParam("gymID") String gymID) {

            int outdoorGymID = Integer.parseInt(gymID);
            OutdoorGym gym = dbm.getOneOutdoorGym(outdoorGymID);
            return gym;
        }
    }

    @RestController
    public class completeChallenge{
        @RequestMapping(value = "/completeChallenge/{id}", method = RequestMethod.PUT)
        public Participation  tcompleteChallengeMethod(@RequestParam Participation  p){
            dbm.completeChallenge(p);
            return p;
        }
    }

    @RestController
    public class sayHello {
        @CrossOrigin
        @RequestMapping(value = "/sayHello", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
        public String sayHellomethod() {
            return "Hello world";
        }
    }



        /*
        long miliseconds = System.currentTimeMillis();
        Time time = new java.sql.Time(miliseconds);
        java.sql.Date date = new java.sql.Date(Calendar.getInstance().getTimeInMillis());
        DBManagement dbm = new DBManagement();
        boolean success = dbm.addChallenge("FITNESSSS", "grimsta!", time, date, 1);
        if (success) {
            System.out.println("challenge has been added");
        }
        if (!success) {
            System.out.println(dbm.getErrorMessage());


                    boolean success = dbm.addOutdoorGym("testGym", "fult gym!",
                314421 ,4214421  );
        if (success) {
            System.out.println("outdoorGym has been added");
        }
        if (!success) {
            System.out.println(dbm.getErrorMessage());
            */
}

//spring demo code. delete after demo
        /*
        DBManagement dbm = new DBManagement();
        String message = dbm.sprintDemo();
        System.out.println(message);
*/

//this is code to test adding data to database, only to test, delete once done
        /*
DBManagement dbm = new DBManagement();
String userName = ("CarlaTheChallenger");
String firstName = ("Carla");
String lastName =("Espinosa");
String email = ("carlachallengercom");
boolean howDidItGo = dbm.addUser(userName,firstName,lastName,email);
if(howDidItGo){
    System.out.println(true);
}else System.out.println(false);
*/

//TEST the Challenge class
//Challenge testChallenge = new Challenge("testChallenge");
//System.out.println(testChallenge.toString());
//System.out.println();

//        TEST the FetchJSONfromAPI class
//        FetchJSONFromAPI fetch = new FetchJSONFromAPI("http://api.stockholm.se/ServiceGuideService/ServiceUnitTypes/96a67da3-938b-487e-ac34-49b155cb277b/ServiceUnits/json?apikey=52f545a2957c4615a67ac2025ad9795f");
//        Location location = new Location(fetch.getX(), fetch.getY());
//        OutdoorGym testAPIOutdoorGym = new OutdoorGym(location,fetch.getName(),false);
//        System.out.println(testAPIOutdoorGym.toString());
