package vlsilab.roa.regcluster;

/**
 * @author xinchen
 * The class for defining a clock node which has a position and the load capacitance
 */
class Node {
	private int _id; // id of the node
	private long _x; // x coordinate of the node
	private long _y; // y coordinate of the node
	private double _cap; // Total capacitance of the node
	
	public Node(int id, long x, long y, double cap) {
		_id = id;
		_x = x;
		_y = y;
		_cap = cap;
	}
	
	public int getId() {
	    return _id;	
	}
	
	public long getX() {
		return _x;
	}
	
	public long getY() {
		return _y;
	}
	
	public double getCap() {
		return _cap;
	}
	
	public long getCapLong() {
		return Math.round(_cap);
	}
}