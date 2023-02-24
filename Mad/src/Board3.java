import java.awt.*;
import javax.swing.*;
import javax.swing.text.*;



import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.TimerTask;
import java.util.Timer;



public class Board3 extends JLayeredPane implements MouseListener{
	
	private Color background, foregroundColour, textColour, borderColour;
	
	private JButton squareButtons[], diceButton;
	
	private JLabel dice[];
	private ImageIcon[] dicePics;
	
	private GridBagConstraints squarePanelCon;
	private GridBagConstraints infoCon;
	
	private JMenu menu;

	private JPanel squaresPanel, infoPanel, confirm, back, overlay, animationPanel;
	private JScrollPane sqex;
	
	private int SQUARE_WIDTH = 150;
	private int SQUARE_TOP_HEIGHT = 50;
	private int numOfSquares = 20;
	private int defaultFontSize = 20;
	private int zoom = 3;
	private int defaultSize = 1000;
	
	private JLabel player, money, time;

	JLabel boardImage;
	
	
	private JMenuItem rules, accessibility, exit;
	
	private SquarePicker SP = new SquarePicker();
	
	private Square[] squares;
	private Display display;
	private boolean disableButtons = false;
	
	double rotation = 0;
	
	private BufferedImage[] squareImages;
	private BufferedImage fullBoard;
	private ImageIcon squareAnimationImages[][];
	
	
	JViewport screenView;
	
	
	private boolean isRotated = false;
	private int focusedSquare;
	
	public Board3( Square[] squares,  Display display) {
		this.display = display;
		this.squares = squares;
		
		//setLayout(new GridBagLayout());
		
		
		
		overlay = new JPanel();
		
		numOfSquares = squares.length - squares.length%4;
		
		setUpSquares();
		setUpInfo();
		
		sqex = new JScrollPane(squaresPanel);
		screenView = sqex.getViewport();
		screenView.setOpaque(false);
		screenView.setBorder(null);
		
		sqex.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		sqex.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		setPreferredSize(new Dimension(100,500));
		sqex.setPreferredSize(new Dimension(500,500));
		//sel.addLayoutComponent("UPPER_LEFT_CORNER", squaresPanel);
		
		overlay.setOpaque(false);
		sqex.setOpaque(false);
		sqex.setBackground(Color.YELLOW);
		
		squarePanelCon.gridx = 0;
		squarePanelCon.gridy = 0;
		squarePanelCon.weighty = 1;
		squarePanelCon.weightx = 1;
		setUpDice();
		//Add(sqex, -100);
		sqex.setBounds(0,0,1000,1000);
		overlay.setBounds(000,000,100,100);
		animationPanel.setBounds(200,200,1000,600);
		
		add(sqex,  1, 1);
		add(overlay, 2, 2);
		add(animationPanel, 3, 3);
		System.out.println(getIndexOf(sqex));
		System.out.println(getIndexOf(overlay));
		System.out.println(getPosition(sqex));
		System.out.println(getPosition(overlay));
		add(Box.createRigidArea(new Dimension(100, 100)));
		squarePanelCon.gridx = 3;
		squarePanelCon.anchor = GridBagConstraints.NORTH;
		//add(infoPanel, squarePanelCon);
		squarePanelCon.anchor = GridBagConstraints.CENTER;
		
		setUpMenu();
		
		changeColours(Color.blue ,new Color(113,205,226,255), Color.black, Color.black);
		
		//for(int i = 0; i < numOfSquares; i++) {
		//	squareButtons[i].setEnabled(false);
		//}
	
		
		
	}
	
	public void resize(int x, int y) {
		
		
		
		//JScrollBar bar = sqex.getVerticalScrollBar();
		//bar.setPreferredSize(new Dimension(2000, 1));
		//sqex.setPreferredSize(new Dimension(x-1, y-1));
		sqex.setBounds(0,0,x,y);
		overlay.setBounds(0,0,x,y);
		//JScrollBar bar2 = sqex.getHorizontalScrollBar();
		//bar2.setPreferredSize(new Dimension(20000, 10));
	}
	
	private void setUpMenu() {
		
		menu = new JMenu("MENU");
		rules = new JMenuItem("Rules");
		rules.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				Timer rollDice = new Timer();
				rollDice.scheduleAtFixedRate(new DiceRoller( 1,  5), 0, 10);
			}
		});
		rules.setFont(new Font("Arial", Font.PLAIN, 40));
		menu.add(rules);
		
		accessibility = new JMenuItem("Accessibility");
		accessibility.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				display.openAccessability();
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
						System.exit(0);
						removeConfirmationPanel();
					}
				}, "Are you sure you want to exit\nYou will lose all your progress");
			}
		});
		exit.setFont(new Font("Arial", Font.PLAIN, 40));
		menu.add(exit);
		menu.setFont(new Font("Arial", Font.PLAIN, 40));
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.add(menu);
		
		
		squarePanelCon.anchor = GridBagConstraints.NORTHEAST;
		squarePanelCon.gridx = 0;
		squarePanelCon.gridy = 0;
		overlay.add(menuBar, BorderLayout.PAGE_START);
		squarePanelCon.anchor = GridBagConstraints.CENTER;
	}

	public void getConfirmation(ActionListener output, String messageText) {
		
		disableButtons(true);
		
		confirm = new JPanel();
		SpringLayout sprLayout = new SpringLayout();  
		confirm.setLayout(sprLayout);
		confirm.setBackground(background);
		
	
		back = new JPanel();
		back.setBackground(new Color(165, 165, 165, 100));
		
		squarePanelCon.fill = GridBagConstraints.BOTH;
		squarePanelCon.anchor = GridBagConstraints.CENTER;
		squarePanelCon.gridwidth = 5;
		squarePanelCon.gridheight = 5;
		squarePanelCon.gridx = 0;
		squarePanelCon.gridy = 0;
		
		back.setOpaque(true);
		add(back, squarePanelCon, 0);
		
		
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
		
		JButton no = new JButton("no");
		no.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				removeConfirmationPanel();
			}
		});
		no.setFont(new Font("Arial", Font.PLAIN, 30));
	
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
		overlay.add(confirm,BorderLayout.CENTER);
		
		
		
		repaint();
		revalidate();
	}
	
	public void removeConfirmationPanel() {
		disableButtons(false);
		overlay.remove(confirm);
		remove(back);
		repaint();
		revalidate();
	}
	
	public void disableButtons(boolean disable) {
		
		disableButtons = disable;
		menu.setEnabled(!disable);
		

	}
	
	private void setUpSquares() {
		
		
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
		
		squaresPanel.addMouseListener(this);
		
		squaresPanel.setLayout(new GridBagLayout());
		squarePanelCon = new GridBagConstraints();
		squaresPanel.setOpaque(true);
		squaresPanel.setBackground(Color.YELLOW);
		
		
		squareButtons = new JButton[numOfSquares];
		squareImages = new BufferedImage[numOfSquares];  
		
		squarePanelCon.gridx = 1;
		squarePanelCon.gridy = 1;
		
		for(int i = 0; i <numOfSquares/4; i++) {
			
			squareButtons[i] = new JButton() {
				public void repaint() {
					if(!disableButtons) super.repaint();
			    }
			};
			squareButtons[i].addActionListener(SP);
			squaresPanel.add(squareButtons[i], squarePanelCon);
			squarePanelCon.gridx++;
			
		}
		
		for(int i = (numOfSquares/4); i < numOfSquares/2; i++) {
			
			squareButtons[i] = new JButton() {
				public void repaint() {
					if(!disableButtons) super.repaint();
			    }
			};
			squareButtons[i].addActionListener(SP);
			squaresPanel.add(squareButtons[i], squarePanelCon);
			squarePanelCon.gridy++;
			
		}
		
		for(int i = (numOfSquares/2); i < numOfSquares/4*3; i++) {

			squareButtons[i] = new JButton() {
				public void repaint() {
					if(!disableButtons) super.repaint();
			    }
			};
			squareButtons[i].addActionListener(SP);
			squaresPanel.add(squareButtons[i], squarePanelCon);
			squarePanelCon.gridx--;
		}

		for(int i = (numOfSquares/4*3); i < numOfSquares; i++) {
			
			squareButtons[i] = new JButton() {
				public void repaint() {
					if(!disableButtons) super.repaint();
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
		squarePanelCon.weightx = 1000;
		squarePanelCon.weighty = 1000;
		
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
		squarePanelCon.ipady = 0;
		squarePanelCon.ipady = 0;
		
	}
	

	private void setUpDice(){
		
		dicePics = new ImageIcon[180];
		
		for(int i = 1; i <= 165; i++)
		{
			dicePics[i-1]= new ImageIcon("dice//dice ("+i+").png");
		}
		dice = new JLabel[2];
		dice[0] = new JLabel();
		//dice[1] = new JLabel(dicePics[2]);
		
		animationPanel = new JPanel();
		animationPanel.setOpaque(false);
		
		squarePanelCon.anchor = GridBagConstraints.EAST;
		squarePanelCon.gridwidth = 1;
		squarePanelCon.gridheight = 2;
		squarePanelCon.gridx = 3;
		squarePanelCon.gridy = 3;
		squarePanelCon.ipadx = 5;
		//squaresPanel.add(dice[0], squarePanelCon);
		squarePanelCon.gridx = 4;
		squarePanelCon.anchor = GridBagConstraints.WEST;
		//squaresPanel.add(dice[1], squarePanelCon);
		squarePanelCon.anchor = GridBagConstraints.CENTER;
		squareButtons[5].setIcon(dicePics[60]);
		
		animationPanel.setLayout(new GridBagLayout());
		
		animationPanel.add(dice[0]);
		
		diceButton = new JButton();
		diceButton.setOpaque(false);
		diceButton.setBackground(Color.BLUE);
		diceButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				Timer rollDice = new Timer();
				rollDice.scheduleAtFixedRate(new DiceRoller( 1,  5), 0, 10);
				
			}
		});
		squarePanelCon.gridx = 0;
		squarePanelCon.gridwidth = 0;
		squarePanelCon.ipady = dicePics[2].getIconHeight();
		squarePanelCon.ipadx = dicePics[2].getIconWidth() * 2;
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
		
		
		player = new JLabel(" Player: (player name)");
		infoCon.anchor = GridBagConstraints.NORTH;
		infoCon.ipady = 5;
		infoCon.ipadx = 60;
		infoCon.gridx = 0;
		infoCon.gridy = 0;
		player.setOpaque(true);
		
		infoPanel.add(player, infoCon);
		
		money = new JLabel(" Money: �0.00");
		infoCon.gridx = 1;
		money.setOpaque(true);
		infoPanel.add(money, infoCon);
		
		time = new JLabel("Time: 0 Hours");
		infoCon.gridx = 2;
		time.setOpaque(true);
		infoPanel.add(time, infoCon);
		
		infoPanel.setOpaque(false);
		
		
	}
	
	public void movePlayer(int playerNum, int pos) {
		
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
		
	}
	
	public void displaySquareInfoAndResources(int squareNum){
		
	}
	
	public void changeColours(Color background, Color squares, Color text, Color border) {
		
		this.background = background;
		this.foregroundColour = squares;
		this.textColour = text;
		this.borderColour = border;
		
		this.setBackground(background);
		this.setOpaque(true);
		
		squaresPanel.setOpaque(false);
		

		player.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, border));
		player.setBackground(foregroundColour);
		money.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, border));
		money.setBackground(foregroundColour);
		time.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, border));
		time.setBackground(foregroundColour);
		
		

		

		for(int i = 1; i < numOfSquares/4; i++) {
			squareImages[i] = createSquareImage(i, SQUARE_WIDTH, SQUARE_TOP_HEIGHT, 1, defaultFontSize);
		}
		for(int i = numOfSquares/4; i < numOfSquares/2; i++) {
			squareImages[i] = createSquareImage(i, SQUARE_WIDTH, SQUARE_TOP_HEIGHT, 2, defaultFontSize);
		}
		for(int i = numOfSquares/2; i < numOfSquares/4*3; i++) {
			squareImages[i] = createSquareImage(i, SQUARE_WIDTH, SQUARE_TOP_HEIGHT, 3, defaultFontSize);
		}
		for(int i = numOfSquares/4*3; i < numOfSquares; i++) {
			squareImages[i] = createSquareImage(i, SQUARE_WIDTH, SQUARE_TOP_HEIGHT, 4, defaultFontSize);
		}
		
		
		squareImages[0] = createSquareImage(0, SQUARE_WIDTH + SQUARE_TOP_HEIGHT, 0, 1, (int) (defaultFontSize*1.2));
		squareImages[numOfSquares/4] = createSquareImage(numOfSquares/4, SQUARE_WIDTH + SQUARE_TOP_HEIGHT, 0, 2, (int) (defaultFontSize*1.2));
		squareImages[numOfSquares/2] = createSquareImage(numOfSquares/2, SQUARE_WIDTH + SQUARE_TOP_HEIGHT, 0, 3, (int) (defaultFontSize*1.2));
		squareImages[numOfSquares/4*3] = createSquareImage(numOfSquares/4*3, SQUARE_WIDTH + SQUARE_TOP_HEIGHT, 0, 4, (int) (defaultFontSize*1.2));
		
		for(int i = 0; i< numOfSquares; i++) {
			ImageIcon im = new ImageIcon(squareImages[i]);
			squareButtons[i].setIcon(im);
			squareButtons[i].setMargin(new Insets(-5,-5,-5,-5));
			squareButtons[i].setBorder(null);
		}
	
		
		revalidate();
		repaint(); 
		
	}
	



	private void expandBoard() {
		
		
		
		zoom = 3;
	
		BufferedImage squareAnimationImages[] = new BufferedImage[numOfSquares];
		
		for(int i = 1; i < numOfSquares/4; i++) {
			squareAnimationImages[i] = createSquareImage(i, 150*zoom, 50*zoom, 1, 20*zoom);
		}
		for(int i = numOfSquares/4 + 1; i < numOfSquares/2; i++) {
			squareAnimationImages[i] = createSquareImage(i, 150*zoom, 50*zoom, 2, 20*zoom);
		}
		for(int i = numOfSquares/2 + 1; i < numOfSquares/4*3; i++) {
			squareAnimationImages[i] = createSquareImage(i, 150*zoom, 50*zoom, 3, 20*zoom);
		}
		for(int i = numOfSquares/4*3 + 1; i < numOfSquares; i++) {
			squareAnimationImages[i] = createSquareImage(i, 150*zoom, 50*zoom, 4, 20*zoom);
		}
		
		squareAnimationImages[0] = createSquareImage(0, 200*zoom, 0, 1, 24*zoom);
		squareAnimationImages[numOfSquares/4] = createSquareImage(numOfSquares/4, 200*zoom, 0, 2, 24*zoom);
		squareAnimationImages[numOfSquares/2] = createSquareImage(numOfSquares/2, 200*zoom, 0, 3, 24*zoom);
		squareAnimationImages[numOfSquares/4*3] = createSquareImage(numOfSquares/4*3, 200*zoom, 0, 4, 24*zoom);
		
		
		
		
			Graphics2D sqImGraphics = null;
			fullBoard = new BufferedImage(defaultSize*zoom,defaultSize*zoom, BufferedImage.TYPE_INT_ARGB);
			sqImGraphics = fullBoard.createGraphics();
		int offsetx = 0;
		int offsety = 0;
		
		
		sqImGraphics.drawImage(squareAnimationImages[0], null, offsetx, offsety);
		offsetx += 200*zoom;
		for(int i = 1; i < numOfSquares/4; i++) {
			sqImGraphics.drawImage(squareAnimationImages[i], null, offsetx, offsety);
			offsetx += 150*zoom;
		}
		sqImGraphics.drawImage(squareAnimationImages[numOfSquares/4], null, offsetx, offsety);
		offsety += 200*zoom;
		for(int i = numOfSquares/4 + 1; i < numOfSquares/2; i++) {
			sqImGraphics.drawImage(squareAnimationImages[i], null, offsetx, offsety);
			offsety += 150*zoom;
		}
		sqImGraphics.drawImage(squareAnimationImages[numOfSquares/2], null, offsetx, offsety);
		for(int i = numOfSquares/2 + 1; i < numOfSquares/4*3; i++) {
			offsetx -= 150*zoom;
			sqImGraphics.drawImage(squareAnimationImages[i], null, offsetx, offsety);			
		}
		offsetx -= 200*zoom;
		sqImGraphics.drawImage(squareAnimationImages[numOfSquares/4*3], null, offsetx, offsety);
		
		for(int i = numOfSquares/4*3 + 1; i < numOfSquares; i++) {
			offsety -= 150*zoom;
			sqImGraphics.drawImage(squareAnimationImages[i], null, offsetx, offsety);
		}
		
		boardImage = new JLabel();
		squarePanelCon.gridx = 1;
		squarePanelCon.gridy = 1;
		squarePanelCon.gridwidth = numOfSquares/4 + 1;
		squarePanelCon.gridheight = numOfSquares/4 + 1;
		
		
		
		squaresPanel.add(boardImage, squarePanelCon);
		
		squaresPanel.getSize();
		sqImGraphics.dispose();
		
	}
	
	
	private class DiceRoller extends TimerTask 
	{
		int dice1;
		int dice2;

		public DiceRoller(int dice1, int dice2) {
			super();
			
			this.dice1 = dice1;
			this.dice2 = dice2;
		}
		
		private int dieNum = 0;
		@Override
		public void run() {
			// TODO Auto-generated method stub
			dice[0].setIcon(dicePics[dieNum]);
			
			animationPanel.repaint();
			
			dieNum++;
			
			
			if(dieNum >= 165) 
			{
				
				//dice[0].setIcon(dicePics[dice1-1]);		
				//dice[1].setIcon(dicePics[dice2-1]);
				//Game.movePlayer(int dice1, int dice2)
				this.cancel();
			}
			
			
		}
		
	}
	
	private class RaiseSquares extends TimerTask
	{

		int moveRight;
		public RaiseSquares(int moveRight) {
			this.moveRight = moveRight;
		}
		
		int loop = 0;
		@Override
		public void run() {
			
			
			Dimension viewSize = sqex.getSize();
			Point viewPos = screenView.getViewPosition();
			screenView.setViewPosition(new Point((int) viewPos.getX() + moveRight,(int)(viewPos.getY() + 1)));
			
			sqex.setBounds(0,0,(int) viewSize.getWidth() - moveRight,(int)(viewSize.getHeight() - 1));
			loop++;
			repaint();
			revalidate();
			if(loop >=400) {
				
				this.cancel();
			}
		}
		
	}
	
	
	
	private class BoardReseter extends TimerTask{

		int moveRight;
		int numOfRotations;
		
		public BoardReseter(int numOfRotations, int moveRight) {
			this.numOfRotations = numOfRotations;
			this.moveRight = moveRight;
		}
		
		double multiplier = 3;
		int loop = 0;
		@Override
		public void run() {
			
			Dimension viewSize = sqex.getSize();
			Point viewPos = screenView.getViewPosition();
			screenView.setViewPosition(new Point((int) viewPos.getX() - moveRight*4,(int)(viewPos.getY() -4)));
			
			sqex.setBounds(0,0,(int) viewSize.getWidth() + moveRight*4,(int)(viewSize.getHeight() + 4));
			loop++;
			repaint();
			revalidate();
			
			rotation -= 0.9 * numOfRotations;
			
			System.out.println(multiplier);
			System.out.println(defaultSize*multiplier);
			boardImage.setIcon(new ImageIcon(fullBoard.getScaledInstance((int)(defaultSize*multiplier),(int) (defaultSize*multiplier),Image.SCALE_FAST)));
			multiplier -= 0.02;
			loop++;
			if(loop == 200) {
				boardImage.setIcon(new ImageIcon(fullBoard.getScaledInstance((int)(defaultSize),(int) (defaultSize),Image.SCALE_FAST)));
				sqex.remove(squaresPanel);
				setUpSquares();
				screenView = new JViewport();
				screenView.setView(squaresPanel);
				
				changeColours(background, foregroundColour, textColour, borderColour);
				this.cancel();
				isRotated = false;
				
			}
		}
		
	}
	
	private class BoardSpinner extends TimerTask 
	{
	
		
		Dimension viewSize = screenView.getExtentSize();
		Point viewPos = screenView.getViewPosition();
		Dimension squareSize = squaresPanel.getSize();
	
			
		double xPos = viewPos.getX() + viewSize.getWidth()/2;
		double yPos = viewPos.getY() + viewSize.getHeight()/2;
		
		private boolean exWid = false;
		private boolean exHigh = false;
		
		double distFromBottom;
		private int numRot;
		private int moveR;
		
		int loopNum = 0;
		
		public BoardSpinner(int numOfRotations, int moveRight) {
			super();
	
			moveR = moveRight;
			this.numRot = numOfRotations;
		}

		private double multiplier = 1;
		
		@Override
		public void run() {
			
		
			
			if(loopNum < 100) {
				
				
				squareSize = squaresPanel.getSize();
				
				
				viewSize = screenView.getExtentSize();
				viewPos = screenView.getViewPosition();
				xPos = viewPos.getX() + viewSize.getWidth()/2;
				yPos = viewPos.getY() + viewSize.getHeight()/2;
				
				distFromBottom = squareSize.getHeight() - viewPos.getY() - viewSize.getHeight();
				Point newPos = new Point();
				if(exWid && exHigh) {
					//newPos.setLocation(1000*(multiplier)/2 - viewSize.getWidth()/2 , 1000*(multiplier)/2 - viewSize.getHeight()/2 );
					
					newPos.setLocation(xPos+(defaultSize*0.01)*(xPos/(defaultSize*multiplier))+ 5 + moveR - viewSize.getWidth()/2, yPos +defaultSize*0.01*yPos/(defaultSize*multiplier) - viewSize.getHeight()/2 + distFromBottom*(multiplier/3));
				}
				else {
					if(exHigh) {
						newPos.setLocation(viewPos.getX(), yPos +defaultSize*0.01*yPos/(defaultSize*multiplier) - viewSize.getHeight()/2 + distFromBottom*(multiplier/3));
						
						//newPos.setLocation(viewPos.getX() , 1000*multiplier/2 - viewSize.getHeight()/2);
						
						if(squareSize.getWidth() > viewSize.getWidth()) {
							exWid = true;
						}
						
					}
					else {
						
						if(exWid) {
							newPos.setLocation(1000*(multiplier)/2 - viewSize.getWidth()/2 ,viewPos.getY() );
						}else {
							if(squareSize.getWidth() > viewSize.getWidth()) {
								exWid = true;
							}
						
						}
						if(squareSize.getHeight() > viewSize.getHeight()) {
							exHigh = true;
							
						}
					}
				}
				
				
				screenView.setViewPosition(newPos);
							
				
				
				repaint();
				revalidate();
					
				boardImage.setIcon(new ImageIcon(fullBoard.getScaledInstance((int)(defaultSize*multiplier),(int) (defaultSize*multiplier),Image.SCALE_FAST)));
				

				multiplier += 0.02;
				
				rotation += 0.9 * numRot;
				
				
				
				
			}else {
				boardImage.setIcon(new ImageIcon(fullBoard));
				
				Timer raiseSq = new Timer();
				if(moveR == 0) {
					raiseSq.scheduleAtFixedRate(new RaiseSquares(0), 0, 5);
				}
				else {
					raiseSq.scheduleAtFixedRate(new RaiseSquares(1), 0, 5);
				
				}
				
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
			if(disableButtons == false) {
				Object ob = e.getSource();
				
				for(int i = 0; i< numOfSquares; i++) {
					if(ob.equals(squareButtons[i])) {
						for(int j = 0; j < numOfSquares; j++) {
							squaresPanel.remove(squareButtons[j]);
							
						}
						expandBoard();
						Timer rollDice = new Timer();
						
						if( i > 0 && i < numOfSquares/4) {
							rollDice.scheduleAtFixedRate(new BoardSpinner(2, 0), 0, 1);
						}
						if( i > numOfSquares/4 && i < numOfSquares/2) {
							rollDice.scheduleAtFixedRate(new BoardSpinner(1, 0), 0, 1);
						}
						if( i > numOfSquares/2 && i < numOfSquares/4*3) {
							rollDice.scheduleAtFixedRate(new BoardSpinner(0, 0), 0, 1);
						}
						if( i > numOfSquares/4*3&& i < numOfSquares) {
							rollDice.scheduleAtFixedRate(new BoardSpinner(-1, 0), 0, 1);
						}
						
						if(i ==0)rollDice.scheduleAtFixedRate(new BoardSpinner(2, 10), 0, 1);
						if(i ==numOfSquares/4)rollDice.scheduleAtFixedRate(new BoardSpinner(2, 5), 0, 1);
						if(i ==numOfSquares/2)rollDice.scheduleAtFixedRate(new BoardSpinner(2, 5), 0, 1);
						if(i ==numOfSquares/4*3)rollDice.scheduleAtFixedRate(new BoardSpinner(2, 5), 0, 1);
						
						focusedSquare = i;
						isRotated = true;
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
		
		sqImGraphics.setFont(font);
		FontMetrics fontMet = sqImGraphics.getFontMetrics(font);
		
		String[] text = squares[squareNum].getName().split(" ");
		sqImGraphics.setColor(textColour);
		int textHeight = fontMet.getHeight();
		int startLine = (width - textHeight * text.length)/2 + textHeight;
		startLine += colourHeight;
		if(colourHeight == 0) {
			sqImGraphics.rotate(Math.toRadians(-45), width/2, width/2);
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
		if(isRotated) {
			Timer rollDice = new Timer();
			if( focusedSquare > 0 && focusedSquare < numOfSquares/4) {
				rollDice.scheduleAtFixedRate(new BoardReseter(2, 0), 0, 1);
			}
			if( focusedSquare > numOfSquares/4 && focusedSquare < numOfSquares/2) {
				rollDice.scheduleAtFixedRate(new BoardReseter(1, 0), 0, 1);
			}
			if( focusedSquare > numOfSquares/2 && focusedSquare < numOfSquares/4*3) {
				rollDice.scheduleAtFixedRate(new BoardReseter(0, 0), 0, 1);
			}
			if( focusedSquare > numOfSquares/4*3&& focusedSquare < numOfSquares) {
				rollDice.scheduleAtFixedRate(new BoardReseter(-1, 0), 0, 1);
			}
			
			if(focusedSquare ==0)rollDice.scheduleAtFixedRate(new BoardSpinner(2, 10), 0, 1);
			if(focusedSquare ==numOfSquares/4)rollDice.scheduleAtFixedRate(new BoardSpinner(2, 5), 0, 1);
			if(focusedSquare ==numOfSquares/2)rollDice.scheduleAtFixedRate(new BoardSpinner(2, 5), 0, 1);
			if(focusedSquare ==numOfSquares/4*3)rollDice.scheduleAtFixedRate(new BoardSpinner(2, 5), 0, 1);
			
		
		}
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
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
	
