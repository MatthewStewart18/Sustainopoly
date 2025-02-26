package sustainopoly;

import java.awt.*;
import javax.swing.*;

public class CompletionBar {

	public static int completionBar = 0;
	public boolean completionDone = false;
	private int max;
	private JProgressBar bar;

	/**
	 * Method returns value of completionBar
	 * 
	 * @return
	 */
	public static double getValue() {
		return completionBar;
	}

	public boolean isCompleted() {
		return completionDone;
	}

	public CompletionBar(int max) {
		this.max = max;
		bar = new JProgressBar(0, max);
		bar.setValue(0);
		bar.setBounds(0, 0, 700, 100);

		bar.setStringPainted(true);
		completionBar = 0;

	}

	/**
	 * Method updates progress bar and checks for completion
	 * 
	 * @param amount
	 */
	public void progress(int amount) {

		// Update the progress bar with the new donation
		completionBar = completionBar + amount;
		bar.setValue(completionBar);

		// Check if the goal has been reached
		if (completionBar >= max) {
			// Game Finishes when goal donation is raised
			completionDone = true;
		}

	}

	/**
	 * Method returns the progress bar
	 * 
	 * @return
	 */
	public JProgressBar getBar() {
		return bar;

	}

}