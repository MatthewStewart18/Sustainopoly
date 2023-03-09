import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

public class EndScreen extends JPanel{
	
	private Color background, foregroundColour, textColour, borderColour;
	private boolean darkMode = false;
	
	

	private Display display;
	
	private BufferedImage[] titles;// the light and dark versions of the title
	private JLabel title;//the label containing the title
	private int screenWidth;
	private int screenHeight;
	
	public EndScreen(Display display, boolean win) {
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
				setMinimumSize(new Dimension((int) (screenWidth/3), (int) (screenHeight/2)));
				setPreferredSize(new Dimension((int) (screenWidth/3), (int) (screenHeight/2)));
				setMaximumSize(new Dimension((int) (screenWidth/3), (int) (screenHeight/2)));
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
		
		mainPanel.setLayout(new GridBagLayout());
		GridBagConstraints gridCon = new GridBagConstraints();
		
		
		JTextPane info = new JTextPane(){
			/**
			 * {@inheritDoc}
			 * overrides the paint method to make sure the panel is the correct size
			 */
			 @Override
			public void paint(Graphics g) {
				setMinimumSize(new Dimension((int) (screenWidth/3), (int) (screenHeight/2)));
				setPreferredSize(new Dimension((int) (screenWidth/3), (int) (screenHeight/2)));
				setFont(new Font("Arial", Font.BOLD, (int) (screenWidth / 75)));
				super.paint(g);
			}
			
		};
		SimpleAttributeSet centre = new SimpleAttributeSet();
		StyleConstants.setAlignment(centre, StyleConstants.ALIGN_CENTER);
		info.setParagraphAttributes(centre, false);

		info.setText("IN 2022: \n\n\n\n\n\n\nIN 2023: \n\n\n\n\n\n\nIN 2024: \n\n\n\n\n\n\nIN 2025: \n\n\n\n\n\n\nIN 2026: \n\n\n\n\n\n\nIN 2027: \n\n\n\n\n\n\nIN 2028: \n\n\n\n\n\n\n"
				+ "IN 2029: \n\n\n\n\n\n\nIN 2030: \n\n\n\n\n\n\nIN 2031: \n\n\n\n\n\n\nIN 2032: \n\n\n\n\n\n\nIN 2033: \n\n\n\n\n\n\n");
		info.setForeground(textColour);
		info.setEditable(false);
		info.setOpaque(false);

		JScrollPane scrollInfo = new JScrollPane(info){
			/**
			 * {@inheritDoc}
			 * overrides the paint method to make sure the panel is the correct size
			 */
			 @Override
			public void paint(Graphics g) {
				setMinimumSize(new Dimension((int) (screenWidth/4), (int) (screenHeight/3)));
				setPreferredSize(new Dimension((int) (screenWidth/4), (int) (screenHeight/3)));
				setMaximumSize(new Dimension((int) (screenWidth/4), (int) (screenHeight/3)));
				
				super.paint(g);
			}
			
		};
		scrollInfo.setOpaque(false);
		scrollInfo.setBorder(null);
		scrollInfo.getViewport().setOpaque(false);
		scrollInfo.getViewport().setBorder(null);
		scrollInfo.setPreferredSize(new Dimension((int) (screenWidth/3), (int) (screenHeight/2)));
		
		mainPanel.add(info, gridCon);
		
		
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
