import java.awt.*;
import java.io.File;  
import java.io.FileNotFoundException; 
import java.util.Scanner; 
public class Square {
	
	protected int index;
	protected final String name;
	private int maxMoney = 300;
	private int maxTime = 0;
	private int money = 0;
	private int time = 0;
	private String devArea = "Website";
	private String info;
	private String[] impact;
	private Color header = Color.PINK;
	private int taskLeader = -1;
	
	// website : Govanshare, 
	
	private int initialTimeInvest = 10;
	private int initialMoneyInvest = 100;
	
	public Square(String name) {
		this.name = name;
		
		String[] file = new String[9];
		impact = new String[3];
		try {
			File squareFile = new File("squares//" + name + ".txt");
			Scanner fileReader = new Scanner(squareFile);
			for (int i = 0; i < 9; i++) {
				file[i] = fileReader.nextLine();
			}
			fileReader.close();
			
			this.maxMoney = Integer.parseInt(file[0]);
			this.maxTime = Integer.parseInt(file[1]);
			this.initialMoneyInvest = Integer.parseInt(file[2]);
			this.initialTimeInvest = Integer.parseInt(file[3]);
			this.info = file[4];
			this.impact[0] = file[5];
			this.impact[1] = file[6];
			this.impact[2] = file[7];
			this.devArea = file[8];

		} catch (FileNotFoundException e) {
		}
		
		if(devArea.equals("Lobbying")) {
			header = Color.BLUE;
		} else if (devArea.equals("Website")){
			header = Color.GREEN;
		} else if (devArea.equals("Advertising")){
			header = Color.PINK;
		} else if (devArea.equals("Fundraising")){
			header = Color.ORANGE;
		}
		
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
		return header;
	}
	
	public String getInfo() {
		return "Task Area: "+ this.devArea + "\n"+ this.info + "\nMoney: " + this.money + "/" + this.maxMoney + "\nTime: " + this.time + "/" + this.maxTime;
		
		
	}
	
	public int getMoney() {
		return money;
	}
	public void setMoney(int m) {
		money+=m;
	}
	
	public int getMaxMoney() {
		return maxMoney;
	}
	
	public int getTime() {
		return time;
	}
	
	public int getMaxTime() {
		return maxTime;
	}

	public void setTime(int time) {
		this.time += time;
		
	}
	
	public String getImpact() {
		int num = (int) (Math.random()*3);
		
		return this.impact[num];
	}
	
	public String getDevArea() {
		return devArea;
	}
	
	public int getInitialTimeInvestment() {
		return initialTimeInvest;
		
		
	}
	
	public int getInitialMoneyInvestment() {
		return initialMoneyInvest;
		
		
	}
	
	public void setTaskLeader(int player) {
		this.taskLeader = player;
	}
	
	public int getTaskLeader() {
		return taskLeader;
		
	}

}
