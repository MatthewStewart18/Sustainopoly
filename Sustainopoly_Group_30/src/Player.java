import java.util.ArrayList;

public class Player {

	private ArrayList<Investment> investments = new ArrayList<Investment>();
	private final String playerName;
	private int position;
	private int playerTime = 40;
	private int playerMoney = 1500;
	private final int playerID;

	public Player(String name, int playerID) {
		this.playerName = name;
		this.playerID = playerID;
		position = 0;
	}



	public String getName() {
		return playerName;
	}

	public int getPosition() {
		return position;
	}

	public int getMoney() {
		return playerMoney;
	}

	public int getTime() {
		return playerTime;
	}

	public void addMoney(int addMoney) {
		if (playerMoney < -addMoney) {
			noFunds(-addMoney - playerMoney);
		}
		this.playerMoney += addMoney;
	}
	
	public void addTime(int addTime) {
		if (playerMoney < -addTime) {
			noFunds(-addTime - playerMoney);
		}
		this.playerTime += addTime;
	}

	private void noFunds(int amountNeeded) {
		System.out.println("you are short $" + amountNeeded);
	}

	public void pay(Player receiving, int amount) {
		receiving.addMoney(amount);
		addMoney(-amount);
	}

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

	public void moveTo(int toPosition, Board board) {
		move((25 - position + toPosition) % 25, board);
	}

	// adding invest to players investments
	public void investIn(Investment investment) {
		addMoney(-investment.getPrice());
		investments.add(investment);

	}

	public  ArrayList<Investment> listInvestments() {
		
		return investments;
		
	}

	  
	  public int getPlayerID() { return playerID; }
	 

}
