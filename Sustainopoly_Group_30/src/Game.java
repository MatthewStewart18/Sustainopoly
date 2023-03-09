import java.util.ArrayList;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.awt.*;


public class Game {
	
	private Board gameBoard;
	private Square[] squares;
	private boolean canEndTurn = false;
	
	public Game(ArrayList<String[]> players, Display dis) {
		String[] squareNames = {"New Week", "Temp", "Donation Page","Create Poll", "Server", "Fundraiser- Donate Phones", "Temp", "Donation Page", "Chance", "Create Website", "Developer Event", "Temp" , "Donation Page",  "Chance", "Real World Advertisement", "Fundraiser", "Temp", "Petition Council", "Chance", "Online Advertisement"};
					squares = new Square[20];
		
					for(int i = 0; i < squareNames.length; i++) {
						squares[i] = new Square(squareNames[i]);
					}
		
		gameBoard = new Board(squares, dis, createIcons(players), this);
		
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
		
		gameBoard.displaySquareInfoAndResources(focusedSquare);
		
	}
	
	public int[] rollDice() {
		if(!canEndTurn) {
			int[] dice = {(int) (Math.random()*6+1), (int) (Math.random()*6+1)};
		
		gameBoard.movePlayer(1, dice[0] + dice[1]);
		
		canEndTurn = true;
		return dice;
		}
		else {
			return null;
		}
		
		
		
	}
	
	private void startTurn() {
		canEndTurn = false;
	}
	
	public void endTurn() {
		if(canEndTurn) {
			
			
			startTurn();
		}
	}
	
	public void spendResources(int time, int money) {
		//if(player[curentPlayer].getTime < time){
		gameBoard.displayMessage("You do not have enough time remaining");
		//}
	}

}
