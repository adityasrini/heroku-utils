package CLI_Module;

import CLI_Module.HerokuUtils.HerokuCLIOperations;
import CLI_Module.HerokuUtils.HerokuPresenceChecker;

public class ApplicationRunner
{
	
	public static void main(String[] args)
	{
		var herokuCLIOperations = new HerokuCLIOperations();
		
		HerokuPresenceChecker.checkHerokuPresenceOrInstall();

		herokuCLIOperations.runApp();
	}
	
}
