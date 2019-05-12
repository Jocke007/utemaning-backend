package se.su.dsv.pvt.utemaning.backend;


/**
 * Inherits from Place
 */
public class OutdoorGym extends Place{
    private String description;

    public OutdoorGym(Location location, String name, int id,String uniqueId, String description){
        super(location, name, id, uniqueId);
        this.description = description;

    }

    // used for testing
    @Override
    public String toString(){
        return "\nOutdoorGym toString: "+
                "\nName: "+ name +
                "\nLocation: "+ location;
    }
}