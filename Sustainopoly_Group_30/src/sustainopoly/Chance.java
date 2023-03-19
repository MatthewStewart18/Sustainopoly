package sustainopoly;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Chance {
	
	public static void chanceTime(Board gameBoard, Player[] players, int player) {
		int chance = (int) (Math.random()*8);
		
		switch (chance){
		case 0: moveForeward(gameBoard, players, player); break;
		case 1: toNextWeek(gameBoard, players, player); break;
		case 2: toFundRaiser(gameBoard, players, player); break;
		case 3: toDevArea(gameBoard, players, player); break;
		case 4: loseMoney(gameBoard, players, player); break;
		case 5: loseTime(gameBoard, players, player); break;
		case 6: gainMoney(gameBoard, players, player); break;
		case 7: gainTime(gameBoard, players, player); break;
		}
	}

	private static void moveForeward(Board gameBoard, Player[] players, int player) {
		
		gameBoard.displayMessage(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				gameBoard.removeConfirmationPanel();
				gameBoard.zoomOut();
				players[player].move(3, gameBoard);
			}
			
		}, "Go forward 3 spaces");
	}

	private static void toNextWeek(Board gameBoard, Player[] players, int player) {
		
		gameBoard.displayMessage(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				gameBoard.removeConfirmationPanel();
				gameBoard.zoomOut();
				players[player].moveTo(0, gameBoard);
			}
			
		}, "Advance to new week square");
	}

	private static void toFundRaiser(Board gameBoard, Player[] players, int player) {
		
		gameBoard.displayMessage(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				gameBoard.removeConfirmationPanel();
				gameBoard.zoomOut();
				players[player].moveTo(15, gameBoard);
			}
			
		}, "Advance to Fundraiser square");
	}
	
	private static void toDevArea(Board gameBoard, Player[] players, int player) {
		
		gameBoard.displayMessage(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				gameBoard.removeConfirmationPanel();
				gameBoard.zoomOut();
				players[player].moveTo(10, gameBoard);
			}
			
		}, "Take a break. Advance to developer event and grab a coffee");
	}

	private static void loseMoney(Board gameBoard, Player[] players, int player) {
		
		gameBoard.displayMessage(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				gameBoard.removeConfirmationPanel();
				gameBoard.zoomOut();
				players[player].addMoney(-100);
				gameBoard.setMoney(players[player].getMoney());
			}
			
		}, "Critical server maintenance required. Pay 100 pounds");
	}
	
	private static void loseTime(Board gameBoard, Player[] players, int player) {
		
		
		gameBoard.displayMessage(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				gameBoard.removeConfirmationPanel();
				gameBoard.zoomOut();
				players[player].addTime(-20);
				gameBoard.setTime(players[player].getTime());
			}
			
		}, "You became sick, you lost 20 hours");
	}

	private static void gainMoney(Board gameBoard, Player[] players, int player) {
		
		gameBoard.displayMessage(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				gameBoard.removeConfirmationPanel();
				gameBoard.zoomOut();
				players[player].addMoney(150);
				gameBoard.setMoney(players[player].getMoney());
			}
			
		}, "You have been donated 150 pounds");
		
	}
	
	private static void gainTime(Board gameBoard, Player[] players, int player) {
		
		
		gameBoard.displayMessage(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				gameBoard.removeConfirmationPanel();
				gameBoard.zoomOut();
				players[player].addTime(20);
				gameBoard.setTime(players[player].getTime());
			}
			
		}, "You had some fee time this week, you gained 20 hours");
	}

}
