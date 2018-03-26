package main.java.HerokuUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class HerokuCLIOperations
{

	public static String appName;
	private static String dbAlias;
	private static List<String> pgInfo;
	private static List<String> pgCredentials;
	public static void herokuApps()
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

	private static void validateAppAndCacheInfo()
	{
		try
		{
			var processInfo = new ProcessBuilder("heroku", "pg:info", "-a", appName).redirectErrorStream(true)
																					.start();


			pgInfo = new BufferedReader(new InputStreamReader(processInfo.getInputStream())).lines()
																							.collect(
																									Collectors.toList());

			if (pgInfo.stream()
					  .anyMatch(s -> s.contains("Expected response to be successful, got 4")))
			{
				System.out.println("Sorry you've entered a wrong app name. Please try again!");
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

	public static void herokuPgInfo()
	{
		if (pgInfo != null)
		{
			pgInfo.forEach(System.out::println);
			return;
		}
		try
		{
			var process = new ProcessBuilder("heroku", "pg:info", "-a", appName).redirectErrorStream(true)
																				.start();
			new BufferedReader(new InputStreamReader(process.getInputStream())).lines()
																			   .forEach(
																					   System.out::println);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public static void herokuPgCredentials()
	{
		if (pgCredentials != null)
		{
			pgCredentials.forEach(System.out::println);
			return;
		}
		try
		{

			var process = new ProcessBuilder("heroku", "pg:credentials:url", dbAlias, "-a",
											 appName).redirectErrorStream(true)
													 .start();
			new BufferedReader(new InputStreamReader(process.getInputStream())).lines()
																			   .forEach(System.out::println);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public static void herokuPgPull()
	{

		System.out.println("Enter the name of the app you want Postgres credentials information from: ");
		var appName = new Scanner(System.in).nextLine();
		try
		{
			var process = new ProcessBuilder("heroku", "pg:pull", "-a", appName).redirectErrorStream(true)
																				.start();
			new BufferedReader(new InputStreamReader(process.getInputStream())).lines()
																			   .forEach(System.out::println);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public static void herokuPgPush()
	{

		System.out.println("Enter the name of the app you want Postgres credentials information from: ");
		var appName = new Scanner(System.in).nextLine();
		try
		{
			var process = new ProcessBuilder("heroku", "pg:credentials:url", dbAlias, "-a",
											 appName).redirectErrorStream(true)
													 .start();
			new BufferedReader(new InputStreamReader(process.getInputStream())).lines()
																			   .forEach(System.out::println);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
