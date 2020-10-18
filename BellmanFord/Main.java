package ShortestMiddPath;
/*
* Author: Shelby Kimmel
* Implements the Bellman Ford algorithm on data from https://geodata.vermont.gov/datasets/VTrans::vt-road-centerline
* Assignment Completed by: Walter Conrad
* I worked for approximately 5 hours on this assignment, and I worked alone this time around.

*/

import java.io.File;
import java.util.Scanner;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Collections;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.lang.Math;
import java.io.File;


public class Main {


	public static void main(String[] args) throws IOException{

		// The following creates an adjacency list data structure that stores the vertices and edges in the graph
		// You may have to adjust the file address in the following line to your computer
		Graph graph = new Graph("ShortestMiddPath/Data/VT_Road_Centerline.csv");

		// The following implements the array-filling part of the Bellman Ford algorithm for all points on the graph, starting at node 53980
		// 53980 is the closest node to 75 Shannon St.
		Double[][] dpArray = graph.ShortestDistance(53980);

		System.out.println("You should take the following walking route to get from 75 Shannon to Green Peppers:");
		graph.ShortestPath(30783,dpArray);

	}






}
