package test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import sustainopoly.CompletionBar;
import junit.framework.TestCase;

public class CompletionBarTest {
	
	@BeforeEach
	protected void setUp() throws Exception {
		super.setUp();
		public static int completionBar = 0;
		public boolean completionDone = false;
		private int max = 15;
		public double updateCompletionBar;
	}
	
	CompletionBar Cbar;

	@Test
	public void testGetValue() {
		double expected = 5.0;
		double actual = Cbar.getValue();
		assertEquals(actual,expected);
		System.out.println("Test for getValue() completed");
	}

	@Test
	public void testProgress() {
		int expected = 20;
		Cbar.progress(expected);
		assertEquals(expected,Cbar.getValue());
		assertEquals(Cbar.completionDone,true);
	}

}
