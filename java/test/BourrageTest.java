package test;

import static org.junit.Assert.*;

import org.junit.Test;

import skaipeuh.Bourrage;

public class BourrageTest {

	
	@Test
	public void testBourrageUser_tooLongName() {
		String aBourrer = "abcdefghijklm";
		String bourrer = "abcdefgh";
		
		assertTrue(Bourrage.bourrageUser(aBourrer).equals(bourrer));
	}

	@Test
	public void testBourrageUser_emptyName() {
		String aBourrer = "";
		String bourrer = "        ";
		
		assertTrue(Bourrage.bourrageUser(aBourrer).equals(bourrer));
	}

	@Test
	public void testBourrageMachine() {
		String aBourrer = "91.01.02.03";
		String bourrer = "091.001.002.003";
		
		assertTrue(Bourrage.bourrageMachine(aBourrer).equals(bourrer));
	}
	
	@Test
	public void testLeftBourrage() {
		String aBourrer = "abc";
		String bourrer = "...abc";
		
		assertTrue(Bourrage.leftBourrage(aBourrer, 6, ".").equals(bourrer));
	}
	
	@Test
	public void testLeftBourrage_tooLongName() {
		String aBourrer = "abcdefg";
		String bourrer = "abc";
		
		assertTrue(Bourrage.leftBourrage(aBourrer, 3, ".").equals(bourrer));
	}

	@Test
	public void testRightBourrage() {
		String aBourrer = "abc";
		String bourrer = "abc...";
		
		assertTrue(Bourrage.rightBourrage(aBourrer, 6, ".").equals(bourrer));
	}

	@Test
	public void testRightBourrage_tooLongName() {
		String aBourrer = "abcdefg";
		String bourrer = "abc";
		
		assertTrue(Bourrage.rightBourrage(aBourrer, 3, ".").equals(bourrer));
	}
}
