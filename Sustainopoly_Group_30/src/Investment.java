import java.lang.reflect.Array;

public abstract class Investment {
	private final int price;
	private final int time;
	protected Player partner;
	private String name;
	
	public Investment(String name, int price, int time) {
		this.name = name;
		this.price = price;
		this.time = time;
	}
	
	public int getPrice() {
		return price;
	}
	public int getTime() {
		return time;
	}
	public Player getPartner() {
		return partner;
	}
	public void setPartner(Player newPartner) {
		partner = newPartner;
	}
	

	
	public void invested(Player currentPlayer) {
		partner = currentPlayer;
		currentPlayer.investIn(this);
	}


public void doAction(Player currentPlayer) {
	if(currentPlayer == partner);
	else {
		
	}
}
}
