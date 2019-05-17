package se.su.dsv.pvt.utemaning.backend;


/**
 * Inherits from Place
 */
public class OutdoorGym extends Place {

    public String getDescription() {
        if (description != null)
            return description;
        else return "No description available";
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private String description;
    private boolean hasChallenge;

    public OutdoorGym(Location location, String name, int id, String uniqueId, String description, double averageRating) {
        super(location, name, id, uniqueId, averageRating);
        this.description = description;
    }

    public boolean getHasChallenge() {
        return hasChallenge;
    }

    // used for testing
    @Override
    public String toString() {
        return "\nOutdoorGym toString: " +
                "\nName: " + getName() +
                "\nLocation: " + getLocation();
    }
}