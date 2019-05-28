package se.su.dsv.pvt.utemaning.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;


@SpringBootApplication
public class BackendApplication {
    DBManagement dbm = new DBManagement();
    String testString = "nothing has happened";


    public static void main(String[] args) {

        SpringApplication.run(BackendApplication.class, args);





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
    public class UtemaningRestController{

        @RequestMapping(value = "/allGyms", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
        public @ResponseBody
        ArrayList<OutdoorGym> getAllGymsmethod() {
            ArrayList<OutdoorGym> allGyms;
            allGyms = dbm.getAllOutdoorGyms();
            return allGyms;
        }

        @RequestMapping(value = "/createChallenge", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
        public String createNewChallengeMethod(@RequestBody Challenge c) {

            if(c == null) {
                return "The entered object is null";
            }
            c.setTimeAndDate();
            dbm.addChallenge(c);


            //Rätt URI?
//            URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(c.getChallengeID()).toUri();

            return "Object received";
        }

        @RequestMapping(value = "/createParticipation/user/{userID}/challenge/{challengeID}", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
        public String createNewChallengeMethod(@PathVariable("userID") int userID, @PathVariable("challengeID")  int challengeID){

            Challenge c = dbm.getSpecificChallenge(challengeID);
            User u = dbm.getOneUserOnId(userID);

            if(c == null)
                return "Challenge does not exist";
            if(u == null)
                return "User does not exist";

            dbm.addParticipation(c, u);
            return "success";
        }

        @RequestMapping(value = "removeChallenge/{id}", method = RequestMethod.PUT, produces = MediaType.TEXT_PLAIN_VALUE)
        public String removeChallengemethod(@PathVariable("id") int challengeID){


            Challenge c = dbm.getSpecificChallenge(challengeID);
            dbm.removeChallenge(c);
            return "Fungerar";
        }

        //changed input to participation ID instead
        @RequestMapping(value = "/removeParticipation/{id}", method = RequestMethod.PUT)
        public String removeParticipationMethod(@PathVariable("id") int participationId){
            //changed from p == null to participationID == 0 as ints cant be null
            if(participationId == 0)
                return "The object is null";

            dbm.removeParticipation(participationId);
            return "Success";
        }

        @RequestMapping (value = "/rateGym/gym/{gymID}/user/{userID}/rate/{rate}", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
        public String createNewRate(@PathVariable("gymID") int workoutSpotID, @PathVariable("userID") int userID, @PathVariable("rate") int rate){
            if(workoutSpotID  ==  0)
                return "object is null";

            if(rate < 1 || rate > 5)
                return "Not a valid rating. Please enter a value between 1 and 5";

            User user = dbm.getOneUserOnId(userID);
            if(user ==  null)
                return "User does not exist";

            dbm.addRating(workoutSpotID,user.getUserName(),rate);
            return "Success";
        }

//        @CrossOrigin
//        @RequestMapping(value = "outdoorgymtest", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
//        public @ResponseBody
//        OutdoorGym response(@RequestParam("gymID") String gymID) {
//
//            int outdoorGymID = Integer.parseInt(gymID);
//            OutdoorGym gym = dbm.getOneOutdoorGym(outdoorGymID);
//            return gym;
//        }

        @RequestMapping(value = "outdoorgym/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
        public @ResponseBody
        OutdoorGym sendOutdoorGym(@PathVariable("id") int id) {

            int outdoorGymID = id;
            OutdoorGym gym = dbm.getOneOutdoorGym(outdoorGymID);
            return gym;
        }

        @RequestMapping(value = "/completeChallenge/{id}", method = RequestMethod.PUT, produces = MediaType.TEXT_PLAIN_VALUE)
        public String  completeChallengeMethod(@PathVariable("id") int  participationId){
            if(participationId  ==  0)
                return "Object is null";
            dbm.completeChallenge(participationId);
            return "Success";
        }

        @RequestMapping(value = "/getParticipation/{userID}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
        public ArrayList<Participation> getParticipation(@PathVariable("userID") int id){
            User u  = dbm.getOneUserOnId(id);
            ArrayList<Participation> participationCollection;
            participationCollection = dbm.getParticipations(u.getUserName(), 0);
            testString = participationCollection.toString();

            return participationCollection;
        }

        @RequestMapping(value = "/createUser/{userName}/{password}", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
        public String createUser(@PathVariable("userName")String userName, @PathVariable("password")String password){
            if(userName == null)
                return "User name not valid";
            if(password == null)
                return "Password not valid";

            dbm.addUser(userName, password);

            User user = dbm.getOneUser(userName);

            return user.getUserID() + "";
        }

        @RequestMapping(value = "/login/{userName}/{password}", method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
        public String login(@PathVariable("userName")String userName, @PathVariable("password")String password){

            User user = dbm.getOneUser(userName);
            if(user == null){
                return"Fail, user does not exist";
            }
            if(!user.getPassword().equals(password))
                return "Password is wrong";

            return user.getUserID() +  "";
        }
        @RequestMapping(value = "/getChallenges/{userID}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
        public ArrayList<Challenge> getChallenges(@PathVariable("userID") int id){
            User u  = dbm.getOneUserOnId(id);
            ArrayList<Challenge> challengeCollection = new ArrayList<>();
            ArrayList<Participation> participationCollection;
            participationCollection = dbm.getParticipations(u.getUserName(), 0);

            for(Participation participation : participationCollection){
                int challengeID = participation.getChallengeID();
                Challenge challenge = dbm.getSpecificChallenge(challengeID);
                challengeCollection.add(challenge);
            }
            testString = challengeCollection.toString();

            return challengeCollection;
        }

    }

    @RestController
    public class tests{

        @PostMapping(path = "/addString", produces = MediaType.TEXT_PLAIN_VALUE)
        public String changeStringMethod(@RequestBody String s){
            if((s == null) || s.equals("")) {
                testString = "you tried but it didn't work";
               return "fungerar ej";

            }

            testString = s;

            return "fungerar!";
        }

        @CrossOrigin
        @RequestMapping(value = "/sayHello", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
        public String sayHellomethod() {
            return "Hello world";
        }

        @RequestMapping(value = "/test")
        public String sayTest() {
            return testString;
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
