package vlsilab.roa.regcluster;

/**
 * @author xinchen
 * Defining the coordinates in Manhattan space
 */
public class Location {
    private long _x; // x coordinates
    private long _y; // y coordinates
    
    public Location(long x, long y) {
    	_x = x;
    	_y = y;
    }
    
    public long getX() {
    	return _x;
    }
    
    public long getY() {
    	return _y;
    }
}
