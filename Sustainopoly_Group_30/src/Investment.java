import java.lang.reflect.Array;

public class Investment {
	private int price;
	private int time;
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
	
	public void addTime(int time) {
		this.time += time;
	}
	
	public void addMoney(int money) {
		this.price += money;
	}

	public String getName() {
		return this.name;
	}
	
}
