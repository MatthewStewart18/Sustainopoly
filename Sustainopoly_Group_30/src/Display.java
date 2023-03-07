

import java.awt.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Display extends JFrame implements WindowStateListener {
	
	private Color background, foregroundColour, textColour, borderColour;
	private Container pane;
	Board3 GB, GB2;
	CardLayout layout;
	private boolean fullScreen = false;
	StartMenu sM;
	private boolean darkMode = false;
	
	
	private Game game;
	
	public Display()
	{
		
		setTitle("BOARD");
		pane = getContentPane();
		layout = new CardLayout();
		pane.setLayout(layout);
		
		DisplayOptions AP = new DisplayOptions(this);
		pack();
		setFullScreen(true);
		
		sM = new StartMenu(this);
		
		pane.add(sM, "startMenu");
		
		pane.add(AP, "access");
		
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
        setLocationRelativeTo(null);
        
        setVisible(true);
        
        this.setBackground(Color.blue);
		addWindowStateListener(this);

		sM.resize(getBounds().getSize().width, getBounds().getSize().height);
		
		setColours(new Color(56, 163, 165), new Color(87, 204, 153), Color.BLACK, new Color(34, 87, 122), darkMode);
		
		
		this.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent componentEvent) {
				if (GB != null) {
					GB.resize(getBounds().getSize().width, getBounds().getSize().height);
				}
				if (sM != null) {
					sM.resize(getBounds().getSize().width, getBounds().getSize().height);
				}
			}
		});
	}

	public void startGame(ArrayList<String[]> players) {
		game = new Game(players, this);
		GB = game.getGameBoard();
		pane.add(GB, "board");
		layout.show(this.getContentPane(), "board");
		sM = null;
		GB.resize(getBounds().getSize().width, getBounds().getSize().height);
		setFullScreen(fullScreen);
	}
	
	public void setFullScreen(boolean full) {
		
		dispose();
		setUndecorated(full);
		setExtendedState(JFrame.MAXIMIZED_BOTH); 
		fullScreen = full;
		pack();
		setVisible(true);

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
