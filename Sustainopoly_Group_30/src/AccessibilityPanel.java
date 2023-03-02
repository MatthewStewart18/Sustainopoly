import java.awt.*;
import javax.swing.*;
import javax.swing.text.*;
import java.awt.event.*;

public class AccessibilityPanel extends JPanel implements ActionListener{
	JComboBox colourOptions;
	Display display;
	public AccessibilityPanel(Display display) {
		
		this.display = display;
		String[] colours = {"LightMode", "LightMode(High Contrast)", "DarkMode", "DarkMode(High Contrast)", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13"};
		
		colourOptions = new JComboBox(colours);
		
		colourOptions.addActionListener(this);
		
		JButton close = new JButton("CLOSE");
		
		close.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				display.openBoard();
			}
		});
		
		this.add(colourOptions);
		this.add(close);
		
	}

	



	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
		int selected = colourOptions.getSelectedIndex();
		
		if(selected == 0) {
			display.setColours(Color.green, Color.CYAN, Color.DARK_GRAY, Color.MAGENTA, false);
			
		}else if(selected == 1) {
			display.setColours(Color.ORANGE, Color.PINK, Color.YELLOW, Color.red, false);
			
		}else if(selected == 2) {
			display.setColours(new Color(224, 20, 76),new Color(255, 88, 88),new Color(255, 151, 193),new Color(154, 22, 99), false);
			
		}else if(selected == 3) {
			display.setColours(new Color(227, 199, 112),new Color(255, 174, 109),Color.BLACK,new Color(254, 205, 112), false);
		}else if(selected == 4) {
			
			display.setColours(new Color(129, 12, 168),new Color(193, 71, 233),new Color(229, 184, 244),new Color(45, 3, 59), false);
		}else if(selected == 5) {
			
			display.setColours(new Color(0, 0, 0),new Color(21, 0, 80),new Color(251, 37, 118),new Color(63, 0, 113), true);
		}else if(selected == 6) {
			
			display.setColours(new Color(234, 4, 126),new Color(252, 231, 0),Color.BLACK,new Color(255, 109, 40), false);
			
		}else if(selected == 7) {
			display.setColours(new Color(84, 140, 255),new Color(147, 255, 216),Color.BLACK,new Color(121, 0, 255), false);
			
		}else if(selected == 8) {
			display.setColours(new Color(169,175,185),new Color(139,210,255),new Color(35,42,44),new Color(145,26,39), false);
			
		}else if(selected == 9) {
			display.setColours(new Color(207, 253, 225),new Color(104, 185, 132),Color.BLACK,new Color(61, 86, 86), false);
			
		}else if(selected == 10) {
			display.setColours(new Color(47, 164, 255),new Color(0, 255, 221),Color.BLACK,new Color(14, 24, 95), false);
			
		}
		else if(selected == 11) {
			display.setColours(new Color(56, 163, 165),new Color(87, 204, 153),Color.BLACK,new Color(34, 87, 122), false);
			
		}else if(selected == 12) {
			display.setColours(Color.WHITE,Color.WHITE,Color.BLACK,Color.BLACK, false);
			
		}else if(selected == 13) {
			display.setColours(Color.BLACK,Color.BLACK,Color.WHITE,Color.WHITE, true);
			
		}
		
		
		
	}
	
	
}
