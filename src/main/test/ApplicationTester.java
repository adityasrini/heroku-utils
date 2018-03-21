package main.test;

import org.junit.jupiter.api.Test;
import static main.java.HerokuUtils.HerokuCLIOperations.herokuApps;

class ApplicationTester
{

	@Test
	void applicationShouldReturnHerokuAppsList()
	{

		herokuApps();
		
	}
}
