package CLI_Module.HerokuUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;
import java.util.stream.Collectors;

import CLI_Module.HerokuBean;
import CLI_Module.HerokuUtils.PostgresOperations.PGOperations;

public class HerokuCLIOperations
{

	private String optionSelected;
	private HerokuBean herokuBean;

	public HerokuCLIOperations()
	{
		System.out.println("\n*****WELCOME!*****\n");
		this.optionSelected = "y";
	}

	public void runApp()
	{
		herokuBean = herokuApps();

		while (optionSelected.equalsIgnoreCase("y"))
		{
			herokuCLIOperations();
		}

	}

	private HerokuBean herokuApps()
	{
		var herokuBean = new HerokuBean();
		try
		{
			System.out.println("Here are the apps under your username:\n");
			var process = new ProcessBuilder("heroku", "apps").redirectErrorStream(true)
															  .start();
			new BufferedReader(new InputStreamReader(process.getInputStream())).lines()
																			   .forEach(System.out::println);
			System.out.println("Which app would you like to perform operations on? Type \"exit\" at anytime to quit:");
			herokuBean.setAppName(new Scanner(System.in).nextLine());

			if (herokuBean.getAppName()
						  .equalsIgnoreCase("exit"))
			{
				System.out.println("Goodbye!");
				optionSelected = "not Y";
				return null;
			}
			System.out.println("***Waiting for heroku***");
			validateAppAndCacheInfo(herokuBean);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return herokuBean;
	}

	private void herokuCLIOperations()
	{
		System.out.println("\nSelect an option from the list. The app name is " + herokuBean.getAppName() +
						   ". Type \"exit\" at any time to quit:");
		System.out.println("1 - View Postgres database information" +
						   "\n2 - View Postgres credentials" +
						   "\n3 - Download the remote database" +
						   "\n4 - Upload a local database to heroku (Warning: This will delete and replace the remote database with your database!)" +
						   "\n5 - More options");
		optionSelected = new Scanner(System.in).nextLine();
		if (optionSelected.equalsIgnoreCase("5"))
		{
			System.out.println(
					"\n6 - Change the selected app from " + herokuBean.getAppName() +
					" and list apps under your username" +
					"\n7 - Reset the remote database (Warning: This will delete and create an empty remote database!)" +
					"\n8 - View or terminate any running queries" +
					"\n9 - Go back to the previous menu");
			optionSelected = new Scanner(System.in).nextLine();
		}

		switch (optionSelected)
		{

			case "1":
				PGOperations.herokuPgInfo(herokuBean);
				break;
			case "2":
				PGOperations.herokuPgCredentials(herokuBean);
				break;
			case "3":
				PGOperations.herokuPgPull(herokuBean);
				break;
			case "4":
				PGOperations.herokuPgPush(herokuBean);
				break;
			case "6":
				herokuBean = herokuApps();
				herokuCLIOperations();
				break;
			case "7":
				PGOperations.herokuPgReset();
				break;
			case "8":
				PGOperations.herokuPgQuery();
				break;
			case "9":
				herokuCLIOperations();
				break;
			case "exit":
				System.out.println("Goodbye!");
				optionSelected = "not Y";
				return;
			default:
				System.out.println("You've entered an option that's not in the list. Let's try this again!");
				herokuCLIOperations();
		}
		doesUserWantToContinue();

	}

	private void doesUserWantToContinue()
	{
		System.out.println("\nWould you like to perform more operations? Type 'y' for yes or 'n' to exit");
		optionSelected = new Scanner(System.in).nextLine();
		switch (optionSelected.toLowerCase())
		{
			case "y":
				return;
			case "n":
				System.out.println("Goodbye!");
				optionSelected = "not Y";
				return;
			default:
				System.err.println("Please enter a valid option!");
				doesUserWantToContinue();
		}
		
	}


	private void validateAppAndCacheInfo(HerokuBean herokuBean)
	{
		try
		{
			var processInfo = new ProcessBuilder("heroku", "pg:info", "-a",
												 herokuBean.getAppName()).redirectErrorStream(
					true)
																		 .start();


			herokuBean.setPgInfo(new BufferedReader(new InputStreamReader(processInfo.getInputStream())).lines()
																										.collect(
																												Collectors.toList()));

			if (herokuBean.getPgInfo()
						  .stream()
						  .noneMatch(s -> s.contains("=== ")))
			{
				System.out.println("Sorry you've entered a wrong app name or there is no Postgres information for this app. Please try again!");
				herokuBean = herokuApps();
				assert herokuBean != null;
				validateAppAndCacheInfo(herokuBean);
			}
			herokuBean.setDbAlias(herokuBean.getPgInfo()
											.get(0)
											.replace("=== ", ""));
			var processCredentials = new ProcessBuilder("heroku", "pg:credentials:url", herokuBean.getDbAlias(), "-a",
														herokuBean.getAppName()).redirectErrorStream(true)
																				.start();
			herokuBean.setPgCredentials(
					new BufferedReader(new InputStreamReader(processCredentials.getInputStream())).lines()
																								  .collect(
																										  Collectors.toList()));

		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}


}
