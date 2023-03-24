package test;
import static org.junit.jupiter.api.Assertions.*;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JProgressBar;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import sustainopoly.Player;
import sustainopoly.Square;
import sustainopoly.Board;
import sustainopoly.Display;
import sustainopoly.Game;
import junit.framework.TestCase;

public class PlayerTest extends TestCase {

	Player player = new Player("Tester", 1);
	
	@BeforeEach
	protected void setUp() throws Exception {
		
	}

	@Test
	public void testGetName() {
		String expected =  "Tester";
		String actual = player.getName();
		assertEquals(actual,expected);
		System.out.println("Test for getName() completed");
	}

	@Test
	public void testGetPosition() {
		int expected = 0;
		int actual = player.getPosition();
		assertEquals(actual,expected);
		System.out.println("Test for getPosition() completed");
	}

	@Test
	public void testGetMoney() {
		int expected = 1500;
		int actual = player.getMoney();
		assertEquals(actual,expected);
		System.out.println("Test for getMoney() completed");
	}

	@Test
	public void testGetTime() {
		int expected = 40;
		int actual = player.getTime();
		assertEquals(actual,expected);
		System.out.println("Test for getTime() completed");
	}

	@Test
	public void testAddMoney() {
		int add = 50;
		int previous = player.getMoney();
		player.addMoney(add);
		
		boolean pass = false;
		
		if (player.getMoney() > previous) {
			pass = true;
		} else {
			pass = false;
		}
		
		assertTrue(pass);
	}

	@Test
	public void testAddTime() {
		int add = 50;
		int previous = player.getTime();
		player.addTime(add);
		
		boolean pass = false;
		
		if (player.getTime() > previous) {
			pass = true;
		} else {
			pass = false;
		}
		
		assertTrue(pass);
	}

	@Test
	public void testInvestIn() {
		player.investIn(20,5,"Online_Advertisement");
		
		int initial = player.listInvestments().size();
		
		boolean pass = false;
		
		if(player.listInvestments().size() > initial) {
			pass = false;
		} else {
			pass = true;
		}
		
		assertTrue(pass);
	}
	
	@Test
	public void testGetPlayerID() {
		int expected = 1;
		int actual = player.getPlayerID();
		assertEquals(expected,actual);
	}

}

