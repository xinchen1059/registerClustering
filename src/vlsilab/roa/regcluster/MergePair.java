package vlsilab.roa.regcluster;

/**
 * @author xinchen
 * The merging pair of the two subtrees
 */
public class MergePair {
	private long _x; // x coordinate 
	private long _y; // y coordinate
    private Node _left; // left node of the tree
    private Node _right; // right node of the tree
    private double _totalCap; // Total capacitance of the subtree
    
    public MergePair(long x, long y, Node left, Node right, double totalCap) {
    	_x = x;
    	_y = y;
    	_left = left;
    	_right = right;
    	_totalCap = totalCap;
    }
    
    public long getX() {
    	return _x;
    }
    
    public long getY() {
    	return _y;
    }
    
    public Node getLeft() {
    	return _left;
    }
    
    public Node getRight() {
    	return _right;
    }
    
    /**
     * The total cost by merging the two subtree
     * @return
     */
    public double getTotalCap() {
    	return _totalCap;
    }
    
    /**
     * The total cap in long for keying
     * @return
     */
    public long getTotalCapInLong() {
    	return Math.round(_totalCap);
    }
}
