package se.su.dsv.pvt.utemaning.backend;


import com.google.gson.*;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;

/**
 * Fetches JSON-objects from the Stockholm API and parses them by looping through the
 * array of JSON-objects and mapping the keys and values into a new java object.
 *
 * @author Efraim (& Edvin)
 */
public class FetchJSONFromAPI{
    private HashMap<Integer, OutdoorGym> outdoorGymHashMap = new HashMap<>();

    /**
     * Fetches a list of all outdoor gyms in Stockholm and loops through it to create
     * OutdoorGym objects that are stored in the outdoorGymHashmap.
     */
    public void parseFromAllOutdoorGyms(){
        try {
            //URL with a list of all outdoor gyms in Stockholm
            URL url = new URL(
                    "http://api.stockholm.se/ServiceGuideService/ServiceUnitTypes/96a67da3-938b-487e-ac34-49b155cb277b/ServiceUnits/json?apikey=52f545a2957c4615a67ac2025ad9795f");
            InputStreamReader reader = new InputStreamReader(url.openStream());
            JsonParser parser = new JsonParser();
            JsonElement rootElement = parser.parse(reader);
            JsonArray rootAsArray = rootElement.getAsJsonArray();

            for(int i = 0; i < rootAsArray.size(); i++){
                JsonObject position = rootAsArray.get(i).getAsJsonObject().getAsJsonObject("GeographicalPosition");
                String gymName = rootAsArray.get(i).getAsJsonObject().get("Name").getAsString();
                String uniqueId = rootAsArray.get(i).getAsJsonObject().get("Id").getAsString();
                Location l = parseLocation(position);
                String gymDescription = parseDescription(uniqueId);
                parseGym(i, l, gymName, uniqueId, gymDescription);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     Fetches the description for a unique OutdoorGym from the Stockholm API.

     This is how the Json-object from the API can look (they have different amounts of objects in the Attributes array):
     {
     0:
     Attributes[] <-- This is the array we need to look in.
     1: {...}
     2: {...}
     3: {...}
     4: {
     "Description" : null
     "Group": "Beskrivning av enheten" <-- Group value has to be this
     "GroupDescription": null,
     "Id": "ShortDescription",
     "Name": "Introduktion", <-- Name value has to be this
     We want this--> "Value": "Redskap för t.ex. styrke- och balansträning särskilt anpassat för seniorer i Tessinparkens norra del. Underlaget består av stenmjöl."
     }
     5: {...} etc.
     GeographicalAreas[]
     GeographicalPosition{}
     Id:
     Name:
     RelatedServiceUnits[]
     ServiceUnittypes[]
     TimeCreated:
     TimeUpdated:
     }

     * @param uniqueId Key for identifying a unique Place
     * @return description for the OutdoorGym with that uniqueId.
     * @author efraim
     */
    public String parseDescription(String uniqueId){
        String description = null;
        try {
            URL url = new URL("http://api.stockholm.se/ServiceGuideService/DetailedServiceUnits/json?apikey=52f545a2957c4615a67ac2025ad9795f&ids="
                    + uniqueId);
            InputStreamReader reader = new InputStreamReader(url.openStream());
            JsonParser parser = new JsonParser();
            JsonElement rootElement = parser.parse(reader);
            JsonArray rootAsArray = rootElement.getAsJsonArray();
            JsonArray attributesArray = rootAsArray.getAsJsonArray().get(0).getAsJsonObject().get("Attributes").getAsJsonArray();

            for (int i = 0; i < attributesArray.size(); i++){

                String group = attributesArray.get(i).getAsJsonObject().get("Group").getAsString();
                if(!group.equals("Beskrivning av enheten"))
                    continue;

                String name = attributesArray.get(i).getAsJsonObject().get("Name").getAsString();
                if(!name.equals("Introduktion"))
                    continue;

                description = attributesArray.get(i).getAsJsonObject().get("Value").getAsString();
                break;

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return description;
    }

    /**
     * Fetches the values from X & Y in the API and returns a Location object created from
     * those values.
     * @param position x,y
     * @return Location location
     */
    public Location parseLocation(JsonObject position){
        int x = position.get("X").getAsInt();
        int y = position.get("Y").getAsInt();
        Location location = new Location(x,y);
        return location;
    }

    /**
     * Creates a new OutdoorGym from the parameters and stores it in the outdoorGymHashMap
     * @param i index value
     * @param position Location with x,y
     * @param gymName Name
     * @param gymDescription Short description
     */
    public void parseGym(int i, Location position, String gymName, String uniqueId, String gymDescription){
        outdoorGymHashMap.put(i, new OutdoorGym(position,gymName,i, uniqueId, gymDescription));

        //TEST
        System.out.println(
                "\ngymLocation: " + position +
                        "\ngymName: " + gymName +
                        "\ngymId: "+ i +
                        "\nuniqueId: " + uniqueId +
                        "\ngymDescription: " + gymDescription +
                        "\ndetailedAPIPage: http://api.stockholm.se/ServiceGuideService/DetailedServiceUnits/json?apikey=52f545a2957c4615a67ac2025ad9795f&ids="
                        + uniqueId);
    }

    public HashMap<Integer,OutdoorGym> getAllOutdoorGyms(){
        parseFromAllOutdoorGyms();
        return outdoorGymHashMap;
    }

    /**
     * method for loading out database with stockholm api database!
     * DO NOT USE! WILL CREATE DUBBLE IN DATABASE AND THAT MUST BE FIXED FIRST!
     * @param position cordinates
     * @param gymName name
     * @param gymDescription description
     * @param uniqueId stockholm api ID
     */

    private void fillDatabase(Location position, String gymName, String gymDescription, String uniqueId){
        DBManagement dbm = new DBManagement();
        int longitude = position.getX();
        int latitude = position.getY();
        dbm.addOutdoorGym(gymName, gymDescription, longitude, latitude, uniqueId);
    }

}