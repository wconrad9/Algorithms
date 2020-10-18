/*
* Author: Walter Conrad
* Implements Huffman's binary encoding algorithm
* I spent approximately 7 hours on this assignment.
* I worked alone this time.
*/


import java.io.IOException;
import java.io.*;
import java.util.*;
import java.lang.StringBuilder;


// define Node class to store each letter and their respective weight
class Node {
	char value;
	double weight;
	Node left;
	Node right;

	Node(char value, double weight) {
			this.value = value;
			this.weight = weight;
			Node right;
			Node left;
		}
	}


public class Huffman {

	// create a HashMap to store the binary encoding of the string
	private static HashMap<Character, String> encodingMap = new HashMap<Character, String>();

	// traverse tree to assign coded values
	private static void setEncoding(Node node, StringBuilder prefix) {

	// if we haven't fallen out of the tree
	if (node != null) {

		// if we're at a leaf node,
		// then both node.left and node.right will be null
		// so we add the current prefix string to encodingMap along with the node's value
		if (node.left == null && node.right == null) {
			encodingMap.put(node.value, prefix.toString());


		// otherwise we have to keep traversing the tree recursively
		// deleting the last character allows the function to check both right and left nodes
		} else {
			prefix.append('0');

			// the left child becomes the new root in the recursive call
			setEncoding(node.left, prefix);
			prefix.deleteCharAt(prefix.length() - 1);

			prefix.append('1');

			// the right child becomes the new root in the recursive call
			setEncoding(node.right, prefix);
			prefix.deleteCharAt(prefix.length() - 1);
			}
		}
	}


	public static void main(String[] args) throws IOException{

		//Runs encode
		System.out.println(encode("Hello, world!"));


	}

	public static String encode(String message){
		// Uses Huffman's algorithm to encode message into binary
		// Also prints a dictionary.

		// split message into individual characters
		char[] split_message =  message.toCharArray();

		// create hashmap to store character frequencies
		HashMap<Character, Double> frequencies = new HashMap<Character, Double>();
		Node[] alphabet = new Node[split_message.length];

		// for each character in the message to be encoded
		for(char c:split_message){

			// if the character is not in the HashMap already
			if(frequencies.containsKey(c)){

				// add the character with a frequency of 1.0
				frequencies.put(c, frequencies.get(c)+1.0);
			}

			// if the character is already in the HashMap
			else{

			// increment its frequency by 1.0
			frequencies.put(c,1.0);
			}
		}


		// create a node for each character, store in ArrayList
		ArrayList<Node> tree = new ArrayList<Node>();

		// for each character in the frequencies HashMap
    for (Map.Entry entry : frequencies.entrySet()) {

				// weight equals the number of occurrences divided by the number of
				// characters in the original message
        double weight = (double)entry.getValue()/(double)split_message.length;

				// create a new Node and add it to a tree
				Node character = new Node((char)entry.getKey(), weight);
				character.left = null;
				character.right = null;
				tree.add(character);
    }

		// build the 'tree' using an arrayList
		while(tree.size() > 1){

			int minIndex = 0;
			double minWeight1 = 2.0;
			double minWeight2 = 2.0;
			Node minNode1 = null;
			Node minNode2 = null;

			// find first lowest weight node
			for(int i = 0; i< tree.size(); i++){

				if(tree.get(i).weight < minWeight1){

					minIndex = i;

					minWeight1 = tree.get(i).weight;

					minNode1 = tree.get(i);
				}

			}

			// remove the Node from the 'tree' with min weight
			tree.remove(minIndex);

			// find second lowest weight node
			for(int i = 0; i< tree.size(); i++){

				if(tree.get(i).weight < minWeight2){
					minIndex = i;
					minWeight2 = tree.get(i).weight;
					minNode2 = tree.get(i);
				}
			}

			// remove the Node from the 'tree' with 2nd min weight
			tree.remove(minIndex);

			// create a new node that links the two lowest weight nodes,
			// has weight equal to the children's weights combined
			Node combined = new Node('-', minNode1.weight + minNode2.weight);
			combined.left = minNode1;
			combined.right = minNode2;

			// add the new 'tree' back to our tree ArrayList and repeat the process
			tree.add(combined);

		}

		// the tree has been built, so we now use a recursive function call to
		// encode the message in binary
		setEncoding(tree.get(0), new StringBuilder());


	// print the encoded message
	for (char c: split_message) {

			System.out.print(encodingMap.get(c));

		}

		System.out.println();

	// print a 'dictionary' showing a user how to decode the coded message
	for (Map.Entry entry : encodingMap.entrySet()) {

			System.out.println(entry.getKey() + " - " + entry.getValue());
	}


		return "Encoding Above^!";
	}



}
