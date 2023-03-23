package sustainopoly;

import java.util.ArrayList;

public class Player {

	private ArrayList<Investment> investments = new ArrayList<Investment>();
	private final String PLAYER_NAME;
	private int position;
	private int playerTime = 40;
	private int playerMoney = 1500;
	private final int PLAYER_ID;

	/**
	 * constructor
	 * 
	 * @param name
	 * @param playerID
	 */
	public Player(String name, int playerID) {
		this.PLAYER_NAME = name;
		this.PLAYER_ID = playerID;
		position = 0;
	}

	/**
	 * method to return playerName
	 * 
	 * @return
	 */
	public String getName() {
		return PLAYER_NAME;
	}

	/**
	 * method to return position
	 * 
	 * @return
	 */
	public int getPosition() {
		return position;
	}

	/**
	 * method to return money
	 * 
	 * @return
	 */
	public int getMoney() {
		return playerMoney;
	}

	/**
	 * method to return time
	 * 
	 * @return
	 */
	public int getTime() {
		return playerTime;
	}

	/**
	 * method to add money to a player's account
	 * 
	 * @param addMoney
	 */
	public void addMoney(int addMoney) {
		
		this.playerMoney += addMoney;
	}

	/**
	 * method to add time to a player's account
	 * 
	 * @param addTime
	 */
	public void addTime(int addTime) {
	
		this.playerTime += addTime;
	}



	/**
	 * method to move player
	 * 
	 * @param numSquares
	 * @param board
	 */
	public void move(int numSquares, Board board) {
		position += numSquares;

		// passing go
		if (position >= 20) {

			playerMoney -= 50;
			playerTime = 40;
			position %= 20;
			board.setTime(playerTime);
			board.setMoney(playerMoney);
		}

		board.movePlayer(this.PLAYER_ID, position);
	}

	/**
	 * method to calculate where to move player and to call move
	 * 
	 * @param toPosition
	 * @param board
	 */
	public void moveTo(int toPosition, Board board) {
		move((20 - position + toPosition) % 20, board);
	}

	/**
	 * method to invest
	 * 
	 * @param investment
	 */
	// adding invest to players investments
	public void investIn(int money, int time, String squareName) {
		boolean exists = false;
		for (int i = 0; i < investments.size(); i++) {

			if (investments.get(i).getName().equals(squareName)) {
				investments.get(i).addMoney(money);
				investments.get(i).addTime(time);
				exists = true;
			}
		}
		if (exists == false) {
			investments.add(new Investment(squareName, money, time));
		}

	}

	public ArrayList<Investment> listInvestments() {

		return investments;

	}

	public int getPlayerID() {
		return PLAYER_ID;
	}

}
