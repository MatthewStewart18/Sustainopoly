

import java.awt.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


/**
 * Display controls what is displayed on the screen by swapping between showing the 
 * JPanels for the start menu, game board, display settings, rules and end game screen
 * @author Magnus
 *
 */
public class Display extends JFrame{
	
	private Color background, foregroundColour, textColour, borderColour;
	private boolean darkMode = false;
	
	
	private Board3 GB;
	private StartMenu gameStartMenu;
	private DisplayOptions displayOptions;
	Rules rulesPanel;
	
	CardLayout layout;
	
	private boolean fullScreen = false;
	
	private Game game;
	
	/**
	 * constructor for the Display class which creates and adds the start menu, display options and rules,
	 * sets the frame to full screen ,shows the start menu and sets it size to fill the screen
	 */
	public Display()
	{
		
		setTitle("SUSTAINOPOLY");//sets the frame name to SUSTAINOPOLY
		
		layout = new CardLayout();//creates a new cardLayout LayoutManager
		setLayout(layout);//sets the frames panels layout to the cardLayout
		
		displayOptions = new DisplayOptions(this);//creates a new DisplayOptions panel
		gameStartMenu = new StartMenu(this);//creates a new StartMenu panel
		rulesPanel = new Rules(this);//creates a new Rules panel
		
		add(gameStartMenu, "startMenu");//adds and shows the StartMenu to the card layout with the name startMenu
		
		add(displayOptions, "display options");//adds the DisplayOptions to the card layout with the name display options
		
		add(rulesPanel, "rules");//adds the rulesPanel to the card layout with the name rules
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//make the program close when the close window button is pressed
		
		setFullScreen(true);//sets the screen to borderless full screen
		setLocationRelativeTo(null);//sets the screen to start at the top left
		resize();//makes the panels resize to fill the window size
		
		setColours(new Color(56, 163, 165), new Color(87, 204, 153), Color.BLACK, new Color(34, 87, 122), darkMode);//set the colour of the panels
		
		
		this.addComponentListener(new ComponentAdapter() {
			/**
			 * resizes everything in the panels whenever the window size is changed
			 */
			public void componentResized(ComponentEvent componentEvent) {
				resize();
			}
		});
		
		addWindowStateListener(new WindowStateListener() {
			/**
			 * 
			 *resizes everything in the panels whenever the window is set/unset from full screen
			 *
			 */
			@Override
			public void windowStateChanged(WindowEvent e) {
				resize();
			}
		});
	}
	
	/**
	 * if the panel is not null then resizes it so that it fills the window size
	 */
	private void resize() {
		if (GB != null) {
			GB.resize(getBounds().getSize().width, getBounds().getSize().height);//resizes the Board
		}
		if (gameStartMenu != null) {
			gameStartMenu.resize(getBounds().getSize().width, getBounds().getSize().height);//resizes the start menu
		}
	}

	/**
	 * creates a new game controller with the array of players then gets and shows from it the newly created Board then removes the start menu
	 * @param players - the players to be added to the game
	 */
	public void startGame(ArrayList<String[]> players) {
		game = new Game(players, this);//create a new game with the players
		GB = game.getGameBoard();//gets the Board that the game controller created
		add(GB, "board");//adds the Board to the card layout with the name board
		layout.show(this.getContentPane(), "board");//replace the startMenu panel with the board panel
		GB.resize(getBounds().getSize().width, getBounds().getSize().height);//resizes the board panel to fill the window
		GB.changeColours(background, foregroundColour, textColour, borderColour, darkMode);//sets the colour of the board panel
		setFullScreen(fullScreen);//resets the window so it displays the board properly
		remove(gameStartMenu);//removes the startMenu from the layout manager
		gameStartMenu = null;//sets the startMenu to null so it can be freed from memory
	}
	
	/**
	 * sets the window to be borderless full screen or not
	 * @param full - whether the screen should be set to borderless or not
	 */
	public void setFullScreen(boolean full) {
		
		dispose();//removes the window
		setUndecorated(full);//sets whether or not to have no border
		if(full) {//if set to full screen make the window take up the entire screen
			setExtendedState(JFrame.MAXIMIZED_BOTH);
		}
		fullScreen = full;
		pack();//puts the panels in the correct positions
		setVisible(true);//makes the window visible again

	}

	/**
	 * sets the colours of all the panels
	 * @param background - the colour to set the background
	 * @param foreground - the colour to set the foreground
	 * @param text - the colour to set the text
	 * @param border - the colour to set the border
	 * @param dark - whether or not to set the title to the dark or light version
	 */
	public void setColours(Color background, Color foreground, Color text, Color border, boolean dark) {
		
		this.background = background;
		this.foregroundColour = foreground;
		this.textColour = text;
		this.borderColour = border;
		this.darkMode = dark;
		
		if(gameStartMenu != null) {
			gameStartMenu.changeColours(background, foreground, text, border, dark);//sets the start menu colours
		}
		if(GB != null) {
			GB.changeColours(background, foreground, text, border, dark);//sets the board colours
		}
	}
	
	/**
	 * changes the panel shown to be the displayOptions
	 */
	public void openDisplayOptions() {
		layout.show(this.getContentPane(), "display options");
	}
	
	/**
	 * changes the panel shown to be the rules panel
	 */
	public void openRules() {
		layout.show(this.getContentPane(), "rules");
	}
	
	/**
	 * changes the panel shown to be the end screen panel
	 */
	public void openEndScreen() {
		
	}
	
	/**
	 * displays either the start menu or the board depending on which is still instantiated
	 */
	public void returnToPrev() {
		
		if(gameStartMenu != null) {
			layout.show(this.getContentPane(), "startMenu");
		}
		layout.show(this.getContentPane(), "board");
	}
	
	/**
	 * returns the players to the main menu by creating a new startMenu and showing it as well as removing the game controller and Board
	 */
	public void returnToMainMenu() {
		gameStartMenu = new StartMenu(this);//creates a new start menu
		add(gameStartMenu, "startMenu");// adds the start menu to the layout manager
		layout.show(this.getContentPane(), "startMenu");// sets the window to show the new start menu
		setFullScreen(fullScreen);//resets the window so it displays the board properly
		gameStartMenu.changeColours(background, foregroundColour, textColour, borderColour, darkMode);//sets the colour of the start menu panel
		//remove the board from the layout manager then sets the board and game controller to null so it can be freed from memory
		remove(GB);
		GB = null;
		game = null;
	}

	/**
	 * starts the program by instantiating the display class on the AWT event dispatching thread
	 * @param args
	 */
	public static void main (String args[])
	{
		
		SwingUtilities.invokeLater(new Runnable() {

			/**
			 * instantiate the display class to start the program
			 */
	        @Override
	        public void run() {
	        	Display d = new Display();
	        }
	    });
	}
	
}
