import java.util.ArrayList;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.awt.*;


public class Game {
	
	private Board3 gameBoard;
	
	
	public Game(ArrayList<String[]> players, Display dis) {
					Square[] sq = new Square[20];
		
					for(int i = 0; i < 20; i++) {
							sq[i] = new Square();
					}
		
		gameBoard = new Board3(sq, dis, createIcons(players));
		
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
	
	public Board3 getGameBoard() {
		return gameBoard;
	}

}
