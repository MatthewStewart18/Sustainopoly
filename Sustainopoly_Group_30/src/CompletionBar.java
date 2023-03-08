

import java.awt.*;
import javax.swing.*;
public class CompletionBar {

	public int completionBar = 0;
	public boolean completionDone;
	public double updateCompletionBar;
	// public double amountDonated = 0;
	// public double amountRequired = 10000;
	
	public double getValue() {
		return completionBar;
	}
	public double setValue() {
		return completionBar;
	}
	
	
	JFrame frame = new JFrame();
	JProgressBar bar = new JProgressBar(0, 5000);
	
	CompletionBar() {
		
		bar.setValue(0);
		bar.setBounds(0,0,420,50);
		bar.setStringPainted(true);
		
		frame.add(bar);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(420, 420);
		frame.setLayout(null);
		frame.setVisible(true);
		
		
	}
	
	public void progress(int amount) {

	        // Update the progress bar with the new donation
	        int currentValue = bar.getValue();
	        int newValue = currentValue + amount;
	        bar.setValue(newValue);

	        // Check if the goal has been reached
	        if (newValue >= 5000) {
	            // Game Finishes when goal donation is raised
	            System.out.println("Progress Bar Completed");
	        }
	    	 	
		
//		while(completionBar <= 100) {
//			bar.setValue(completionBar);
//			
//		} 
//				completionDone = true;
}
	
	public static void main(String[] args) {
		
		SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                CompletionBar game = new CompletionBar();
                // frame.setVisible(true);

                // Simulate some donations
                game.progress(1000);
                game.progress(1000);
            }
        });
	}

}