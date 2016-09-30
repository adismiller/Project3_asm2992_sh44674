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

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class MainTest {
	
	@Before
	public void setUp() throws Exception {
		Main.initialize();
	}
	
	@Test
	public void testBFSNoLadder() {
		String start = "aloud";
		String end = "aloof";
		assertTrue(Main.getWordLadderBFS(start, end).isEmpty());
	}
	
	@Test
	public void testDFSNoLadder() {
		String start = "aloud";
		String end = "aloof";
		assertTrue(Main.getWordLadderDFS(start, end).isEmpty());
	}
	
	@Test
	public void testBFSSmallLadder() {
		String start = "money";
		String end = "honey";
		assertTrue(!Main.getWordLadderBFS(start, end).isEmpty());
	}
	
	@Test
	public void testDFSSmallLadder() {
		String start = "money";
		String end = "honey";
		assertTrue(!Main.getWordLadderDFS(start, end).isEmpty());
	}
	
	@Test
	public void testMediumLadderBFS(){
		String start = "smart";
		String end = "money";
		assertTrue(!Main.getWordLadderBFS(start, end).isEmpty());
	}
	
	@Test
	public void testMediumLadderDFS(){
		String start = "smart";
		String end = "money";
		assertTrue(!Main.getWordLadderDFS(start, end).isEmpty());
	}
	
	@Test
	public void testBFSFlip() {
		String start = "start";
		String end = "money";
		assertEquals(Main.getWordLadderBFS(start, end).size(), Main.getWordLadderBFS(end, start).size());
	}
	
	@Test
	public void testBFSShortest() {
		String start = "smart";
		String end = "money";
		assertTrue(Main.getWordLadderBFS(start, end).size() <= Main.getWordLadderDFS(start, end).size());
	}
	
	@Test
	public void testGetAllMutants() {
		String test = "honey";
		Set<String> wordsToCheck = Main.getAllMutantsOfWord(test, new HashSet<String>(), new int[] {1, 1, 1, 1, 1});
		for(String word : wordsToCheck) {
			assertTrue(isMutation(word, test));
		}
	}
	
	@Test
	public void testGetNextMutant() {
		String test = "honey";
		String mutant = Main.getNextMutantWord(test, new HashSet<String>(), new int[] {1, 1, 1, 1, 1});
		assertTrue(isMutation(mutant, test));
	}
	
	private boolean isMutation(String check, String original) {
		int numMismatch = 0;
		for (int i = 0; i < check.length(); i++) {
			if(check.charAt(i) != original.charAt(i))
				numMismatch++;
		}
		return numMismatch == 1;
	}
}
