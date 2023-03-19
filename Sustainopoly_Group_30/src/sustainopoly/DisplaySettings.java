package sustainopoly;
import java.awt.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.plaf.metal.MetalCheckBoxIcon;
import javax.swing.text.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class DisplaySettings extends JPanel implements ActionListener{
	
	private Color background, foregroundColour, textColour, borderColour;
	private boolean darkMode = false;
	
	

	private Display display;
	
	private BufferedImage[] titles;// the light and dark versions of the title
	private JLabel title;//the label containing the title
	private int screenWidth;
	private int screenHeight;
	
	public DisplaySettings(Display display) {
		this.display = display;
		
		//sets the layout manager of the panel to be GridBag
		setLayout(new GridBagLayout());
		GridBagConstraints gridCon = new GridBagConstraints();
		gridCon.gridx = 1;
		gridCon.gridy = 1;
		gridCon.anchor = GridBagConstraints.CENTER;
		
		add(setUpOptions(), gridCon);
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
			
			titles[0] = (BufferedImage) ImageIO.read(new File("titles//displaySettingsLight.png"));
			titles[1] = (BufferedImage) ImageIO.read(new File("titles//displaySettingsDark.png"));
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
		
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
		changeColours(background, foregroundColour, textColour, borderColour, darkMode);
		repaint();
	}

	@SuppressWarnings("unchecked")
	private JPanel  setUpOptions() {
		
		JPanel mainPanel = new JPanel() {
			/**
			 * {@inheritDoc}
			 * overrides the paint method to make sure the panel is the correct size as well as add a border with rounded corners
			 */
			 @Override
			public void paint(Graphics g) {
				setMinimumSize(new Dimension((int) (screenWidth/3), (int) (screenHeight/2)));
				setPreferredSize(new Dimension((int) (screenWidth/3), (int) (screenHeight/2)));
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
		
		String[] colours = {"LightMode", "LightMode(High Contrast)", "DarkMode", "DarkMode(High Contrast)"};
		
		JComboBox colourOptions = new JComboBox(colours) {
			public void paint(Graphics g) {
				//override the paint method keep the correct colour and size
				this.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, borderColour));
				this.setBackground(foregroundColour);
				this.setForeground(textColour);
				this.setFont( new Font("Arial", Font.BOLD, screenWidth/70));
				super.paint(g);
			}
		};
		
		colourOptions.addActionListener(this);
		colourOptions.setFocusable(false);
		
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
		
		JCheckBox fullScreen = new JCheckBox() {
			
			public void paint(Graphics g) {
				
				int checkboxSize = screenWidth/50;
				
				super.paint(g);
				Graphics2D graphics = (Graphics2D) g;
				setMinimumSize(new Dimension(checkboxSize*2, checkboxSize*5/2));
				setPreferredSize(new Dimension(checkboxSize*2, checkboxSize*5/2));

				graphics.setColor(foregroundColour);
				graphics.fillRect(0,0, this.getWidth(), this.getHeight());
				graphics.setStroke(new BasicStroke(checkboxSize/4, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
				graphics.setColor(borderColour);
				graphics.drawRect(checkboxSize/2,checkboxSize*3/2- checkboxSize/2, checkboxSize, checkboxSize);
				if(isSelected()) {
					graphics.setColor(textColour);
					graphics.drawLine(checkboxSize*3/5,checkboxSize*11/10,checkboxSize, checkboxSize*5/2-checkboxSize/4*3);
					graphics.drawLine(checkboxSize, checkboxSize*5/2-checkboxSize/4*3,checkboxSize*3/2, 0);
				}
				
				
				
			}
		};
		fullScreen.setSelected(true);
		fullScreen.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				display.setFullScreen(fullScreen.isSelected());
				
			}
			
		});
		fullScreen.setPreferredSize(new Dimension(50,50));
		
		
		JLabel pickColourLabel = new JLabel("Pick a colour:"){
			//override the paint method keep the correct text colour and size
			public void paint(Graphics g) {
				this.setForeground(textColour);
				this.setFont( new Font("Arial", Font.BOLD, screenWidth/70));
				super.paint(g);
			}
			
		};
		
		
		JLabel fullScreenLabel = new JLabel("Borderless FullScreen:"){
			//override the paint method keep the correct text colour and size
			public void paint(Graphics g) {
				this.setForeground(textColour);
				this.setFont( new Font("Arial", Font.BOLD, screenWidth/70));
				super.paint(g);
			}
			
		};
		
		
		
		gridCon.gridy = 1;
		mainPanel.add(pickColourLabel, gridCon);
		
		gridCon.gridwidth = 2;
		gridCon.gridy = 3;
		mainPanel.add(fullScreenLabel, gridCon);
		
		
		gridCon.gridx = 1;
		gridCon.gridy = 1;
		mainPanel.add(colourOptions, gridCon);
		
		gridCon.gridwidth = 1;
		gridCon.gridy = 3;
		gridCon.gridx = 2;
		mainPanel.add(fullScreen, gridCon);
		gridCon.gridy = 5;
		mainPanel.add(close, gridCon);
		
		
		JLabel spacer1 = new JLabel();
		JLabel spacer2 = new JLabel();
		JLabel spacer3 = new JLabel();
		JLabel spacer4 = new JLabel();
		gridCon.gridheight = 1;
		gridCon.gridwidth = 3;
		gridCon.gridx = 1;
		gridCon.gridy = 0;
		gridCon.weightx = 0;
		gridCon.weighty = 1;
		mainPanel.add(spacer1, gridCon);
		gridCon.gridy = 2;
		mainPanel.add(spacer2, gridCon);
		gridCon.gridy = 4;
		mainPanel.add(spacer3, gridCon);
		gridCon.gridy = 6;
		mainPanel.add(spacer4, gridCon);
		return mainPanel;
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
		int selected = ((JComboBox) e.getSource()).getSelectedIndex();
		
		if(selected == 0) {
			display.setColours(new Color(56, 163, 165),new Color(87, 204, 153),Color.BLACK,new Color(34, 87, 122), false);
			
		}else if(selected == 1) {
			display.setColours(Color.WHITE,Color.WHITE,Color.BLACK,Color.BLACK, false);
			
		}else if(selected == 2) {
			display.setColours(new Color(0, 0, 0),new Color(21, 0, 80),new Color(251, 37, 118),new Color(63, 0, 113), true);
			
		}else if(selected == 3) {
			display.setColours(Color.BLACK,Color.BLACK,Color.WHITE,Color.WHITE, true);
		}
		
		
		
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
			title.setIcon(new ImageIcon(titles[1].getScaledInstance(screenWidth/2, screenHeight/4,Image.SCALE_FAST)));
		}else {
			title.setIcon(new ImageIcon(titles[0].getScaledInstance(screenWidth/2, screenHeight/4,Image.SCALE_FAST)));
		}
		
		repaint();
		revalidate();
		
	}
	
	
}
