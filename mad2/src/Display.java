

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class Display extends JFrame implements WindowStateListener {
	
	private Color background, foregroundColour, textColour, borderColour;
	private Container pane;
	Board3 GB, GB2;
	CardLayout layout;
	private boolean fullScreen = false;
	
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
		
		GB = new Board3(sq, this);
		AccessibilityPanel AP = new AccessibilityPanel(this);
		
		
		
		
		pane.add(GB, "board");
		pane.add(AP, "access");
		
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
        setLocationRelativeTo(null);
        
        setVisible(true);
        
        this.setBackground(Color.blue);
        addWindowStateListener(this);
        h();
        
this.addComponentListener(new ComponentAdapter() {
	    public void componentResized(ComponentEvent componentEvent) {
	    	if(fullScreen) GB.resize(getBounds().getSize().width, getBounds().getSize().height);
	    	else GB.resize(getBounds().getSize().width, getBounds().getSize().height);
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

	@Override
	public void windowStateChanged(WindowEvent e) {
		if(fullScreen) GB.resize(getBounds().getSize().width, getBounds().getSize().height);
    	else GB.resize(getBounds().getSize().width, getBounds().getSize().height);
	}
	
	
	
	
}
