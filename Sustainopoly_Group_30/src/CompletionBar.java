

import java.awt.*;
import javax.swing.*;
public class CompletionBar {

	public static int completionBar = 0;
	public boolean completionDone;
	public double updateCompletionBar;
	// public double amountDonated = 0;
	// public double amountRequired = 10000;
	
	/**
	 * Method returns value of completionBar
	 * @return 
	 */
	public static double getValue() {
		return completionBar;
	}
	
	public double setValue() {
		return completionBar;
	}
	
	
	JFrame frame = new JFrame();
	JProgressBar bar ;
	
	CompletionBar(int max) {
		bar = new JProgressBar(0, max);
		bar.setValue(0);
		bar.setBounds(0,0,700,100);
		
		bar.setStringPainted(true);
		
	}
	/**
	 * Method updates progress bar and checks for completion
	 * @param amount
	 */
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
	
	/**
	 * Method returns the progress bar
	 * @return 
	 */
	public JProgressBar getBar() {
		return bar;
		
	}
	
	

}