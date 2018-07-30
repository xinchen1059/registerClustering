package vlsilab.roa.regcluster;

/**
 * @author xinchen
 * Define the tree nodes
 */
public class TreeNode extends Node{
	Node _left;
	Node _right;
	
    public TreeNode(int id, long x, long y, double cap, Node left, Node right) {
    	super(id, x, y, cap);
    	_left = left;
    	_right = right;
    }
    
    public Node getLeft() {
    	return _left;
    }
    
    public Node getRight() {
    	return _right;
    }
    
    /**
     * Print the content of the tree node
     */
    public String toString() {
    	return "The node id " + this.getId() + ": Coordinate(" + this.getX() +
    			", " + this.getY() + "), total cap " + this.getCap();
    }
}
