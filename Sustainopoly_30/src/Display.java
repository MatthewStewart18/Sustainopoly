

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class Display extends JFrame {
	
	private Color background, foregroundColour, textColour, borderColour;
	private Container pane;
	Board2 GB, GB2;
	CardLayout layout;
	
	public Display()
	{
		Square[] sq = new Square[20];
		
		for(int i = 0; i < 20; i++) {
			sq[i] = new Square();
		}
		setTitle("BOARD");
		pane = getContentPane();
		layout = new CardLayout();
		pane.setLayout(layout);
		
		GB = new Board2(sq, this);
		AccessibilityPanel AP = new AccessibilityPanel(this);
		
		
		
		
		pane.add(GB, "board");
		pane.add(AP, "access");
		
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
        setLocationRelativeTo(null);
        setVisible(true);
        
        
        this.setBackground(Color.blue);
        

				
				
	}
	
	public void setColours(Color background, Color squares, Color text, Color border) {
		
		this.background = background;
		this.foregroundColour = squares;
		this.textColour = text;
		this.borderColour = border;
		
		GB.changeColours(background, squares, text, border);
			
	}
	
	public void openAccessability() {
		layout.show(this.getContentPane(), "access");
	}
	
	public void openBoard() {
		layout.show(this.getContentPane(), "board");
	}
	
	


	
}
