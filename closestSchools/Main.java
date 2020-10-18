package ClosestSchools;
/*
* Author: Walter Conrad
* I worked for approximately 7 hours on this assignment.
* Collaborator: Will Kelley, Bobo
* Implements the closest pair of points recursive algorithm
* on locations of K-12 schools in Vermont obtained from http://geodata.vermont.gov/datasets/vt-school-locations-k-12

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
import java.util.Random;
import java.lang.Math;
import java.lang.Object;
import java.io.File;

public class Main {


	public static void main(String[] args) throws IOException{

		//Creates an ArrayList containing School objects from the .csv file
		// Based on https://stackoverflow.com/questions/49599194/reading-csv-file-into-an-arrayliststudent-java
		String line = null;
		ArrayList<School> schoolList = new ArrayList <School>();
		// You may have to adjust the file address in the following line to your computer
		BufferedReader br = new BufferedReader(new FileReader("ClosestSchools/Data/VT_School_Locations__K12(1).csv"));
		if ((line=br.readLine())==null){
			return;
		}
		while ((line = br.readLine())!=null) {
			String[] temp = line.split(",");
			schoolList.add(new School(temp[4],Double.parseDouble(temp[0]),Double.parseDouble(temp[1])));
		}

		//Preprocess the data to create two sorted arrayLists (one by X-coordinate and one by Y-coordinate):
		ArrayList<School> Xsorted = new ArrayList <School>();
		ArrayList<School> Ysorted = new ArrayList <School>();
		Collections.sort(schoolList, new SortbyX());
		Xsorted.addAll(schoolList);
		Collections.sort(schoolList, new SortbyY());
		Ysorted.addAll(schoolList);

		//Run the Recursive Algorithm
		School[] cp = new School[2];
		cp = ClosestPoints(Xsorted,Ysorted);
		if(cp[0]!=null)
			System.out.println("The two closest schools are "+ cp[0].name + " and " + cp[1].name +".");


	}


	public static double EucDist(School a, School b) {

		double dist;

		dist = Math.sqrt((a.getX() - b.getX())*(a.getX() - b.getX()) + (a.getY() - b.getY())*(a.getY() - b.getY()));

		return dist;

	}


	public static School[] ClosestPoints(ArrayList<School> sLx, ArrayList<School> sLy){
		// Recursive divide and conquer algorithm for closest points
		// sLx should be sorted by x coordinate and sLy should be sorted by y coordinate
		// Returns an array containing the two closest School objects

		//System.out.println("nX: " + sLx.size()); for debugging, allows verification that sLx and sLy are equal size in recursive calls
		//System.out.println("nY: " + sLy.size());

		//to store closest pair of schools
		School[] closestPair = new School[2];

		//double to store closest distance between schools
		Double delta;

		//bases: brute force
		if (sLx.size() <= 3) {
			int first; int second; int third = 0;

			if(sLx.size() == 3){

				Double comp1;
				Double comp2;
				Double comp3;

				comp1 = Math.sqrt(Math.pow((sLx.get(0).getX() - sLx.get(1).getX()),2) + Math.pow((sLx.get(0).getY() - sLx.get(1).getY()),2));			//calculating Euclidean Distance
				comp2 = Math.sqrt(Math.pow((sLx.get(0).getX() - sLx.get(2).getX()),2) + Math.pow((sLx.get(0).getY() - sLx.get(2).getY()),2));
				comp3 = Math.sqrt(Math.pow((sLx.get(1).getX() - sLx.get(2).getX()),2) + Math.pow((sLx.get(1).getY() - sLx.get(2).getY()),2));

				delta = Math.min(comp1, comp2);
				delta = Math.min(delta, comp3);

				//System.out.println("delta0: " + delta);

				if(comp1.equals(delta)){
						//closestPairDelta.add(sLx.get(0));
						//closestPairDelta.add(sLx.get(1));
						closestPair[0] = (sLx.get(0));
						closestPair[1] = (sLx.get(1));

						//System.out.println("returns3");

						return(closestPair);
					}

				else if(comp2.equals(delta)){
						//closestPairDelta.add(sLx.get(0));
						//closestPairDelta.add(sLx.get(2));
						closestPair[0] = (sLx.get(0));
						closestPair[1] = (sLx.get(2));

						//System.out.println("returns3");

						return(closestPair);
					}

				else {
						//closestPairDelta.add(sLx.get(1)); 		//add return
						//closestPairDelta.add(sLx.get(2));
						closestPair[0] = (sLx.get(1));
						closestPair[1] = (sLx.get(2));

						//System.out.println("returns3");

						return(closestPair);
					}
			}

			else if(sLx.size() == 2) {
				//closestPairDelta.add(sLx.get(0)); 		//add return
				//closestPairDelta.add(sLx.get(1));
				closestPair[0] = (sLx.get(0));
				closestPair[1] = (sLx.get(1));

				//System.out.println("returns2");

				return(closestPair);
			}
		}

		//initialize arrays for recursive calls
		ArrayList<School> Xl = new ArrayList <School>();
		ArrayList<School> Xr = new ArrayList <School>();
		ArrayList<School> Yl = new ArrayList <School>();
		ArrayList<School> Yr = new ArrayList <School>();
		double midX = sLx.get(sLx.size()/2).getX();

		//adding elements to Xl and Xr
		for (int i = 0; i < sLx.size(); i++) {
			if(sLx.get(i).getX() < midX){
				Xl.add(sLx.get(i));
			}
			else {
				Xr.add(sLx.get(i));
			}
		}

		//adding elements to Yl and Yr
		for (int i = 0; i < sLy.size(); i++) {
			if(sLy.get(i).getX() < midX){
				Yl.add(sLy.get(i));
			}
			else {
				Yr.add(sLy.get(i));
			}
		}


		//System.out.println(ClosestPoints(Xl,Yl)[0]);
		//first store recursive calls in school array so that we don't have to call 4 times in delta recursive call function
		School[] LeftPoints = (ClosestPoints(Xl,Yl));
		School[] RightPoints = (ClosestPoints(Xr,Yr));


		//recursive call
		delta = Math.min(EucDist(LeftPoints[0], LeftPoints[1]),
								     EucDist(RightPoints[0], RightPoints[1]));


		//whatever the minimum is, put the corresponding points into ClosestPair
		if(delta.equals(EucDist(LeftPoints[0], LeftPoints[1]))){
			closestPair[0] = LeftPoints[0];
			closestPair[1] = LeftPoints[1];
		}
		else {
			closestPair[0] = RightPoints[0];
			closestPair[1] = RightPoints[1];
		}

		//initialize arraylists for closest ydelta points
		ArrayList<School> ydelta = new ArrayList <School>();
		ArrayList<School> closestPairYdelta = new ArrayList <School>();


		//add schools to ydelta that are within delta of midX
		for (int i = 0; i < sLx.size(); i++) {

			if(Math.abs(sLx.get(i).getX() - midX) < delta) {

				ydelta.add(sLx.get(i));

			}
		}

		//System.out.println(ydelta);
		//System.out.println(ydelta.size());

		//System.out.println("Ydelta1 size: " + ydelta.size());

		//to check next points in ydelta
		for(int i = 0; i < ydelta.size(); i++) {

			for(int j=i+1; j < ydelta.size() && j<6; j++){

				//System.out.println("Ydelta loop:");

				if(EucDist(ydelta.get(i), ydelta.get(j)) < delta) {

				closestPairYdelta.add(ydelta.get(i));
				closestPairYdelta.add(ydelta.get(j));

				//System.out.println(closestPairYdelta.get(0));
				//System.out.println(closestPairYdelta.get(1));

				delta = EucDist(closestPairYdelta.get(0), closestPairYdelta.get(1));

				}
			}
		}

		//System.out.println("post-loop ydelta0: " + closestPairYdelta.get(0));
		//System.out.println("post-loop ydelta1: " + closestPairYdelta.get(1));

		//if null, don't compare
		if(closestPairYdelta.size() > 0){

		//compare delta to ydelta
		if((EucDist(closestPair[0], closestPair[1])) > EucDist(closestPairYdelta.get(0), closestPairYdelta.get(1))) {

				closestPair[0] = closestPairYdelta.get(0);
				closestPair[1] = closestPairYdelta.get(1);
			}

		}


		return closestPair;

	}



}
