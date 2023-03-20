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

public class Rules extends JPanel{
	
	private Color background, foregroundColour, textColour, borderColour;
	private boolean darkMode = false;
	
	

	private Display display;
	
	private BufferedImage[] titles;// the light and dark versions of the title
	private JLabel title;//the label containing the title
	private int screenWidth;
	private int screenHeight;
	
	private int currentPic = 1;
	
	
	public Rules(Display display) {
		
		this.display = display;
		
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
			
			titles[0] = (BufferedImage) ImageIO.read(new File("titles//rulesLight.png"));
			titles[1] = (BufferedImage) ImageIO.read(new File("titles//rulesDark.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.display = display;
		
		
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
				setMinimumSize(new Dimension((int) (screenWidth/2), (int) (screenHeight/3*2)));
				setPreferredSize(new Dimension((int) (screenWidth/2), (int) (screenHeight/3*2)));
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
		
		JButton image = new JButton(){
			
			public void paint(Graphics g) {
				setMinimumSize(new Dimension((int) (screenWidth/3), (int) (screenHeight/3)));
				setPreferredSize(new Dimension((int) (screenWidth/3), (int) (screenHeight/3)));
				BufferedImage buffImage = null;
				try {
					buffImage = (BufferedImage) ImageIO.read(new File("rules//" + currentPic + ".png"));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				setIcon(new ImageIcon(buffImage.getScaledInstance(screenWidth/3, screenHeight/3,Image.SCALE_FAST)));
				
				
				super.paint(g);
			}
			
		};
		image.setFocusPainted(false);
		image.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, borderColour));
		
		String[] infoText = {"To start your turn press the dice", "if you press a square it will zoom in on the square and give you a description about it", 
				"Every time you start a new week your time returns to 40 hours and 50 pounds is taken from your money", "If you run out of money everyone will lose the game", 
				"before you can progress on a task you must own the task which requires an initial investment", 
				"To progress a task you must spend resources on it by clicking on it and using the sliders",
				"You can only progress tasks you own or are currently landed on",
				"If you do not have enough of a resouce, spending the resource will fail", "progressing tasks will increase the progress bar and once it hits 100% you will win"};
		
		JTextPane description = new JTextPane() {
			public void paint(Graphics g) {
				setMinimumSize(new Dimension((int) (screenWidth/3), (int) (screenHeight/6)));
				setPreferredSize(new Dimension((int) (screenWidth/3), (int) (screenHeight/6)));
				setFont(new Font("Arial", Font.BOLD, (int) (screenWidth / 50)));
				setText(infoText[currentPic-1]);
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
				if(currentPic < 9) {
					currentPic++;
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
				if(currentPic > 1) {
					currentPic--;
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
				display.returnToPrev();
			}
		});
		
		gridCon.gridx = 1;
		gridCon.insets = new Insets(5, 5, 5, 5);
		mainPanel.add(image, gridCon);
		
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
			title.setIcon(new ImageIcon(titles[1].getScaledInstance(screenWidth/2, screenHeight/5,Image.SCALE_FAST)));
		}else {
			title.setIcon(new ImageIcon(titles[0].getScaledInstance(screenWidth/2, screenHeight/5,Image.SCALE_FAST)));
		}
		
		repaint();
		revalidate();
		
	}

}
