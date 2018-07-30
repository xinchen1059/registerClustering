package vlsilab.roa.regcluster;

/**
 * @author xinchen
 * Utility class for commonly used methods
 */
public class RegisterClusteringUtility {
	
	public static final double UNIT_CAP = 12.0; // Unit capacitance for wires
	
	/**
	 * Compute total capacitance cost giving two sub-tree nodes
	 * @param left
	 * @param right
	 * @return
	 */
	public static double computeTotalCap(Node left, Node right) {
		return left.getCap() + right.getCap() + computeWireCap(left, right);
	}
	
	/**
	 * Compute the wire capacitance giving the two sub-tree nodes
	 * @param left left sub-tree
	 * @param right right sub-tree
	 * @return
	 */
	public static double computeWireCap(Node left, Node right) {
		return (Math.abs(left.getX() - right.getX()) + 
				Math.abs(left.getY() - right.getY())) * UNIT_CAP; 
	}
	
	/**
	 * Compute the root location of the newly merged tree given two sub-trees
	 * @param left left sub-tree
	 * @param right right sub-tree
	 * @return
	 */
	public static Location computeMergeLocation(Node left, Node right) {
		return new Location((left.getX() + right.getX())/2, 
				(left.getY() + right.getY())/2);
		
	}
}
