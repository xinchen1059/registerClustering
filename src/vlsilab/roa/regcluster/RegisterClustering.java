package vlsilab.roa.regcluster;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

/**
 * The main functionality for forming the clock trees through register 
 * clustering
 * @author xinchen
 *
 */
public class RegisterClustering {
	
	// Tree map to store the merging pairs ordered by the merging cost
	private TreeMap<Long, List<MergePair>> _mergePairTreeMap = null;
	
	// Store the current sub tree roots
	private HashMap<Integer, Node> _subTreeNodes = null;
	private int _numClusters;
	private static int _newId = 0;
	
    public RegisterClustering(ArrayList<Node> sinkNodes, int numClusters) {
    	_numClusters = numClusters;
    	_mergePairTreeMap = new TreeMap<Long, List<MergePair>>();
    	_subTreeNodes = new HashMap<Integer, Node>(sinkNodes.size());
    	_newId = sinkNodes.size();
    	
    	// initialize the tree map
    	initializeMaps(sinkNodes);
    }
    
    /**
     * Use for initializing the internal data structures
     * @param sinkNodes
     */
    private void initializeMaps(ArrayList<Node> sinkNodes) {
    	int numNodes = sinkNodes.size();
    	
    	// Compute the merging cost for each pair of sink nodes and 
    	// update the tree map
    	for (int i = 0; i < numNodes; i++) {
    		// All sink nodes are sub-tree roots initially
    		_subTreeNodes.put(sinkNodes.get(i).getId(), sinkNodes.get(i));
    		for (int j = i + 1; j < numNodes; j++) {
    			MergePair mergePair = mergeTwoSubTree(sinkNodes.get(i), sinkNodes.get(j));
    			updateMergeTreeMap(mergePair);
    		}
    	}
    }
	
	/**
	 * Given all the sink (leave) nodes and number of clusters (N), build N sub-trees where
	 * the total wire and load capacitance is minimal 
	 * @return List of subtree (cluster) roots
	 */
	public ArrayList<Node> run() {
		// As long as the current number of subtree roots is greater than
		// target number, continue to merge the new pair with lowest cost
		while(_subTreeNodes.size() > _numClusters) {
			merge();
		}
		return new ArrayList<Node>(_subTreeNodes.values());
	}
	
	/**
	 * Merge one pair of subtree with lowest cost
	 */
	private void merge() {
		// Purge the merging pairs from low cost until we see a pair that
		// should not be purged, which should be the pair to merge
		lazyCleanup();
		MergePair mergePair = _mergePairTreeMap.firstEntry().getValue().get(0);
		
		// Merging the pair to form a new sub-tree
		TreeNode subTreeRoot = new TreeNode(getNewId(), mergePair.getX(), 
				mergePair.getY(), mergePair.getTotalCap(), mergePair.getLeft(), mergePair.getRight());
		
		// Remove merged nodes and create a new node
		_subTreeNodes.remove(mergePair.getLeft().getId());
		_subTreeNodes.remove(mergePair.getRight().getId());
		_subTreeNodes.put(subTreeRoot.getId(), subTreeRoot);
		
		// Update tree with new possible pairs
		updateCostMap(subTreeRoot);
		
	}
	
	/**
	 * Update the tree map for the newly formed subtree
	 * @param treeNode The newly formed subtree
	 */
	private void updateCostMap(TreeNode treeNode) {
		if (treeNode == null) { return; }
		
		// For each pair of new tree root and existing tree roots, compute
		// the merge cost and update the cost tree map
		for(Node node : _subTreeNodes.values()) {
			if (node.getId() == treeNode.getId()) {
				continue;
			}
			MergePair mergePair = mergeTwoSubTree(node, treeNode);
			updateMergeTreeMap(mergePair);
		}
	}
	
	/**
	 * Create the new subtree roots by merging two sub-trees given by input
	 * @param left root of left sub-tree
	 * @param right root of right sub-tree
	 * @return root of new subtree
	 */
	private MergePair mergeTwoSubTree(Node left, Node right) {
		if (left == null || right == null) {
			return null;
		}
		
		// Compute total cap (cost) and location for potential merge pair
		double totalCap = RegisterClusteringUtility.computeTotalCap(left, right);
		Location loc = RegisterClusteringUtility.computeMergeLocation(left, right);
		return new MergePair(loc.getX(), loc.getY(), left, right, totalCap);
	}
	
	/**
	 * Insert potential merging pairs into the tree map
	 * @param mergePair
	 */
	private void updateMergeTreeMap(MergePair mergePair) {
		long mergeCost = mergePair.getTotalCapInLong();
		
		if (_mergePairTreeMap.containsKey(mergeCost)) {
			// If merge cost already exists, meaning duplicate cost,
			// append the potential pair to the end of the array
			_mergePairTreeMap.get(mergeCost).add(mergePair);
		} else {
			// No same merging cost exists, create new entry
			List<MergePair> pairList = new ArrayList<MergePair>() {
				{add(mergePair);}};
			_mergePairTreeMap.put(mergeCost, pairList);
		}
		return;
	}
	
	/**
	 * Purge the tree to remove already merged trees from the tree map
	 * This is called lazy cleanup because this is call every-time we are
	 * trying to find the merging pair with minimum cost. This is to 
	 * guarantee we only visit each node in tree map once.
	 */
	private void lazyCleanup() {
		while(!_mergePairTreeMap.isEmpty()) {
			// Purging from the lowest rank
			Entry<Long, List<MergePair>> entry = _mergePairTreeMap.firstEntry();
			List<MergePair> mergePairs = entry.getValue();
			while(!mergePairs.isEmpty()) {
				MergePair mergePair = mergePairs.get(0);
				// Stop until we see a pair that should not be purged
				if(isNodeMerged(mergePair.getLeft()) || 
						isNodeMerged(mergePair.getRight())) {
					mergePairs.remove(0);
				} else {
					return;
				}
			}
			_mergePairTreeMap.remove(entry.getKey());
		}
	}
	
	/**
	 * Check if a node is already merged
	 * @param node
	 * @return
	 */
	private boolean isNodeMerged(Node node) {
		return node != null && !_subTreeNodes.containsKey(node.getId());
	}
	
	/**
	 * Return a new id for the newly generated tree node
	 * @return
	 */
	private int getNewId() {
		return _newId++;
	}
}
