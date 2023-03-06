import java.awt.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class StartMenu extends JPanel {
	
	private Color background, foregroundColour, textColour, borderColour;
	private boolean darkMode = false;
	private JLabel title;
	
	private int buttonWidth = 100;
	private int buttonHeight = 100;
	private BufferedImage[] titles;
	
	private JButton newGame;
	private JButton rules;
	private JButton displaySettings;
	private JButton exitGame;
	
	private Display display;
	private JPanel playerPicker;
	private JPanel border;
	
	private ArrayList<String[]> players = new ArrayList<String[]>();
	
	public StartMenu(int screenWidth, int screenHeight, Display display) {
		this.display = display;
		setLayout(new GridBagLayout());
		
		
		titles = new BufferedImage[2];
		try {
			BufferedImage backgroundPict = (BufferedImage) ImageIO.read(new File("titles//govanmap.png"));
			titles[0] = (BufferedImage) ImageIO.read(new File("titles//mainTitleLight.png"));
			titles[1] = (BufferedImage) ImageIO.read(new File("titles//mainTitleDark.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		GridBagConstraints gridCon = new GridBagConstraints();
		gridCon.gridx = 1;
		gridCon.gridy = 1;
		gridCon.anchor = GridBagConstraints.CENTER;
		add(setUpStartMenu(), gridCon);
		add(setUpPlayerPicker(), gridCon);
		
		title = new JLabel();
		gridCon.gridy = 0;
		add(title, gridCon);
		
		
		gridCon.gridx = 0;
		gridCon.gridwidth = 3;
		gridCon.gridheight = 3;
		
		JLabel background = new JLabel(new ImageIcon("titles//govanmap.png"));
		
		add(background, gridCon);
		
		
		
		
		
		
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
	}
	
	public void resize(int screenWidth, int screenHeight) {
		
		buttonWidth = screenWidth/3;
		buttonHeight = screenHeight/15;
		changeColours(background, foregroundColour, textColour, borderColour, darkMode);
	}
	
	
	
	private JPanel setUpStartMenu() {
		
		JPanel start = new JPanel() {
			
			public void paint(Graphics g) {
				setMinimumSize(new Dimension((int) (buttonWidth*1.5)+ 50, (int) (buttonHeight*9)));
				setPreferredSize(new Dimension((int) (buttonWidth*1.5)+ 50, (int) (buttonHeight*9)));
				Graphics2D sqImGraphics = (Graphics2D) g;
				sqImGraphics.setColor(foregroundColour);
				sqImGraphics.fillRoundRect(5,5, this.getWidth()-10, this.getHeight()-10, 30,30);
				sqImGraphics.setStroke(new BasicStroke(10));
				sqImGraphics.setColor(borderColour);
				sqImGraphics.drawRoundRect(5,5, this.getWidth()-10, this.getHeight()-10, 30,30);
				super.paint(g);
			}
			
		};
		
		start.setLayout(new BoxLayout(start, BoxLayout.Y_AXIS));
		
		
		newGame = new JButton("New Game");
		newGame.setOpaque(true);
		newGame.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				start.setVisible(false);
				border.setVisible(true);
			}
			
		});
		
		newGame.setFocusPainted(false);
		newGame.setAlignmentX(Component.CENTER_ALIGNMENT);
		start.add(Box.createVerticalGlue());
		start.add(newGame);
		
		start.add(Box.createVerticalGlue());
		rules = new JButton("Rules");
		rules.setOpaque(true);
		rules.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				
			}
			
		});
		
		rules.setFocusPainted(false);
		rules.setAlignmentX(Component.CENTER_ALIGNMENT);
		start.add(rules);
		start.add(Box.createVerticalGlue());
		displaySettings = new JButton("Display Settings");
		displaySettings.setOpaque(true);
		displaySettings.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				display.openAccessability();
				
			}
			
		});
		
		displaySettings.setFocusPainted(false);
		displaySettings.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		start.add(displaySettings);
		start.add(Box.createVerticalGlue());
		
		exitGame = new JButton("Exit Game");
		exitGame.setOpaque(true);
		exitGame.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
				
			}
			
		});
		
		exitGame.setFocusPainted(false);
		exitGame.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		start.add(exitGame);
		start.add(Box.createVerticalGlue());
		
		start.setOpaque(false);
		return start;
		
	
		
	}
	
	private JPanel setUpPlayerPicker() {
		
		
		
		playerPicker = new JPanel() ;
		playerPicker.setOpaque(false);
		playerPicker.setLayout(new BoxLayout(playerPicker, BoxLayout.Y_AXIS));
		playerPicker.add(addNewPlayerBox());
		playerPicker.add(addPlayerButton());
		
		JScrollPane scroll = new JScrollPane(playerPicker);
		
		border = new JPanel(){
			
			public void paint(Graphics g) {
				
				Graphics2D sqImGraphics = (Graphics2D) g;
				setMinimumSize(new Dimension((int) (buttonWidth*1.5)+ 50, (int) (buttonHeight*9)));
				setPreferredSize(new Dimension((int) (buttonWidth*1.5)+ 50, (int) (buttonHeight*9)));
				scroll.setMinimumSize(new Dimension((int) (buttonWidth*1.5)+20, (int) (buttonHeight*7)));
				scroll.setPreferredSize(new Dimension((int) (buttonWidth*1.5)+20, (int) (buttonHeight*7)));
				sqImGraphics.setColor(foregroundColour);
				sqImGraphics.fillRoundRect(5,5, this.getWidth()-10, this.getHeight()-10, 30,30);
				sqImGraphics.setStroke(new BasicStroke(10));
				sqImGraphics.setColor(borderColour);
				sqImGraphics.drawRoundRect(5,5, this.getWidth()-10, this.getHeight()-10, 30,30);
				
				super.paint(g);
			}
			
		};
		
		scroll.setOpaque(false);
		scroll.getViewport().setOpaque(false);
		scroll.setBorder(null);
		scroll.setViewportBorder(null);
		border.add(scroll, BorderLayout.PAGE_START);
		border.setOpaque(false);
		border.setVisible(false);
		
		
		JButton submitPlayers = new JButton("Continue") {
			
			
			public void paint(Graphics g) {
				
				this.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, borderColour));
				this.setBackground(foregroundColour);
				this.setForeground(textColour);
				this.setFont( new Font("Arial", Font.BOLD, buttonWidth/15));
				this.setMinimumSize(new Dimension((int) (buttonWidth*1.5), buttonHeight));
				this.setPreferredSize(new Dimension((int) (buttonWidth*1.5), buttonHeight));
				this.setMaximumSize(new Dimension((int) (buttonWidth*1.5), buttonHeight));
				super.paint(g);
			}
		};
		
	
		submitPlayers.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				display.openBoard();
			}
			
		});
		
		border.add(submitPlayers, BorderLayout.PAGE_END);
		return border;
	}
	
	private JButton addPlayerButton(){
		JButton addPlayer = new JButton("ADD PLAYER") {
			
			
			public void paint(Graphics g) {
				
				this.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, borderColour));
				this.setBackground(foregroundColour);
				this.setForeground(textColour);
				this.setFont( new Font("Arial", Font.BOLD, buttonWidth/15));
				this.setMinimumSize(new Dimension((int) (buttonWidth*1.5), (int) (buttonWidth*1.5)));
				this.setPreferredSize(new Dimension((int) (buttonWidth*1.5), (int) (buttonWidth*1.5)));
				this.setMaximumSize(new Dimension((int) (buttonWidth*1.5), (int) (buttonHeight*1.5)));
				super.paint(g);
			}
		};
		
		addPlayer.setAlignmentX(Component.CENTER_ALIGNMENT);
		addPlayer.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(players.size()<8) {
					playerPicker.remove(addPlayer);
					playerPicker.add(addNewPlayerBox());
					playerPicker.add(addPlayerButton());
				}
				
			}
			
		});
		
		return addPlayer;
		
		
		
		
	}
	
	private JPanel addNewPlayerBox() {
		
		String[] playerInfo = new String[3];
		
		players.add(playerInfo);
		
		JPanel player = new JPanel(new GridBagLayout()){
			
			public void paint(Graphics g) {
				
				this.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, borderColour));
				this.setBackground(foregroundColour);
				this.setMinimumSize(new Dimension((int) (buttonWidth*1.5), (int) (buttonHeight*1.5)));
				this.setPreferredSize(new Dimension((int) (buttonWidth*1.5), (int) (buttonHeight*1.5)));
				this.setMaximumSize(new Dimension((int) (buttonWidth*1.5), (int) (buttonHeight*1.5)));
				super.paint(g);
			}
			
		};
		player.setOpaque(true);
		player.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		JLabel nameLabel = new JLabel("Name(max 20 char):"){
			
			public void paint(Graphics g) {
				
				
				this.setForeground(textColour);
				this.setFont( new Font("Arial", Font.BOLD, buttonWidth/20));
				super.paint(g);
			}
			
		};
		JTextField name = new JTextField(){
			
			public void paint(Graphics g) {
				
				this.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, borderColour));
				this.setBackground(foregroundColour);
				this.setForeground(textColour);
				this.setFont( new Font("Arial", Font.BOLD, buttonWidth/20));
				super.paint(g);
			}
			
		};
		name.setColumns(15);
		name.addPropertyChangeListener(new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				String playerName = name.getText();
				if (playerName != null) {
					if (playerName.length() > 15) {
						playerName = playerName.substring(0, 20);
						name.setText(playerName);
					}
				}
				playerInfo[0] = playerName;
			}

		});
		
		
		
		
		JLabel colourLabel = new JLabel("Colour:"){
			
			public void paint(Graphics g) {
				
				this.setForeground(textColour);
				this.setFont( new Font("Arial", Font.BOLD, buttonWidth/20));
				super.paint(g);
			}
			
		};
		String[] colours = {"Red", "Orange", "Yellow", "Green", "Blue", "Magenta", "Pink", "Cyan"};
		
		@SuppressWarnings("unchecked")
		JComboBox colourOptions = new JComboBox(colours) {
			public void paint(Graphics g) {
				
				this.setPreferredSize(new Dimension(buttonHeight, buttonHeight/3*2));
				this.setMinimumSize(new Dimension(buttonHeight, buttonHeight/3*2));
				super.paint(g);
			}
		};
		colourOptions.setRenderer(new ComboBoxImages());
		
		colourOptions.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				playerInfo[1] = (String) colourOptions.getSelectedItem();
				
			}
		});
		
		
		
		
		JLabel iconLabel = new JLabel("Icon:"){
			
			public void paint(Graphics g) {
				
				this.setForeground(textColour);
				this.setFont( new Font("Arial", Font.BOLD, buttonWidth/20));
				super.paint(g);
			}
			
		};
		String[] Icon = {"Bike", "Boat", "Bus", "Car", "Helicopter", "Plane", "Train", "Truck"};
		
		@SuppressWarnings("unchecked")
		JComboBox iconOptions = new JComboBox(Icon) {
			public void paint(Graphics g) {
				
				this.setPreferredSize(new Dimension(buttonHeight, buttonHeight/3*2));
				this.setMinimumSize(new Dimension(buttonHeight, buttonHeight/3*2));
				super.paint(g);
			}
		};
		iconOptions.setRenderer(new ComboBoxImages());
		
		iconOptions.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				playerInfo[2] = (String) iconOptions.getSelectedItem();
				
			}
		});
		
		
		
		
		
		
		JButton remove = new JButton("Remove"){
			
			public void paint(Graphics g) {
				
				this.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, borderColour));
				this.setBackground(foregroundColour);
				this.setForeground(textColour);
				this.setFont( new Font("Arial", Font.BOLD, buttonWidth/20));
				super.paint(g);
			}
			
		};
		
		remove.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				playerPicker.remove(player);
				players.remove(playerInfo);
			}
			
		});
		
		
		GridBagConstraints gridCon = new GridBagConstraints();
		
		player.add(nameLabel, gridCon);
		gridCon.gridy = 1;
		gridCon.weightx = 1;
		player.add(name, gridCon);
		//gridCon.weightx = 0;
		
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
	
	public void changeColours(Color background, Color squares, Color text, Color border, boolean dark) {
		this.background = background;
		this.foregroundColour = squares;
		this.textColour = text;
		this.borderColour = border;
		this.darkMode = dark;
		this.setBackground(background);
		
		if(dark) {
			title.setIcon(new ImageIcon(titles[1].getScaledInstance(buttonWidth*2,buttonHeight*4,Image.SCALE_FAST)));
		}else {
			title.setIcon(new ImageIcon(titles[0].getScaledInstance(buttonWidth*2,buttonHeight*4,Image.SCALE_FAST)));
		}
		
		
		newGame.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, borderColour));
		newGame.setBackground(foregroundColour);
		newGame.setForeground(textColour);
		newGame.setFont( new Font("Arial", Font.BOLD, buttonWidth/10));
		newGame.setMinimumSize(new Dimension(buttonWidth, buttonHeight));
		newGame.setPreferredSize(new Dimension(buttonWidth, buttonHeight));
		
		rules.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, borderColour));
		rules.setBackground(foregroundColour);
		rules.setForeground(textColour);
		rules.setFont( new Font("Arial", Font.BOLD, buttonWidth/10));
		
		displaySettings.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, borderColour));
		displaySettings.setBackground(foregroundColour);
		displaySettings.setForeground(textColour);
		displaySettings.setFont( new Font("Arial", Font.BOLD, buttonWidth/10));
		
		exitGame.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, borderColour));
		exitGame.setBackground(foregroundColour);
		exitGame.setForeground(textColour);
		exitGame.setFont( new Font("Arial", Font.BOLD, buttonWidth/10));
		
		repaint();
		revalidate();
		
	}
	
	private class ComboBoxImages extends JLabel implements ListCellRenderer{
		
		
		public ComboBoxImages() {
			setOpaque(true);
			setHorizontalAlignment(CENTER);
			setVerticalAlignment(CENTER);
		}
		
		


		@Override
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
				boolean cellHasFocus) {
			
			String imageName = (String) (value);

			BufferedImage icon = null;
			try {
				if (imageName.equals("Bike")) {
					icon = (BufferedImage) ImageIO.read(new File("icons\\Bike.png"));
				} else if (imageName.equals("Boat")) {
					icon = (BufferedImage) ImageIO.read(new File("icons\\Boat.png"));				
				} else if (imageName.equals("Bus")) {
					icon = (BufferedImage) ImageIO.read(new File("icons\\Bus.png"));
				} else if (imageName.equals("Car")) {
					icon = (BufferedImage) ImageIO.read(new File("icons\\Car.png"));
				} else if (imageName.equals("Helicopter")) {
					icon = (BufferedImage) ImageIO.read(new File("icons\\Helicopter.png"));
				} else if (imageName.equals("Plane")) {
					icon = (BufferedImage) ImageIO.read(new File("icons\\Plane.png"));
				} else if (imageName.equals("Train")) {
					icon = (BufferedImage) ImageIO.read(new File("icons\\Train.png"));
				} else if (imageName.equals("Truck")) {
					icon = (BufferedImage) ImageIO.read(new File("icons\\Truck.png"));
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
			if(icon != null) {
				setIcon(new ImageIcon(icon.getScaledInstance(buttonHeight/3*2,buttonHeight/3*2,Image.SCALE_FAST)));
				return this;
			}
		
			BufferedImage colour = new BufferedImage(100, 100,  BufferedImage.TYPE_INT_RGB);
			Graphics2D graphics = colour.createGraphics();
			
			
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
			
			graphics.fillRect(0, 0, 100, 100);
			
			graphics.dispose();
			
			setIcon(new ImageIcon(colour));
			
			return this;
		}
		
	}
	

}
