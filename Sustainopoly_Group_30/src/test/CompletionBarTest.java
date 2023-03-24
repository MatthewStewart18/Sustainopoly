package test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import sustainopoly.CompletionBar;
import junit.framework.TestCase;

public class CompletionBarTest {
	CompletionBar cbar = new CompletionBar(15);
	
	@BeforeEach
	protected void setUp() throws Exception {
		CompletionBar.completionBar = 0;
		cbar.completionDone = false;
	}

	@Test
	public void testGetValue() {
		double expected = 0;
		double actual = CompletionBar.getValue();
		assertEquals(actual,expected);
		System.out.println("Test for getValue() completed");
	}
	
	@Test
	public void testIsCompleted() {
		boolean expected = false;
		boolean actual = cbar.isCompleted();
		assertEquals(actual,expected);
	}

	@Test
	public void testProgress() {
		int expected = 20;
		cbar.progress(expected);
		assertEquals(expected,CompletionBar.getValue());
		assertEquals(cbar.completionDone,true);
	}

}
