package test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import sustainopoly.Board;
import sustainopoly.Game;
import sustainopoly.Player;

/**
 * 
 */

/**
 * tests the Game class
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
	 * creates a new game with 3 players for each test 
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
	 * tests that all the players were added and their names are correct
	 */
	@Test
	void testGame() {
		Player[] players = game.getPlayers();
		assertEquals(players[0].getName(), "player1");
		assertEquals(players[1].getName(), "player2");
		assertEquals(players[2].getName(), "player3");
	}

	/**
	 * Test method for {@link Game#getGameBoard()}.
	 * tests that a board was created along side the Game
	 */
	@Test
	void testGetGameBoard() {
		if(game.getGameBoard() != null) {
			assertTrue(game.getGameBoard() instanceof Board);
			
		} else {
			fail("nothing was returned");
		}
		
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
	 * tests that the dice cannot be rolled twice in a row
	 */
	@Test
	void testDiceOnlyRollOnce() {
		game.rollDice();
		assertTrue(game.rollDice()==null);
	}

	/**
	 * Test method for {@link Game#endTurn()}.
	 * tests if the dice can be rolled after the previous turn has been ended
	 */
	@Test
	void testDiceCanRerollAfterEndTurn() {
		game.rollDice();
		assertTrue(game.rollDice()==null);
		game.endTurn();
		int[] dice = game.rollDice();
		assertTrue(dice[0]>0 && dice[0]<=6);
		assertTrue(dice[1]>0 && dice[1]<=6);
	}

	/**
	 * Test method for {@link Game#spendResources(int, int, int)}.
	 * tests if the player loses the right amount of resources when they are spent
	 */
	@Test
	void testSpendResources() {
		Player[] players = game.getPlayers();
		int initialTime = players[0].getTime();
		int initialMoney = players[0].getMoney();
		
		game.spendResources(20, 100, 1);
		
		assertEquals(players[0].getTime(), initialTime -= 20);
		assertEquals(players[0].getMoney(), initialMoney -= 100);
		
		game.spendResources(0, 100, 1);
		
		assertEquals(players[0].getTime(), initialTime );
		assertEquals(players[0].getMoney(), initialMoney -= 100);
		
		game.spendResources(20, 0, 1);
		
		assertEquals(players[0].getTime(), initialTime -= 20);
		assertEquals(players[0].getMoney(), initialMoney );
	}
	
	/**
	 * tests to see if the player gains money from spending time on Fundraiser
	 */
	@Test
	void testGainMoneyFromFundraiser() {
		Player[] players = game.getPlayers();
		int initialTime = players[0].getTime();
		int initialMoney = players[0].getMoney();
		
		game.spendResources(10, 0, 5);
		
		assertEquals(players[0].getTime(), initialTime - 10);
		assertEquals(players[0].getMoney(), initialMoney + 200);
	}
	

	/**
	 * tests if the player is stopped if they try to spend more time than they have
	 */
	@Test
	void testSpendTooMuchTime() {
		Player[] players = game.getPlayers();
		int initialTime = players[0].getTime();
		int initialMoney = players[0].getMoney();
		
		game.spendResources(200, 100, 1);
		
		assertEquals(players[0].getTime(), initialTime);
		assertEquals(players[0].getMoney(), initialMoney);
	}
	
	/**
	 * tests if the player is stopped if they try to spend more money than they have
	 */
	@Test
	void testSpendToMuchMoney() {
		Player[] players = game.getPlayers();
		int initialTime = players[0].getTime();
		int initialMoney = players[0].getMoney();
		
		game.spendResources(20, 10000, 1);
		
		assertEquals(players[0].getTime(), initialTime);
		assertEquals(players[0].getMoney(), initialMoney);
	}
	
	/**
	 * tests to see if the players moved by the sum on the dice
	 */
	@Test
	void testPlayerMovesAfterDiceMove() {
		int[] dice = game.rollDice();
		int movement = dice[0] + dice[1];
		
		Player[] players = game.getPlayers();
		
		assertEquals(players[0].getPosition(), movement);
	}
	
	/**
	 * tests to see if the next player will move on their turn
	 */
	@Test
	void testNextPlayerMovesAfterEndTurn() {
		game.rollDice();
		game.endTurn();
		int[] dice = game.rollDice();
		int movement = dice[0] + dice[1];
		
		Player[] players = game.getPlayers();
		
		assertEquals(players[1].getPosition(), movement);
	}

}
