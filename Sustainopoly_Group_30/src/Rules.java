import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

public class Rules extends JPanel{
	
	Display display;
	
	public Rules(Display display) {
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

}
