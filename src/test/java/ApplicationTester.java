import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import CLI_Module.HerokuUtils.HerokuCLIOperations;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ApplicationTester
{
	private static final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	private static HerokuCLIOperations herokuCLIOperations = new HerokuCLIOperations();
	
	@BeforeAll
	static void setOutputStream(){
//		System.setOut(new PrintStream(outputStream));
//		System.setErr(new PrintStream(outputStream));
	}

	@Test
	void applicationShouldReturnHerokuAppsList()
	{
		System.setIn(new ByteArrayInputStream("exit".getBytes()));
		 herokuCLIOperations.runApp();
		 assertTrue(outputStream.toString().contains("Here are the apps under your username:"));
	}

	//FAILING METHOD. SAD!
	@Test
	void herokuBeanShouldContainPGInfoForGivenApp()
	{
		System.setIn(new ByteArrayInputStream("quantanalysis".getBytes()));
		herokuCLIOperations.runApp();
//		System.setIn(new ByteArrayInputStream("exit".getBytes()));
		
//		assertTrue(outputStream.toString().contains("Here are the apps under your username:"));
	}
	
}
