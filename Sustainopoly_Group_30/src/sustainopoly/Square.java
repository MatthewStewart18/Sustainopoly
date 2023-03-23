package sustainopoly;
import java.awt.*;
import java.io.File;  
import java.io.FileNotFoundException; 
import java.util.Scanner; 
public class Square {
	
	
	private final String NAME;
	private int maxMoney = 300;
	private int maxTime = 0;
	private int money = 0;
	private int time = 0;
	private String devArea = "Website";
	private String info;
	private String[] impact;
	private Color header = Color.PINK;
	private int taskLeader = -2;
	

	
	private int initialTimeInvest = 10;
	private int initialMoneyInvest = 100;
	
	public Square(String name) {
		this.NAME = name;
		Scanner fileReader = null;
		String[] file = new String[9];
		impact = new String[3];
		try {
			File squareFile = new File("squares//" + name + ".txt");
			fileReader = new Scanner(squareFile);
			for (int i = 0; i < 9; i++) {
				file[i] = fileReader.nextLine();
			}
			
			
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
		
		if(fileReader != null) {
			fileReader.close();
		}
		if(devArea.equals("Lobbying")) {
			header = Color.BLUE;
			taskLeader = -1;
		} else if (devArea.equals("Website")){
			header = Color.GREEN;
			taskLeader = -1;
		} else if (devArea.equals("Advertising")){
			header = Color.PINK;
			taskLeader = -1;
		} else if (devArea.equals("Fundraising")){
			header = Color.ORANGE;
			taskLeader = -2;
		} else if (devArea.equals("Chance")){
			header = Color.MAGENTA;
			taskLeader = -2;
		} else if (devArea.equals("Corner")){
			taskLeader = -2;
		}else if(devArea.equals("Wifi")) {
			header = Color.RED;
			taskLeader = -1;
		} else if(devArea.equals("Fundraise Money")) {
			header = Color.RED;
			taskLeader = 100;
		}
		
	}
	
	
	public String getName() {
		return NAME;
	}
	
	public Color getColour() {
		return header;
	}
	
	public String getInfo() {
		String text = "Task Area: "+ this.devArea + "\n"+ this.info;
		if(maxMoney>0) {
			text += "\nMoney: " + this.money + "/" + this.maxMoney;
		}
		if(maxTime > 0) {
			text += "\nTime: " + this.time + "/" + this.maxTime;
		}

		
		return text;
		
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
