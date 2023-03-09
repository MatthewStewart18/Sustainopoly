import java.awt.*;
public class Square {
	
	protected int index;
	protected final String name;
	
	public Square(String name) {
		this.name = name;
	}
	
	public void doAction(Player currentPlayer) {
	};
	
	public String toString() {
		return name;
	}
	
	public String getName() {
		return name;
	}
	
	public Color getColour() {
		return Color.PINK;
	}
	
	public String getInfo() {
		
		return "blahhhh blahhh blahhhhhhhhhhhhhhhhhhhhhh blahhhhhhhhhhh";
		
	}
	
	public int getMoney() {
		return 300;
	}
	
	public int getMaxMoney() {
		return 2000;
	}
	
	public int getTime() {
		return 20;
	}
	
	public int getMaxTime() {
		return 150;
	}

}
