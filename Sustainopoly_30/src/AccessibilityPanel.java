import java.awt.*;
import javax.swing.*;
import javax.swing.text.*;
import java.awt.event.*;

public class AccessibilityPanel extends JPanel implements ActionListener{
	JComboBox colourOptions;
	Display display;
	public AccessibilityPanel(Display display) {
		
		this.display = display;
		String[] colours = {"LightMode", "LightMode(High Contrast)", "DarkMode", "DarkMode(High Contrast)"};
		
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
			display.setColours(Color.green, Color.CYAN, Color.DARK_GRAY, Color.MAGENTA);
			
		}else if(selected == 1) {
			display.setColours(Color.ORANGE, Color.PINK, Color.YELLOW, Color.red);
			
		}else if(selected == 2) {
			
		}else if(selected == 3) {
			
		}
		
		
		
	}
	
	
}
