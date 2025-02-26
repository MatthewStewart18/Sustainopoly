package sustainopoly;
import java.awt.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * the EndScreen shows either the win or lose screen after the game ends
 * it shows the future of the website and how much each person contributed
 * @author Magnus
 *
 */
public class EndScreen extends JPanel{
	
	private Color background, foregroundColour, textColour, borderColour;
	private boolean darkMode = false;
	
	

	private Display display;
	
	private BufferedImage[] titles;// the light and dark versions of the title
	private JLabel title;//the label containing the title
	private int screenWidth;
	private int screenHeight;
	private boolean win;
	
	private Player[] players;
	
	int currentText = 0;
	
	public EndScreen(Display display, boolean win, Player[] players) {
		this.display = display;
		this.players = players;
		this.win = win;
		//sets the layout manager of the panel to be GridBag
		setLayout(new GridBagLayout());
		GridBagConstraints gridCon = new GridBagConstraints();
		gridCon.gridx = 1;
		gridCon.gridy = 1;
		gridCon.anchor = GridBagConstraints.CENTER;
		add(setUpInfo(), gridCon);
		
		
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
		
		//add spacers at the left and right areas of the screen so that the information is centred
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
			if(win) {
				titles[0] = (BufferedImage) ImageIO.read(new File("titles//winLight.png"));
				titles[1] = (BufferedImage) ImageIO.read(new File("titles//winDark.png"));
			}else {
				titles[0] = (BufferedImage) ImageIO.read(new File("titles//loseLight.png"));
				titles[1] = (BufferedImage) ImageIO.read(new File("titles//loseDark.png"));
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void resize(int screenWidth, int screenHeight) {
		
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
		changeColours(background, foregroundColour, textColour, borderColour, darkMode);
		repaint();
	}
	
	private JPanel setUpInfo() {
		
		JPanel mainPanel = new JPanel() {
			/**
			 * {@inheritDoc}
			 * overrides the paint method to make sure the panel is the correct size as well as add a border with rounded corners
			 */
			 @Override
			public void paint(Graphics g) {
				setMinimumSize(new Dimension((int) (screenWidth/4*3), (int) (screenHeight/5*3)));
				setPreferredSize(new Dimension((int) (screenWidth/4*3), (int) (screenHeight/5*3)));
				Graphics2D sqImGraphics = (Graphics2D) g;
				sqImGraphics.setColor(foregroundColour);
				sqImGraphics.fillRoundRect(5,5, this.getWidth()-10, this.getHeight()-10, 30,30);
				sqImGraphics.setStroke(new BasicStroke(10));
				sqImGraphics.setColor(borderColour);
				sqImGraphics.drawRoundRect(5,5, this.getWidth()-10, this.getHeight()-10, 30,30);
				super.paint(g);
			}
			
		};
		
		mainPanel.setOpaque(false);
		
		//sets the layout manager of the panel to be GridBag
		mainPanel.setLayout(new GridBagLayout());
		GridBagConstraints gridCon = new GridBagConstraints();
		
		
		
		ArrayList<String> infoText = new ArrayList();
		if(win) {
			infoText.add("2023: \nYou have just released the website for public use.\nA few people have started using it.");
			infoText.add("2024:\nThe traffic on the site has drasticaly increased.\nDonating old phones to people has made the website much more well known.");
			infoText.add("2025:\nThe number of active users of the website has reached over 2 thousand");
			infoText.add("2026:\nWith the large number of users, the district council is listening to the polls hosted on the website.\nThis means th eease of travel in govan should increase");
			infoText.add("2030:\nThanks to the website and the chabges in public transport due to the polls on the website, transportation in Govan has become much easier");
		}
		
		
		for(int i = 0; i < players.length; i++) {
			ArrayList<Investment> investments = players[i].listInvestments();
			String text = players[i].getName();
			for(int j = 0; j < investments.size(); j++) {
				text = text + "\n" + investments.get(j).getName() + ": "+ investments.get(j).getPrice() + " pounds, " + investments.get(j).getTime() + " hours";
			}
			infoText.add(text);
		}
		
		
		JTextPane description = new JTextPane() {
			public void paint(Graphics g) {
				setMinimumSize(new Dimension((int) (screenWidth/5*3), (int) (screenHeight/5*2)));
				setPreferredSize(new Dimension((int) (screenWidth/5*3), (int) (screenHeight/5*2)));
				setFont(new Font("Arial", Font.BOLD, (int) (screenWidth / 50)));
				setText(infoText.get(currentText));
				this.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, borderColour));
				setForeground(textColour);
				super.paint(g);
			}
		};

		SimpleAttributeSet centre = new SimpleAttributeSet();
		StyleConstants.setAlignment(centre, StyleConstants.ALIGN_CENTER);
		description.setParagraphAttributes(centre, false);
		description.setEditable(false);
		description.setOpaque(false);
		
		
		
		JButton next = new JButton("NEXT"){
			//override the paint method keep the correct text colour and size
			public void paint(Graphics g) {
				this.setForeground(textColour);
				this.setFont( new Font("Arial", Font.BOLD, screenWidth/70));
				this.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, borderColour));
				this.setBackground(foregroundColour);
				super.paint(g);
			}
			
		};
		
		next.setFocusPainted(false);
		
		next.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				if(currentText < infoText.size()-1) {
					currentText++;
				}
			}
		});
		
		JButton back = new JButton("BACK"){
			//override the paint method keep the correct text colour and size
			public void paint(Graphics g) {
				this.setForeground(textColour);
				this.setFont( new Font("Arial", Font.BOLD, screenWidth/70));
				this.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, borderColour));
				this.setBackground(foregroundColour);
				super.paint(g);
			}
			
		};
		
		back.setFocusPainted(false);
		
		back.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				if(currentText > 0) {
					currentText--;
				}
			}
		});
		
		
		JButton close = new JButton("CLOSE"){
			//override the paint method keep the correct text colour and size
			public void paint(Graphics g) {
				this.setForeground(textColour);
				this.setFont( new Font("Arial", Font.BOLD, screenWidth/70));
				this.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, borderColour));
				this.setBackground(foregroundColour);
				super.paint(g);
			}
			
		};
		
		close.setFocusPainted(false);
		
		close.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				display.returnToMainMenu();
			}
		});
		
		gridCon.gridx = 1;
		gridCon.insets = new Insets(5, 5, 5, 5);
		
		gridCon.gridy = 1;
		mainPanel.add(description, gridCon);
		
		gridCon.gridy = 2;
		gridCon.anchor = GridBagConstraints.WEST;
		mainPanel.add(back, gridCon);
		gridCon.anchor = GridBagConstraints.EAST;
		mainPanel.add(next, gridCon);
		
		
		gridCon.gridy = 3;
		gridCon.insets = new Insets(20, 20, 20, 20);
		mainPanel.add(close, gridCon);
		
		
		return mainPanel;
	}
	
	
	public void changeColours(Color background, Color foreground, Color text, Color border, boolean dark) {
		this.background = background;
		this.foregroundColour = foreground;
		this.textColour = text;
		this.borderColour = border;
		this.darkMode = dark;
		this.setBackground(background);
		
		//sets the title to be the dark version if dark is true else it becomes the light version
		if(dark) {
			title.setIcon(new ImageIcon(titles[1].getScaledInstance(screenWidth/3, screenHeight/4,Image.SCALE_FAST)));
		}else {
			title.setIcon(new ImageIcon(titles[0].getScaledInstance(screenWidth/3, screenHeight/4,Image.SCALE_FAST)));
		}
		
		repaint();
		revalidate();
		
	}

}
