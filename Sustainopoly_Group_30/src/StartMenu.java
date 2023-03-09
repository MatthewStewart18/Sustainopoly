import java.awt.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
@SuppressWarnings({ "unchecked", "rawtypes", "serial" })
/**
 * StartMenu displays a start menu which allows you to:
 * start a game, which includes picking the players
 * tells the display class to show the rules
 * tells the display class to show the display settings
 * exit out of the game 
 * @author Magnus
 *
 */
public class StartMenu extends JPanel {
	
	//the colours of the panel
	private Color background, foregroundColour, textColour, borderColour;
	//whether or not the title is dark or light
	private boolean darkMode = false;
	private JLabel title;//the label containing the title
	
	private int buttonWidth = 100;//the normal width of the buttons
	private int buttonHeight = 100;// the normal height of the buttons
	private BufferedImage[] titles;// the light and dark versions of the title
	
	private Display display;//the display class that this is contained inside
	private JPanel playerPicker;//the panel which shows the players
	private JPanel border;//the panel that contains the scroll pane that shows the player picker
	
	private ArrayList<String[]> players = new ArrayList<String[]>();//the list of the players
	
	/**
	 * Constructor for the StartMenu,
	 * sets the title and the background image,
	 * sets up and shows the menu buttons,
	 * sets up the player selector menu
	 * @param display - controls screen size and what is shown on the screen
	 */
	public StartMenu(Display display) {
		this.display = display;
		
		//sets the layout manager of the panel to be GridBag
		setLayout(new GridBagLayout());
		GridBagConstraints gridCon = new GridBagConstraints();
		gridCon.gridx = 1;
		gridCon.gridy = 1;
		gridCon.anchor = GridBagConstraints.CENTER;
		
		add(setUpStartMenu(), gridCon);//adds the menu buttons to the screen which are visible
		add(setUpPlayerPicker(), gridCon);//adds the player picker to the screen which is invisible
		
		//add the label which will display the title
		title = new JLabel();
		gridCon.gridy = 0;
		add(title, gridCon);
		
		//sets the background image
		gridCon.gridx = 0;
		gridCon.gridwidth = 3;
		gridCon.gridheight = 3;
		JLabel background = new JLabel(new ImageIcon("titles//govanmap.png"));
		add(background, gridCon);
		
		//add spacers at the left and right areas of the screen so that the menu is centred
		JLabel spacer1 = new JLabel();
		JLabel spacer2 = new JLabel();
		gridCon.fill = GridBagConstraints.BOTH;
		gridCon.gridheight = 3;
		gridCon.gridwidth = 1;
		gridCon.gridx = 0;
		gridCon.gridy = 0;
		gridCon.weightx = 1;
		gridCon.weighty = 1;
		add(spacer1, gridCon);
		gridCon.gridx = 2;
		add(spacer2, gridCon);
		
		//reads in and saves the images for the light mode and dark mode title
		titles = new BufferedImage[2];
		try {
			
			titles[0] = (BufferedImage) ImageIO.read(new File("titles//mainTitleLight.png"));
			titles[1] = (BufferedImage) ImageIO.read(new File("titles//mainTitleDark.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * resizes the components on screen relative to the screenWidth and screenHeight
	 * @param screenWidth - the width of the window
	 * @param screenHeight - the height of the window
	 */
	public void resize(int screenWidth, int screenHeight) {
		
		buttonWidth = screenWidth/3;
		buttonHeight = screenHeight/15;
		changeColours(background, foregroundColour, textColour, borderColour, darkMode);
	}
	
	/**
	 * creates a JPanel with a rounded border then adds each of the start menu buttons with their action listeners
	 * @return a JPanel which contains all the start menu buttons
	 */
	private JPanel setUpStartMenu() {
		
		JPanel start = new JPanel() {
			/**
			 * {@inheritDoc}
			 * overrides the paint method to make sure the panel is the correct size as well as add a border with rounded corners
			 */
			 @Override
			public void paint(Graphics g) {
				setMinimumSize(new Dimension((int) (buttonWidth*1.3), (int) (buttonHeight*9)));
				setPreferredSize(new Dimension((int) (buttonWidth*1.3), (int) (buttonHeight*6)));
				Graphics2D sqImGraphics = (Graphics2D) g;
				sqImGraphics.setColor(foregroundColour);
				sqImGraphics.fillRoundRect(5,5, this.getWidth()-10, this.getHeight()-10, 30,30);
				sqImGraphics.setStroke(new BasicStroke(10));
				sqImGraphics.setColor(borderColour);
				sqImGraphics.drawRoundRect(5,5, this.getWidth()-10, this.getHeight()-10, 30,30);
				super.paint(g);
			}
			
		};
		
		start.setLayout(new BoxLayout(start, BoxLayout.Y_AXIS));//sets the panel layout manager to be BoxLayout
		
		start.add(Box.createVerticalGlue());//add a gap between buttons and top of panel

		start.add(createButton("New Game", new ActionListener() { // adds the new game button which displays the player picker

			/**
			 * {@inheritDoc}
			 * sets the start menu to be invisible then sets the border JPanel(containing squarePicker) to be visible
			 */
			@Override
			public void actionPerformed(ActionEvent e) {
				start.setVisible(false);
				border.setVisible(true);
			}
			
		}));
		
		start.add(Box.createVerticalGlue());//add a gap between buttons

		start.add(createButton("Rules", new ActionListener() { // adds the button that displays the rules
			/**
			 * {@inheritDoc}
			 * calls the display method to show the rules
			 */
			@Override
			public void actionPerformed(ActionEvent e) {
				display.openRules();
			}
			
		}));
		
		start.add(Box.createVerticalGlue());//add a gap between buttons

		start.add(createButton("Display Settings", new ActionListener() { // adds the button to display the display settings
			/**
			 * {@inheritDoc}
			 * calls the display method to show the display settings
			 */
			@Override
			public void actionPerformed(ActionEvent e) {
				display.openDisplayOptions();
			}
			
		}));
		
		start.add(Box.createVerticalGlue());//add a gap between buttons
		
		start.add(createButton("Exit Game", new ActionListener() { // adds the button which closes the game
			/**
			 * {@inheritDoc}
			 * closes the application
			 */
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
			
		}));
		
		start.add(Box.createVerticalGlue());//add a gap between buttons and bottom of panel
		
		start.setOpaque(false);
		
		return start;
	}
	
	/**
	 * Create a button that displays a string name which when pressed executes the code in ActionListener action
	 * @param name - the words displayed on the button
	 * @param action - the task the button performs when pressed
	 * @return the button
	 */
	private JButton createButton(String name, ActionListener action) {
		
		JButton button = new JButton(name) {
			/**
			 * {@inheritDoc}
			 * override the paint method to the object to ensure the colour and size relative to the screen are correct before painting the button
			 */
			 @Override
			public void paint(Graphics g) {
				
				this.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, borderColour));
				this.setBackground(foregroundColour);
				this.setForeground(textColour);
				this.setFont( new Font("Arial", Font.BOLD, buttonWidth/10));
				this.setMinimumSize(new Dimension(buttonWidth, buttonHeight));
				this.setMaximumSize(new Dimension(buttonWidth, buttonHeight));
				super.paint(g);
			}
		};
		
		button.addActionListener(action); //set the action of the button
		
		button.setOpaque(true); //make the button show its background
		button.setFocusPainted(false); //make the button not be highlighted after it is pressed
		button.setAlignmentX(Component.CENTER_ALIGNMENT); //set the button the be displayed in the middle of the panel
		
		return button;
		
	}
	
	/**
	 * creates a JPanel which contains:
	 * a title "Enter Players"
	 * a scroll pane in which you add players and change their names, colours and icons
	 * a button which will start the game if all the players have unique names, colours and icons
	 * @return a JPanel containing the player selector
	 */
	private JPanel setUpPlayerPicker() {
		
		playerPicker = new JPanel() { 
			
			/**
			 * {@inheritDoc}
			 * overrides the paint method to make sure the panel is the correct size(1.5*width of a normal button, 8*the height of the newPlayerBox)
			 */
			 @Override
			public void paint(Graphics g) {
				
				
				setMinimumSize(new Dimension((int) (buttonWidth*1.5), (int) (buttonHeight*12)));
				setPreferredSize(new Dimension((int) (buttonWidth*1.5), (int) (buttonHeight*12)));
				
				super.paint(g);
			}
			
		};
		playerPicker.setOpaque(false);
		playerPicker.setLayout(new BoxLayout(playerPicker, BoxLayout.Y_AXIS));
		playerPicker.add(addNewPlayerBox());//add a box that shows and allows editing of the first players information
		playerPicker.add(addPlayerButton());//add a button that if pressed adds a new player
		
		JScrollPane scroll = new JScrollPane(playerPicker);//adds the panel with the players information to a scroll pane
		
		//create the JPanel that everything is stored in
		border = new JPanel(){
			
			public void paint(Graphics g) {
				Graphics2D sqImGraphics = (Graphics2D) g;
				//set the border correct size to fit the scroll panel, the header and the continue button
				setMinimumSize(new Dimension((int) (buttonWidth*1.5)+ 50, (int) (buttonHeight*9)));
				setPreferredSize(new Dimension((int) (buttonWidth*1.5)+ 50, (int) (buttonHeight*9)));
				//set the scroll panel size so it can fit the player picker's width and can show 4 player editors vertically
				scroll.setMinimumSize(new Dimension((int) (buttonWidth*1.5)+20, (int) (buttonHeight*6)));
				scroll.setPreferredSize(new Dimension((int) (buttonWidth*1.5)+20, (int) (buttonHeight*6)));
				//draw a border around the panel with rounded corners
				sqImGraphics.setColor(foregroundColour);
				sqImGraphics.fillRoundRect(5,5, this.getWidth()-10, this.getHeight()-10, 30,30);
				sqImGraphics.setStroke(new BasicStroke(10));
				sqImGraphics.setColor(borderColour);
				sqImGraphics.drawRoundRect(5,5, this.getWidth()-10, this.getHeight()-10, 30,30);
				
				super.paint(g);
			}
			
		};
		
		//sets the scroll panel to not show its background or border
		scroll.setOpaque(false);
		scroll.getViewport().setOpaque(false);
		scroll.setBorder(null);
		scroll.setViewportBorder(null);
		
		border.setOpaque(false);
		border.setVisible(false);
		
		//Creates a label which shows "enter player" but also changes to a red error message if there is a problem with the players information
		JLabel submitPlayersLabel = new JLabel("Enter Players") {
			
			/**
			 * {@inheritDoc}
			 * overrides the paint method to make sure the panel is the correct size and colour
			 */
			 @Override
			public void paint(Graphics g) {
				//sets the size of the label
				this.setMinimumSize(new Dimension((int) (buttonWidth), (int) (buttonHeight*1.2)));
				this.setPreferredSize(new Dimension((int) (buttonWidth), (int) (buttonHeight*1.2)));
				
				//if it displays normal text, the text is the text colour otherwise it is red
				if(this.getText().length() < 15) {
					setForeground(textColour);
				}else {
					setForeground(Color.RED);
				}
				//sets the text size relative to the width and length of message
				this.setFont( new Font("Arial", Font.BOLD, (int) (buttonWidth/this.getText().length()*1.9)));
				super.paint(g);
			}
		};
		
		
		
		border.add(submitPlayersLabel, BorderLayout.PAGE_START);//add the enter players label to the top of the main panel
		border.add(scroll, BorderLayout.CENTER);//adds the scroll pane to the middle of the main panel
		border.add(createButton("Continue",new ActionListener() {//adds the button to submit the players and continue to the board at the bottom of the main panel

			/**
			* {@inheritDoc}
			* checks if the players information is valid then calls the method in display to show the board
			*/
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if(players.size()== 0) {
					submitPlayersLabel.setText("create a player");
					return;
				}
				
				boolean check = true;
				
				for(int i = 0; i< players.size();i++) {
					
					if(players.get(i)[0]== null|| players.get(i)[0].equals("")) {
						submitPlayersLabel.setText("Each Player must have a name");//checks if any of the player names is null then sets the warning message
						check = false;
						break;
					}
					
					//checks if any player has a character in their name that isn't a letter or a number, if they do it displays a warning
					for(int j = 0; j < players.get(i)[0].length(); j++) {
						char character = players.get(i)[0].charAt(j);
						if(!(character >= 'a' && character <= 'z' || character >= 'A' && character <= 'Z'|| character >= '1' && character <= '9' )) {
							submitPlayersLabel.setText("Player names can only contain letters and numbers ");
							check = false;
							break;
						}
					}
					
					for(int j = 0; j< players.size();j++) {
						
						if(players.get(i)[0].equals(players.get(j)[0]) && i!=j) {
							submitPlayersLabel.setText("Each Player must have a unique name");//checks if any 2 players have the same name then sets the warning message
							check = false;
							break;
						}
						
						else if (players.get(i)[1].equals(players.get(j)[1]) && i != j) {
							submitPlayersLabel.setText("Each Player must have a unique Colour");//checks if any 2 players have the same colour then sets the warning message
							check = false;
							break;
						}

						else if (players.get(i)[2].equals(players.get(j)[2]) && i != j) {
							submitPlayersLabel.setText("Each Player must have a unique Icon");//checks if any 2 players have the same icon then sets the warning message
							check = false;
							break;
						}
					}

				}

				if (check) {
					display.startGame(players);//if the check was true then call the Display method to start the game with the players information
				}
				
			}

		}), BorderLayout.PAGE_END);
		
		return border;
	}

	/**
	 * creates and returns a button that, if the number of players is less that 8, once pressed will:
	 * 	remove itself
	 * 	add a new player to playerPicker
	 * 	recalls the method to add a new addPlayerButton to the playerPicker 
	 * @return - a button that once pressed adds a player to the playerPicker
	 */
	private JButton addPlayerButton(){
		//create the addPlayer button witch displays the text "Add Player"
		JButton addPlayer = new JButton("ADD PLAYER") {
			//overrides the paint method to make sure the size and colour is always correct
			public void paint(Graphics g) {
				//sets the border and colour of the button
				this.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, borderColour));
				this.setBackground(foregroundColour);
				this.setForeground(textColour);
				this.setFont( new Font("Arial", Font.BOLD, buttonWidth/15));
				//sets the size of the button to be 1.5*the height and width of the normal buttons
				this.setMinimumSize(new Dimension((int) (buttonWidth*1.5), (int) (buttonHeight*1.5)));
				this.setPreferredSize(new Dimension((int) (buttonWidth*1.5), (int) (buttonHeight*1.5)));
				this.setMaximumSize(new Dimension((int) (buttonWidth*1.5), (int) (buttonHeight*1.5)));
				super.paint(g);
			}
		};
		
		addPlayer.setAlignmentX(Component.CENTER_ALIGNMENT);//sets the button to be in the middle of the panel
		addPlayer.addActionListener(new ActionListener() {//sets the buttons action
			@Override
			public void actionPerformed(ActionEvent e) {
				if(players.size()<8) {//if the number of players is less than 8
					playerPicker.remove(addPlayer);//remove itself from the playerPicker
					playerPicker.add(addNewPlayerBox());//adds a new player editor to the playerPicker
					playerPicker.add(addPlayerButton());//adds a new add player button to the playerPicker
				}
				
			}
			
			
		});
		
		return addPlayer;
		
	}

	/**
	 * creates and returns a panel that is an editor for a new player which contains:
	 * 	a text field to enter the player name
	 * 	a dropdown from which the player can pick a colour
	 * 	a dropdown from which the player can pick an icon
	 * 	a remove player button to remove unwanted players
	 * @return a panel that an editor for a new player
	 */
	private JPanel addNewPlayerBox() {
		
		String[] playerInfo = {null, "Red", "Bike"};//set the defaults for a player
		
		players.add(playerInfo);//add this player to the list of players
		
		//creates the panel which holds everything and set the layoutManager to gridBagLayout
		JPanel player = new JPanel(new GridBagLayout()){
			
			public void paint(Graphics g) {
				//sets the border and the background colour
				this.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, borderColour));
				this.setBackground(foregroundColour);
				//sets the size of the panel to be 1.5* the width and height of a normal button
				this.setMinimumSize(new Dimension((int) (buttonWidth*1.5), (int) (buttonHeight*1.5)));
				this.setPreferredSize(new Dimension((int) (buttonWidth*1.5), (int) (buttonHeight*1.5)));
				this.setMaximumSize(new Dimension((int) (buttonWidth*1.5), (int) (buttonHeight*1.5)));
				super.paint(g);
			}
			
		};
		player.setOpaque(true);
		player.setAlignmentX(Component.CENTER_ALIGNMENT);//sets the panel to be in the centre of the playerPicker panel 
		
		//create a label to tell the user that the text field is for the player name with a max of 20 characters
		JLabel nameLabel = new JLabel("Name(max 20 char):"){
			//override the paint method keep the correct text colour and size
			public void paint(Graphics g) {
				this.setForeground(textColour);
				this.setFont( new Font("Arial", Font.BOLD, buttonWidth/20));
				super.paint(g);
			}
			
		};
		//create a text field in which the player will put their name
		JTextField name = new JTextField(){
			//override the paint method keep the correct text colour and size
			public void paint(Graphics g) {
				
				this.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, borderColour));
				this.setBackground(foregroundColour);
				this.setForeground(textColour);
				this.setFont( new Font("Arial", Font.BOLD, buttonWidth/20));
				super.paint(g);
			}
			
		};
		name.setColumns(15);//set the text field to be 15 characters wide
		//add a property change listener to the text field
		name.addPropertyChangeListener(new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				String playerName = name.getText();
				//if the entered name is more than 20 characters then remove the excess characters
				if (playerName != null) {
					if (playerName.length() > 20) {
						playerName = playerName.substring(0, 20);
						name.setText(playerName);
					}
				}
				playerInfo[0] = playerName;//save the players name to position 0 of its array in the array of players
			}

		});
		
		
		
		//create a label to tell the user that the combo box is to select the player colour
		JLabel colourLabel = new JLabel("Colour:"){
			//override the paint method keep the correct text colour and size
			public void paint(Graphics g) {
				this.setForeground(textColour);
				this.setFont( new Font("Arial", Font.BOLD, buttonWidth/20));
				super.paint(g);
			}
			
		};
		String[] colours = {"Red", "Orange", "Yellow", "Green", "Blue", "Magenta", "Pink", "Cyan"};
		
		//creates a combo box which lets the players choose 1 of 8 colours
		JComboBox colourOptions = new JComboBox(colours) {
			//override the paint method keep the correct size
			public void paint(Graphics g) {
				
				this.setPreferredSize(new Dimension(buttonHeight, buttonHeight/3*2));
				this.setMinimumSize(new Dimension(buttonHeight, buttonHeight/3*2));
				super.paint(g);
			}
		};
		colourOptions.setRenderer(new ComboBoxImages());//sets the renderer to be a custom rendered which shows the colours
		
		//adds an action listener to save the selected colour to the playerInfo array
		colourOptions.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				playerInfo[1] = (String) colourOptions.getSelectedItem();
				
			}
		});
		
		
		
		//create a label to tell the user that the combo box is to select the player icon
		JLabel iconLabel = new JLabel("Icon:"){
			//override the paint method keep the correct text colour and size
			public void paint(Graphics g) {
				this.setForeground(textColour);
				this.setFont( new Font("Arial", Font.BOLD, buttonWidth/20));
				super.paint(g);
			}
			
		};
		String[] Icon = {"Bike", "Boat", "Bus", "Car", "Helicopter", "Plane", "Train", "Truck"};
		
		//creates a combo box which lets the players choose 1 of 8 icons
		JComboBox iconOptions = new JComboBox(Icon) {
			public void paint(Graphics g) {
				//override the paint method keep the correct size
				this.setPreferredSize(new Dimension(buttonHeight, buttonHeight/3*2));
				this.setMinimumSize(new Dimension(buttonHeight, buttonHeight/3*2));
				super.paint(g);
			}
		};
		iconOptions.setRenderer(new ComboBoxImages());//sets the renderer to be a custom rendered which shows the icons
		
		//adds an action listener to save the selected icon to the playerInfo array
		iconOptions.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				playerInfo[2] = (String) iconOptions.getSelectedItem();
				
			}
		});
		
		
		//create a button that removes this player and its editor
		JButton remove = new JButton("Remove"){
			
			public void paint(Graphics g) {
				//override the paint method keep the correct colour and size
				this.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, borderColour));
				this.setBackground(foregroundColour);
				this.setForeground(textColour);
				this.setFont( new Font("Arial", Font.BOLD, buttonWidth/20));
				super.paint(g);
			}
			
		};
		//add an action listener which removes the player
		remove.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				playerPicker.remove(player);//removes the editor for this player
				players.remove(playerInfo);//removes this player from the ArrayList of players
			}
			
		});
		
		
		GridBagConstraints gridCon = new GridBagConstraints();
		
		//adds the labels, text field, comboBoxs, and button to the panel
		player.add(nameLabel, gridCon);
		gridCon.gridy = 1;
		gridCon.weightx = 1;
		player.add(name, gridCon);
		
		gridCon.gridx = 1;
		player.add(colourOptions, gridCon);
		
		gridCon.gridy = 0;
		player.add(colourLabel, gridCon);
		
		gridCon.gridx = 2;
		player.add(iconLabel, gridCon);
		
		gridCon.gridy = 1;
		player.add(iconOptions, gridCon);
		
		gridCon.gridx = 3;
		gridCon.gridy = 0;
		gridCon.gridheight = 2;
		
		player.add(remove, gridCon);
		
		return player;
	}

	/**
	 * sets the colours of everything in the panel
	 * @param background - the background colour
	 * @param foreground - the foreground colour
	 * @param text  - the colour of the text
	 * @param border - the colour of the border
	 * @param dark - whether the colours title should be light or dark
	 */
	public void changeColours(Color background, Color foreground, Color text, Color border, boolean dark) {
		this.background = background;
		this.foregroundColour = foreground;
		this.textColour = text;
		this.borderColour = border;
		this.darkMode = dark;
		this.setBackground(background);
		
		//sets the title to be the dark version if dark is true else it becomes the light version
		if(dark) {
			title.setIcon(new ImageIcon(titles[1].getScaledInstance(buttonWidth*2,buttonHeight*4,Image.SCALE_FAST)));
		}else {
			title.setIcon(new ImageIcon(titles[0].getScaledInstance(buttonWidth*2,buttonHeight*4,Image.SCALE_FAST)));
		}
		
		repaint();
		revalidate();
		
	}

	/**
	 * ComboBoxImages implements ListCellRenderer so that it can replace the JComboBox rendered so that it can show images
	 * @author Magnus
	 *
	 */
	private class ComboBoxImages extends JLabel implements ListCellRenderer{
	

		/**
		 * {@inheritDoc}
		 * sets the image of the object to  be the image that corresponds to the value then returns this object
		 */
		@Override
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
				boolean cellHasFocus) {
			//gets the value as a string
			String imageName = (String) (value);

			BufferedImage icon = null;
			//sets icon to the image that corresponds the the imageName
			try {
				icon = (BufferedImage) ImageIO.read(new File("icons\\"+imageName+".png"));
				
			} catch (IOException e) {
			}
		
			//if the image was an icon, scale the icon size for the combo box, set the icon to be the image then return this object
			if(icon != null) {
				setIcon(new ImageIcon(icon.getScaledInstance(buttonHeight/3*2,buttonHeight/3*2,Image.SCALE_FAST)));
				return this;
			}
		
			BufferedImage colour = new BufferedImage(100, 100,  BufferedImage.TYPE_INT_RGB);
			Graphics2D graphics = colour.createGraphics();
			
			//set the colour graphics uses corresponding to the value
			if(imageName.equals("Red")) {
				graphics.setColor(Color.RED);
			}else if(imageName.equals("Orange")) {
				graphics.setColor(Color.ORANGE);
			}else if(imageName.equals("Yellow")) {
				graphics.setColor(Color.YELLOW);
			}else if(imageName.equals("Green")) {
				graphics.setColor(Color.GREEN);
			}else if(imageName.equals("Blue")) {
				graphics.setColor(Color.BLUE);
			}else if(imageName.equals("Magenta")) {
				graphics.setColor(Color.MAGENTA);
			}else if(imageName.equals("Pink")) {
				graphics.setColor(Color.PINK);
			}else if(imageName.equals("Cyan")) {
				graphics.setColor(Color.CYAN);
			}
			
			//fill in the bufferedImage colour with the selected colour
			graphics.fillRect(0, 0, 100, 100);
			
			graphics.dispose();
			
			//set the icon to be the coloured image then return this object
			setIcon(new ImageIcon(colour));
			
			return this;
		}
		
	}
	

}
