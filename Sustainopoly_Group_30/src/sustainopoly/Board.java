package sustainopoly;
import java.awt.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.*;

import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.TimerTask;
import java.util.Timer;

/**
 * The Board class controls the GUI of the game board
 * It shows the board with the squares and the overlay containing the static elements(player info, menu etc) on a panel layered over the board
 * Board give the interface for spending resources
 * It detects when buttons on the screen are pressed and performs the desired task and calls methods in the Game class
 * @author Magnus
 *
 */
public class Board extends JLayeredPane implements MouseListener {

	//the colours of the board and whether it is set to dark mode
	private Color background, foregroundColour, textColour, borderColour;
	private boolean darkMode = false;

	//the buttons displayed on the board
	private JButton squareButtons[], diceButton, endTurn;

	//the images of the dice and the JLabel that displays them
	private JLabel dice;
	private ImageIcon[] dicePics;

	//the GridBagConstraints used to position everything on the board
	private GridBagConstraints squarePanelCon;
	private GridBagConstraints infoCon;

	private JMenu menu;

	//the panels that store and position all the components to be shown
	private JPanel squaresPanel, infoPanel, confirm, message , overlay, animationPanel, backgroundPanel, squareInfo;

	//the values used to scale the board to fit in almost any sized window
	private int squareWidth = 150;
	private int squareHeight = 50;
	private int numOfSquares = 20;
	private int zoom = 3;
	private int boardWidth = 1000;
	private int screenWidth;
	private int screenHeight;

	//keeps track of what rotation the board should be in
	private int currentRotation = 0;

	//the labels showing the current player name, money and time
	private JLabel player, money, time;

	private JLabel boardImage, title, backgroundJLabel;

	//images of the top and bottom dice with each value from 1 to 6
	private BufferedImage[] topDice;
	private BufferedImage[] botDice;
	private BufferedImage backgroundDice;

	private JMenuItem rules, displaySettings, exit;

	private SquarePicker SP = new SquarePicker();

	private Square[] squares;
	private Display display;
	private Game gameController;
	
	
	//stores the actual rotation of the board in degrees
	private double rotation = 0;

	private BufferedImage fullBoard, backgroundPict;
	private Image[] squareAnimationImages;
	private BufferedImage[] titles;

	
	//the booleans ensure events don't overlap
	private boolean buttonsDisabled = false;
	private boolean isZoomed = false;
	private boolean finishedAnimation = false;
	private boolean playerCanMove;
	private boolean messageDisplayed = false;

	//the player positions and their icons
	private int[] playerPositions;
	private BufferedImage[] playerIcons;
	private JProgressBar bar;

	/**
	 * constructor for the board class
	 * sets up the board using the squares, playerIcons and progress bar
	 * sets the number of squares to be the closest multiple of 4 that is under or equal to the number of squares in the squares array
	 * sets the number of players to the number of icons in the playerIcons and sets the positions to 0
	 * adds a menu and overlay in panel over the board and add a background to a panel behind the board and sets the colours
	 * @param squares - the squares on the board
	 * @param display - controls what is being shown on the screen
	 * @param playerIcons - array of buffered images containing the icons for all the players
	 * @param gameController - the class that controls the game logic
	 * @param bar - the bar that displays the game progress
	 */
	public Board(Square[] squares, Display display, BufferedImage[] playerIcons, Game gameController, JProgressBar bar) {
		this.display = display;
		this.squares = squares;
		this.playerIcons = playerIcons;
		this.gameController = gameController;
		this.bar = bar;
		
		playerPositions = new int[playerIcons.length];
		for (int i = 0; i < playerIcons.length; i++)
			playerPositions[i] = 0;

		overlay = new JPanel();

		numOfSquares = squares.length - squares.length % 4;

		squaresPanel = new JPanel() {
			public void paint(Graphics g) {

				Graphics2D g2D = (Graphics2D) g;
				g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

				int wid = getWidth();
				int high = getHeight();

				g2D.rotate(Math.toRadians(rotation), wid / 2, high / 2);

				super.paint(g);

			}
		};

		setUpSquares();
		setUpInfo();

		setPreferredSize(new Dimension(1000, 1000));

		overlay.setOpaque(false);
		squaresPanel.setOpaque(false);
		squaresPanel.setBackground(Color.YELLOW);

		squarePanelCon.gridx = 0;
		squarePanelCon.gridy = 0;
		squarePanelCon.weighty = 1;
		squarePanelCon.weightx = 1;
		setUpDice();
		// Add(sqex, -100);
		squaresPanel.setBounds(0, 0, 1000, 1000);
		overlay.setBounds(000, 000, 100, 100);
		animationPanel.setBounds(200, 200, 1000, 600);
		infoPanel.setBounds(000, 000, 100, 100);
		add(squaresPanel, 1, 1);
		add(overlay, 5, 5);
		add(animationPanel, 3, 3);
		add(infoPanel, 4, 4);

		squarePanelCon.gridx = 3;
		squarePanelCon.anchor = GridBagConstraints.NORTH;
		// add(infoPanel, squarePanelCon);
		squarePanelCon.anchor = GridBagConstraints.CENTER;

		setUpMenu();

		backgroundPanel = new JPanel();
		backgroundPanel.setLayout(new BoxLayout(backgroundPanel, BoxLayout.Y_AXIS));
		add(backgroundPanel, 0, 0);
		backgroundPanel.setBounds(0, 0, 1000, 1000);
		backgroundPanel.setOpaque(false);
		backgroundJLabel = new JLabel();
		backgroundPanel.add(backgroundJLabel);
		try {
			backgroundPict = (BufferedImage) ImageIO.read(new File("titles//govanmap.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		changeColours(new Color(56, 163, 165), new Color(87, 204, 153), Color.BLACK, new Color(34, 87, 122), darkMode);

		// for(int i = 0; i < numOfSquares; i++) {
		// squareButtons[i].setEnabled(false);
		// }

	}

	/**
	 * resizes the components on screen relative to the screenWidth and screenHeight
	 * if the board is zoomed in it makes it zoom out
	 * @param screenWidth - the width of the window
	 * @param screenHeight - the height of the window
	 */
	public void resize(int x, int y) {

		
		setBounds(0, 0, x, y);
		squaresPanel.setBounds(0, 0, x, y);
		overlay.setBounds(0, 0, x, y);
		animationPanel.setBounds(0, 0, x, y);
		backgroundPanel.setBounds(0, 0, x, y);
		infoPanel.setBounds(000, 000, x, y);
		screenWidth = x;
		screenHeight = y;

		//finds the minimum between the width and the height and set the board width to be 90 percent of it
		if (screenWidth >= screenHeight) {
			boardWidth = screenHeight - screenHeight / 10;
		} else {
			boardWidth = screenWidth - screenWidth / 10;
		}

		squareWidth = boardWidth / numOfSquares * 3;
		squareHeight = boardWidth / numOfSquares;
		//call change colour to make everything reset itself to fit the new screen size
		changeColours(background, foregroundColour, textColour, borderColour, darkMode);

		zoomOut();

	}
	
	/**
	 * makes the board zoom out if it is not already zoomed out
	 */
	public void zoomOut() {
		if (isZoomed) {
			isZoomed = false;
			Timer zoomOut = new Timer();
			zoomOut.scheduleAtFixedRate(new BoardZoom(false), 0, 10);
			infoPanel.remove(squareInfo);

		}
	}

	/**
	 * creates the menu's components with action listeners to perform actions when pressed and add them to the overlay panel which goes over the board
	 * the menu contains Rules which shows the rules panel, displaySettings which shows the displaySettings panel and exit which exits the game
	 */
	private void setUpMenu() {
		overlay.setLayout(new GridBagLayout());

		//creates the menu
		menu = new JMenu("MENU");
		
		//creates the rules menu option and adds it to the menu
		rules = new JMenuItem("Rules");
		rules.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				display.openRules();
				
			}
		});
		rules.setFont(new Font("Arial", Font.PLAIN, 40));
		menu.add(rules);

		//creates the display settings menu option and adds it to the menu
		displaySettings = new JMenuItem("DisplaySettings");
		displaySettings.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				display.openDisplayOptions();
			}
		});
		displaySettings.setFont(new Font("Arial", Font.PLAIN, 40));
		menu.add(displaySettings);

		//creates the exit menu option and adds it to the menu
		exit = new JMenuItem("Exit");
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getConfirmation(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						display.returnToMainMenu();
						removeConfirmationPanel();
					}
				},null, "Are you sure you want to exit\nYou will lose all your progress");
			}
		});
		exit.setFont(new Font("Arial", Font.PLAIN, 40));
		menu.add(exit);
		menu.setFont(new Font("Arial", Font.PLAIN, 40));
		menu.setOpaque(true);
		menu.getPopupMenu().setOpaque(false);
		menu.getPopupMenu().setBorderPainted(false);

		JMenuBar menuBar = new JMenuBar();
		menuBar.add(menu);
		menuBar.setBackground(Color.GREEN);
		menuBar.setBorderPainted(false);
		;
		menuBar.setOpaque(false);

		//adds the menu bar to the top right corner of the screen
		squarePanelCon.anchor = GridBagConstraints.NORTHEAST;
		squarePanelCon.gridx = 1;
		squarePanelCon.gridy = 0;
		overlay.add(menuBar, squarePanelCon);
		squarePanelCon.anchor = GridBagConstraints.CENTER;
	}

	/**
	 * displays a box containing the message text, a yes button, and a no button
	 * the yes and no buttons will run the code in the ActionListeners
	 * the ActionListeners must call the removeConfirmationPanel method at the end
	 * @param outputYes - what happens if the player presses yes
	 * @param outputNo - what happens if the player presses no
	 * @param messageText - the message to be displayed
	 */
	public void getConfirmation(ActionListener outputYes, ActionListener outputNo, String messageText) {

		removeConfirmationPanel();
		messageDisplayed = true;
		disableButtons(true);

		confirm = new JPanel() {
			public void paint(Graphics g) {
		
			Graphics2D sqImGraphics = (Graphics2D) g;
			setMinimumSize(new Dimension(screenWidth/3, screenHeight/2));
			setPreferredSize(new Dimension(screenWidth/3, screenHeight/2));
			
			//draw a border around the panel with rounded corners
			sqImGraphics.setColor(background);
			sqImGraphics.fillRoundRect(5,5, this.getWidth()-10, this.getHeight()-10, 30,30);
			sqImGraphics.setStroke(new BasicStroke(10));
			sqImGraphics.setColor(borderColour);
			sqImGraphics.drawRoundRect(5,5, this.getWidth()-10, this.getHeight()-10, 30,30);
			
			super.paint(g);
		}};
		SpringLayout sprLayout = new SpringLayout();
		confirm.setLayout(sprLayout);
		confirm.setBackground(background);
		

		

		JTextPane message = new JTextPane();
		SimpleAttributeSet centre = new SimpleAttributeSet();
		StyleConstants.setAlignment(centre, StyleConstants.ALIGN_CENTER);
		message.setParagraphAttributes(centre, false);
		message.setEditable(false);
		message.setForeground(textColour);
		message.setBackground(foregroundColour);
		message.setFont(new Font("Arial", Font.PLAIN, screenHeight/30));
		message.setPreferredSize(new Dimension(400, 400));
		message.setText("\n\n" + messageText);
		
		confirm.add(message);

		JButton yes = new JButton("yes");
		yes.addActionListener(outputYes);
		yes.setFont(new Font("Arial", Font.PLAIN, 30));
		yes.setBackground(foregroundColour);
		yes.setForeground(textColour);

		JButton no = new JButton("no");
		if(outputNo == null) {
			no.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				removeConfirmationPanel();
			}
		});
		}else {
			no.addActionListener(outputNo);
		}
		
		no.setFont(new Font("Arial", Font.PLAIN, 30));
		no.setBackground(foregroundColour);
		no.setForeground(textColour);

		confirm.add(no);

		confirm.add(yes);

		confirm.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, borderColour));

		sprLayout.putConstraint(SpringLayout.NORTH, message, 5, SpringLayout.NORTH, confirm);
		sprLayout.putConstraint(SpringLayout.WEST, message, 5, SpringLayout.WEST, confirm);
		sprLayout.putConstraint(SpringLayout.EAST, confirm, 5, SpringLayout.EAST, message);
		sprLayout.putConstraint(SpringLayout.NORTH, no, 5, SpringLayout.SOUTH, message);
		sprLayout.putConstraint(SpringLayout.WEST, no, 5, SpringLayout.WEST, message);
		sprLayout.putConstraint(SpringLayout.SOUTH, confirm, 5, SpringLayout.SOUTH, no);
		sprLayout.putConstraint(SpringLayout.EAST, yes, -5, SpringLayout.EAST, message);
		sprLayout.putConstraint(SpringLayout.NORTH, yes, 5, SpringLayout.SOUTH, message);

		squarePanelCon.fill = GridBagConstraints.NONE;
		squarePanelCon.anchor = GridBagConstraints.CENTER;
		squarePanelCon.gridwidth = 5;
		squarePanelCon.gridheight = 5;
		squarePanelCon.gridx = 0;
		squarePanelCon.gridy = 0;
		overlay.add(confirm, squarePanelCon);

		

		repaint();
		revalidate();
	}

	/**
	 * if either the confirmation or message panels are displayed this will remove them
	 */
	public void removeConfirmationPanel() {
		disableButtons(false);
		messageDisplayed = false;
		if(confirm != null)overlay.remove(confirm);
		if(message != null)overlay.remove(message);
		repaint();
		revalidate();
	}
	
	/**
	 * displays a box containing the message text and a continue button
	 * the continue will run the code in the ActionListener
	 * the ActionListener must call the removeConfirmationPanel method at the end
	 * if outputContinue is null, pressing continue will only remove the message
	 * @param outputContinue - what happens when the continue button is pressed
	 * @param messageText - the message to be displayed
	 */
	public void displayMessage(ActionListener outputContinue, String messageText) {
		removeConfirmationPanel();
		disableButtons(true);
		messageDisplayed = true;
		
		message = new JPanel() {
			public void paint(Graphics g) {
		
			Graphics2D sqImGraphics = (Graphics2D) g;
			setMinimumSize(new Dimension(screenWidth/3, screenHeight/2));
			setPreferredSize(new Dimension(screenWidth/3, screenHeight/2));
			setFont(new Font("Arial", Font.PLAIN, screenHeight/70));
			
			//draw a border around the panel with rounded corners
			sqImGraphics.setColor(background);
			sqImGraphics.fillRoundRect(5,5, this.getWidth()-10, this.getHeight()-10, 30,30);
			sqImGraphics.setStroke(new BasicStroke(10));
			sqImGraphics.setColor(borderColour);
			sqImGraphics.drawRoundRect(5,5, this.getWidth()-10, this.getHeight()-10, 30,30);
			
			super.paint(g);
		}};
		message.setOpaque(false);
		
		//use spring layout manager to position the components
		SpringLayout sprLayout = new SpringLayout();
		message.setLayout(sprLayout);

		//create the text pane which holds the message
		JTextPane text = new JTextPane();
		SimpleAttributeSet centre = new SimpleAttributeSet();
		StyleConstants.setAlignment(centre, StyleConstants.ALIGN_CENTER);
		text.setParagraphAttributes(centre, false);
		text.setEditable(false);
		text.setForeground(textColour);
		text.setBackground(foregroundColour);
		text.setFont(new Font("Arial", Font.PLAIN, screenHeight/30));
		text.setPreferredSize(new Dimension(screenWidth/4, screenHeight/4));
		text.setText( messageText);
		
		
		//create the continue button
		JButton ok = new JButton("Continue");
		//if the action listener is null then use a default action listener that only removes the message panel when pressed
		if (outputContinue == null) {
			ok.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					removeConfirmationPanel();
					playerCanMove = true;
					repaint();
					revalidate();
				}

			});
		} else {
			ok.addActionListener(outputContinue);
		}
		
		ok.setFont(new Font("Arial", Font.PLAIN, 30));
		ok.setBackground(foregroundColour);
		ok.setForeground(textColour);
		ok.setFocusPainted(false);
		
		message.add(text);
		message.add(ok);
		
		//sets the position of the components relative to each other and the sides of the panel
		sprLayout.putConstraint(SpringLayout.NORTH, text, screenWidth/100, SpringLayout.NORTH, message);
		sprLayout.putConstraint(SpringLayout.WEST, text, screenWidth/100, SpringLayout.WEST, message);
		sprLayout.putConstraint(SpringLayout.EAST, message, screenWidth/100, SpringLayout.EAST, text);
		sprLayout.putConstraint(SpringLayout.NORTH, ok, screenWidth/100, SpringLayout.SOUTH, text);
		sprLayout.putConstraint(SpringLayout.SOUTH, message, screenWidth/100, SpringLayout.SOUTH, ok);
		sprLayout.putConstraint(SpringLayout.EAST, ok, -screenWidth/100, SpringLayout.EAST, text);
		
		//adds the message to the centre of the overlay panel
		squarePanelCon.fill = GridBagConstraints.NONE;
		squarePanelCon.anchor = GridBagConstraints.CENTER;
		squarePanelCon.gridwidth = 5;
		squarePanelCon.gridheight = 5;
		squarePanelCon.gridx = 0;
		squarePanelCon.gridy = 0;
		overlay.add(message, squarePanelCon);
		repaint();
		revalidate();
	}

	/**
	 * sets buttonsDisabled to true to disable the buttons, or false to enable the buttons
	 * disables the menu buttons
	 * @param disable - whether the buttons should be disabled or enabled
	 */
	public void disableButtons(boolean disable) {

		buttonsDisabled = disable;
		menu.setEnabled(!disable);

	}

	/**
	 * sets up the JPanel that shows the squares by adding JButtons for all the squares to it and adding the title in the centre
	 */
	private void setUpSquares() {

		squaresPanel.addMouseListener(this);

		squaresPanel.setLayout(new GridBagLayout());
		squarePanelCon = new GridBagConstraints();
		squaresPanel.setOpaque(true);
		squaresPanel.setBackground(Color.YELLOW);

		squareButtons = new JButton[numOfSquares];

		squarePanelCon.gridx = 1;
		squarePanelCon.gridy = 1;

		//adds all the buttons of the squares to the squares panel
		for (int i = 0; i < numOfSquares / 4; i++) {

			squareButtons[i] = new JButton() {
				
			};
			squareButtons[i].addActionListener(SP);
			squaresPanel.add(squareButtons[i], squarePanelCon);
			squarePanelCon.gridx++;

		}

		for (int i = (numOfSquares / 4); i < numOfSquares / 2; i++) {

			squareButtons[i] = new JButton() {
				
			};
			squareButtons[i].addActionListener(SP);
			squaresPanel.add(squareButtons[i], squarePanelCon);
			squarePanelCon.gridy++;

		}

		for (int i = (numOfSquares / 2); i < numOfSquares / 4 * 3; i++) {

			squareButtons[i] = new JButton() {
				
			};
			squareButtons[i].addActionListener(SP);
			squaresPanel.add(squareButtons[i], squarePanelCon);
			squarePanelCon.gridx--;
		}

		for (int i = (numOfSquares / 4 * 3); i < numOfSquares; i++) {

			squareButtons[i] = new JButton() {
				
			};
			squareButtons[i].addActionListener(SP);
			squaresPanel.add(squareButtons[i], squarePanelCon);
			squarePanelCon.gridy--;

		}
		
		for (int i = 0; i < numOfSquares; i++) {

			squareButtons[i].setMargin(new Insets(-3, -3, -3, -3));
			squareButtons[i].setBorderPainted(false);
			squareButtons[i].setOpaque(false);
			squareButtons[i].setFocusPainted(false);
		}

		squarePanelCon.gridx = 0;
		squarePanelCon.gridy = 0;
		squarePanelCon.gridwidth = numOfSquares / 4 + 3;
		squarePanelCon.gridheight = 1;

		//add spacers to the board to help position the squares
		squaresPanel.add(new JLabel(), squarePanelCon);
		squarePanelCon.gridy = numOfSquares / 4 + 2;
		squaresPanel.add(new JLabel(), squarePanelCon);
		squarePanelCon.gridwidth = 1;
		squarePanelCon.gridheight = numOfSquares / 4 + 3;
		squarePanelCon.gridy = 0;
		squaresPanel.add(new JLabel(), squarePanelCon);
		squarePanelCon.gridx = numOfSquares / 4 + 2;
		squaresPanel.add(new JLabel(), squarePanelCon);

		squarePanelCon.weightx = 0;
		squarePanelCon.weighty = 0;
		squarePanelCon.gridx = 2;
		squarePanelCon.gridy = 2;
		squarePanelCon.gridwidth = numOfSquares / 4 - 1;
		squarePanelCon.gridheight = numOfSquares / 4 - 1;

		//adds the title to the centre of the screen
		title = new JLabel();
		squaresPanel.add(title, squarePanelCon);
		titles = new BufferedImage[2];

		try {
			titles[0] = (BufferedImage) ImageIO.read(new File("titles//sustainopolyTitleLight1.png"));
			titles[1] = (BufferedImage) ImageIO.read(new File("titles//sustainopolyTitleDark1.png"));
		} catch (IOException e) {
		}

	}

	/**
	 * sets up the dice by reading in all the images for the dice a JPanel for the dice rolling animation, 
	 * adding a button to it that starts the dice rolling animation
	 * sets the button to look like 2 dice
	 */
	private void setUpDice() {

		dicePics = new ImageIcon[60];

		for (int i = 1; i <= 59; i++) {
			dicePics[i - 1] = new ImageIcon("dice//dice (" + i + ").png");
		}

		dice = new JLabel();

		topDice = new BufferedImage[6];
		botDice = new BufferedImage[6];

		for (int i = 1; i <= 6; i++) {

			try {
				topDice[i - 1] = ImageIO.read(new File("dice//dicetop" + i + ".png"));
				botDice[i - 1] = ImageIO.read(new File("dice//dicebot" + i + ".png"));
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}

		animationPanel = new JPanel();
		animationPanel.setOpaque(false);

		squarePanelCon.gridwidth = 2;
		squarePanelCon.gridheight = 1;
		squarePanelCon.gridx = 1;
		squarePanelCon.gridy = 1;

		squarePanelCon.anchor = GridBagConstraints.EAST;

		animationPanel.setLayout(new GridBagLayout());

		animationPanel.add(dice, squarePanelCon);

		diceButton = new JButton() {
			public void repaint() {

			}
		};
		diceButton.setOpaque(false);
		diceButton.setBackground(Color.BLUE);
		diceButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//starts the dice rolling animation only if the dice returned are not null which means it is possible to roll the dice 
				int[] finalDice = gameController.rollDice();
				Timer rollDice = new Timer();
				if(finalDice != null)rollDice.scheduleAtFixedRate(new DiceRoller(finalDice), 0, 40);
				

			}
		});

		Graphics2D sqImGraphics = null;
		backgroundDice = new BufferedImage(220, 140, BufferedImage.TYPE_INT_ARGB);
		sqImGraphics = backgroundDice.createGraphics();

		sqImGraphics.drawImage(botDice[2], null, -310, -410);
		sqImGraphics.drawImage(topDice[5], null, -210, -180);

		sqImGraphics.dispose();
		diceButton.setIcon(new ImageIcon(backgroundDice));

		squarePanelCon.anchor = GridBagConstraints.CENTER;
		squarePanelCon.gridx = 1;
		squarePanelCon.gridy = 1;
		squarePanelCon.gridwidth = 1;
		squarePanelCon.gridheight = 1;
		diceButton.setToolTipText("Click dice to roll");
		diceButton.setBorder(null);

		animationPanel.add(diceButton, squarePanelCon);

		squarePanelCon.ipady = 0;
		squarePanelCon.ipadx = 0;
		squarePanelCon.gridwidth = 1;
		squarePanelCon.gridheight = 1;

	}

	/**
	 * set up the info panel which is a static panel that overlays the board
	 * the player name, money and time is added to the info panel
	 * the progress bar and its label is added to the info panel
	 */
	private void setUpInfo() {
		infoPanel = new JPanel();
		infoPanel.setLayout(new GridBagLayout());
		infoCon = new GridBagConstraints();

		player = new JLabel("Player Name: ");

		infoCon.ipady = 5;
		infoCon.ipadx = 60;
		infoCon.gridx = 1;
		infoCon.gridy = 0;
		infoCon.insets = new Insets(10, 10, 10, 10);
		infoCon.anchor = GridBagConstraints.NORTH;

		player.setOpaque(true);

		infoPanel.add(player, infoCon);

		money = new JLabel(" Money: 0");
		infoCon.gridx = 2;
		money.setOpaque(true);
		infoPanel.add(money, infoCon);

		time = new JLabel("Time: 0 Hours");
		infoCon.gridx = 3;
		time.setOpaque(true);
		infoPanel.add(time, infoCon);

		endTurn = new JButton("EndTurn");
		infoCon.gridx = 3;
		infoCon.gridy = 1;
		infoCon.gridwidth = 2;
		endTurn.setOpaque(true);
		endTurn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(!buttonsDisabled&& !isZoomed) {
					gameController.endTurn();
				}
				
			}

		});
		
		endTurn.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, borderColour));
		endTurn.setBackground(foregroundColour);
		endTurn.setForeground(textColour);
		endTurn.setFont(new Font("Arial", Font.BOLD, screenWidth / 70));
		endTurn.setFocusPainted(false);
		infoCon.insets = new Insets(10, 10, 50, 30);
		infoCon.anchor = GridBagConstraints.SOUTHEAST;
		infoPanel.add(endTurn, infoCon);
		
		infoCon.insets = new Insets(5, 10, 0, 0);
		infoCon.anchor = GridBagConstraints.NORTHWEST;
		infoCon.gridx = 0;
		infoCon.gridy = 1;
		infoPanel.add(bar, infoCon);
		JLabel progressLabel = new JLabel("Progress Bar:") {
			public void paint(Graphics g) {
				setFont(new Font("Arial", Font.BOLD, screenWidth / 70));
				setForeground(textColour);
				setBackground(foregroundColour);
				super.paint(g);
			}
		};
		infoCon.gridy = 0;
		infoPanel.add(progressLabel, infoCon);
		

		JLabel spacer1 = new JLabel();
		JLabel spacer2 = new JLabel();
		JLabel spacer3 = new JLabel();

		infoCon.insets = new Insets(0, 0, 0, 0);
		infoCon.fill = GridBagConstraints.BOTH;
		infoCon.gridheight = 2;
		infoCon.gridwidth = 1;
		infoCon.gridx = 0;
		infoCon.gridy = 0;
		infoCon.weightx = 1;
		infoCon.weighty = 1;

		infoPanel.add(spacer1, infoCon);

		infoCon.gridx = 4;
		infoPanel.add(spacer2, infoCon);

		infoCon.gridheight = 1;
		infoCon.gridwidth = 5;
		infoCon.gridx = 0;
		infoCon.gridy = 1;

		infoPanel.setOpaque(false);

	}

	/**
	 * starts the animation of the selected player moving to the selected position
	 * each movement will have a gap of 200 milliseconds
	 * @param playerNum - the player being moved
	 * @param pos - final position of the player
	 */
	public void movePlayer(int playerNum, int pos) {
		
		Timer move = new Timer();
		move.scheduleAtFixedRate(new playerMover(playerPositions[playerNum - 1], pos, playerNum), 0, 200);


	}

	/**
	 * changes the player name displayed to a new name
	 * @param playerName - the new player name 
	 */
	public void setPlayer(String playerName) {
		player.setText("Player Name: " + playerName);
	}

	/**
	 * changes the amount of money displayed to a new amount
	 * @param money - the new amount of money
	 */
	public void setMoney(int money) {
		this.money.setText("Money: " + money);
	}

	/**
	 * sets the amount of time displayed to a new amount
	 * @param time - the new amount of time
	 */
	public void setTime(int time) {
		this.time.setText("Time: " + time + " Hours");
	}

	/**
	 * Displays the information about the square, it:
	 * creates a JPanel with a rounded border, sets the name of the square at the top and adds a JScrollPane containing a text description of the square
	 * @param squareNum - the number of the square who's information is to be shown
	 */
	public void displaySquareInfo(int squareNum) {

		infoCon.fill = GridBagConstraints.NONE;
		infoCon.gridx = 0;
		infoCon.gridy = 1;
		infoCon.weightx = 0;
		infoCon.weighty = 0;
		infoCon.gridwidth = 5;
		infoCon.anchor = GridBagConstraints.EAST;
		infoCon.insets = new Insets(10, 10, 10, 10);

		squareInfo = new JPanel() {

			public void paint(Graphics g) {

				Graphics2D sqImGraphics = (Graphics2D) g;
				sqImGraphics.setColor(foregroundColour);
				sqImGraphics.fillRoundRect(5, 5, this.getWidth() - 10, this.getHeight() - 10, 30, 30);
				sqImGraphics.setStroke(new BasicStroke(10));
				sqImGraphics.setColor(borderColour);
				sqImGraphics.drawRoundRect(5, 5, this.getWidth() - 10, this.getHeight() - 10, 30, 30);

				super.paint(g);
			}

		};
		squareInfo.setPreferredSize(new Dimension((int) (screenWidth / 3.5), screenHeight - screenHeight / 4));
		squareInfo.setOpaque(false);
		squareInfo.setVisible(false);

		infoPanel.add(squareInfo, infoCon);

		squareInfo.setLayout(new GridBagLayout());

		JTextPane title = new JTextPane();

		SimpleAttributeSet centre = new SimpleAttributeSet();
		StyleConstants.setAlignment(centre, StyleConstants.ALIGN_CENTER);
		title.setParagraphAttributes(centre, false);

		title.setFont(new Font("Arial", Font.BOLD, (int) (screenWidth / 3.5 / 7.5)));
		title.setText(squares[squareNum].getName());
		title.setBorder(BorderFactory.createMatteBorder(0, 0, 5, 0, borderColour));
		title.setForeground(textColour);
		title.setEditable(false);
		title.setOpaque(false);

		infoCon.gridwidth = 2;
		infoCon.gridheight = 1;
		infoCon.gridy = 0;
		infoCon.anchor = GridBagConstraints.NORTH;
		infoCon.fill = GridBagConstraints.BOTH;

		squareInfo.add(title, infoCon);

		JTextPane info = new JTextPane();

		info.setParagraphAttributes(centre, false);

		info.setFont(new Font("Arial", Font.BOLD, (int) (screenWidth / 75)));
		info.setText(squares[squareNum].getInfo());
		info.setForeground(textColour);
		info.setEditable(false);
		info.setOpaque(false);

		JScrollPane scrollInfo = new JScrollPane(info);
		scrollInfo.setOpaque(false);
		scrollInfo.setBorder(null);
		scrollInfo.getViewport().setOpaque(false);
		scrollInfo.getViewport().setBorder(null);
		scrollInfo.setPreferredSize(new Dimension((int) (screenWidth / 3.5) - 15, 500));

		infoCon.gridy = 1;
		infoCon.weightx = 1;
		infoCon.weighty = 1;
		infoCon.fill = GridBagConstraints.BOTH;
		infoCon.insets = new Insets(10, 20, 20, 20);
		squareInfo.add(scrollInfo, infoCon);
	}

	/**
	 * displays the information about the square by calling thedisplaySquareInfo method then 
	 * if the player can spend money on the square it adds a JSlider and a JSpinner underneath the information with max amounts being the amount of money it takes to complete the square take away the amount of money already spent  
	 * if the player can spend time on the square it adds a JSlider and a JSpinner underneath the information with max amounts being the amount of time it takes to complete the square take away the amount of time already spent  
	 * @param squareNum - the number of the square who's information is to be shown
	 */
	public void displaySquareInfoAndResources(int squareNum) {

		displaySquareInfo(squareNum);
		
		JSpinner moneyNumber, timeNumber;
		JSlider spendMoney, spendTime;

		infoCon.weightx = 0;
		infoCon.weighty = 0;
		infoCon.gridy = 2;
		infoCon.gridwidth = 2;

		JLabel resourceSubmission = new JLabel("Select the amount you want to enter:");
		resourceSubmission.setForeground(textColour);
		resourceSubmission.setFont(new Font("Arial", Font.BOLD, screenWidth / 80));

		squareInfo.add(resourceSubmission, infoCon);
		infoCon.fill = GridBagConstraints.HORIZONTAL;
		
		int max = squares[squareNum].getMaxMoney() - squares[squareNum].getMoney();
		
		spendMoney = new JSlider(JSlider.HORIZONTAL, 0, max, 0);
		moneyNumber = new JSpinner();
		
		if (squares[squareNum].getMaxMoney() > 0) {
			
			int spacing = max/4 - (max/4)%10 + 10;
			
			spendMoney.setForeground(textColour);
			spendMoney.setFont(new Font("Arial", Font.BOLD, screenWidth / 80));

			spendMoney.setMajorTickSpacing(spacing);
			spendMoney.setMinorTickSpacing(spacing/5);
			spendMoney.setPaintTicks(true);
			spendMoney.setPaintLabels(true);
			
			spendMoney.setBackground(background);
			
			
			SpinnerNumberModel numSpin = new SpinnerNumberModel(0, 0,
					squares[squareNum].getMaxMoney() - squares[squareNum].getMoney(), 50);

			moneyNumber.setModel(numSpin);
			numSpin.addChangeListener(new ChangeListener() {

				@Override
				public void stateChanged(ChangeEvent e) {
					spendMoney.setValue(numSpin.getNumber().intValue());

				}

			});
			spendMoney.addChangeListener(new ChangeListener() {

				@Override
				public void stateChanged(ChangeEvent e) {
					moneyNumber.setValue(spendMoney.getValue());

				}

			});
			
			infoCon.insets = new Insets(10, 20, 20, 20);
			infoCon.gridy = 4;
			squareInfo.add(spendMoney, infoCon);
			infoCon.gridwidth = 1;
			infoCon.gridy = 3;
			infoCon.fill = GridBagConstraints.NONE;
			JLabel moneySpinnerLabel = new JLabel("Money:");
			squareInfo.add(moneySpinnerLabel, infoCon);
			moneySpinnerLabel.setForeground(textColour);
			moneySpinnerLabel.setFont(new Font("Arial", Font.BOLD, screenWidth / 80));
			
			infoCon.gridx = 1;
			infoCon.gridy = 3;
			infoCon.anchor = GridBagConstraints.WEST;
			infoCon.insets = new Insets(0, 0, 0, 0);
			squareInfo.add(moneyNumber, infoCon);
			
		}

		infoCon.fill = GridBagConstraints.HORIZONTAL;
		spendTime = new JSlider(JSlider.HORIZONTAL, 0,squares[squareNum].getMaxTime() - squares[squareNum].getTime(), 0);
		timeNumber = new JSpinner();
		
		max = squares[squareNum].getMaxTime() - squares[squareNum].getTime();
		
		if (squares[squareNum].getMaxTime() > 0) {
			
			int spacing = max/4 - (max/4)%10 + 10;
			
			spendTime.setForeground(textColour);
			spendTime.setFont(new Font("Arial", Font.BOLD, screenWidth / 80));

			spendTime.setMajorTickSpacing(spacing);
			spendTime.setMinorTickSpacing(spacing/5);
			spendTime.setPaintTicks(true);
			spendTime.setPaintLabels(true);
			
			spendTime.setBackground(background);
			
			

			SpinnerNumberModel numSpin2 = new SpinnerNumberModel(0, 0,squares[squareNum].getMaxTime() - squares[squareNum].getTime(), 10);
			
			timeNumber.setModel(numSpin2);
			numSpin2.addChangeListener(new ChangeListener() {

				@Override
				public void stateChanged(ChangeEvent e) {
					spendTime.setValue(numSpin2.getNumber().intValue());

				}

			});
			spendTime.addChangeListener(new ChangeListener() {

				@Override
				public void stateChanged(ChangeEvent e) {
					timeNumber.setValue(spendTime.getValue());

				}

			});
			
			
			infoCon.insets = new Insets(10, 20, 20, 20);
			infoCon.gridx = 0;
			infoCon.gridwidth = 2;
			infoCon.gridy = 6;
			squareInfo.add(spendTime, infoCon);
			infoCon.gridwidth = 1;
			infoCon.gridy = 5;
			infoCon.fill = GridBagConstraints.NONE;
			JLabel timeSpinnerLabel = new JLabel("Time:");
			squareInfo.add(timeSpinnerLabel, infoCon);
			timeSpinnerLabel.setForeground(textColour);
			timeSpinnerLabel.setFont(new Font("Arial", Font.BOLD, screenWidth / 80));
			infoCon.gridx = 1;
			infoCon.anchor = GridBagConstraints.WEST;

			infoCon.insets = new Insets(0, 0, 0, 0);
			squareInfo.add(timeNumber, infoCon);
		}
		

		
		JButton submitResources = new JButton("submitResources");
		submitResources.setFocusPainted(false);
		infoCon.gridx = 0;
		infoCon.gridy = 7;
		infoCon.gridwidth = 2;
		submitResources.setOpaque(true);
		submitResources.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (squares[squareNum].getMaxTime() > 0) {
					if (squares[squareNum].getMaxMoney() > 0) {
						gameController.spendResources(spendTime.getValue(), spendMoney.getValue(), squareNum);
					} else {
						gameController.spendResources(spendTime.getValue(), 0, squareNum);
					}
				} else if (squares[squareNum].getMaxMoney() > 0) {
					gameController.spendResources(0, spendMoney.getValue(), squareNum);
				}

			}

		});
		infoCon.insets = new Insets(10, 10, 50, 30);
		infoCon.anchor = GridBagConstraints.SOUTHEAST;
		submitResources.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, borderColour));
		submitResources.setBackground(foregroundColour);
		submitResources.setForeground(textColour);
		submitResources.setFont(new Font("Arial", Font.BOLD, screenWidth / 80));
		squareInfo.add(submitResources, infoCon);

	}

	/**
	 * sets the colour of the board to the colours provided and changes the components to the colours
	 * redraws all the squares to make them the new colours
	 * @param background - the background colour
	 * @param squares - the main colour of the squares
	 * @param text  - the colour of the text
	 * @param border - the colour of the border
	 * @param dark - whether the colours title should be light or dark
	 */
	public void changeColours(Color background, Color squares, Color text, Color border, boolean dark) {

		

		this.background = background;
		this.foregroundColour = squares;
		this.textColour = text;
		this.borderColour = border;
		this.darkMode = dark;
		this.setBackground(background);
		bar.setBackground(squares);
		bar.setForeground(text);
		bar.setPreferredSize(new Dimension(screenWidth/6,screenHeight/20));
		bar.setFont(new Font("Arial", Font.BOLD, screenWidth / 70));

		backgroundJLabel.setIcon(new ImageIcon(backgroundPict));
		this.setOpaque(true);

		squaresPanel.setOpaque(false);

		player.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, border));
		player.setFont(new Font("Arial", Font.BOLD, screenWidth / 70));
		player.setBackground(foregroundColour);
		player.setForeground(text);
		money.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, border));
		money.setBackground(foregroundColour);
		money.setForeground(text);
		money.setFont(new Font("Arial", Font.BOLD, screenWidth / 70));
		time.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, border));
		time.setBackground(foregroundColour);
		time.setForeground(text);
		time.setFont(new Font("Arial", Font.BOLD, screenWidth / 70));

		endTurn.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, borderColour));
		endTurn.setBackground(foregroundColour);
		endTurn.setForeground(textColour);
		endTurn.setFont(new Font("Arial", Font.BOLD, screenWidth / 70));

		menu.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, border));
		menu.setForeground(text);
		menu.setBackground(foregroundColour);

		rules.setBackground(foregroundColour);
		displaySettings.setBackground(foregroundColour);
		exit.setBackground(foregroundColour);
		rules.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, border));
		displaySettings.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, border));
		exit.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, border));
		rules.setForeground(text);
		displaySettings.setForeground(text);
		exit.setForeground(text);

		

		for (int i = 0; i < numOfSquares; i++) {
			redrawSquare(i);
		}

		int picWidth = (numOfSquares / 4 - 1) * squareWidth;
		if (!dark) {
			title.setIcon(new ImageIcon(titles[0].getScaledInstance(picWidth, picWidth, Image.SCALE_FAST)));
		} else {
			title.setIcon(new ImageIcon(titles[1].getScaledInstance(picWidth, picWidth, Image.SCALE_FAST)));
		}

		revalidate();
		repaint();

	}
	
	/**
	 * redraws the selected square to update it to the what it should look like currently
	 * e.g. when a player moves the old and new squares have to be redrawn
	 * @param squareNum - the selected square
	 */
	private void redrawSquare(int squareNum) {
		if (squareNum > 0 && squareNum < numOfSquares / 4) {
			squareButtons[squareNum].setIcon(new ImageIcon(
					createSquareImage(squareNum, squareWidth, squareHeight, 1, (int) (squareWidth / 7.5))));
		}
		else if (squareNum > numOfSquares / 4 && squareNum < numOfSquares / 2) {
			squareButtons[squareNum].setIcon(new ImageIcon(
					createSquareImage(squareNum, squareWidth, squareHeight, 2, (int) (squareWidth / 7.5))));
		}
		else if (squareNum > numOfSquares / 2 && squareNum < numOfSquares / 4 * 3) {
			squareButtons[squareNum].setIcon(new ImageIcon(
					createSquareImage(squareNum, squareWidth, squareHeight, 3, (int) (squareWidth / 7.5))));
		}
		else if (squareNum > numOfSquares / 4 * 3 && squareNum < numOfSquares) {
			squareButtons[squareNum].setIcon(new ImageIcon(
					createSquareImage(squareNum, squareWidth, squareHeight, 4, (int) (squareWidth / 7.5))));
		}
		else if (squareNum == 0) {
		squareButtons[0].setIcon(new ImageIcon(
				createSquareImage(0, squareWidth + squareHeight, 0, 1, (int) (squareWidth / 6.25))));
		}else if (squareNum == numOfSquares / 4) {
		squareButtons[numOfSquares / 4].setIcon(new ImageIcon(createSquareImage(numOfSquares / 4,
				squareWidth + squareHeight, 0, 2, (int) (squareWidth / 6.25))));
		}else if (squareNum == numOfSquares / 2) {
		squareButtons[numOfSquares / 2].setIcon(new ImageIcon(createSquareImage(numOfSquares / 2,
				squareWidth + squareHeight, 0, 3, (int) (squareWidth / 6.25))));
		}else if (squareNum == numOfSquares / 4 * 3) {
		squareButtons[numOfSquares / 4 * 3].setIcon(new ImageIcon(createSquareImage(numOfSquares / 4 * 3,
				squareWidth + squareHeight, 0, 4, (int) (squareWidth / 6.25))));
		}
		
		
		
	}

	/**
	 * Creates a buffered image of the board which is the size of the screen multiplied by the zoom
	 * creates the buffered image to be used when zooming in on the board so that the board doesn't become pixelated
	 */
	private void expandBoard() {

	

		//creates scaled up iages if all the squares
		BufferedImage squareAnimationImages[] = new BufferedImage[numOfSquares];

		for (int i = 1; i < numOfSquares / 4; i++) {
			squareAnimationImages[i] = createSquareImage(i, squareWidth * zoom, squareHeight * zoom, 1,
					(int) (squareWidth / 7.5 * zoom));
		}
		for (int i = numOfSquares / 4 + 1; i < numOfSquares / 2; i++) {
			squareAnimationImages[i] = createSquareImage(i, squareWidth * zoom, squareHeight * zoom, 2,
					(int) (squareWidth / 7.5 * zoom));
		}
		for (int i = numOfSquares / 2 + 1; i < numOfSquares / 4 * 3; i++) {
			squareAnimationImages[i] = createSquareImage(i, squareWidth * zoom, squareHeight * zoom, 3,
					(int) (squareWidth / 7.5 * zoom));
		}
		for (int i = numOfSquares / 4 * 3 + 1; i < numOfSquares; i++) {
			squareAnimationImages[i] = createSquareImage(i, squareWidth * zoom, squareHeight * zoom, 4,
					(int) (squareWidth / 7.5 * zoom));
		}

		squareAnimationImages[0] = createSquareImage(0, (squareWidth + squareHeight) * zoom, 0, 1,
				(int) ((int) (squareWidth / 6.25) * zoom));
		squareAnimationImages[numOfSquares / 4] = createSquareImage(numOfSquares / 4,
				(squareWidth + squareHeight) * zoom, 0, 2, (int) (squareWidth / 6.25 * zoom));
		squareAnimationImages[numOfSquares / 2] = createSquareImage(numOfSquares / 2,
				(squareWidth + squareHeight) * zoom, 0, 3, (int) (squareWidth / 6.25) * zoom);
		squareAnimationImages[numOfSquares / 4 * 3] = createSquareImage(numOfSquares / 4 * 3,
				(squareWidth + squareHeight) * zoom, 0, 4, (int) (squareWidth / 6.25 * zoom));

		//creates the bufferdImage 
		Graphics2D sqImGraphics = null;
		fullBoard = new BufferedImage(screenWidth * zoom, screenHeight * zoom, BufferedImage.TYPE_INT_ARGB);
		sqImGraphics = fullBoard.createGraphics();
		int offsetx = (screenWidth - boardWidth) / 2 * zoom;
		int offsety = (screenHeight - boardWidth) / 2 * zoom;

		//draws all the squares onto the buffered image
		sqImGraphics.drawImage(squareAnimationImages[0], null, offsetx, offsety);
		offsetx += (squareWidth + squareHeight) * zoom;
		for (int i = 1; i < numOfSquares / 4; i++) {
			sqImGraphics.drawImage(squareAnimationImages[i], null, offsetx, offsety);
			offsetx += squareWidth * zoom;
		}
		sqImGraphics.drawImage(squareAnimationImages[numOfSquares / 4], null, offsetx, offsety);
		offsety += (squareWidth + squareHeight) * zoom;
		for (int i = numOfSquares / 4 + 1; i < numOfSquares / 2; i++) {
			sqImGraphics.drawImage(squareAnimationImages[i], null, offsetx, offsety);
			offsety += squareWidth * zoom;
		}
		sqImGraphics.drawImage(squareAnimationImages[numOfSquares / 2], null, offsetx, offsety);
		for (int i = numOfSquares / 2 + 1; i < numOfSquares / 4 * 3; i++) {
			offsetx -= squareWidth * zoom;
			sqImGraphics.drawImage(squareAnimationImages[i], null, offsetx, offsety);
		}
		offsetx -= (squareWidth + squareHeight) * zoom;
		sqImGraphics.drawImage(squareAnimationImages[numOfSquares / 4 * 3], null, offsetx, offsety);

		for (int i = numOfSquares / 4 * 3 + 1; i < numOfSquares; i++) {
			offsety -= squareWidth * zoom;
			sqImGraphics.drawImage(squareAnimationImages[i], null, offsetx, offsety);
		}
		offsetx += (squareWidth + squareHeight) * zoom;
		//draws the title in the centre of the buffered image
		int picWidth = (numOfSquares / 4 - 1) * squareWidth * zoom;
		if (!darkMode) {
			sqImGraphics.drawImage(titles[0].getScaledInstance(picWidth, picWidth, Image.SCALE_FAST), offsetx, offsety,
					null);
		} else {
			sqImGraphics.drawImage(titles[1].getScaledInstance(picWidth, picWidth, Image.SCALE_FAST), offsetx, offsety,
					null);
		}

		//add an new JLabel to the animation panel to be used to display the animation of the board zooming in
		boardImage = new JLabel();
		boardImage.setOpaque(true);
		squarePanelCon.gridwidth = 3;
		squarePanelCon.gridheight = 3;
		squarePanelCon.gridx = 0;
		squarePanelCon.gridy = 0;
		squarePanelCon.ipadx = 0;
		squarePanelCon.ipady = 0;
		squarePanelCon.anchor = GridBagConstraints.CENTER;

		animationPanel.add(boardImage, squarePanelCon);

		squaresPanel.getSize();
		sqImGraphics.dispose();

	}

	/**
	 * createAnimation is a private class which runs the code to create the frames of the zoom animation in an new thread to reduce the performance impact on the game
	 * the animation is made at the same time as the board spins so being on another tread means it wont cause the spin animation to be interrupted
	 * @author Magnus
	 *
	 */
	private class createAnimation implements Runnable {

		private int rotation;
		private int squarePos;

		/**
		 * constructor for createAnimation sets the rotation the board will be
		 * works out how far across the square is from -15 to 15 so it will know how far left or right it should move when zooming in
		 * @param rotation - the rotation that the board will be
		 * @param squarePos - the position of the square along its row
		 */
		public createAnimation(int rotation, int squarePos) {
			this.rotation = rotation;
			this.squarePos = (squarePos - 3) * 6 + 3;

			if (squarePos == 0)
				this.squarePos--;
			if (squarePos == 5)
				this.squarePos++;
		}

		/**
		 * the code to create the frames of the zoom animation by saving cropped images of the scaled up board created by expandBoard
		 * it works out how wide the image should be and how far in from the left and how far down from the top it should be
		 * saves the cropped image to an array and repeat the previous steps but creating a smaller image
		 * 
		 *  
		 */
		@Override
		public void run() {
			finishedAnimation = false;
			expandBoard();

			squareAnimationImages = new Image[31];

			Graphics2D sqImGraphics = null;

			BufferedImage tempImage = new BufferedImage(screenWidth * zoom, screenHeight * zoom,
					BufferedImage.TYPE_INT_ARGB);
			sqImGraphics = tempImage.createGraphics();
			sqImGraphics.setColor(background);
			sqImGraphics.fillRect(0, 0, screenWidth * zoom, screenHeight * zoom);
			sqImGraphics.drawImage(backgroundPict.getScaledInstance(1920 * zoom, 1311 * zoom, Image.SCALE_FAST), 0, 0,
					null);

			sqImGraphics.rotate(Math.toRadians(90 * rotation), screenWidth * zoom / 2, screenHeight * zoom / 2);
			sqImGraphics.drawImage(fullBoard, null, 0, 0);

			for (int i = 0; i < 31; i++) {

				int width = (screenWidth * zoom) - (screenWidth * zoom) * i / 90 * 2;
				int xOffset = (screenWidth * zoom / 3 + boardWidth * squarePos / 20 * zoom / 2) * i / 30;
				if (xOffset < 0)
					xOffset = 0;
				else if (xOffset + width > screenWidth * zoom)
					xOffset = screenWidth * zoom - width;

				BufferedImage buffImage = tempImage.getSubimage(xOffset, (screenHeight * zoom / 3) * i / 30 * 2, width,
						(screenHeight * zoom) - (screenHeight * zoom) * i / 90 * 2);

				squareAnimationImages[i] = buffImage.getScaledInstance(screenWidth, screenHeight, Image.SCALE_FAST);
			}

			sqImGraphics.dispose();
			finishedAnimation = true;
		}

	}

	/**
	 * private class that animates the player moving
	 *it extends TimerTask so a Timer can run the code repeatedly at a fixed rate
	 * @author Magnus
	 *
	 */
	private class playerMover extends TimerTask{

		int position;
		int end;
		int playerNum;
		/**
		 * constructor for PlayerMover
		 * sets the start position of the player, the end position and the player that will move 
		 * @param start - the start position
		 * @param end - the end position
		 * @param playerNum - the player to be moved
		 */
		public playerMover(int start, int end, int playerNum) {
			position = start;
			this.end = end;
			this.playerNum = playerNum;
		}
		
		/**
		 * moves the position of the player 1 forward until the player reaches the end position
		 * every time the player is moved forward the squares of the old and new position are redrawn
		 * if the player passes the new week square the animation pauses and tells the player that they have passed a new week and then starts the animation again
		 */
		@Override
		public void run() {
			if(playerCanMove ) {
				
				position++;
				
				if(position == numOfSquares) {
					playerCanMove = false;
					displayMessage(null, "You Have Started A new Week, your time has been replenished and 50 pounds have been deducted for server running costs");
					position = 0;
				}
				
				playerPositions[playerNum - 1] = position;
				changeColours(background, foregroundColour, textColour, borderColour, darkMode);
				
				if(position == 0)redrawSquare(numOfSquares-1);
				else redrawSquare(position-1);
				
				redrawSquare(position);
				
				if(position == end) {
					buttonsDisabled = false;
					zoomIn(end);
					this.cancel();
				}
			}
			
		}
		
	}

	/**
	 * private class that animate the dice
	 * it extends TimerTask so a Timer can run the code repeatedly at a fixed rate
	 * @author Magnus
	 *
	 */
	private class DiceRoller extends TimerTask {

		/**
		 * constructor for DiceRoller which removes the dice currently on the screen
		 * then creates the final image of the animation which will be the 2 dice passed to it
		 * @param finalDice - the values the dice land on
		 */
		public DiceRoller(int[] finalDice) {
			super();

			buttonsDisabled = true;
			playerCanMove = false;
			diceButton.setIcon(null);
			

			BufferedImage finalImage = new BufferedImage(960, 540, BufferedImage.TYPE_INT_ARGB);
			Graphics2D sqImGraphics = null;
			sqImGraphics = finalImage.createGraphics();

			sqImGraphics.drawImage(topDice[finalDice[0] - 1], null, 0, 0);
			sqImGraphics.drawImage(botDice[finalDice[1] - 1], null, 0, 0);

			dicePics[59] = new ImageIcon(finalImage);
			sqImGraphics.dispose();

			backgroundDice = new BufferedImage(220, 140, BufferedImage.TYPE_INT_ARGB);
			sqImGraphics = backgroundDice.createGraphics();

			sqImGraphics.drawImage(botDice[finalDice[0] - 1], null, -310, -410);
			sqImGraphics.drawImage(topDice[finalDice[1] - 1], null, -210, -180);

			sqImGraphics.dispose();

		}

		private int dieNum = 0;

		/**
		 * changes the frame of animation of the dice rolling to the next one until it reaches the last frame then sets removes the dice animation and adds the image of the dice to the 
		 * dice button then  ends itself
		 */
		@Override
		public void run() {

			if (dieNum < 60) {

				dice.setIcon(dicePics[dieNum]);

				animationPanel.repaint();

			}

			dieNum++;

			if (dieNum > 120) {
				dice.setIcon(null);
				diceButton.setIcon(new ImageIcon(backgroundDice));
				repaint();
				buttonsDisabled = false;
				playerCanMove = true;
				
				this.cancel();
			}

		}

	}

	/**
	 * private class that animate the board spinning to a selected orientation
	 * it extends TimerTask so a Timer can run the code repeatedly at a fixed rate
	 * @author Magnus
	 *
	 */
	private class BoardSpinner extends TimerTask {

		int loopNum = 0;
		int numOfRotations;

		/**
		 * gets the final orientation the board has to be and works out the fastest way to spin to that orientation
		 * @param edge
		 */
		public BoardSpinner(int edge) {

			numOfRotations = edge - currentRotation;

			if (numOfRotations == 3)
				numOfRotations = -1;
			if (numOfRotations == -3)
				numOfRotations = 1;

			currentRotation = edge;
			buttonsDisabled = true;

		}

		/**
		 * adds or takes away from the rotation of the board by 3 * the number of rotations then repaints the board where it will be at the new rotation
		 * it repeats 30 times then starts the animation to zoom in to a square then ends itself
		 */
		@Override
		public void run() {

			if (loopNum < 30) {

				rotation += 3 * numOfRotations;
				repaint();
				revalidate();

			} else {
				if (finishedAnimation) {
					buttonsDisabled = false;

					Timer rollDice = new Timer();
					rollDice.scheduleAtFixedRate(new BoardZoom(true), 0, 10);

					this.cancel();
				}

			}

			loopNum++;

		}

	}

	/**
	 * private class that animate the board zooming to a selected square
	 * it extends TimerTask so a Timer can run the code repeatedly at a fixed rate
	 * @author Magnus
	 *
	 */
	private class BoardZoom extends TimerTask {

		private int loopNum = 0;
		private boolean zoomIn = true;

		/**
		 * sets whether the board zooms in or out
		 * @param zoomIn - if the board is zooming in
		 */
		public BoardZoom(boolean zoomIn) {

			this.zoomIn = zoomIn;
			buttonsDisabled = true;

		}

		/**
		 * changes the frame of the animation to the next image
		 * if the board is zooming in it goes from the start of the animation to the end
		 * if the board is zooming out it starts at the end of the animation and goes to the start in reverse order
		 * once it is at the end if it is zooming in it shows the square info bit if it is zooming out then it removes the animation and shows the original board
		 */
		@Override
		public void run() {

			if (loopNum == 1) {
				rotation = 0;
				diceButton.setVisible(false);
				for (int j = 0; j < numOfSquares; j++) {
					squareButtons[j].setVisible(false);

				}
			}

			try {
				if (zoomIn) {
				boardImage.setIcon(new ImageIcon(squareAnimationImages[loopNum]));
			} else {
				boardImage.setIcon(new ImageIcon(squareAnimationImages[30 - loopNum]));
			}
			}catch(Exception e) {
				
			}
			

			if (loopNum == 30) {
				if (!zoomIn) {
					rotation = 90 * currentRotation;
					for (int j = 0; j < numOfSquares; j++) {
						squareButtons[j].setVisible(true);

					}

					animationPanel.remove(boardImage);
					for (int j = 0; j < 30; j++) {
						try {
						squareAnimationImages[j].flush();
						squareAnimationImages[j] = null;
						System.gc();
						}catch(Exception e) {
							
						} 

					}

					diceButton.setVisible(true);

				} else {
					squareInfo.setVisible(true);
				}
				buttonsDisabled = false;

				repaint();
				revalidate();
				this.cancel();
			}

			loopNum++;

		}
	}
	
	/**
	 * works out from the number of the focused square which orientation the screen should turn to and which position the square is in its row
	 * it then starts the board spinning animation to spin to the correct orientation at the same time as the creates the frames of the animation of the board spinning
	 * finally it creates the square info panel
	 * @param focusedSquare - the square to be zoomed in on
	 */
	public void zoomIn(int focusedSquare) {
		Timer spin = new Timer();

		buttonsDisabled = true;
		if (focusedSquare > 0 && focusedSquare < numOfSquares / 4) {
			spin.scheduleAtFixedRate(new BoardSpinner(2), 0, 10);
			Thread animationThread = new Thread(
					new createAnimation(2, numOfSquares / 4 - focusedSquare));
			animationThread.start();
		}
		else if (focusedSquare > numOfSquares / 4 && focusedSquare < numOfSquares / 2) {
			spin.scheduleAtFixedRate(new BoardSpinner(1), 0, 10);
			Thread animationThread = new Thread(
					new createAnimation(1, numOfSquares / 2 - focusedSquare));
			animationThread.start();
		}
		else if (focusedSquare > numOfSquares / 2 && focusedSquare < numOfSquares / 4 * 3) {
			spin.scheduleAtFixedRate(new BoardSpinner(0), 0, 10);
			Thread animationThread = new Thread(
					new createAnimation(0, numOfSquares / 4 * 3 - focusedSquare));
			animationThread.start();
		}
		else if (focusedSquare > numOfSquares / 4 * 3 && focusedSquare < numOfSquares) {
			spin.scheduleAtFixedRate(new BoardSpinner(3), 0, 10);
			Thread animationThread = new Thread(new createAnimation(3, numOfSquares - focusedSquare));
			animationThread.start();
		}

		else if (focusedSquare == 0) {
			spin.scheduleAtFixedRate(new BoardSpinner(2), 0, 10);
			Thread animationThread = new Thread(new createAnimation(2, 5));
			animationThread.start();
		}
		else if (focusedSquare == numOfSquares / 4) {
			spin.scheduleAtFixedRate(new BoardSpinner(1), 0, 10);
			Thread animationThread = new Thread(new createAnimation(1, 5));
			animationThread.start();
		}
		else if (focusedSquare == numOfSquares / 2) {
			spin.scheduleAtFixedRate(new BoardSpinner(0), 0, 10);
			Thread animationThread = new Thread(new createAnimation(0, 5));
			animationThread.start();
		}
		else if (focusedSquare == numOfSquares / 4 * 3) {
			spin.scheduleAtFixedRate(new BoardSpinner(3), 0, 10);
			Thread animationThread = new Thread(new createAnimation(3, 5));
			animationThread.start();
		}

		
		gameController.displaySquareInfo(focusedSquare);
		isZoomed = true;
		
	}

	/**
	 * the private class SquarePicker implements ActionListener so that the actionPerformed method will be run whenever a square is clicked on
	 * @author Magnus
	 *
	 */
	private class SquarePicker implements ActionListener {

		/**
		 * if the buttons are not disabled or a message is being shown then the method gets the button that has been pressed, works out which square it belongs to then zooms in on that square
		 */
		@Override
		public void actionPerformed(ActionEvent e) {

			// TODO Auto-generated method stub
			if (!buttonsDisabled && !messageDisplayed) {
				buttonsDisabled = true;
				Object ob = e.getSource();

				for (int i = 0; i < numOfSquares; i++) {
					if (ob.equals(squareButtons[i])) {

						int focusedSquare = i - numOfSquares / 4 * currentRotation;
						if (focusedSquare >= numOfSquares)
							focusedSquare -= numOfSquares;
						else if (focusedSquare < 0)
							focusedSquare += numOfSquares;
						
						zoomIn(focusedSquare);
						
						
						break;
						
					}
				}

			}

		}

	}

	/**
	 * draws the image of the square to a buffered image using Graphics2D and returns the buffered image
	 * first create the buffered image and set its rotation then add the main colour, task area colour and border
	 * if there is a player on that square their icon is drawn on top then the name of the square is drawn on the square
	 * @param squareNum - the square being drawn
	 * @param width - the width the square should be
	 * @param colourHeight - the height the task area indicator should be
	 * @param edge - the orientation the square should be in
	 * @param fontsize - the size of the font for the name
	 * @return - the image  of the square drawn
	 */
	private BufferedImage createSquareImage(int squareNum, int width, int colourHeight, int edge, int fontsize) {

		Font font = new Font("Arial", Font.BOLD, fontsize);
		BufferedImage squareImage = null;
		Graphics2D sqImGraphics = null;

		if (edge == 1) {
			squareImage = new BufferedImage(width, colourHeight + width, BufferedImage.TYPE_INT_ARGB);
			sqImGraphics = squareImage.createGraphics();
			sqImGraphics.rotate(Math.toRadians(180), width / 2, (colourHeight + width) / 2);
		} else if (edge == 2) {
			squareImage = new BufferedImage(colourHeight + width, width, BufferedImage.TYPE_INT_ARGB);
			sqImGraphics = squareImage.createGraphics();
			sqImGraphics.rotate(Math.toRadians(270), width / 2, width / 2);
		} else if (edge == 3) {
			squareImage = new BufferedImage(width, colourHeight + width, BufferedImage.TYPE_INT_ARGB);
			sqImGraphics = squareImage.createGraphics();
		} else if (edge == 4) {
			squareImage = new BufferedImage(colourHeight + width, width, BufferedImage.TYPE_INT_ARGB);
			sqImGraphics = squareImage.createGraphics();
			sqImGraphics.translate(0, -colourHeight);
			sqImGraphics.rotate(Math.toRadians(90), width / 2, colourHeight + width / 2);
		}

		sqImGraphics.setColor(squares[squareNum].getColour());
		sqImGraphics.fillRect(0, 0, width, colourHeight);
		sqImGraphics.setColor(foregroundColour);
		sqImGraphics.fillRect(0, colourHeight, width, width);
		
		if(colourHeight == 0) {
			sqImGraphics.setColor(background);
			sqImGraphics.fillRect(0, 0, width/4, width/4);
		}

		int borderWidth = width / 50;

		sqImGraphics.setColor(borderColour);
		sqImGraphics.fillRect(0, 0, width, borderWidth);
		sqImGraphics.fillRect(0, 0, borderWidth, colourHeight + width);
		sqImGraphics.fillRect(0, colourHeight + width - borderWidth, width, borderWidth);
		sqImGraphics.fillRect(width - borderWidth, 0, borderWidth, colourHeight + width);

		sqImGraphics.setFont(font);
		FontMetrics fontMet = sqImGraphics.getFontMetrics(font);

		String[] text = squares[squareNum].getName().split(" ");
		sqImGraphics.setColor(textColour);
		int textHeight = fontMet.getHeight();
		int startLine = (width - textHeight * text.length) / 2 + textHeight;
		startLine += colourHeight;

		if (colourHeight == 0) {
			for (int i = 0; i < playerPositions.length; i++) {
				if (squareNum == playerPositions[i]) {

					if (i == 0) {
						sqImGraphics.drawImage(playerIcons[i].getScaledInstance(width / 4, width / 4, Image.SCALE_FAST),
								0, 0, null);
					} else if (i == 1) {
						sqImGraphics.drawImage(playerIcons[i].getScaledInstance(width / 4, width / 4, Image.SCALE_FAST),
								width / 8 * 3, 0, null);
					} else if (i == 2) {
						sqImGraphics.drawImage(playerIcons[i].getScaledInstance(width / 4, width / 4, Image.SCALE_FAST),
								width / 4 * 3, 0, null);
					} else if (i == 3) {
						sqImGraphics.drawImage(playerIcons[i].getScaledInstance(width / 4, width / 4, Image.SCALE_FAST),
								0, width / 8 * 3, null);
					} else if (i == 4) {
						sqImGraphics.drawImage(playerIcons[i].getScaledInstance(width / 4, width / 4, Image.SCALE_FAST),
								width / 4 * 3, width / 8 * 3, null);
					} else if (i == 5) {
						sqImGraphics.drawImage(playerIcons[i].getScaledInstance(width / 4, width / 4, Image.SCALE_FAST),
								0, width / 4 * 3, null);
					} else if (i == 6) {
						sqImGraphics.drawImage(playerIcons[i].getScaledInstance(width / 4, width / 4, Image.SCALE_FAST),
								width / 8 * 3, width / 4 * 3, null);
					} else if (i == 7) {
						sqImGraphics.drawImage(playerIcons[i].getScaledInstance(width / 4, width / 4, Image.SCALE_FAST),
								width / 4 * 3, width / 4 * 3, null);
					}

				}
			}
			sqImGraphics.rotate(Math.toRadians(-45), width / 2, width / 2);
		} else {
			for (int i = 0; i < playerPositions.length; i++) {
				if (squareNum == playerPositions[i]) {

					if (i == 0) {
						sqImGraphics.drawImage(playerIcons[i].getScaledInstance(width / 3, width / 3, Image.SCALE_FAST),
								0, colourHeight, null);
					} else if (i == 1) {
						sqImGraphics.drawImage(playerIcons[i].getScaledInstance(width / 3, width / 3, Image.SCALE_FAST),
								width / 3, colourHeight, null);
					} else if (i == 2) {
						sqImGraphics.drawImage(playerIcons[i].getScaledInstance(width / 3, width / 3, Image.SCALE_FAST),
								width / 3 * 2, colourHeight, null);
					} else if (i == 3) {
						sqImGraphics.drawImage(playerIcons[i].getScaledInstance(width / 3, width / 3, Image.SCALE_FAST),
								0, colourHeight + width / 3, null);
					} else if (i == 4) {
						sqImGraphics.drawImage(playerIcons[i].getScaledInstance(width / 3, width / 3, Image.SCALE_FAST),
								width / 3 * 2, colourHeight + width / 3, null);
					} else if (i == 5) {
						sqImGraphics.drawImage(playerIcons[i].getScaledInstance(width / 3, width / 3, Image.SCALE_FAST),
								0, colourHeight + width / 3 * 2, null);
					} else if (i == 6) {
						sqImGraphics.drawImage(playerIcons[i].getScaledInstance(width / 3, width / 3, Image.SCALE_FAST),
								width / 3, colourHeight + width / 3 * 2, null);
					} else if (i == 7) {
						sqImGraphics.drawImage(playerIcons[i].getScaledInstance(width / 3, width / 3, Image.SCALE_FAST),
								width / 3 * 2, colourHeight + width / 3 * 2, null);
					}

				}
			}
		}
		for (int i = 0; i < text.length; i++) {
			int inset = (width - fontMet.stringWidth(text[i])) / 2;
			sqImGraphics.drawString(text[i], inset, startLine);
			startLine += textHeight;
		}

		sqImGraphics.dispose();

		return squareImage;

	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	/**
	 * detects when the mouse is pressed
	 * if the board is zoomed in, buttons are not disabled and a message is not displayed it checks the position of the mouse and if it is not over the info panel then it causes the board to zoom out
	 */
	@Override
	public void mousePressed(MouseEvent e) {
		if (isZoomed && !buttonsDisabled && !messageDisplayed) {

			int xPos = e.getX();
			int yPos = e.getY();

			int sqInPosx = squareInfo.getX();
			int sqInPosy = squareInfo.getY();

			int sqInWidth = squareInfo.getWidth();
			int sqInHeight = squareInfo.getHeight();

			if (xPos < sqInPosx || xPos > sqInPosx + sqInWidth) {
				if (yPos < sqInPosx || yPos > sqInPosx + sqInHeight) {
					Timer rollDice = new Timer();
					rollDice.scheduleAtFixedRate(new BoardZoom(false), 0, 10);
					infoPanel.remove(squareInfo);
					isZoomed = false;
				}
			}

		}

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

}
