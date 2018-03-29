package main.java;

import main.java.HerokuUtils.HerokuCLIOperations;
import main.java.HerokuUtils.HerokuPresenceChecker;

public class ApplicationRunner
{
	
	public static void main(String[] args)
	{
		var herokuCLIOperations = new HerokuCLIOperations();
		
		HerokuPresenceChecker.checkHerokuPresenceOrInstall();

		herokuCLIOperations.runApp();
	}
	
}
