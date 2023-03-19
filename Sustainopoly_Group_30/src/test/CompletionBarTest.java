package test;
import org.junit.jupiter.api.Test;

import sustainopoly.CompletionBar;
import sustainopoly.Game;
import junit.framework.TestCase;

public class CompletionBarTest extends TestCase {
	
//	protected void setUp() throws Exception {
//		super.setUp();
//		int completionBar=5,amount=7;
//	}
	

	@Test
	public void testGetValue() {
		double expected = 5.0;
		double actual = CompletionBar.getValue();
		assertEquals(actual,expected);
		System.out.println("Test for getValue() completed");
	}

	@Test
	public void testSetValue() {
		fail("Not yet implemented");
	}

	@Test
	public void testProgress() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetBar() {
		fail("Not yet implemented");
	}

}
