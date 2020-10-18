package ShortestMiddPath;

/*
* Author: Shelby Kimmel
* Creates a adjacency list object to store information about the graph of roads, and contains the main functions used to
* run the Bellman Ford algorithm

*/

import java.io.File;
import java.util.Scanner;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import java.lang.Math;
import java.io.File;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Graph {

	// Object that contains an adjacency list of a road network, and a dictionary from elements of the list to indices from 0 to |V|-1
	HashMap<Integer, ArrayList<Road>> adjList;
	HashMap<Integer,Integer> nodeDict;


	public Graph(String file) throws IOException{
		// We will store the information about the road graph in an adjacency list
		// We will use a HashMap to store the Adjacency List, since each vertex in the graph has a more or less random integer name.
		// Each element of the HashMap will be an ArrayList containing all roads (edges) connected to that vertex
		adjList = new HashMap<>();
		nodeDict = null;

		// Based on https://stackoverflow.com/questions/49599194/reading-csv-file-into-an-arrayliststudent-java
		String line = null;
		BufferedReader br = new BufferedReader(new FileReader(file));
		if ((line=br.readLine())==null){
			return;
		}
		while ((line = br.readLine())!=null) {
			String[] temp = line.split(",");
			//Assume all roads are two-way, and using ArcMiles as distance:
			this.addToList(new Road(Integer.parseInt(temp[60]),Integer.parseInt(temp[61]),temp[9],Double.parseDouble(temp[31])));
			this.addToList(new Road(Integer.parseInt(temp[61]),Integer.parseInt(temp[60]),temp[9],Double.parseDouble(temp[31])));
		}


		//For dynamic programming, we will have an array with indeces 0 to |V|-1,
		// where |V| is the number of vertices. Thus we need to associate each element of adjList with a number between 0 and |V|-1
		// We will use a Dictionary (HashMap) to do this.
		nodeDict = new HashMap<>();
		int j = 0;
		for (Integer nodeName: adjList.keySet()){
			nodeDict.put(nodeName, j);
			j++;
		}
	}


	// get functions
	public HashMap<Integer, ArrayList<Road>> getAdjList(){
		return adjList;
	}
	public HashMap<Integer,Integer> getDict(){
		return nodeDict;
	}

	//Adds the Road (edge) to the appropriate list of the adjacency list, used by the constructor method
	//Based on https://stackoverflow.com/questions/12134687/how-to-add-element-into-arraylist-in-hashmap
	public synchronized void addToList(Road road) {
		Integer node = road.getStart();
    	ArrayList<Road> roadList = this.getAdjList().get(node);

    	// if node is not already in adjacency list, we creat a list for it
    	if(roadList == null) {
    	    roadList = new ArrayList<Road>();
    	    roadList.add(road);
   		    this.getAdjList().put(node, roadList);
  	  	}
  	  	else {
        	// add if item is not already in list
        	if(!roadList.contains(road)) roadList.add(road);
    	}

    }


    public Double[][] ShortestDistance(Integer startNode){
    	// This method should create the array storing the objective function values of subproblems used in Bellman Ford.

			Double[][] distArray = new Double[nodeDict.size()][nodeDict.size()];

			distArray[getDict().get(startNode)][0] = 0.0;

			for(int x = 0; x < getDict().size(); x++){

				if(x != 134) {
				distArray[x][0] = 100000000.0;
				}
			}

			// i represents number of edges
			for(int i = 1; i < getDict().size(); i++){

				// j represents the node in the adjacency list
				for(int j = 0; j < getDict().size(); j++){

					distArray[j][i] = distArray[j][i-1];

					// entry represents all the nodes
					for(HashMap.Entry<Integer, ArrayList<Road>> mapLocation: getAdjList().entrySet()) {

						//k represents an edge between the current node, j, and another node
						for(int road = 0; road < getAdjList().get(mapLocation.getKey()).size(); road++){

							// System.out.println(nodeDict.get(entry.getKey()));
							// System.out.println(entry.getValue().get(k).getMiles());
							// System.out.println(entry.getValue().get(k).getEnd());
							int currentIndex = getDict().get(mapLocation.getKey());

							// does this edge connect the current node to a new node?
							if(getDict().get(mapLocation.getValue().get(road).getEnd()) == j) {

								// is the distance shorter than a currently existing path?
								if(distArray[currentIndex][i-1]
								+ mapLocation.getValue().get(road).getMiles()
								< distArray[j][i]) {

									distArray[j][i] = distArray[getDict().get(mapLocation.getKey())][i-1]
									+ mapLocation.getValue().get(road).getMiles();

									// System.out.println(entry.getValue().get(k))s
								}
							}

						}

					}
					// access relevant item in adjacency list
				}
			}

		return distArray;
    }



    public void ShortestPath(Integer endNode, Double[][] dpArray){
		// This method should work backwards through the array you created in ShortestDistance and output the
		// sequence of streets you should take to get from your starting point to your ending point.

		if(dpArray[getDict().get(endNode)][getDict().size()-1] == 100000000.0){
			System.out.println("No path possible, sorry!");
		}

		int i = getDict().size()-1;
		int v = endNode;
	 	ArrayList<String> path = new ArrayList<String>(0);
		ArrayList<Double> path_distance = new ArrayList<Double>(0);

		while (i > 0) {

			if (dpArray[getDict().get(v)][i] != dpArray[getDict().get(v)][i-1]) {

				// runs through all the possible edges
				for(HashMap.Entry<Integer, ArrayList<Road>> mapLocation: getAdjList().entrySet()) {

					//k represents an edge between the current node, j, and another node
					for(int road = 0; road < getAdjList().get(mapLocation.getKey()).size(); road++){

						if (dpArray[getDict().get(v)][i] == dpArray[getDict().get(mapLocation.getValue().get(road).getEnd())][i-1] + mapLocation.getValue().get(road).getMiles()) {

							//System.out.println("connection: " + mapLocation.getValue().get(road));
							path.add(mapLocation.getValue().get(road).getName());
							path_distance.add(mapLocation.getValue().get(road).getMiles());
							v = mapLocation.getValue().get(road).getEnd();

							//System.out.println("connected: " + mapLocation.getValue().get(road).getEnd());
						}
					}
				}
			}

			i--;
		}

		double total_dist = 0.0;

		for(int l = 0; l < path.size(); l++){

			total_dist+= path_distance.get(l);

			if(l<path.size()-1){
			System.out.print(path.get(path.size()-l-1) + ", ");
			}

			else{
				System.out.print(path.get(path.size()-l-1) + "\n");
			}
		}

		System.out.println(total_dist);

	}


}
