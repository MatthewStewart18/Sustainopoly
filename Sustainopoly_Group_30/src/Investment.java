import java.lang.reflect.Array;

public abstract class Investment extends Square {
	private final int price;
	private final int time;
	protected Player partner;
	
	public Investment(String name, int price, int time) {
		super(name);
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
	
	
	public void investOffer(Player currentPlayer) {
		System.out.println("Would you like to invest in " + name);
		String decision = Input.read().toLowerCase();
		
		if(decision.contains("y")) {
			invested(currentPlayer);
		}
	}
	
	public void invested(Player currentPlayer) {
		partner = currentPlayer;
		currentPlayer.investIn(this);
	}


public void doAction(Player currentPlayer) {
	if(currentPlayer == partner);
	else {
		investOffer(currentPlayer);
	}
}
}
