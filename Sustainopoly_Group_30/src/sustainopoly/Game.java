package sustainopoly;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class Game {
	
	private Board gameBoard;
	private Square[] squares;
	private boolean canEndTurn = false;
	private boolean startOfTurn = false;
	private Display display;
	private Player[] players;
	private int currentPlayer = 0;
	private CompletionBar bar;
	
	public Game(ArrayList<String[]> players, Display dis) {
		this.display = dis;
		String[] squareNames = {"New Week", "Wireless Community Networks", "Donation Page","Create Poll", "Server", "Fundraiser", "Govan Share", "Donation Page", "Chance", "Create Website", "Developer Event", "Upgrading wireless community network" , "Donation Page",  "Chance", "Real World Advertisement", "Fundraiser", "Fundraiser- Donate Phones", "Petition Council", "Chance", "Online Advertisement"};
					squares = new Square[20];
		
					for(int i = 0; i < squareNames.length; i++) {
						squares[i] = new Square(squareNames[i]);
					}
		int totalNeeded = 0;
		
		for(int i = 0; i < squareNames.length; i++) {
			totalNeeded +=squares[i].getMaxMoney();
			totalNeeded +=squares[i].getMaxTime();
		}
		bar = new CompletionBar(totalNeeded - 80);
		
		gameBoard = new Board(squares, dis, createIcons(players), this, bar.getBar());
		
		this.players = new Player[players.size()];
		for(int i = 0; i < players.size();i++) {
			this.players[i] = new Player(players.get(i)[0], i+1);
		}
		
		
		startTurn();
	}
	
	/**
	 * for every player in the ArrayList, load in their icon and change the colour to the players colour then returns an array of the icons
	 * @param players - array of player arrays with colour at index 1 and icon name at index 2 
	 * @return an array of BufferedImages which contains the coloured icons for all the players
	 */
	private BufferedImage[] createIcons(ArrayList<String[]> players) {
		
		BufferedImage[] icons = new BufferedImage[players.size()];
		
		for(int i = 0; i < players.size(); i++) {
			try {
				icons[i] = (BufferedImage) ImageIO.read(new File("icons//"+players.get(i)[2]+".png"));
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			int RGBColour = 0;
			
			if(players.get(i)[1].equals("Red")) {
				RGBColour = Color.RED.getRGB();
			}else if(players.get(i)[1].equals("Orange")) {
				RGBColour = Color.ORANGE.getRGB();
			}else if(players.get(i)[1].equals("Yellow")) {
				RGBColour = Color.YELLOW.getRGB();
			}else if(players.get(i)[1].equals("Green")) {
				RGBColour = Color.GREEN.getRGB();
			}else if(players.get(i)[1].equals("Blue")) {
				RGBColour = Color.BLUE.getRGB();
			}else if(players.get(i)[1].equals("Magenta")) {
				RGBColour = Color.MAGENTA.getRGB();
			}else if(players.get(i)[1].equals("Pink")) {
				RGBColour = Color.PINK.getRGB();
			}else if(players.get(i)[1].equals("Cyan")) {
				RGBColour = Color.CYAN.getRGB();
			}
			
			for (int x = 0; x < 120; x++) {
		        for (int y = 0; y < 120; y++) {
		        	if(icons[i].getRGB(x,y) != 0) {
		        		icons[i].setRGB(x,y, RGBColour);
		        	}
		        }
		    }
		}
		
		return icons;
		
	}
	
	public Board getGameBoard() {
		return gameBoard;
	}
	
	
	public void displaySquareInfo(int focusedSquare) {
		
		if( startOfTurn == true) {
			if(squares[focusedSquare].getName().equals("Chance")) {
			
				Chance.chanceTime(gameBoard, players, currentPlayer);
			
			} else if(squares[focusedSquare].getName().equals("Donation Page")) {
				int moneyMade = (int) (Math.random()*200+100);
				
				gameBoard.displayMessage(null, "The donation page on the website has made " + moneyMade + " pounds");
				players[currentPlayer].addMoney(moneyMade);
				gameBoard.setMoney(players[currentPlayer].getMoney());
			} else if(squares[focusedSquare].getName().equals("Developer Event")) {
				gameBoard.displayMessage(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						
						gameBoard.removeConfirmationPanel();
						endTurn();
						gameBoard.zoomOut();
						
					}
					
				}, "You are participating in a developer event so you skip your turn");
			}
	
			startOfTurn = false;
			
		}
		
		
		if (players[currentPlayer].getPosition() == focusedSquare) {
			if(squares[focusedSquare].getTaskLeader() >= 0) {
				gameBoard.displaySquareInfoAndResources(focusedSquare);
			}
			else if (squares[focusedSquare].getTaskLeader() == -1) {
				gameBoard.displaySquareInfo(focusedSquare);

				int initMoney = squares[focusedSquare].getInitialMoneyInvestment();
				int initTime = squares[focusedSquare].getInitialTimeInvestment();
				String message = null;
				if (initMoney > 0) {
					if (initTime > 0) {
						message = " to become task leader and start this task you must spend " + initTime + " hours and " + initMoney + " pounds";
					} else {
						message = " to become task leader and start this task you must spend " + initMoney + " pounds";
					}
				} else if (initTime > 0) {
					message = " to become task leader and start this task you must spend " + initTime + " hours";
				}
				makeNewTaskLeader(currentPlayer, players.length,  focusedSquare,  message);
			} else {
				gameBoard.displaySquareInfo(focusedSquare);
			}
		} else if(squares[focusedSquare].getTaskLeader() == currentPlayer) {
			gameBoard.displaySquareInfoAndResources(focusedSquare);
		} else {
			gameBoard.displaySquareInfo(focusedSquare);
		}
		
		gameBoard.repaint();
		gameBoard.revalidate();
		
	}
	
	private void makeNewTaskLeader(int player, int playersLeft, int squareNum, String message) {
		
		if(playersLeft == 0) return;
		int initMoney = squares[squareNum].getInitialMoneyInvestment();
		int initTime = squares[squareNum].getInitialTimeInvestment();
		ActionListener no = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				gameBoard.removeConfirmationPanel();
				if(player == players.length-1) makeNewTaskLeader(0, playersLeft-1,  squareNum,  message);
				else makeNewTaskLeader(player + 1, playersLeft-1,  squareNum,  message);
				gameBoard.zoomOut();
				
			}
			
		};

		if (players[player].getMoney() >= initMoney) {

			if (players[player].getTime() >= initTime) {
				
				gameBoard.getConfirmation(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						players[player].addMoney(-initMoney);
						gameBoard.setMoney(players[player].getMoney());
						players[player].addTime(-initTime);
						gameBoard.setTime(players[player].getTime());
						gameBoard.removeConfirmationPanel();
						squares[squareNum].setTaskLeader(player);
						gameBoard.zoomOut();
					}

				}, no, players[player].getName() + message);
			} else {
				gameBoard.displayMessage(no, players[player].getName() + " does not have enough time to start this task");
			}

		} else {
			gameBoard.displayMessage(no, players[player].getName() + " does not have enough money to start this task");
		}

	}
	
	public int[] rollDice() {
		if(!canEndTurn) {
			int[] dice = {(int) (Math.random()*6+1), (int) (Math.random()*6+1)};
			
		players[currentPlayer].move(dice[0] + dice[1], gameBoard);
		
		canEndTurn = true;
		startOfTurn = true;
		return dice;
		}
		else {
			return null;
		}
		
		
		
	}
	
	private void startTurn() {
		canEndTurn = false;
		
		gameBoard.setPlayer(players[currentPlayer].getName());
		gameBoard.setMoney(players[currentPlayer].getMoney());
		gameBoard.setTime(players[currentPlayer].getTime());
	}
	
	public void endTurn() {
		if(canEndTurn) {
			if(players[currentPlayer].getMoney() < 0) {
				display.openEndScreen(false, players);
			}
			
			if(bar.isCompleted()) {
				display.openEndScreen(true, players);
			}
			
			currentPlayer++;
			if(currentPlayer >= players.length) {
				currentPlayer = 0;
			}
			
			startTurn();
		}
	}
	
	public void spendResources(int time, int money, int square) {
		if (players[currentPlayer].getTime() < time) {
			gameBoard.displayMessage(null, "You do not have enough time remaining");

		} else if (players[currentPlayer].getMoney() < money) {
			gameBoard.displayMessage(null, "You do not have enough money remaining");

		} else if(squares[square].getName().equals("Fundraiser")){
			int moneyEarned = time*20;
			gameBoard.displayMessage(null, "You have earned " + moneyEarned + " pounds");
			
			players[currentPlayer].addMoney(moneyEarned);
			gameBoard.setMoney(players[currentPlayer].getMoney());
			
			players[currentPlayer].addTime(-time);
			gameBoard.setTime(players[currentPlayer].getTime());
			
		}else {
			
			if(money > 0 && time > 0) {
				squares[square].setMoney(money);
				players[currentPlayer].addMoney(-money);
				gameBoard.setMoney(players[currentPlayer].getMoney());
				squares[square].setTime(time);
				players[currentPlayer].addTime(-time);
				gameBoard.setTime(players[currentPlayer].getTime());
				gameBoard.displayMessage(null, "You have invested "+ money +" pounds and " + time +" hours in " + squares[square].getName() + squares[square].getImpact());
			}
			else if(money > 0) {
				squares[square].setMoney(money);
				players[currentPlayer].addMoney(-money);
				gameBoard.setMoney(players[currentPlayer].getMoney());
				gameBoard.displayMessage(null, "You have invested "+ money +" pounds in " + squares[square].getName() + squares[square].getImpact());
			}
			
			else if(time > 0) {
				squares[square].setTime(time);
				players[currentPlayer].addTime(-time);
				gameBoard.setTime(players[currentPlayer].getTime());
				gameBoard.displayMessage(null, "You have invested "  + time +" hours in " + squares[square].getName() + squares[square].getImpact());
			}
			
			gameBoard.zoomOut();
			players[currentPlayer].investIn(money, time, squares[square].getName());
			
			
			bar.progress(money + time); 
		}
		
		boolean devAreaFinished = true;
		String devArea = squares[square].getDevArea();
		
		for(int i = 0; i< squares.length; i++) {
			if(squares[i].getDevArea().equals(devArea)) {
				if(squares[i].getTime()< squares[i].getMaxTime() || squares[i].getMoney() < squares[i].getMaxMoney()) {
					devAreaFinished = false;
					break;
				}
			}
		}
		
		if(devAreaFinished) {
			gameBoard.removeConfirmationPanel();
			String message = null;
			if(devArea.equals("Lobbying")) {
				message = "You have completed Lobbying the council task area, Govan will now have better public transport";
			}
			gameBoard.displayMessage(null, message);
		}
		
	}
	
	public Player[] getPlayers() {
		return  players;
	}
}
