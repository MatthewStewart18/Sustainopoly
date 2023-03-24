package sustainopoly;

import java.util.ArrayList;

public class Player {

	private ArrayList<Investment> investments = new ArrayList<Investment>();
	private final String playerName;
	private int position;
	private int playerTime = 40;
	private int playerMoney = 1500;
	private final int playerID;

	/**
	 * constructor
	 * @param name
	 * @param playerID
	 */
	public Player(String name, int playerID) {
		this.playerName = name;
		this.playerID = playerID;
		position = 0;
	}


	/**
	 * method to return playerName
	 * @return
	 */
	public String getName() {
		return playerName;
	}
	
	/**
	 * method to return position
	 * @return
	 */
	public int getPosition() {
		return position;
	}

	/**
	 * method to return money
	 * @return
	 */
	public int getMoney() {
		return playerMoney;
	}

	/**
	 * method to return time
	 * @return
	 */
	public int getTime() {
		return playerTime;
	}

	/**
	 * method to add money to a player's account
	 * @param addMoney
	 */
	public void addMoney(int addMoney) {
		if (playerMoney < -addMoney) {
			noFunds(-addMoney - playerMoney);
		}
		this.playerMoney += addMoney;
	}
	
	/**
	 * method to add time to a player's account
	 * @param addTime
	 */
	public void addTime(int addTime) {
		if (playerMoney < -addTime) {
			noFunds(-addTime - playerMoney);
		}
		this.playerTime += addTime;
	}
	
	/**
	 * method to print out 
	 * @param amountNeeded
	 */
	private void noFunds(int amountNeeded) {
		System.out.println("you are short $" + amountNeeded);
	}

	/**
	 * method to pay
	 * @param receiving
	 * @param amount
	 */
	public void pay(Player receiving, int amount) {
		receiving.addMoney(amount);
		addMoney(-amount);
	}

	/**
	 * method to move player
	 * @param numSquares
	 * @param board
	 */
	public void move(int numSquares, Board board) {
		position += numSquares;

		// passing go
		if (position >= 20) {
			
			playerMoney -= 100;
			playerTime = 40;
			position %= 20;
			board.setTime(playerTime);
			board.setMoney(playerMoney);
		}

		board.movePlayer(this.playerID, position);
	}

	/**
	 * method to calculate where to move player and to call move
	 * @param toPosition
	 * @param board
	 */
	public void moveTo(int toPosition, Board board) {
		move((25 - position + toPosition) % 25, board);
	}

	/**
	 * method to invest
	 * @param investment
	 */
	// adding invest to players investments
	public void investIn(Investment investment) {
		addMoney(-investment.getPrice());
		investments.add(investment);

	}

	public  ArrayList<Investment> listInvestments() {
		
		return investments;
		
	}

	  /**
	   * returns playerID
	   * @return
	   */
	  public int getPlayerID() { return playerID; }
	 

}
