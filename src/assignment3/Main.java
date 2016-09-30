/**
 * WORD LADDER Main.java
 * EE422C Project 3 submission by
 * Replace <...> with your actual data.
 * Pranav Harathi
 * sh44674
 * 16460
 * Adi Miller
 * asm2992
 * 16480
 * Slip days used: <0>
 * Git URL: https://github.com/adismiller/Project3_asm2992_sh44674
 * Fall 2016
 */

package assignment3;
import java.util.*;
import java.io.*;

public class Main {
	
	// static variables and constants only here.
	public static Set<String> dictionary;
	public static String startWord;
	public static String endWord;
	public static void main(String[] args) throws Exception {
		
		Scanner kb;	// input Scanner for commands
		PrintStream ps;	// output file
		// If arguments are specified, read/write from/to files instead of Std IO.
		if (args.length != 0) {
			kb = new Scanner(new File(args[0]));
			ps = new PrintStream(new File(args[1]));
			System.setOut(ps);			// redirect output to ps
		} else {
			kb = new Scanner(System.in);// default from Stdin
			ps = System.out;			// default to Stdout
		}
		initialize();
		// TODO methods to read in words, output ladder
	}
	
	public static void initialize() {
		// initialize your static variables or constants here.
		// We will call this method before running our JUNIT tests.  So call it 
		// only once at the start of main.
		dictionary = makeDictionary();
	}
	
	/**
	 * @param keyboard Scanner connected to System.in
	 * @return ArrayList of 2 Strings containing start word and end word. 
	 * If command is /quit, quit the program. 
	 */
	public static ArrayList<String> parse(Scanner keyboard) {
		String[] input = keyboard.nextLine().trim().split("\\s+");
		if(input.length == 1 && input[0].equalsIgnoreCase("\\quit")) {
			System.exit(0);
		} else if(input.length == 2) {
			return (ArrayList<String>) Arrays.asList(input);
		}
		return null;
	}
	
	/**
	 * Generates a word ladder using DFS with one heuristic (pruning neighbors
	 * by only generating neighbors where the indices between an input word and the
	 * goal word have different chars) and randomizes the order of neighbor generation
	 * @param start
	 * @param end
	 * @return
	 */
	public static ArrayList<String> getWordLadderDFS(String start, String end) {
		// Returned list should be ordered start to end.  Include start and end.
		// Return empty list if no ladder.
		Main.startWord = start;
		Main.endWord = end;
		Set<String> visited = new HashSet<>();
		ArrayList<String> path = new ArrayList<>();
		try {
			path.add(start);
			DFShelper(start, end, visited, path);
		} catch (StackOverflowError e) {
			path.clear();
			path.add(end);
			DFShelper(end, start, visited, path);
			Collections.reverse(path);
		}
		return path; 
	}
	
	/**
	 * Helper method that implements actual recursion
	 * @param input
	 * @param end
	 * @param visited
	 * @param path
	 */
	public static void DFShelper(String input, String end, Set<String> visited, ArrayList<String> path) {
		if(input.equalsIgnoreCase(end)) {
			return;
		}
		visited.add(input);
		int[] differences = getDifferentIndices(input, end);
		String nextInput = getNextMutantWord(input, visited, differences);
		if(nextInput.equals("")) {
			if(path.isEmpty())
				return;
			DFShelper(path.remove(path.size()-1), end, visited, path);
		} else {
			path.add(nextInput);
			DFShelper(nextInput, end, visited, path);
		}
	}
	

	/**
	 * Uses the BFS algorithm to search for a word ladder
	 * Generates all neighbors dynamically and uses the WordNode class for pathfinding
	 * @param start
	 * @param end
	 * @return
	 */
    public static ArrayList<String> getWordLadderBFS(String start, String end) {
		
    	// Initialize data structures to track info
		Queue<WordNode> bfsQ = new LinkedList<>();
		Set<String> visited = new HashSet<>();		
		ArrayList<String> path = new ArrayList<>();
		
		WordNode startNode = new WordNode(start);
    	bfsQ.offer(startNode);
    
    	WordNode endNode = null;
    	
    	// search logic
    	outerLoop:
    	while(!bfsQ.isEmpty()) {
    		WordNode current = bfsQ.poll();
    		visited.add(current.value);
    		int[] differences = getDifferentIndices(current.value, end);
    		Set<String> neighbors = getAllMutantsOfWord(current.value, visited, differences);
    		for(String n : neighbors) {
    			WordNode temp = new WordNode(n);
    			temp.parent = current;
    			if(n.equals(end)) {
    				endNode = temp;
    				break outerLoop;
    			}
    			bfsQ.offer(temp);
    		}
    	}
    	
    	if(endNode == null)
    		return new ArrayList<String>();
    	
    	// backtrack to find path
    	WordNode c = endNode;
    	path.add(endNode.value);
    	while((c = c.parent) != null) {
    		path.add(0, c.value);
    	}
    	
		return path; // replace this line later with real return
	}
    
    /**
     * Makes the dictionary set
     * @return
     */
	public static Set<String> makeDictionary () {
		Set<String> words = new HashSet<String>();
		Scanner infile = null;
		try {
			infile = new Scanner(new File("five_letter_words.txt"));
		} catch (FileNotFoundException e) {
			System.out.println("Dictionary File not Found!");
			e.printStackTrace();
			System.exit(1);
		}
		while (infile.hasNext()) {
			words.add(infile.next().toUpperCase()); //changes to all upper case
		}
		return words;
	}
	
	/**
	 * Prints out ladder of words in specified format
	 * @param ladder
	 */
	public static void printLadder(ArrayList<String> ladder) {
		if(ladder.isEmpty()) {
			System.out.printf("no word ladder can be found between %s and %s\n", startWord, endWord);
		} else {
			int size = ladder.size() - 2;
			System.out.printf("a %d-rung word ladder exists between %s and %s\n", size, startWord, endWord);
			for(String s : ladder) {
				System.out.println(s);
			}
		}
	}
	
	/**
	 * Generates all possible neighbors of word in dictionary (optimized)
	 * @param source
	 * @param visitedWords
	 * @param diffs
	 * @return
	 */
	public static Set<String> getAllMutantsOfWord(String source, Set<String> visitedWords, int[] diffs){
		Set<String> mutants = new HashSet<String>();
		for (int i = 0; i < source.length(); i++) {
			if(diffs[i] == 0) continue;
			for(char c = 'a'; c <= 'z'; c++) {
				String ch = Character.toString(c);
				String word = source.substring(0, i) + ch + source.substring(i+1);
				if(word.equals(source)) continue;
				if(!visitedWords.contains(word) && dictionary.contains(word.toUpperCase())) {
					mutants.add(word);
				}
			}
		}
		return mutants;
	}
	
	/**
	 * Generates next mutation of source word for DFS
	 * @param source
	 * @param visitedWords
	 * @return
	 */
	public static String getNextMutantWord(String source, Set<String> visitedWords, int[] diffs) {
		List<Integer> randomizedIndices = randomizedNums(source.length(), 0);
		for(int i : randomizedIndices) {
			if(diffs[i] == 0) continue;
			List<Integer> randomizedChars = randomizedNums(26, (int)'a');
			for(Integer c : randomizedChars) {
				String ch = Character.toString((char) c.intValue());
				String word = source.substring(0, i) + ch + source.substring(i+1);
				if(word.equals(source)) continue;
				if(!visitedWords.contains(word) && dictionary.contains(word.toUpperCase())) {
					return word;
				}
			}
		}
		return "";
	}
	
	/**
	 * DFS heuristic that limits search for mutant words to only include the indices of
	 * the string that are different
	 * @param input
	 * @param end
	 * @return
	 */
	public static int[] getDifferentIndices(String input, String end) {
		int[] diffs = new int[end.length()];
		for(int i = 0; i < diffs.length; i++) {
			if(input.charAt(i) != end.charAt(i)) diffs[i] = 1;
		}
		return diffs;
	}
	
	/**
	 * DFS randomization for neighbors in different order
	 * @return
	 */
	public static List<Integer> randomizedNums(int size, int add) {
		List<Integer> randomizedNums = new ArrayList<>();
		for(int i1 = 0; i1 < size; i1++) {
			randomizedNums.add(new Integer(i1 + add));
		}
		Collections.shuffle(randomizedNums);
		return randomizedNums;
	}
}

/**
 * Class for BFS that includes parent node reference and String value
 * @author pranavh
 *
 */
class WordNode {
	WordNode parent;
	String value;
	public WordNode(String value) {
		this.value = value;
		parent = null;
	}
}
