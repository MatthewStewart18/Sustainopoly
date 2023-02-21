import java.awt.*;
import javax.swing.*;
import javax.swing.text.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.TimerTask;
import java.util.Timer;



public class Board3 extends JLayeredPane {
	
	private Color background, foregroundColour, textColour, borderColour;
	
	private JButton squareButtons[], diceButton;
	
	private JLabel dice[];
	private ImageIcon[] dicePics;
	
	private GridBagConstraints squarePanelCon;
	private GridBagConstraints infoCon;
	
	private JMenu menu;

	private JPanel squaresPanel, infoPanel, confirm, back;
	
	final private int SQUARE_WIDTH = 150;
	final private int SQUARE_TOP_HEIGHT = 50;
	private int numOfSquares = 20;
	private int fontSize;
	
	private JLabel player, money, time;
	
	
	private JMenuItem rules, accessibility, exit;
	
	private SquarePicker SP = new SquarePicker();
	
	private Square[] squares;
	private Display display;
	private boolean disableButtons = false;
	
	public Board3( Square[] squares,  Display display) {
		this.display = display;
		this.squares = squares;
		
		setLayout(new GridBagLayout());
		
		numOfSquares = squares.length - squares.length%4;
		
		setUpSquares();
		setUpInfo();
		
		squarePanelCon.gridx = 1;
		squarePanelCon.gridy = 1;
		
		add(squaresPanel, squarePanelCon);
		squarePanelCon.gridx = 3;
		squarePanelCon.anchor = GridBagConstraints.NORTH;
		add(infoPanel, squarePanelCon);
		squarePanelCon.anchor = GridBagConstraints.CENTER;
		setUpDice();
		setUpMenu();
		
		changeColours(Color.blue ,new Color(113,205,226,255), Color.black, Color.black);
		
		
		
	}
	
	private void setUpMenu() {
		
		menu = new JMenu("MENU");
		rules = new JMenuItem("Rules");
		rules.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				//game.showRules
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
		squarePanelCon.gridx = 4;
		squarePanelCon.gridy = 0;
		add(menuBar, squarePanelCon);
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
		add(confirm, squarePanelCon, 0);
		
		
		
		repaint();
		revalidate();
	}
	
	public void removeConfirmationPanel() {
		disableButtons(false);
		remove(confirm);
		remove(back);
		repaint();
		revalidate();
	}
	
	public void disableButtons(boolean disable) {
		
		disableButtons = disable;
		menu.setEnabled(!disable);
		
		for(int i = 0; i < numOfSquares; i++) {
			squareButtons[i].setEnabled(!disable);
		}
	}
	
	private void setUpSquares() {
		
		squaresPanel = new JPanel();
		squaresPanel.setLayout(new GridBagLayout());
		squarePanelCon = new GridBagConstraints();
		
		
		squareButtons = new JButton[numOfSquares];
		
		squarePanelCon.gridx = 0;
		squarePanelCon.gridy = 0;
		
		for(int i = 0; i <numOfSquares/4; i++) {
			
			squareButtons[i] = new JButton();
			squareButtons[i].addActionListener(SP);
			squaresPanel.add(squareButtons[i], squarePanelCon);
			squarePanelCon.gridx++;
			
		}
		
		for(int i = (numOfSquares/4); i < numOfSquares/2; i++) {
			
			squareButtons[i] = new JButton();
			squareButtons[i].addActionListener(SP);
			squaresPanel.add(squareButtons[i], squarePanelCon);
			squarePanelCon.gridy++;
			
		}
		
		for(int i = (numOfSquares/2); i < numOfSquares/4*3; i++) {

			squareButtons[i] = new JButton();
			squareButtons[i].addActionListener(SP);
			squaresPanel.add(squareButtons[i], squarePanelCon);
			squarePanelCon.gridx--;
		}

		for(int i = (numOfSquares/4*3); i < numOfSquares; i++) {
			
			squareButtons[i] = new JButton();
			squareButtons[i].addActionListener(SP);
			squaresPanel.add(squareButtons[i], squarePanelCon);
			squarePanelCon.gridy--;
	
		}
		
	}
	

	private void setUpDice(){
		
		dicePics = new ImageIcon[6];
		
		for(int i = 1; i <= 6; i++)
		{
			dicePics[i-1]= new ImageIcon("dice//dice"+i+".png");
		}
		dice = new JLabel[2];
		dice[0] = new JLabel(dicePics[5]);
		dice[1] = new JLabel(dicePics[2]);
		
		squarePanelCon.anchor = GridBagConstraints.EAST;
		squarePanelCon.gridwidth = 1;
		squarePanelCon.gridheight = 2;
		squarePanelCon.gridx = 2;
		squarePanelCon.gridy = 2;
		squarePanelCon.ipadx = 5;
		squaresPanel.add(dice[0], squarePanelCon);
		squarePanelCon.gridx = 3;
		squarePanelCon.anchor = GridBagConstraints.WEST;
		squaresPanel.add(dice[1], squarePanelCon);
		squarePanelCon.anchor = GridBagConstraints.CENTER;
		
		diceButton = new JButton();
		diceButton.setOpaque(false);
		diceButton.setBackground(Color.BLUE);
		diceButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				
				Timer rollDice = new Timer();
				rollDice.scheduleAtFixedRate(new DiceRoller( 1,  5), 0, 100);
				
			}
		});
		squarePanelCon.gridx = 2;
		squarePanelCon.gridwidth = 2;
		squarePanelCon.ipady = dicePics[2].getIconHeight();
		squarePanelCon.ipadx = dicePics[2].getIconWidth() * 2;
		diceButton.setToolTipText("Click dice to roll");
		diceButton.setBorder(null);
		
		squaresPanel.add(diceButton, squarePanelCon);
		
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
		

		for(int i = 1; i < 5; i++) createSquareImage(i, SQUARE_WIDTH, SQUARE_TOP_HEIGHT);
		for(int i = 6; i < 10; i++) createSquareImage(i, SQUARE_WIDTH, SQUARE_TOP_HEIGHT);
		for(int i = 11; i < 15; i++) createSquareImage(i, SQUARE_WIDTH, SQUARE_TOP_HEIGHT);
		for(int i = 16; i < 20; i++) createSquareImage(i, SQUARE_WIDTH, SQUARE_TOP_HEIGHT);
		
		createSquareImage(0, SQUARE_WIDTH + SQUARE_TOP_HEIGHT, 0);
		createSquareImage(numOfSquares/4, SQUARE_WIDTH + SQUARE_TOP_HEIGHT, 0);
		createSquareImage(numOfSquares/2, SQUARE_WIDTH + SQUARE_TOP_HEIGHT, 0);
		createSquareImage(numOfSquares/4*3, SQUARE_WIDTH + SQUARE_TOP_HEIGHT, 0);
		
		revalidate();
		repaint();
		
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
			dice[0].setIcon(dicePics[dieNum %6]);
			dice[1].setIcon(dicePics[(dieNum + 3) %6]);
			squaresPanel.repaint();
			
			dieNum++;
			
			
			if(dieNum >= 40) 
			{
				dice[0].setIcon(dicePics[dice1-1]);		
				dice[1].setIcon(dicePics[dice2-1]);
				//Game.movePlayer(int dice1, int dice2)
				this.cancel();
			}
			
			
		}
		
	}
	
	private class SquarePicker implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			
			Object ob = e.getSource();
			
			
		}
		
	}


	
	private void createSquareImage(int squareNum, int width, int colourHeight) {
		
		Font font = new Font("Arial", Font.BOLD, 21);
		BufferedImage squareImage = null;
		Graphics2D sqImGraphics = null;
		
		if(squareNum >=0 && squareNum < numOfSquares/4) {
			squareImage = new BufferedImage(width, colourHeight + width, BufferedImage.TYPE_INT_ARGB);
			sqImGraphics = squareImage.createGraphics();
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
			for(int i = 0; i < text.length; i++) {
				int inset = (width - fontMet.stringWidth(text[i]))/2;
				sqImGraphics.drawString(text[i], inset, startLine);
				startLine += textHeight;
			}
			
			
		} else if(squareNum >= (numOfSquares/4) && squareNum < numOfSquares/2) {
			squareImage = new BufferedImage(colourHeight + width, width, BufferedImage.TYPE_INT_ARGB);
			sqImGraphics = squareImage.createGraphics();
			sqImGraphics.setColor(squares[squareNum].getColour());
			sqImGraphics.fillRect(width, 0, colourHeight, width);
			sqImGraphics.setColor(foregroundColour);
			sqImGraphics.fillRect(0, 0, width, width);
			
			sqImGraphics.setFont(font);
			FontMetrics fontMet = sqImGraphics.getFontMetrics(font);
			
			String[] text = squares[squareNum].getName().split(" ");
			sqImGraphics.setColor(textColour);
			int textHeight = fontMet.getHeight();
			int startLine = (width - textHeight * text.length)/2 + textHeight;
			for(int i = 0; i < text.length; i++) {
				int inset = (width - fontMet.stringWidth(text[i]))/2;
				sqImGraphics.drawString(text[i], inset, startLine);
				startLine += textHeight;
			}
			
		} else if(squareNum >= (numOfSquares/2) && squareNum < numOfSquares/4*3) {
			squareImage = new BufferedImage(width, colourHeight + width, BufferedImage.TYPE_INT_ARGB);
			sqImGraphics = squareImage.createGraphics();
			sqImGraphics.setColor(squares[squareNum].getColour());
			sqImGraphics.fillRect(0, width, width, colourHeight);
			sqImGraphics.setColor(foregroundColour);
			sqImGraphics.fillRect(0, 0, width, width);
			
			sqImGraphics.setFont(font);
			FontMetrics fontMet = sqImGraphics.getFontMetrics(font);
			
			String[] text = squares[squareNum].getName().split(" ");
			sqImGraphics.setColor(textColour);
			int textHeight = fontMet.getHeight();
			int startLine = (width - textHeight * text.length)/2 + textHeight;
			for(int i = 0; i < text.length; i++) {
				int inset = (width - fontMet.stringWidth(text[i]))/2;
				sqImGraphics.drawString(text[i], inset, startLine);
				startLine += textHeight;
			}
			
		} else if(squareNum >= (numOfSquares/4*3) && squareNum < numOfSquares) {
			squareImage = new BufferedImage(colourHeight + width, width, BufferedImage.TYPE_INT_ARGB);
			sqImGraphics = squareImage.createGraphics();
			sqImGraphics.setColor(squares[squareNum].getColour());
			sqImGraphics.fillRect(0, 0, colourHeight, width);
			sqImGraphics.setColor(foregroundColour);
			sqImGraphics.fillRect(colourHeight, 0, width, width);
			
			sqImGraphics.setFont(font);
			FontMetrics fontMet = sqImGraphics.getFontMetrics(font);
			sqImGraphics.setColor(textColour);
			String[] text = squares[squareNum].getName().split(" ");
			
			int textHeight = fontMet.getHeight();
			int startLine = (width - textHeight * text.length)/2 + textHeight;
			for(int i = 0; i < text.length; i++) {
				int inset = (width - fontMet.stringWidth(text[i]))/2 + colourHeight;
				sqImGraphics.drawString(text[i], inset, startLine);
				startLine += textHeight;
			}
			
		}
		
		ImageIcon im = new ImageIcon(squareImage);
		squareButtons[squareNum].setIcon(im);
		squareButtons[squareNum].setMargin(new Insets(0,0,0,0));
		squareButtons[squareNum].setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, borderColour));
		
		sqImGraphics.dispose();
	}

}
