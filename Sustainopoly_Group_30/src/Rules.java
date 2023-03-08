import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Rules extends JPanel{
	
	private Color background, foregroundColour, textColour, borderColour;
	private boolean darkMode = false;
	
	

	private Display display;
	
	private BufferedImage[] titles;// the light and dark versions of the title
	private JLabel title;//the label containing the title
	private int screenWidth;
	private int screenHeight;
	
	
	public Rules(Display display) {
		
this.display = display;
		
		//sets the layout manager of the panel to be GridBag
		setLayout(new GridBagLayout());
		GridBagConstraints gridCon = new GridBagConstraints();
		gridCon.gridx = 1;
		gridCon.gridy = 1;
		gridCon.anchor = GridBagConstraints.CENTER;
		
		
		
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
			
			titles[0] = (BufferedImage) ImageIO.read(new File("titles//mainTitleLight.png"));
			titles[1] = (BufferedImage) ImageIO.read(new File("titles//mainTitleDark.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.display = display;
		
		JButton close = new JButton("CLOSE");
		
		close.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				display.returnToPrev();
			}
		});
		
		this.add(close);
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
				Graphics2D sqImGraphics = (Graphics2D) g;
				sqImGraphics.setColor(foregroundColour);
				sqImGraphics.fillRoundRect(5,5, this.getWidth()-10, this.getHeight()-10, 30,30);
				sqImGraphics.setStroke(new BasicStroke(10));
				sqImGraphics.setColor(borderColour);
				sqImGraphics.drawRoundRect(5,5, this.getWidth()-10, this.getHeight()-10, 30,30);
				super.paint(g);
			}
			
		};
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
