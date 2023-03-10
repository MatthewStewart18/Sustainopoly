import java.util.ArrayList;


public class Player {
	
	private ArrayList<Investment> investments = new ArrayList<Investment>(); 
	private final String playerName;
    private int position;
    private int playerTime;
    private int playerMoney = 1500;
	private final playerID;

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
    
    private void noFunds(int amountNeeded) {
    	System.out.println("you are short $" + amountNeeded);
    }
    
    public void pay(Player receiving, int amount) {
    	receiving.addMoney(amount);
    	addMoney(-amount);
    }
    
    public void move(int numSquares, Board board) {
    	position += numSquares;
    	
    	//passing go
    	if(position >= 25) {
    		System.out.println(playerName + "passed go and collected $200");
    		playerMoney += 200;
    		position %= 25;
    	}
    	System.out.println("You just landed on " + board.getCurrentSquare(this));
    	board.getCurrentSquare(this).doAction(this);
    }
    
    public void moveTo(int toPosition, Board board) {
    	move((25 - position + toPosition) % 25, board);
    }
    
    //adding invest to players investments
    public void investIn(Investment investment) {
    	addMoney(-investment.getPrice());
    	investments.add(investment);
    	
    }
    
    public void listInvestments() {
    	if(investments.isEmpty()) {
    		System.out.println("You have no investments");
    	}
    	for(Investment investment : investments) {
    		System.out.println(investment);
    	}
    }

	  
	  public int getPlayerID() { return playerID; }
	 


}
