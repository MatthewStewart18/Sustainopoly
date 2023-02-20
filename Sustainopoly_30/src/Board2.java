
import java.awt.*;
import javax.swing.*;
import javax.swing.text.*;

import java.awt.event.*;
import java.util.TimerTask;
import java.util.Timer;



public class Board2 extends JLayeredPane {
	
	private Color background, foregroundColour, textColour, borderColour;
	
	private JButton squareButtons[], diceButton;
	
	private JLabel dice[], squareTop[], squareBot[];
	private ImageIcon[] dicePics;
	
	private GridBagConstraints squareCon;
	private GridBagConstraints squareTopCon;
	private GridBagConstraints squareButtonCon;
	private GridBagConstraints infoCon;
	
	private JMenu menu;

	private JPanel squaresPanel, infoPanel, confirm, back;
	
	final private int SQUARE_WIDTH = 125;
	final private int SQUARE_HEIGHT = 165;
	private int numOfSquares = 20;
	private int fontSize;
	
	private JLabel player, money, time;
	
	
	private JMenuItem rules, accessibility, exit;
	
	private SquarePicker SP = new SquarePicker();
	
	private Square[] squares;
	Display display;
	private boolean disableButtons = false;
	public Board2( Square[] squares,  Display display) {
		this.display = display;
		this.squares = squares;
		
		setLayout(new GridBagLayout());
		
		numOfSquares = squares.length - squares.length%4;
		
		setUpSquares();
		setUpInfo();
		
		squareTopCon.gridx = 1;
		squareTopCon.gridy = 1;
		
		add(squaresPanel, squareTopCon);
		squareTopCon.gridx = 3;
		squareTopCon.anchor = GridBagConstraints.SOUTH;
		add(infoPanel, squareTopCon);
		squareTopCon.anchor = GridBagConstraints.CENTER;
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
		
		squareCon.fill = GridBagConstraints.NONE;
		squareCon.anchor = GridBagConstraints.NORTHEAST;
		squareCon.ipady = 0;
		squareCon.ipadx = 0;
		squareCon.gridwidth = 1;
		squareCon.gridheight = 1;
		squareCon.gridx = 4;
		squareCon.gridy = 0;
		add(menuBar, squareCon);
	}

	public void getConfirmation(ActionListener output, String messageText) {
		
		disableButtons(true);
		
		confirm = new JPanel();
		SpringLayout sprLayout = new SpringLayout();  
		confirm.setLayout(sprLayout);
		confirm.setBackground(background);
		
	
		back = new JPanel();
		back.setBackground(new Color(165, 165, 165, 100));
		squareCon.fill = GridBagConstraints.BOTH;
		squareCon.anchor = GridBagConstraints.CENTER;
		squareCon.ipady = 0;
		squareCon.ipadx = 0;
		squareCon.gridwidth = 5;
		squareCon.gridheight = 5;
		squareCon.gridx = 0;
		squareCon.gridy = 0;
		
		back.setOpaque(true);
		add(back, squareCon, 0);
		
		
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
		
		squareCon.fill = GridBagConstraints.NONE;
		squareCon.anchor = GridBagConstraints.CENTER;
		squareCon.ipady = 0;
		squareCon.ipadx = 0;
		squareCon.gridwidth = 5;
		squareCon.gridheight = 5;
		squareCon.gridx = 0;
		squareCon.gridy = 0;
		add(confirm, squareCon, 0);
		
		
		
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
	}
	
	private void setUpSquares() {
		
		
		
		squaresPanel = new JPanel();
		squaresPanel.setLayout(new GridBagLayout());
		
		squareCon = new GridBagConstraints();
		squareTopCon = new GridBagConstraints();
		squareButtonCon = new GridBagConstraints();
		
		squareTop = new JLabel[numOfSquares];
		squareBot = new JLabel[numOfSquares];
		squareButtons = new JButton[numOfSquares];
		
		squareCon.ipady = 0;
		squareCon.ipadx = 0;
		
		squareTopCon.ipady = SQUARE_HEIGHT - SQUARE_WIDTH;
		squareTopCon.ipadx = SQUARE_HEIGHT - SQUARE_WIDTH;
		
		squareButtonCon.fill = GridBagConstraints.BOTH;
		squareTopCon.fill = GridBagConstraints.BOTH;
		
		Dimension di = new Dimension(135, 135);
		
		squareTopCon.gridy = 0;
		squareCon.gridy = 1;
		squareCon.gridx = 1;
		squareTopCon.gridx = 1;
		squareButtonCon.gridheight = 2;
		for(int i = 1; i <numOfSquares/4; i++) {
			
			squareCon.gridx++;
			squareTopCon.gridx++;
			squareButtonCon.gridx = squareTopCon.gridx;
			squareButtonCon.gridy = squareTopCon.gridy;
			
			squareBot[i] = new JLabel(squares[i].getName()  );
			
			squareTop[i] = new JLabel();
			squareTop[i].setBackground(squares[i].getColour());
			squareTop[i].setOpaque(true);
			
			squaresPanel.add(squareBot[i], squareCon);
			squaresPanel.add(squareTop[i], squareTopCon);
			
			squareButtons[i] = new JButton();
			squareButtons[i].setOpaque(false);
			squareButtons[i].addActionListener(SP);
			squaresPanel.add(squareButtons[i], squareButtonCon);
			squareBot[i].setPreferredSize(di);
			
		}
		
		squareCon.gridx++;
		squareTopCon.gridx += 2;
		squareTopCon.gridy++;
		squareButtonCon.gridheight = 1;
		squareButtonCon.gridwidth = 2;
		for(int i = (numOfSquares/4) +1; i < numOfSquares/2; i++) {
			
			squareCon.gridy++;
			squareTopCon.gridy++;
			squareButtonCon.gridx = squareCon.gridx;
			squareButtonCon.gridy = squareCon.gridy;
			
			squareBot[i] = new JLabel(squares[i].getName()  );
			squareTop[i] = new JLabel();
			squareTop[i].setBackground(squares[i].getColour());
			squareTop[i].setOpaque(true);
			
			squaresPanel.add(squareBot[i], squareCon);
			squaresPanel.add(squareTop[i], squareTopCon);
			
			squareButtons[i] = new JButton();
			squareButtons[i].setOpaque(false);
			squareButtons[i].addActionListener(SP);
			squaresPanel.add(squareButtons[i], squareButtonCon);
			squareBot[i].setPreferredSize(di);
			
		}
		
		squareCon.gridy++;
		squareTopCon.gridy+=2;
		squareTopCon.gridx--;
		squareButtonCon.gridheight = 2;
		squareButtonCon.gridwidth = 1;
		for(int i = (numOfSquares/2) +1; i < numOfSquares/4*3; i++) {
			
			squareCon.gridx--;
			squareTopCon.gridx--;
			squareButtonCon.gridx = squareCon.gridx;
			squareButtonCon.gridy = squareCon.gridy;
			
			squareBot[i] = new JLabel(squares[i].getName()  );
			squareTop[i] = new JLabel();
			squareTop[i].setBackground(squares[i].getColour());
			squareTop[i].setOpaque(true);
			
			squaresPanel.add(squareBot[i], squareCon);
			squaresPanel.add(squareTop[i], squareTopCon);
			
			squareButtons[i] = new JButton();
			squareButtons[i].setOpaque(false);
			squareButtons[i].addActionListener(SP);
			squaresPanel.add(squareButtons[i], squareButtonCon);
			squareBot[i].setPreferredSize(di);
			
		}

		squareCon.gridx--;
		squareTopCon.gridx-=2;
		squareTopCon.gridy--;
		squareButtonCon.gridheight = 1;
		squareButtonCon.gridwidth = 2;
		for(int i = (numOfSquares/4*3) +1; i < numOfSquares; i++) {
			
			squareCon.gridy--;
			squareTopCon.gridy--;
			squareButtonCon.gridx = squareTopCon.gridx;
			squareButtonCon.gridy = squareTopCon.gridy;
			
			squareBot[i] = new JLabel(squares[i].getName()  );
			squareTop[i] = new JLabel();
			squareTop[i].setBackground(squares[i].getColour());
			squareTop[i].setOpaque(true);
			
			squaresPanel.add(squareBot[i], squareCon);
			squaresPanel.add(squareTop[i], squareTopCon);
			
			squareButtons[i] = new JButton();
			squareButtons[i].setOpaque(false);
			squareButtons[i].addActionListener(SP);
			squaresPanel.add(squareButtons[i], squareButtonCon);
			squareBot[i].setPreferredSize(di);
	
		}
		

		squareCon.gridwidth = 2;
		squareCon.gridheight = 2;
		squareButtonCon.gridheight = 2;
		di = new Dimension(185, 185);
		
		squareBot[0] = new JLabel(squares[0].getName()  );
		squareBot[numOfSquares/4] = new JLabel(squares[numOfSquares/4].getName()  );
		squareBot[numOfSquares/2] = new JLabel(squares[numOfSquares/2].getName()  );
		squareBot[numOfSquares/4*3] = new JLabel(squares[numOfSquares/4*3].getName()  );
		
		addCornerSquare(0, 0, 0, di);
		addCornerSquare(numOfSquares/4, numOfSquares/4 + 1, 0, di);
		addCornerSquare(numOfSquares/2, numOfSquares/4 + 1, numOfSquares/4 + 1, di);
		addCornerSquare(numOfSquares/4*3, 0, numOfSquares/4 + 1, di);
		
		

		
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
		
		squareCon.fill = GridBagConstraints.NONE;
		squareCon.anchor = GridBagConstraints.EAST;
		squareCon.gridwidth = 1;
		squareCon.gridheight = 2;
		squareCon.gridx = 3;
		squareCon.gridy = 3;
		squareCon.ipady = 0;
		squareCon.ipadx = 5;
		squaresPanel.add(dice[0], squareCon);
		squareCon.gridx = 4;
		squareCon.anchor = GridBagConstraints.WEST;
		squaresPanel.add(dice[1], squareCon);
		
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
		squareCon.gridx = 3;
		squareCon.gridwidth = 2;
		squareCon.anchor = GridBagConstraints.CENTER;
		squareCon.ipady = dicePics[2].getIconHeight();
		squareCon.ipadx = dicePics[2].getIconWidth() * 2;
		diceButton.setToolTipText("Click dice to roll");
		diceButton.setBorder(null);
		
		
		squaresPanel.add(diceButton, squareCon);
		
		
	}
	
	private void addCornerSquare(int squareNum, int x, int y, Dimension di) {
		
		squareCon.gridx = x;
		squareCon.gridy = y;
		squareButtonCon.gridx = x;
		squareButtonCon.gridy = y;
		squaresPanel.add(squareBot[squareNum], squareCon);
		squareBot[squareNum].setPreferredSize(di);
		squareButtons[squareNum] = new JButton();
		squareButtons[squareNum].setOpaque(false);
		squareButtons[squareNum].addActionListener(SP);
		squaresPanel.add(squareButtons[squareNum], squareButtonCon);
		
		
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
		
		for(int i = 0; i < numOfSquares; i++)
		{
			squareBot[i].setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, borderColour));
			squareBot[i].setBackground(foregroundColour);
			squareBot[i].setOpaque(true);
			
			
		}
		
		for(int i = 1; i < 5; i++) squareTop[i].setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, borderColour));
		
		for(int i = 6; i < 10; i++) squareTop[i].setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, borderColour));
		
		for(int i = 11; i < 15; i++) squareTop[i].setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, borderColour));
		
		for(int i = 16; i < 20; i++) squareTop[i].setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, borderColour));
		
		
		player.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, border));
		player.setBackground(foregroundColour);
		money.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, border));
		money.setBackground(foregroundColour);
		time.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, border));
		time.setBackground(foregroundColour);
		
		
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
	
}

