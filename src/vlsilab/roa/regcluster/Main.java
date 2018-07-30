package vlsilab.roa.regcluster;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

/**
 * @author xinchen
 * The main program for performing the register clustering algorithm 
 * to build clock trees
 */

public class Main {

   public static void main(String[] args){
	   // args should have input file name, cluster number and output files
	   if (args.length < 3) {
	      System.err.println("Input file, num clusters and output files are required as input argument: argument size" + args.length);
	      System.exit(1);
	   }
	   
	   // Parse input file
	   ArrayList<Node> nodeList = processInputFile(args);
	   
	   int numClusters = Integer.valueOf(args[1]);
	   RegisterClustering regCluster = new RegisterClustering(nodeList, numClusters);
	   // Perform the main register clustering algorithm
	   ArrayList<Node> subTrees = regCluster.run();
	   
	   // Write to output file 
	   processOutputFile(args, subTrees);
   }
   
   /**
    * Read input file from input argument and parse the file content into sink nodes
    * @param args 
    * @return array of sink nodes
    */
   private static ArrayList<Node> processInputFile(String[] args) {
	   ArrayList<Node> nodeList = new ArrayList<Node>();
	   
       try {
           // Reads text file from input
           FileReader fileReader = 
               new FileReader(args[0]);

           // Put FileReader in BufferedReader.
           BufferedReader bufferedReader = 
               new BufferedReader(fileReader);

           String line = null;
           int nodeIdx = 0;
           
           // Parse the input file and generate the sink node array 
           while((line = bufferedReader.readLine()) != null) {
        	   String[] nodeValues = line.split(" +"); // split that works for multiple spaces
        	   try {
    	           Node sinkNode = new TreeNode(nodeIdx, Long.valueOf(nodeValues[0]), 
        			   Long.valueOf(nodeValues[1]), Double.parseDouble(nodeValues[2]), null, null);
    	           nodeList.add(sinkNode);
        	   } catch (NumberFormatException e) {
        		   System.out.println("Invalid format for node " + nodeIdx);
        		   bufferedReader.close();
        		   throw e;
        	   }
        	   nodeIdx++;
           }

           // Close file
           bufferedReader.close();         
       } catch(FileNotFoundException ex) {
           System.out.println("Cannot open file " + args[0]);                
       } catch(IOException ex) {
           System.out.println("Error reading file "  + args[0]);
       }
	   
	   return nodeList;
   }
   
   /**
    * Write the subtree root and cost information into the output file specified from input
    * @param args stores output file name
    * @param subTreeRoots The root of the N subtree built
    */
   private static void processOutputFile(String[] args, ArrayList<Node> subTreeRoots) {
	   Writer writer = null;
	   try {
	       writer = new BufferedWriter(new OutputStreamWriter(
	             new FileOutputStream(args[2])));
	       
	       // Write each sub-tree root information into the output file
	       for (Node subTreeRoot : subTreeRoots) {
	    	   writer.write(subTreeRoot.toString() + "\n");
		   } 
	   } catch (IOException ex) {
		   System.out.println("Error writing file "  + args[2]);
	   } finally {
	       try {
	           writer.close();
	       } catch (IOException e) {
	    	   System.out.println("Error closing file "  + args[2]);
	       }
	   }
   }
}
