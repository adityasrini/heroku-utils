package main.java.HerokuUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import static main.java.HerokuUtils.PostgresOperations.PGOperations.*;

public class HerokuCLIOperations
{

	private String optionSelected;
	public String appName;
	public String dbAlias;
	public String localDbName;
	public List<String> pgInfo;
	public List<String> pgCredentials;

	public HerokuCLIOperations()
	{
		System.out.println("\n*****WELCOME!*****\n");
		this.optionSelected = "y";
	}

	public void runApp()
	{
		herokuApps();

		while (optionSelected.equalsIgnoreCase("y"))
		{
			herokuCLIOperations();
		}

	}

	private void herokuApps()
	{
		try
		{
			System.out.println("Here are the apps under your username:\n");
			var process = new ProcessBuilder("heroku", "apps").redirectErrorStream(true)
															  .start();
			new BufferedReader(new InputStreamReader(process.getInputStream())).lines()
																			   .forEach(System.out::println);
			System.out.println("Which app would you like to perform operations on? Type \"exit\" at anytime to quit:");
			appName = new Scanner(System.in).nextLine();
			System.out.println("***Waiting for heroku***");
			validateAppAndCacheInfo();
			if (appName.equalsIgnoreCase("exit"))
			{
				System.out.println("Goodbye!");
				System.exit(0);
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	private void herokuCLIOperations()
	{
		System.out.println("\nSelect a heroku operation to perform on " + this.appName +
						   ". Type \"exit\" at any time to quit:");
		System.out.println("1 - View Postgres database information" +
						   "\n2 - View Postgres credentials" +
						   "\n3 - Download the remote database from " + this.appName +
						   "\n4 - Upload a local database to heroku (Warning: This will delete and replace the remote database with your database!)" +
						   "\n5 - More options");
		optionSelected = new Scanner(System.in).nextLine();
		if (optionSelected.equalsIgnoreCase("5"))
		{
			System.out.println(
					"\n6 - Change the selected app from " + this.appName +
					" and list apps under your username" +
					"\n7 - Reset the remote database (Warning: This will delete and create an empty remote database!)" +
					"\n8 - View or terminate any running queries" +
					"\n9 - Go back to the previous menu");
			optionSelected = new Scanner(System.in).nextLine();
		}


		switch (optionSelected)
		{

			case "1":
				herokuPgInfo(this);
				break;
			case "2":
				herokuPgCredentials(this);
				break;
			case "3":
				herokuPgPull(this);
				break;
			case "4":
				herokuPgPush(this);
				break;
			case "6":
				herokuApps();
				herokuCLIOperations();
				break;
			case "7":
				herokuPgReset();
				break;
			case "8":
				herokuPgQuery();
				break;
			case "9":
				herokuCLIOperations();
				break;
			case "exit":
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


	private void validateAppAndCacheInfo()
	{
		try
		{
			var processInfo = new ProcessBuilder("heroku", "pg:info", "-a", appName).redirectErrorStream(true)
																					.start();


			pgInfo = new BufferedReader(new InputStreamReader(processInfo.getInputStream())).lines()
																							.collect(
																									Collectors.toList());

			if (pgInfo.stream()
					  .noneMatch(s -> s.contains("=== ")))
			{
				System.out.println("Sorry you've entered a wrong app name or there is no Postgres information for this app. Please try again!");
				herokuApps();
				validateAppAndCacheInfo();
			}
			dbAlias = pgInfo.get(0)
							.replace("=== ", "");
			var processCreds = new ProcessBuilder("heroku", "pg:credentials:url", dbAlias, "-a",
												  appName).redirectErrorStream(true)
														  .start();
			pgCredentials = new BufferedReader(new InputStreamReader(processCreds.getInputStream())).lines()
																									.collect(
																											Collectors.toList());

		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}


}
