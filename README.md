# Register Clustering Algorithm Implementation
**Usage**
```
executable <input file path> <number of clusters to create> <output file path>
```

**Input file format**
A text file with sink nodes locations and the load values, one node per line. For example, if the input file contains 3 sink nodes -- node 1: location (51,23), capacitance 6.8; node 2: location (31,73), capacitance 2.8; node 3: location (123,92), capacitance 13. The input file would look like the following:<br/>
51 23 6.8<br/>
31 73 2.8<br/>
123 92 13

**Output file format**
Assuming we are clustering registers to form two trees, the output file may look like the following:<br/>
The node id 14: Coordinate(197, 307), total load 17620.4<br/>
The node id 15: Coordinate(1328, 3989), total load 37286.0

**Java Class Summary:**
- **Main.java**: The main program entry point, which reads the input file from input argument, processes it into a list of nodes and call the RegisterClustering.run method to execute the register cluster algorithm. 
- **RegisterClustering.java**: The main algorithm implementation class, where I used TreeMap to maintain the possible pairs of sub-trees to merge and used the load cost as the key for fast retrieval of sub-trees with lowest cost for merging. The TreeMap is updated during merging time. We continue to merge the sub-tree until there are only a number of sub-trees left. 
- **RegisterClusteringUtility.java**:  Utility class for shared methods among different classes.
- **TreeNode.java, Node.java, Location.java, MergePair.java**: Class definitions for different objects used.
