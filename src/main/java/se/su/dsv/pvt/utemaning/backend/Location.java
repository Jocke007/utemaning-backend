package se.su.dsv.pvt.utemaning.backend;

/**
 * GPS coordinates
 */
public class Location {

    private double x;
    private double y;


    public Location(double x, double y){
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }
    public double getY() {
        return y;
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
