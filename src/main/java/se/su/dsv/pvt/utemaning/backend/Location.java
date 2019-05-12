package se.su.dsv.pvt.utemaning.backend;

/**
 * GPS coordinates
 */
public class Location {

    private int x;
    private int y;


    public Location(int x, int y){
        this.x = x;
        this.y = y;
    }

    public String getCoordinates() {
        return x + ", " + y;
    }

    // used for testing
    @Override
    public String toString(){
        return x + ", " + y;
    }
}
