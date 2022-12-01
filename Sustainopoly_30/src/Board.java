

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class Board extends JFrame {
	
	
	private myHandler h = new myHandler();
	private JButton[] b;
	private GridBagConstraints c;
	private Container pane;
	private JLabel player, money, time;
	private JButton menu, endTurn, analyse;

	public Board()
	{
		setTitle("BOARD");
		b = new JButton[14];
		pane = getContentPane();
		pane.setLayout(new GridBagLayout());
		c = new GridBagConstraints();
		
		//add the tiles to the board
		addTile(1,1,3);
		addTile(2,2,3);
		addTile(3,3,3);
		addTile(3,5,5);
		addTile(3,1,5);
		addTile(4,4,3);
		addTile(5,5,3);
		addTile(6,5,4);
		addTile(7,5,6);
		addTile(8,5,7);
		addTile(9,4,7);
		addTile(10,3,7);
		addTile(11,2,7);
		addTile(12,1,7);
		addTile(13,1,6);
		addTile(14,1,4);
		
		
		//add current player, amount of money, and time remaining to top right of board
		player = new JLabel(" Player: (player name)");
		c.fill = GridBagConstraints.BOTH; 
		c.ipady = 5;
		c.ipadx = 0;
		c.weightx = 1;
		c.weighty = 1;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.gridx = 1;
		c.gridy = 1;
		player.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, Color.LIGHT_GRAY));
		pane.add(player, c);
		
		money = new JLabel(" Money: £0.00");
		c.gridx = 2;
		money.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, Color.LIGHT_GRAY));
		pane.add(money, c);
		
		time = new JLabel("Time: 0 Hours");
		c.gridx = 3;
		time.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, Color.LIGHT_GRAY));
		pane.add(time, c);
		
		
		
		
		menu = new JButton("MENU");
		menu.addActionListener(h);
		c.fill = GridBagConstraints.VERTICAL; 
		c.ipady = 0; 
		c.ipadx = 0;
		c.weightx = 0;
		c.weighty = 1;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.gridx = 5;
		c.gridy = 1;
		c.anchor = GridBagConstraints.NORTHEAST;
		pane.add(menu, c);
		
		endTurn = new JButton("END TURN");
		endTurn.addActionListener(h);
		c.gridx = 5;
		c.gridy = 9;
		c.anchor = GridBagConstraints.SOUTHEAST;
		pane.add(endTurn, c);
		
		analyse = new JButton("ANALYSE TILE");
		analyse.addActionListener(h);
		c.gridx = 1;
		c.gridy = 9;
		c.anchor = GridBagConstraints.SOUTHWEST;
		pane.add(analyse, c);
		
		
		addSpacer(0,2,7,1,0,30);
		addSpacer(0,0,7,1,0,30);
		addSpacer(0,8,7,1,0,30);
		addSpacer(0,10,7,1,0,30);
		addSpacer(0,3,1,5,30,0);
		addSpacer(6,3,1,5,30,0);
		
		
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
        setLocationRelativeTo(null);
        setVisible(true);
		
		
	}
	
	
	private static void menu()
	{
		JFrame frame = new JFrame("MENU");
	    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	     
	    Container con = frame.getContentPane();
	     
	    JButton button;
	    JLabel space;
	    
		con.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
	    
		space = new JLabel("MENU", SwingConstants.CENTER);
		space.setFont(new Font("Serif", Font.BOLD, 50)); 
		c.fill = GridBagConstraints.BOTH; 
		c.ipady = 100; 
		c.ipadx = 100;
		c.gridx = 0;
		c.gridy = 0;
		
		con.add(space, c);
		
		button = new JButton("CLOSE MENU"); 
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				frame.dispose();
			}
		});
			
		c.fill = GridBagConstraints.NONE; 
		c.ipady = 20; 
		c.ipadx = 20;
		c.gridx = 0;
		c.gridy = 1;
		c.anchor = GridBagConstraints.CENTER;
		c.insets = new Insets(10,10,10,10);
		con.add(button, c);
		
		button = new JButton("RULES"); 
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				
			}
		});
		c.fill = GridBagConstraints.NONE; 
		c.ipady = 20;
		c.ipadx = 57;
		c.gridx = 0;
		c.gridy = 2;
		c.anchor = GridBagConstraints.CENTER;
		c.insets = new Insets(10,10,10,10);
		con.add(button, c);
		
		button = new JButton("EXIT"); 
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				System.exit(0);
			}
		});
		c.fill = GridBagConstraints.NONE; 
		c.ipady = 20;
		c.ipadx = 71;
		c.gridx = 0;
		c.gridy = 3;
		c.anchor = GridBagConstraints.CENTER;
		c.insets = new Insets(10,10,30,10);
		con.add(button, c);
		
		
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	     
	}
	
	private void addTile(int number, int xPos, int yPos)
	{
		ImageIcon tile = new ImageIcon(number + ".png");
		
		b[number-1] = new JButton(tile);
		b[number-1].addActionListener(h);
		 
		c.fill = GridBagConstraints.BOTH; 
		c.ipady = -10;
		c.ipadx = -30;
		c.gridx = xPos;
		c.gridy = yPos;
		pane.add(b[number-1], c);
	}
	
	private void addSpacer(int xPos, int yPos, int width, int height, int xPad, int yPad)
	{
		JLabel spacer = new JLabel(); 
		 
		c.fill = GridBagConstraints.BOTH; 
		c.gridx = xPos;
		c.gridy = yPos;
		c.gridwidth = width;
		c.gridheight = height;
		c.ipadx = xPad;
		c.ipady = yPad;
		c.weightx = 1;
		c.weighty = 1;
		
		
		pane.add(spacer, c);
	}


	private class myHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			Object ob = e.getSource();
			
			if(ob == menu) menu();

			

		} 

	} 


}