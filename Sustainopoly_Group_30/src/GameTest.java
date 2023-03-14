import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * 
 */

/**
 * @author Magnus
 *
 */
class GameTest {

	Game game;
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		ArrayList<String[]> players = new ArrayList<String[]>();
		String[] p1 = {"player1", "Red", "Bike"};
		String[] p2 = {"player2", "Orange", "Bus"};
		String[] p3 = {"player3", "Green", "Helicopter"};
		players.add(p1);
		players.add(p2);
		players.add(p3);
		game = new Game(players, null);
	}

	/**
	 * Test method for {@link Game#Game(java.util.ArrayList, Display)}.
	 */
	@Test
	void testGame() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link Game#getGameBoard()}.
	 */
	@Test
	void testGetGameBoard() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link Game#displaySquareInfo(int)}.
	 */
	@Test
	void testDisplaySquareInfo() {
		fail("Not yet implemented");
	}

	/**
	 * tests that the dice returned are 2 numbers from 1 and 6
	 * Test method for {@link Game#rollDice()}.
	 */
	@Test
	void testRollDiceNum1To6() {
		int[] dice = game.rollDice();
		assertTrue(dice[0]>0 && dice[0]<=6);
		assertTrue(dice[1]>0 && dice[1]<=6);
	}
	
	/**
	 * Test method for {@link Game#rollDice()}.
	 */
	@Test
	void testRollDiceRandom() {
		int[] dice = game.rollDice();
		assertTrue(dice[0]>0 && dice[0]<=6);
		assertTrue(dice[1]>0 && dice[1]<=6);
	}

	/**
	 * Test method for {@link Game#endTurn()}.
	 */
	@Test
	void testEndTurn() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link Game#spendResources(int, int, int)}.
	 */
	@Test
	void testSpendResources() {
		fail("Not yet implemented");
	}

}
