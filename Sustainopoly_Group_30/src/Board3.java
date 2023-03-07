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



public class Board3 extends JLayeredPane implements MouseListener{
	
	private Color background, foregroundColour, textColour, borderColour;
	
	private JButton squareButtons[], diceButton, endTurn;
	
	private JLabel dice;
	private ImageIcon[] dicePics;
	
	private GridBagConstraints squarePanelCon;
	private GridBagConstraints infoCon;
	
	private JMenu menu;

	private JPanel squaresPanel, infoPanel, confirm, back, overlay, animationPanel, backgroundPanel, squareInfo;

	
	private int SQUARE_WIDTH = 150;
	private int SQUARE_TOP_HEIGHT = 50;
	private int numOfSquares = 20;
	private int zoom = 3;
	private int boardWidth = 1000;
	private int screenWidth;
	private int screenHeight;
	
	private int currentRotation = 0;
	
	private JLabel player, money, time;



	private JLabel boardImage, title, backgroundJLabel;
	
	private BufferedImage[] topDice; 
	private BufferedImage[] botDice;
	private BufferedImage backgroundDice;
	
	private JMenuItem rules, accessibility, exit;
	
	private SquarePicker SP = new SquarePicker();
	
	private Square[] squares;
	private Display display;
	private boolean disableButtons = false;
	
	double rotation = 0;
	
	private BufferedImage fullBoard, backgroundPict;
	private Image[] squareAnimationImages;
	private BufferedImage[] titles;
	
	private JSpinner moneyNumber, timeNumber;
	private JSlider spendMoney, spendTime;
	
	private boolean isZoomed = false;
	private boolean finishedAnimation = false;
	private boolean darkMode = false;
	private int focusedSquare;
	
	private int[] playerPositions;
	private BufferedImage[] playerIcons;
	
	public Board3( Square[] squares,  Display display, BufferedImage[] playerIcons ) {
		this.display = display;
		this.squares = squares;
		this.playerIcons = playerIcons;
		playerPositions = new int[playerIcons.length];
		for(int i = 0; i< playerIcons.length; i++) playerPositions[i] = 0;
		
		
		overlay = new JPanel();
		
		numOfSquares = squares.length - squares.length%4;
		
		squaresPanel = new JPanel() {
			public void paint(Graphics g) {
			
			Graphics2D g2D = (Graphics2D)g;
			g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
			
			int wid = getWidth();
			int high = getHeight();
			
			g2D.rotate(Math.toRadians(rotation), wid/2, high/2);
			
			super.paint(g);
			
		}};
		
		setUpSquares();
		setUpInfo();
		
		
		
		setPreferredSize(new Dimension(1000,1000));
		
		
		
		overlay.setOpaque(false);
		squaresPanel.setOpaque(false);
		squaresPanel.setBackground(Color.YELLOW);
		
		squarePanelCon.gridx = 0;
		squarePanelCon.gridy = 0;
		squarePanelCon.weighty = 1;
		squarePanelCon.weightx = 1;
		setUpDice();
		//Add(sqex, -100);
		squaresPanel.setBounds(0,0,1000,1000);
		overlay.setBounds(000,000,100,100);
		animationPanel.setBounds(200,200,1000,600);
		infoPanel.setBounds(000,000,100,100);
		add(squaresPanel,  1, 1);
		add(overlay, 5, 5);
		add(animationPanel, 3, 3);
		add(infoPanel, 4,4);
	
		squarePanelCon.gridx = 3;
		squarePanelCon.anchor = GridBagConstraints.NORTH;
		//add(infoPanel, squarePanelCon);
		squarePanelCon.anchor = GridBagConstraints.CENTER;
		
		setUpMenu();
		
		backgroundPanel = new JPanel();
		backgroundPanel.setLayout(new BoxLayout(backgroundPanel, BoxLayout.Y_AXIS));
		add(backgroundPanel,0,0);
		backgroundPanel.setBounds(0,0,1000,1000);
		backgroundPanel.setOpaque(false);
		backgroundJLabel = new JLabel();
		backgroundPanel.add(backgroundJLabel);
		try {
			backgroundPict = (BufferedImage) ImageIO.read(new File("titles//govanmap.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		changeColours(new Color(56, 163, 165),new Color(87, 204, 153),Color.BLACK,new Color(34, 87, 122),darkMode);
		
		//for(int i = 0; i < numOfSquares; i++) {
		//	squareButtons[i].setEnabled(false);
		//}
	
		
		
	}
	
	public void resize(int x, int y) {
	
		setBounds(0,0,x,y);
		squaresPanel.setBounds(0,0,x,y);
		overlay.setBounds(0,0,x,y);
		animationPanel.setBounds(0,0,x,y);
		backgroundPanel.setBounds(0,0,x,y);
		infoPanel.setBounds(000,000,x,y);
		screenWidth = x;
		screenHeight = y;
		
		if(screenWidth >= screenHeight ) {
			boardWidth = screenHeight - screenHeight/10;
		} else {
			boardWidth = screenWidth - screenWidth/10;
		}
		
		SQUARE_WIDTH = boardWidth/numOfSquares*3;
		SQUARE_TOP_HEIGHT = boardWidth/numOfSquares;
		changeColours(background, foregroundColour, textColour, borderColour, darkMode);
		
		if(isZoomed) {
			isZoomed = false;
			Timer rollDice = new Timer();
			rollDice.scheduleAtFixedRate(new BoardZoom(false), 0, 10);
			infoPanel.remove(squareInfo);
			
		}
		
		
	}
	
	private void setUpMenu() {
		overlay.setLayout(new GridBagLayout());
		
		
		menu = new JMenu("MENU");
		rules = new JMenuItem("Rules");
		rules.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				display.openRules();
			}
		});
		rules.setFont(new Font("Arial", Font.PLAIN, 40));
		menu.add(rules);
		
		accessibility = new JMenuItem("Accessibility");
		accessibility.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				display.openDisplayOptions();
			}
		});
		accessibility.setFont(new Font("Arial", Font.PLAIN, 40));
		menu.add(accessibility);
		
		exit = new JMenuItem("Exit");
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				getConfirmation(new ActionListener() {
					public void actionPerformed(ActionEvent e)
					{
						display.returnToMainMenu();
						removeConfirmationPanel();
					}
				}, "Are you sure you want to exit\nYou will lose all your progress");
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
		menuBar.setBorderPainted(false);;
		menuBar.setOpaque(false);
		
		squarePanelCon.anchor = GridBagConstraints.NORTHEAST;
		squarePanelCon.gridx = 1;
		squarePanelCon.gridy = 0;
		overlay.add(menuBar, squarePanelCon);
		squarePanelCon.anchor = GridBagConstraints.CENTER;
	}

	public void getConfirmation(ActionListener output, String messageText) {
		
		disableButtons(true);
		
		confirm = new JPanel();
		SpringLayout sprLayout = new SpringLayout();  
		confirm.setLayout(sprLayout);
		confirm.setBackground(background);
		
		
		JTextPane message = new JTextPane();
		StyledDocument styledDoc = message.getStyledDocument();
		try {
			styledDoc.insertString(0, "\n\n" + messageText, null);
		} catch (BadLocationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		SimpleAttributeSet center = new SimpleAttributeSet();
		StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
		styledDoc.setParagraphAttributes(0, styledDoc.getLength(), center, false);
		message.setEditable(false);
		message.setForeground(textColour);
		message.setBackground(foregroundColour);
		message.setFont(new Font("Arial", Font.PLAIN, 40));
		message.setPreferredSize(new Dimension(400, 400));
		
		confirm.add(message);
		
		JButton yes = new JButton("yes");
		yes.addActionListener(output);
		yes.setFont(new Font("Arial", Font.PLAIN, 30));
		yes.setBackground(foregroundColour);
		yes.setForeground(textColour);
		
		JButton no = new JButton("no");
		no.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				removeConfirmationPanel();
			}
		});
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
		overlay.add(confirm,squarePanelCon);
		
		back = new JPanel();
		back.setBackground(new Color(165, 165, 165, 200));
		
		squarePanelCon.fill = GridBagConstraints.BOTH;
		
		
		back.setOpaque(true);
		overlay.add(back,squarePanelCon);
		
		repaint();
		revalidate();
	}
	
	public void removeConfirmationPanel() {
		disableButtons(false);
		overlay.remove(confirm);
		overlay.remove(back);
		repaint();
		revalidate();
	}
	
	public void disableButtons(boolean disable) {
		
		disableButtons = disable;
		menu.setEnabled(!disable);
		

	}
	
	private void setUpSquares() {
		
		
		
		
		squaresPanel.addMouseListener(this);
		
		squaresPanel.setLayout(new GridBagLayout());
		squarePanelCon = new GridBagConstraints();
		squaresPanel.setOpaque(true);
		squaresPanel.setBackground(Color.YELLOW);
		
		
		squareButtons = new JButton[numOfSquares];
		
		squarePanelCon.gridx = 1;
		squarePanelCon.gridy = 1;
		
		for(int i = 0; i <numOfSquares/4; i++) {
			
			squareButtons[i] = new JButton() {
				public void repaint() {
					
			    }
			};
			squareButtons[i].addActionListener(SP);
			squaresPanel.add(squareButtons[i], squarePanelCon);
			squarePanelCon.gridx++;
			
		}
		
		for(int i = (numOfSquares/4); i < numOfSquares/2; i++) {
			
			squareButtons[i] = new JButton() {
				public void repaint() {
				
			    }
			};
			squareButtons[i].addActionListener(SP);
			squaresPanel.add(squareButtons[i], squarePanelCon);
			squarePanelCon.gridy++;
			
		}
		
		for(int i = (numOfSquares/2); i < numOfSquares/4*3; i++) {

			squareButtons[i] = new JButton() {
				public void repaint() {
				
			    }
			};
			squareButtons[i].addActionListener(SP);
			squaresPanel.add(squareButtons[i], squarePanelCon);
			squarePanelCon.gridx--;
		}

		for(int i = (numOfSquares/4*3); i < numOfSquares; i++) {
			
			squareButtons[i] = new JButton() {
				public void repaint() {
					
			    }
			};
			squareButtons[i].addActionListener(SP);
			squaresPanel.add(squareButtons[i], squarePanelCon);
			squarePanelCon.gridy--;
	
		}
		
		
		
		squarePanelCon.gridx = 0;
		squarePanelCon.gridy = 0;
		squarePanelCon.gridwidth = numOfSquares/4 + 3;
		squarePanelCon.gridheight = 1;
	
		
		squaresPanel.add(new JLabel(), squarePanelCon);
		squarePanelCon.gridy = numOfSquares/4 + 2;
		squaresPanel.add(new JLabel(), squarePanelCon);
		squarePanelCon.gridwidth = 1;
		squarePanelCon.gridheight = numOfSquares/4 + 3;
		squarePanelCon.gridy = 0;
		squaresPanel.add(new JLabel(), squarePanelCon);
		squarePanelCon.gridx = numOfSquares/4 + 2;
		squaresPanel.add(new JLabel(), squarePanelCon);
		
		squarePanelCon.weightx = 0;
		squarePanelCon.weighty = 0;
		squarePanelCon.gridx = 2;
		squarePanelCon.gridy = 2;
		squarePanelCon.gridwidth = numOfSquares/4-1;
		squarePanelCon.gridheight = numOfSquares/4-1;
		
		title = new JLabel();
		squaresPanel.add(title, squarePanelCon);
		titles = new BufferedImage[2];
		
		
		try {
			titles[0] = (BufferedImage) ImageIO.read(new File("titles//sustainopolyTitleLight1.png"));
			titles[1] = (BufferedImage) ImageIO.read(new File("titles//sustainopolyTitleDark1.png"));
		} catch (IOException e) {
		}
		
	}
	

	private void setUpDice(){
		
		dicePics = new ImageIcon[60];
		
		for(int i = 1; i <= 59; i++)
		{
			dicePics[i-1]= new ImageIcon("dice//dice ("+i+").png");
		}
		
		dice = new JLabel();
		
		topDice = new BufferedImage[6];
		botDice = new BufferedImage[6];
		
		for(int i = 1; i <= 6; i++) {
			
				try {
					topDice[i-1] = ImageIO.read(new File("dice//dicetop" + i + ".png"));
					botDice[i-1] = ImageIO.read(new File("dice//dicebot" + i + ".png"));
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
			public void actionPerformed(ActionEvent e)
			{
				Timer rollDice = new Timer();
				rollDice.scheduleAtFixedRate(new DiceRoller(), 0, 40);
				
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
	

	private void setUpInfo() {
		infoPanel = new JPanel();
		infoPanel.setLayout(new GridBagLayout());
		infoCon = new GridBagConstraints();
		
		
		player = new JLabel("Player Name:");
		
		infoCon.ipady = 5;
		infoCon.ipadx = 60;
		infoCon.gridx = 1;
		infoCon.gridy = 0;
		infoCon.insets = new Insets(10, 10, 10, 10);
		
		player.setOpaque(true);
		
		infoPanel.add(player, infoCon);
		
		
		money = new JLabel(" Money: �0.00");
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
				
				
			}
			
		});
		endTurn.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, borderColour));
		endTurn.setBackground(foregroundColour);
		endTurn.setForeground(textColour);
		endTurn.setFont( new Font("Arial", Font.BOLD, screenWidth/70));
		endTurn.setFocusPainted(false);
		infoCon.insets = new Insets(10, 10, 50, 30);
		infoCon.anchor = GridBagConstraints.SOUTHEAST;
		infoPanel.add(endTurn, infoCon);
		
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
		//infoPanel.add(spacer3, infoCon);
		
		infoPanel.setOpaque(false);
		
		
		
	}
	
	public void movePlayer(int playerNum, int pos) {
		
		playerPositions[playerNum-1] = pos;
		changeColours(background, foregroundColour, textColour, borderColour, darkMode);
		
		
	}
	
	
	public void setPlayer(String playerName) {
		
	}
	
	public void setMoney(double money) {
		
	}
	
	public void setTime(int time) {
		
	}
	
	
	public void setProgression(int progress) {
		
	}
	
	public void displaySquareInfo(int squareNum){
		
		
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
				sqImGraphics.fillRoundRect(5,5, this.getWidth()-10, this.getHeight()-10, 30,30);
				sqImGraphics.setStroke(new BasicStroke(10));
				sqImGraphics.setColor(borderColour);
				sqImGraphics.drawRoundRect(5,5, this.getWidth()-10, this.getHeight()-10, 30,30);
				
				super.paint(g);
			}
			
		};
		squareInfo.setPreferredSize(new Dimension((int) (screenWidth/3.5), screenHeight- screenHeight/4));
		squareInfo.setOpaque(false);
		squareInfo.setVisible(false);
		
		infoPanel.add(squareInfo, infoCon);
		
		squareInfo.setLayout(new GridBagLayout());
		
		JTextPane title = new JTextPane();
		
		SimpleAttributeSet centre = new SimpleAttributeSet();
		StyleConstants.setAlignment(centre, StyleConstants.ALIGN_CENTER);
		title.setParagraphAttributes(centre, false);
		
		title.setFont(new Font("Arial", Font.BOLD, (int) (screenWidth/3.5/7.5)));
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
		
		info.setFont(new Font("Arial", Font.BOLD, (int) (screenWidth/75)));
		info.setText(squares[squareNum].getInfo());
		info.setForeground(textColour);
		info.setEditable(false);
		info.setOpaque(false);
		
		JScrollPane scrollInfo = new JScrollPane(info);
		scrollInfo.setOpaque(false);
		scrollInfo.setBorder(null);
		scrollInfo.getViewport().setOpaque(false);
		scrollInfo.getViewport().setBorder(null);
		scrollInfo.setPreferredSize(new Dimension((int) (screenWidth/3.5)-15,500));
		
		infoCon.gridy = 1;
		infoCon.weightx = 1;
		infoCon.weighty = 1;
		infoCon.fill = GridBagConstraints.BOTH;
		infoCon.insets = new Insets(10, 20, 20, 20);
		squareInfo.add(scrollInfo, infoCon);
	}
	
	public void displaySquareInfoAndResources(int squareNum){
		
		displaySquareInfo(squareNum);
		
		infoCon.weightx = 0;
		infoCon.weighty = 0;
		infoCon.gridy = 2;
		
		JLabel resourceSubmission = new JLabel("Select the amount you want to enter:");
		resourceSubmission.setForeground(textColour);
		resourceSubmission.setFont( new Font("Arial", Font.BOLD, screenWidth/80));
		
		squareInfo.add(resourceSubmission, infoCon);
		
		spendMoney = new JSlider(JSlider.HORIZONTAL, 0,squares[squareNum].getMaxMoney() - squares[squareNum].getMoney(), 0 );
		spendMoney.setForeground(textColour);
		spendMoney.setFont( new Font("Arial", Font.BOLD, screenWidth/80));
		
		spendMoney.setMajorTickSpacing(500);
		spendMoney.setMinorTickSpacing(100);
		spendMoney.setPaintTicks(true);
		spendMoney.setPaintLabels(true);
		spendMoney.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				moneyNumber.setValue(spendMoney.getValue());
				
			}
			
		});
		spendMoney.setBackground(background);
		
		spendTime = new JSlider(JSlider.HORIZONTAL, 0,squares[squareNum].getMaxTime()- squares[squareNum].getTime(), 0 );
		spendTime.setForeground(textColour);
		spendTime.setFont( new Font("Arial", Font.BOLD, screenWidth/80));
		
		spendTime.setMajorTickSpacing(50);
		spendTime.setMinorTickSpacing(10);
		spendTime.setPaintTicks(true);
		spendTime.setPaintLabels(true);
		spendTime.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				timeNumber.setValue(spendTime.getValue());
				
			}
			
		});
		spendTime.setBackground(background);
		
		
		infoCon.gridy = 4;
		
		infoCon.fill = GridBagConstraints.HORIZONTAL;
		
		squareInfo.add(spendMoney, infoCon);
		
		infoCon.gridy = 6;
		squareInfo.add(spendTime, infoCon);
		
		infoCon.gridwidth = 1;
		infoCon.gridy = 3;
		infoCon.fill = GridBagConstraints.NONE;
		
		JLabel moneySpinnerLabel = new JLabel("Money:");
		squareInfo.add(moneySpinnerLabel, infoCon);
		moneySpinnerLabel.setForeground(textColour);
		moneySpinnerLabel.setFont( new Font("Arial", Font.BOLD, screenWidth/80));
		
		infoCon.gridy = 5;
		
		JLabel timeSpinnerLabel = new JLabel("Time:");
		squareInfo.add(timeSpinnerLabel, infoCon);
		timeSpinnerLabel.setForeground(textColour);
		timeSpinnerLabel.setFont( new Font("Arial", Font.BOLD, screenWidth/80));
		
		
		moneyNumber = new JSpinner();
		SpinnerNumberModel numSpin = new SpinnerNumberModel(0, 0, squares[squareNum].getMaxMoney() - squares[squareNum].getMoney() , 50);
		
		moneyNumber.setModel(numSpin); 
		numSpin.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				spendMoney.setValue(numSpin.getNumber().intValue());
				
			}
			
		});
		
		
		
		timeNumber = new JSpinner();
		
		SpinnerNumberModel numSpin2 = new SpinnerNumberModel(0, 0, squares[squareNum].getMaxTime() - squares[squareNum].getTime() , 10);
		
		timeNumber.setModel(numSpin2); 
		numSpin2.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				spendTime.setValue(numSpin2.getNumber().intValue());
				
			}
			
		});
		
		infoCon.gridx = 1;
		infoCon.gridy = 3;
		infoCon.anchor = GridBagConstraints.WEST;
		infoCon.insets = new Insets(0, 0, 0, 0);
		
		
		squareInfo.add(moneyNumber, infoCon);
		
		infoCon.gridy = 5;
		
		squareInfo.add(timeNumber, infoCon);
		
		
		JButton submitResources = new JButton("submitResources") ;
		submitResources.setFocusPainted(false);
		infoCon.gridx = 0;
		infoCon.gridy = 7;
		infoCon.gridwidth = 2;
		submitResources.setOpaque(true);
		submitResources.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
		   
		        	
		        
				
			}
			
		});
		infoCon.insets = new Insets(10, 10, 50, 30);
		infoCon.anchor = GridBagConstraints.SOUTHEAST;
		submitResources.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, borderColour));
		submitResources.setBackground(foregroundColour);
		submitResources.setForeground(textColour);
		submitResources.setFont( new Font("Arial", Font.BOLD, screenWidth/80));
		squareInfo.add(submitResources, infoCon);
		
	}
	
	
	

	
	public void changeColours(Color background, Color squares, Color text, Color border, boolean dark) {
		
		int screenWidth = (int) squaresPanel.getSize().getWidth();
		int screenHeight = (int) squaresPanel.getSize().getHeight();
		
		this.background = background;
		this.foregroundColour = squares;
		this.textColour = text;
		this.borderColour = border;
		this.darkMode = dark;
		this.setBackground(background);
		
		
		
		backgroundJLabel.setIcon(new ImageIcon(backgroundPict));
		this.setOpaque(true);
		
		squaresPanel.setOpaque(false);
		

		player.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, border));
		player.setFont( new Font("Arial", Font.BOLD, screenWidth/70));
		player.setBackground(foregroundColour);
		player.setForeground(text);
		money.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, border));
		money.setBackground(foregroundColour);
		money.setForeground(text);
		money.setFont( new Font("Arial", Font.BOLD, screenWidth/70));
		time.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, border));
		time.setBackground(foregroundColour);
		time.setForeground(text);
		time.setFont( new Font("Arial", Font.BOLD, screenWidth/70));
		
		endTurn.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, borderColour));
		endTurn.setBackground(foregroundColour);
		endTurn.setForeground(textColour);
		endTurn.setFont( new Font("Arial", Font.BOLD, screenWidth/70));
		
		menu.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, border));
		menu.setForeground(text);
		menu.setBackground(foregroundColour);
		
		
		rules.setBackground(foregroundColour);
		accessibility.setBackground(foregroundColour);
		exit.setBackground(foregroundColour);
		rules.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, border));
		accessibility.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, border));
		exit.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, border));
		rules.setForeground(text);
		accessibility.setForeground(text);
		exit.setForeground(text);
		
		
		
		for(int i = 1; i < numOfSquares/4; i++) {
			squareButtons[i].setIcon(new ImageIcon(createSquareImage(i, SQUARE_WIDTH, SQUARE_TOP_HEIGHT, 1, (int) (SQUARE_WIDTH/7.5))));
		}
		for(int i = numOfSquares/4; i < numOfSquares/2; i++) {
			squareButtons[i].setIcon(new ImageIcon(createSquareImage(i, SQUARE_WIDTH, SQUARE_TOP_HEIGHT, 2, (int) (SQUARE_WIDTH/7.5))));	
			}
		for(int i = numOfSquares/2; i < numOfSquares/4*3; i++) {
			squareButtons[i].setIcon(new ImageIcon(createSquareImage(i, SQUARE_WIDTH, SQUARE_TOP_HEIGHT, 3, (int) (SQUARE_WIDTH/7.5))));	
			}
		for(int i = numOfSquares/4*3; i < numOfSquares; i++) {
			squareButtons[i].setIcon(new ImageIcon(createSquareImage(i, SQUARE_WIDTH, SQUARE_TOP_HEIGHT, 4, (int) (SQUARE_WIDTH/7.5))));
			}
		
		
		squareButtons[0].setIcon(new ImageIcon(createSquareImage(0, SQUARE_WIDTH + SQUARE_TOP_HEIGHT, 0, 1, (int) (SQUARE_WIDTH/6.25))));
		squareButtons[numOfSquares/4].setIcon(new ImageIcon(createSquareImage(numOfSquares/4, SQUARE_WIDTH + SQUARE_TOP_HEIGHT, 0, 2, (int) (SQUARE_WIDTH/6.25))));
		squareButtons[numOfSquares/2].setIcon(new ImageIcon(createSquareImage(numOfSquares/2, SQUARE_WIDTH + SQUARE_TOP_HEIGHT, 0, 3, (int) (SQUARE_WIDTH/6.25))));
		squareButtons[numOfSquares/4*3].setIcon(new ImageIcon(createSquareImage(numOfSquares/4*3, SQUARE_WIDTH + SQUARE_TOP_HEIGHT, 0, 4, (int) (SQUARE_WIDTH/6.25))));
		
		for(int i = 0; i< numOfSquares; i++) {
			
			squareButtons[i].setMargin(new Insets(-3,-3,-3,-3));
			squareButtons[i].setBorderPainted(false);
			squareButtons[i].setOpaque(false);
		}
	
		int picWidth = (numOfSquares/4-1)* SQUARE_WIDTH;
		if(!dark) {
			title.setIcon(new ImageIcon(titles[0].getScaledInstance(picWidth,picWidth,Image.SCALE_FAST)));
		} else {
			title.setIcon(new ImageIcon(titles[1].getScaledInstance(picWidth,picWidth,Image.SCALE_FAST)));
		}
		
		
		revalidate();
		repaint(); 
		
	}
	



	private void expandBoard() {
		
		int screenWidth = (int) squaresPanel.getSize().getWidth();
		int screenHeight = (int) squaresPanel.getSize().getHeight();
		
		
	
		BufferedImage squareAnimationImages[] = new BufferedImage[numOfSquares];
		
		for(int i = 1; i < numOfSquares/4; i++) {
			squareAnimationImages[i] = createSquareImage(i, SQUARE_WIDTH*zoom, SQUARE_TOP_HEIGHT*zoom, 1, (int) (SQUARE_WIDTH/7.5*zoom));
		}
		for(int i = numOfSquares/4 + 1; i < numOfSquares/2; i++) {
			squareAnimationImages[i] = createSquareImage(i, SQUARE_WIDTH*zoom, SQUARE_TOP_HEIGHT*zoom, 2, (int) (SQUARE_WIDTH/7.5*zoom));
		}
		for(int i = numOfSquares/2 + 1; i < numOfSquares/4*3; i++) {
			squareAnimationImages[i] = createSquareImage(i, SQUARE_WIDTH*zoom, SQUARE_TOP_HEIGHT*zoom, 3, (int) (SQUARE_WIDTH/7.5*zoom));
		}
		for(int i = numOfSquares/4*3 + 1; i < numOfSquares; i++) {
			squareAnimationImages[i] = createSquareImage(i, SQUARE_WIDTH*zoom, SQUARE_TOP_HEIGHT*zoom, 4, (int) (SQUARE_WIDTH/7.5*zoom));
		}
		
		squareAnimationImages[0] = createSquareImage(0, (SQUARE_WIDTH + SQUARE_TOP_HEIGHT)*zoom, 0, 1, (int) ((int)(SQUARE_WIDTH/6.25)*zoom));
		squareAnimationImages[numOfSquares/4] = createSquareImage(numOfSquares/4, (SQUARE_WIDTH + SQUARE_TOP_HEIGHT)*zoom, 0, 2, (int) (SQUARE_WIDTH/6.25*zoom));
		squareAnimationImages[numOfSquares/2] = createSquareImage(numOfSquares/2, (SQUARE_WIDTH + SQUARE_TOP_HEIGHT)*zoom, 0, 3, (int) (SQUARE_WIDTH/6.25)*zoom);
		squareAnimationImages[numOfSquares/4*3] = createSquareImage(numOfSquares/4*3, (SQUARE_WIDTH + SQUARE_TOP_HEIGHT)*zoom, 0, 4, (int) (SQUARE_WIDTH/6.25*zoom));
		
		
		
		
			Graphics2D sqImGraphics = null;
			fullBoard = new BufferedImage(screenWidth*zoom,screenHeight*zoom, BufferedImage.TYPE_INT_ARGB);
			sqImGraphics = fullBoard.createGraphics();
		int offsetx = (screenWidth- boardWidth)/2*zoom;
		int offsety = (screenHeight - boardWidth)/2*zoom;
		
		
		sqImGraphics.drawImage(squareAnimationImages[0], null, offsetx, offsety);
		offsetx += (SQUARE_WIDTH + SQUARE_TOP_HEIGHT)*zoom;
		for(int i = 1; i < numOfSquares/4; i++) {
			sqImGraphics.drawImage(squareAnimationImages[i], null, offsetx, offsety);
			offsetx += SQUARE_WIDTH*zoom;
		}
		sqImGraphics.drawImage(squareAnimationImages[numOfSquares/4], null, offsetx, offsety);
		offsety += (SQUARE_WIDTH + SQUARE_TOP_HEIGHT)*zoom;
		for(int i = numOfSquares/4 + 1; i < numOfSquares/2; i++) {
			sqImGraphics.drawImage(squareAnimationImages[i], null, offsetx, offsety);
			offsety += SQUARE_WIDTH*zoom;
		}
		sqImGraphics.drawImage(squareAnimationImages[numOfSquares/2], null, offsetx, offsety);
		for(int i = numOfSquares/2 + 1; i < numOfSquares/4*3; i++) {
			offsetx -= SQUARE_WIDTH*zoom;
			sqImGraphics.drawImage(squareAnimationImages[i], null, offsetx, offsety);			
		}
		offsetx -= (SQUARE_WIDTH + SQUARE_TOP_HEIGHT)*zoom;
		sqImGraphics.drawImage(squareAnimationImages[numOfSquares/4*3], null, offsetx, offsety);
		
		for(int i = numOfSquares/4*3 + 1; i < numOfSquares; i++) {
			offsety -= SQUARE_WIDTH*zoom;
			sqImGraphics.drawImage(squareAnimationImages[i], null, offsetx, offsety);
		}
		offsetx += (SQUARE_WIDTH + SQUARE_TOP_HEIGHT)*zoom;
		int picWidth = (numOfSquares/4-1)* SQUARE_WIDTH * zoom;
		if(!darkMode) {
			sqImGraphics.drawImage( titles[0].getScaledInstance(picWidth,picWidth,Image.SCALE_FAST), offsetx, offsety, null);
		} else {
			sqImGraphics.drawImage( titles[1].getScaledInstance(picWidth,picWidth,Image.SCALE_FAST), offsetx, offsety, null);
		}
		
		
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
	
	private class createAnimation implements Runnable{
		
		private int rotation;
		private int squarePos;

		public createAnimation(int rotation, int squarePos) {
			this.rotation = rotation;
			this.squarePos = (squarePos -3)*6 + 3;
			
			if(squarePos == 0) this.squarePos --;
			if(squarePos == 5) this.squarePos ++;
		}
		
		@Override
		public void run() {
			finishedAnimation = false;
			expandBoard();
			
			squareAnimationImages = new Image[31];
			
			
			
			
			
			Graphics2D sqImGraphics = null;
			
			BufferedImage tempImage = new BufferedImage(screenWidth*zoom,screenHeight*zoom, BufferedImage.TYPE_INT_ARGB);
			sqImGraphics = tempImage.createGraphics();
			sqImGraphics.setColor(background);
			sqImGraphics.fillRect(0, 0, screenWidth*zoom,screenHeight*zoom);
			sqImGraphics.drawImage( backgroundPict.getScaledInstance(1920*zoom,1311*zoom,Image.SCALE_FAST), 0, 0, null);
				
			sqImGraphics.rotate(Math.toRadians(90 * rotation), screenWidth*zoom/2, screenHeight*zoom/2);
			sqImGraphics.drawImage(fullBoard, null, 0, 0);
			
			for(int i = 0; i < 31; i++) {
				
				int width = (screenWidth * zoom) -(screenWidth * zoom)*i/90 * 2;
				int xOffset = (screenWidth*zoom/3 + boardWidth *squarePos/20 * zoom/2) * i/30;
				if(xOffset < 0) xOffset = 0;
				else if(xOffset + width > screenWidth * zoom) xOffset = screenWidth * zoom - width;
				
				BufferedImage buffImage = tempImage.getSubimage(xOffset,(screenHeight *zoom/3) * i/30 * 2, width, (screenHeight * zoom) - (screenHeight * zoom) * i/90*2);
				
				
				
				squareAnimationImages[i] = buffImage.getScaledInstance(screenWidth,screenHeight,Image.SCALE_FAST);
			}
			
			
			
			sqImGraphics.dispose();
			finishedAnimation = true;
		}
		
	}
	
	
	
	private class DiceRoller extends TimerTask 
	{

	
		
		public DiceRoller() {
			super();
			
			
			diceButton.setIcon(null);
			//finalDice = game.rolldice();
		
			int[] finalDice = {2,6};
			
			BufferedImage finalImage = new BufferedImage(960, 540, BufferedImage.TYPE_INT_ARGB);
			Graphics2D sqImGraphics = null;
			sqImGraphics = finalImage.createGraphics();
			
			
			sqImGraphics.drawImage(topDice[finalDice[0]-1], null, 0, 0);
			sqImGraphics.drawImage(botDice[finalDice[1]-1], null, 0, 0);
			
			dicePics[59] = new ImageIcon(finalImage);
			sqImGraphics.dispose();
			
			backgroundDice = new BufferedImage(220, 140, BufferedImage.TYPE_INT_ARGB);
			sqImGraphics = backgroundDice.createGraphics();
			
			sqImGraphics.drawImage(botDice[finalDice[0]-1], null, -310, -410);
			sqImGraphics.drawImage(topDice[finalDice[1]-1], null, -210, -180);
			
			sqImGraphics.dispose();
			
			
		}
		
		private int dieNum = 0;
		@Override
		public void run() {

			if(dieNum < 60) 
			{
				
				dice.setIcon(dicePics[dieNum]);
			
				animationPanel.repaint();

			}
			
			
			dieNum++;
			
			
			if(dieNum > 120) 
			{
				dice.setIcon(null);
				diceButton.setIcon(new ImageIcon(backgroundDice));
				repaint();
				//Game.movePlayer(int dice1, int dice2)
				this.cancel();
			}
			
			
		}
		
	}
	
	
	private class BoardSpinner extends TimerTask 
	{

		int loopNum = 0;
		int numOfRotations;
		
		public BoardSpinner(int edge) {
			
			numOfRotations = edge - currentRotation;
			
			if(numOfRotations == 3) numOfRotations = -1;
			if(numOfRotations == -3) numOfRotations = 1;
			
			currentRotation = edge;
			disableButtons = true;
			
			
		}

		@Override
		public void run() {
			
		
			
			if(loopNum < 30) {
				
		
				rotation += 3 * numOfRotations ;
				repaint();
				revalidate();
			
			} else {
				if(finishedAnimation) {
					disableButtons = false;
				
					Timer rollDice = new Timer();
					rollDice.scheduleAtFixedRate(new BoardZoom(true), 0, 10);
					
					this.cancel();
				}
				
			}
			
			loopNum++;	
			
		}
		
	}
	
	private class BoardZoom extends TimerTask{
		
		
		private int loopNum = 0;
		private boolean zoomIn = true;
		
		public BoardZoom(boolean zoomIn) {
			
			this.zoomIn = zoomIn;
			disableButtons = true;
			
			
				
		}

		@Override
		public void run() {
			
			if(loopNum == 1) {
				rotation = 0;
				diceButton.setVisible(false);
				for(int j = 0; j < numOfSquares; j++) {
					squareButtons[j].setVisible(false);
					
				}
			}
				
			if(zoomIn) {
				boardImage.setIcon(new ImageIcon(squareAnimationImages[loopNum]));
			} else {
				boardImage.setIcon(new ImageIcon(squareAnimationImages[30 - loopNum]));
			}
				
			
			if(loopNum == 30) {
				if(!zoomIn) {
					rotation = 90 * currentRotation;
					for(int j = 0; j < numOfSquares; j++) {
						squareButtons[j].setVisible(true);
						
					}
					
					animationPanel.remove(boardImage);
					for(int j = 0; j < 30; j++) {
						squareAnimationImages[j].flush();
						squareAnimationImages[j] = null;
						System.gc();
						
					}
					
					diceButton.setVisible(true);
					
				}else {
					squareInfo.setVisible(true);
				}
				disableButtons = false;
				
				repaint();
				revalidate();
				this.cancel();
			}
			
			loopNum++;	
			
		}
	}
	
	
private class SquarePicker implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e) {
			
			// TODO Auto-generated method stub
			if(!disableButtons) {
				disableButtons = true;
				Object ob = e.getSource();
				
				for(int i = 0; i< numOfSquares; i++) {
					if(ob.equals(squareButtons[i])) {
						
						int focusedSquare = i  - numOfSquares/4*currentRotation;
						if(focusedSquare >= numOfSquares) focusedSquare -= numOfSquares;
						else if(focusedSquare <0) focusedSquare += numOfSquares;
						Timer spin = new Timer();
						
						if( focusedSquare > 0 && focusedSquare < numOfSquares/4) {
							spin.scheduleAtFixedRate(new BoardSpinner(2), 0, 10);
							Thread animationThread = new Thread(new createAnimation(2, numOfSquares/4-focusedSquare));
							animationThread.start();
						}
						if( focusedSquare > numOfSquares/4 && focusedSquare < numOfSquares/2) {
							spin.scheduleAtFixedRate(new BoardSpinner(1), 0, 10);
							Thread animationThread = new Thread(new createAnimation(1, numOfSquares/2-focusedSquare));
							animationThread.start();
						}
						if( focusedSquare > numOfSquares/2 && focusedSquare < numOfSquares/4*3) {
							spin.scheduleAtFixedRate(new BoardSpinner(0), 0, 10);
							Thread animationThread = new Thread(new createAnimation(0, numOfSquares/4*3-focusedSquare));
							animationThread.start();
						}
						if( focusedSquare > numOfSquares/4*3&& focusedSquare < numOfSquares) {
							spin.scheduleAtFixedRate(new BoardSpinner(3), 0, 10);
							Thread animationThread = new Thread(new createAnimation(3, numOfSquares-focusedSquare));
							animationThread.start();
						}
						
						if(focusedSquare ==0) {
							spin.scheduleAtFixedRate(new BoardSpinner(2), 0, 10);
							Thread animationThread = new Thread(new createAnimation(2, 5));
							animationThread.start();
						}
						if(focusedSquare ==numOfSquares/4) {
							spin.scheduleAtFixedRate(new BoardSpinner(1), 0, 10);
							Thread animationThread = new Thread(new createAnimation(1, 5));
							animationThread.start();
						}
						if(focusedSquare ==numOfSquares/2) {
							spin.scheduleAtFixedRate(new BoardSpinner(0), 0, 10);
							Thread animationThread = new Thread(new createAnimation(0, 5));
							animationThread.start();
						}
						if(focusedSquare ==numOfSquares/4*3) {
							spin.scheduleAtFixedRate(new BoardSpinner(3), 0, 10);
							Thread animationThread = new Thread(new createAnimation(3, 5));
							animationThread.start();
						}
						
						
						
						//displaySquareInfo(focusedSquare);
						displaySquareInfoAndResources(focusedSquare);
						
						
						isZoomed = true;
					}
				}
				
			}
			
		}
		
	}


	
	private BufferedImage createSquareImage(int squareNum, int width, int colourHeight, int edge, int fontsize) {
		
		Font font = new Font("Arial", Font.BOLD, fontsize);
		BufferedImage squareImage = null;
		Graphics2D sqImGraphics = null;
		
		if(edge == 1) {
			squareImage = new BufferedImage(width, colourHeight + width, BufferedImage.TYPE_INT_ARGB);
			sqImGraphics = squareImage.createGraphics();
			sqImGraphics.rotate(Math.toRadians(180), width/2, (colourHeight + width)/2);
		}else if(edge == 2) {
			squareImage = new BufferedImage(colourHeight + width, width, BufferedImage.TYPE_INT_ARGB);
			sqImGraphics = squareImage.createGraphics();
			sqImGraphics.rotate(Math.toRadians(270), width/2, width/2);
		}else if(edge == 3) {
			squareImage = new BufferedImage(width, colourHeight + width, BufferedImage.TYPE_INT_ARGB);
			sqImGraphics = squareImage.createGraphics();
		}else if(edge == 4) {
			squareImage = new BufferedImage(colourHeight + width, width, BufferedImage.TYPE_INT_ARGB);
			sqImGraphics = squareImage.createGraphics();
			sqImGraphics.translate(0, -colourHeight);
			sqImGraphics.rotate(Math.toRadians(90), width/2, colourHeight + width/2);
		}
		
		
		sqImGraphics.setColor(squares[squareNum].getColour());
		sqImGraphics.fillRect(0, 0, width, colourHeight);
		sqImGraphics.setColor(foregroundColour);
		sqImGraphics.fillRect(0, colourHeight, width, width);
		
		int borderWidth = width/50;
		
		sqImGraphics.setColor(borderColour);
		sqImGraphics.fillRect(0, 0, width, borderWidth);
		sqImGraphics.fillRect(0, 0, borderWidth, colourHeight + width);
		sqImGraphics.fillRect(0, colourHeight+ width-borderWidth, width, borderWidth);
		sqImGraphics.fillRect( width-borderWidth, 0, borderWidth, colourHeight + width);
		
		sqImGraphics.setFont(font);
		FontMetrics fontMet = sqImGraphics.getFontMetrics(font);
		
		String[] text = squares[squareNum].getName().split(" ");
		sqImGraphics.setColor(textColour);
		int textHeight = fontMet.getHeight();
		int startLine = (width - textHeight * text.length)/2 + textHeight;
		startLine += colourHeight;
		
		
		
		if(colourHeight == 0) {
			for(int i = 0; i < playerPositions.length; i++) {
				if(squareNum == playerPositions[i]) {
					
					if(i == 0) {
						sqImGraphics.drawImage(playerIcons[i].getScaledInstance(width/4,width/4,Image.SCALE_FAST),  0, 0, null);
					}else if(i == 1){
						sqImGraphics.drawImage(playerIcons[i].getScaledInstance(width/4,width/4,Image.SCALE_FAST),  width/8*3, 0, null);
					}else if(i == 2){
						sqImGraphics.drawImage(playerIcons[i].getScaledInstance(width/4,width/4,Image.SCALE_FAST),  width/4*3, 0, null);
					}else if(i == 3){
						sqImGraphics.drawImage(playerIcons[i].getScaledInstance(width/4,width/4,Image.SCALE_FAST),  0, width/8*3, null);
					}else if(i == 4){
						sqImGraphics.drawImage(playerIcons[i].getScaledInstance(width/4,width/4,Image.SCALE_FAST),  width/4*3, width/8*3, null);
					}else if(i == 5){
						sqImGraphics.drawImage(playerIcons[i].getScaledInstance(width/4,width/4,Image.SCALE_FAST),  0, width/4*3, null);
					}else if(i == 6){
						sqImGraphics.drawImage(playerIcons[i].getScaledInstance(width/4,width/4,Image.SCALE_FAST),  width/8*3, width/4*3, null);
					}else if(i == 7){
						sqImGraphics.drawImage(playerIcons[i].getScaledInstance(width/4,width/4,Image.SCALE_FAST),  width/4*3, width/4*3, null);
					}
					
				}
			}
			sqImGraphics.rotate(Math.toRadians(-45), width/2, width/2);
		}else {
			for(int i = 0; i < playerPositions.length; i++) {
				if(squareNum == playerPositions[i]) {
					
					if(i == 0) {
						sqImGraphics.drawImage(playerIcons[i].getScaledInstance(width/3,width/3,Image.SCALE_FAST),  0, colourHeight, null);
					} else if(i == 1) {
						sqImGraphics.drawImage(playerIcons[i].getScaledInstance(width/3,width/3,Image.SCALE_FAST),  width/3, colourHeight, null);
					} else if(i == 2) {
						sqImGraphics.drawImage(playerIcons[i].getScaledInstance(width/3,width/3,Image.SCALE_FAST),  width/3*2, colourHeight, null);
					} else if(i == 3) {
						sqImGraphics.drawImage(playerIcons[i].getScaledInstance(width/3,width/3,Image.SCALE_FAST),  0, colourHeight + width/3, null);
					} else if(i == 4) {
						sqImGraphics.drawImage(playerIcons[i].getScaledInstance(width/3,width/3,Image.SCALE_FAST),  width/3*2, colourHeight + width/3, null);
					} else if(i == 5) {
						sqImGraphics.drawImage(playerIcons[i].getScaledInstance(width/3,width/3,Image.SCALE_FAST),  0, colourHeight + width/3*2, null);
					} else if(i == 6) {
						sqImGraphics.drawImage(playerIcons[i].getScaledInstance(width/3,width/3,Image.SCALE_FAST),  width/3, colourHeight + width/3*2, null);
					} else if(i == 7) {
						sqImGraphics.drawImage(playerIcons[i].getScaledInstance(width/3,width/3,Image.SCALE_FAST),  width/3*2, colourHeight + width/3*2, null);
					}
					
				}
			}
		}
		for(int i = 0; i < text.length; i++) {
			int inset = (width - fontMet.stringWidth(text[i]))/2;
			sqImGraphics.drawString(text[i], inset, startLine);
			startLine += textHeight;
		}
		
		
		
		
		
		sqImGraphics.dispose();

		
		
		return squareImage;
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if(isZoomed && !disableButtons) {
			
			int xPos = e.getX();
			int yPos = e.getY();
					
			int sqInPosx = squareInfo.getX();
			int sqInPosy = squareInfo.getY();
			
			int sqInWidth = squareInfo.getWidth();
			int sqInHeight = squareInfo.getHeight();
			
			if(xPos < sqInPosx || xPos > sqInPosx + sqInWidth) {
				if(yPos < sqInPosx || yPos > sqInPosx + sqInHeight) {
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
	
