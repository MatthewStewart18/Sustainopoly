

import java.awt.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Display extends JFrame implements WindowStateListener {
	
	private Color background, foregroundColour, textColour, borderColour;
	private Container pane;
	Board3 GB, GB2;
	CardLayout layout;
	private boolean fullScreen = false;
	StartMenu sM;
	private boolean darkMode = false;
	
	public Display()
	{
		
		setTitle("BOARD");
		pane = getContentPane();
		layout = new CardLayout();
		pane.setLayout(layout);
		
		
		
		//GB = new Board3(sq, this, playerIcons);
		AccessibilityPanel AP = new AccessibilityPanel(this);
		pack();
		h();
		
		sM = new StartMenu(getBounds().getSize().width, getBounds().getSize().height, this);
		
		
		
		pane.add(sM, "startMenu");
		//
		pane.add(AP, "access");
		
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
        setLocationRelativeTo(null);
        
        setVisible(true);
        
        this.setBackground(Color.blue);
        addWindowStateListener(this);
        
        sM.resize(getBounds().getSize().width, getBounds().getSize().height);
        sM.changeColours(new Color(56, 163, 165),new Color(87, 204, 153),Color.BLACK,new Color(34, 87, 122),darkMode);
this.addComponentListener(new ComponentAdapter() {
	    public void componentResized(ComponentEvent componentEvent) {
	    	if(GB!= null) {
				GB.resize(getBounds().getSize().width, getBounds().getSize().height);
			}
			if(sM != null) {
				sM.resize(getBounds().getSize().width, getBounds().getSize().height);
			}
	    }
	});

	
				
				
	}
	
	public void h() {
		
		dispose();
		setUndecorated(true);
		setExtendedState(JFrame.MAXIMIZED_BOTH); 
		fullScreen = true;
		pack();
		setExtendedState(JFrame.MAXIMIZED_BOTH); 
		setVisible(true);

	}
	
	public void openBoard() {
		Square[] sq = new Square[20];
		
		for(int i = 0; i < 20; i++) {
			sq[i] = new Square();
		}
		BufferedImage im = null;
		try {
			im = (BufferedImage) ImageIO.read(new File("icons\\Helicopter.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BufferedImage[] playerIcons = {im};
		GB = new Board3(sq, this, playerIcons);
		
		pane.add(GB, "board");
		layout.show(this.getContentPane(), "board");
		
		sM = null;
		h();
	}
	
	public void setColours(Color background, Color squares, Color text, Color border, boolean dark) {
		
		this.background = background;
		this.foregroundColour = squares;
		this.textColour = text;
		this.borderColour = border;
		this.darkMode = dark;
		
		if(sM != null) {
			sM.changeColours(background, squares, text, border, dark);
		}
		if(GB != null) {
			GB.changeColours(background, squares, text, border, dark);
		}
		
			
	}
	
	public void openAccessability() {
		layout.show(this.getContentPane(), "access");
	}
	
	public void returnToPrev() {
		
		if(sM != null) {
			layout.show(this.getContentPane(), "startMenu");
		}
		layout.show(this.getContentPane(), "board");
	}

	@Override
	public void windowStateChanged(WindowEvent e) {
		if(GB!= null) {
			GB.resize(getBounds().getSize().width, getBounds().getSize().height);
		}
		if(sM != null) {
			sM.resize(getBounds().getSize().width, getBounds().getSize().height);
		}
		
    	
	}
	
	
	
	
}
