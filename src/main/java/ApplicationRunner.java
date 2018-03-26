package main.java;

import java.util.NoSuchElementException;
import java.util.Scanner;

import main.java.HerokuUtils.HerokuCLIOperations;
import static main.java.HerokuUtils.HerokuCLIOperations.*;
import static main.java.HerokuUtils.InstallUtils.askIfUserWantsToInstallHeroku;
import static main.java.HerokuUtils.InstallUtils.checkToSeeIfHerokuIsInstalled;

public class ApplicationRunner
{
 
	String optionSelected = "y";
	
	public static void main(String[] args)
	{
		var applicationRunner = new ApplicationRunner();
		System.out.println("\n*****WELCOME!*****\n");
		if (!checkToSeeIfHerokuIsInstalled())
		{
			askIfUserWantsToInstallHeroku();
		}

		herokuApps();

		//While loop that looks for user input and runs a heroku operation. Terminates when user wants to exit.
		while (!applicationRunner.optionSelected.equalsIgnoreCase("n"))
		{
			applicationRunner.herokuCLIOperations();
		}
	}

	private void herokuCLIOperations()
	{
		System.out.println("\nSelect a heroku operation to perform on " + HerokuCLIOperations.appName +
						   ". Type \"exit\" at any time to quit:");
		System.out.println("1. To see Postgres database information, type \"info\"" +
						   "\n2. To see Postgres credentials, type \"credentials\"" +
						   "\n3. To get a local copy of " + HerokuCLIOperations.appName + "\'s database, type \"pull\"" +
						   "\n4. To get upload a copy of your local database to heroku, type \"push\"" +
						   "\n5. For more advanced options, type \"more\"");
		optionSelected = new Scanner(System.in).nextLine();
		if (optionSelected.equalsIgnoreCase("more"))
		{
			System.out.println(
					"\n7. To change the selected app from " + HerokuCLIOperations.appName + " and list apps under your username, type \"apps\"" +
					"\n8. To drop and create the database in one operation, type \"reset\"" +
					"\n9. To view or terminate any running queries, type \"query\"");
			optionSelected = new Scanner(System.in).nextLine();
		}
		HerokuOptions herokuOptions;
		try
		{
			herokuOptions = HerokuOptions.matcher(this);
		}
		catch (NoSuchElementException e)
		{
			System.err.println("Please enter a valid option!");
			return;
		}
		switch (herokuOptions)
		{
			case HEROKUAPPS:
				herokuApps();
				herokuCLIOperations();
				break;
			case PGINFO:
				herokuPgInfo();
				break;
			case PGCREDENTIALS:
				herokuPgCredentials();
				break;
			case PGPULL:
				herokuPgPull();
				break;
			case PGPUSH:
				herokuPgPush();
				break;
			case EXITAPP:
				System.out.println("Goodbye!");
				System.exit(0);
				break;
		}
		doesUserWantToContinue();

	}

	private void doesUserWantToContinue()
	{
		while (true)
		{
			System.out.println("\nWould you like to perform more operations? Type 'y' for yes or 'n' to exit");
			optionSelected = new Scanner(System.in).nextLine();
			switch (optionSelected.toLowerCase())
			{
				case "y":
					return;
				case "n":
					System.out.println("Goodbye!");
					System.exit(0);
					return;
				default:
					System.err.println("Please enter a valid option!");
			}
		}
	}
	
}
