package main.java.HerokuUtils.PostgresOperations;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.DriverManager;
import java.util.Scanner;

import main.java.HerokuUtils.HerokuCLIOperations;

public class PGOperations
{

	public static void herokuPgInfo(HerokuCLIOperations herokuCLIOperations)
	{
		if (herokuCLIOperations.pgInfo != null)
		{
			herokuCLIOperations.pgInfo.forEach(System.out::println);
			return;
		}
		try
		{
			var process = new ProcessBuilder("heroku", "pg:info", "-a",
											 herokuCLIOperations.appName).redirectErrorStream(true)
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

	public static void herokuPgCredentials(HerokuCLIOperations herokuCLIOperations)
	{
		if (herokuCLIOperations.pgCredentials != null)
		{
			herokuCLIOperations.pgCredentials.forEach(System.out::println);
			return;
		}
		try
		{

			var process = new ProcessBuilder("heroku", "pg:credentials:url", herokuCLIOperations.dbAlias, "-a",
											 herokuCLIOperations.appName).redirectErrorStream(true)
																		 .start();
			new BufferedReader(new InputStreamReader(process.getInputStream())).lines()
																			   .forEach(System.out::println);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public static void herokuPgPull(HerokuCLIOperations herokuCLIOperations)
	{

		System.out.println("Enter the name of an existing local database that you want heroku to download into");
		herokuCLIOperations.localDbName = new Scanner(System.in).nextLine();
		try
		{
			Class.forName("org.postgresql.Driver");
			DriverManager.getConnection("jdbc:postgresql://localhost:5432/" + herokuCLIOperations.localDbName);
		}
		catch (Exception e)
		{
			System.out.println("An error occurred. Perhaps the database hasn't been created yet.");

			herokuPgPull(herokuCLIOperations);
		}

		try
		{
			var process = new ProcessBuilder("heroku", "pg:pull", herokuCLIOperations.dbAlias,
											 herokuCLIOperations.localDbName, "-a",
											 herokuCLIOperations.appName).redirectErrorStream(true)
																		 .start();
			new BufferedReader(new InputStreamReader(process.getInputStream())).lines()
																			   .forEach(System.out::println);
			System.out.println(
					"Successfully downloaded the remote database from " + herokuCLIOperations.localDbName +
					" to your local database!");
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public static void herokuPgPush(HerokuCLIOperations herokuCLIOperations)
	{

		System.out.println("Enter the name of your local database that your want to upload to the remote");
		herokuCLIOperations.localDbName = new Scanner(System.in).nextLine();
		try
		{
			Class.forName("org.postgresql.Driver");
			DriverManager.getConnection("jdbc:postgresql://localhost:5432/" + herokuCLIOperations.localDbName);
		}
		catch (Exception e)
		{
			System.out.println("An error occurred. Perhaps you entered an incorrect local database name.");

			herokuPgPush(herokuCLIOperations);
		}
		System.out.println(
				"Warning this will wipe out data from the remote database and replace it with yours! Type \"confirm\" to proceed with this!");
		var confirm = new Scanner(System.in).nextLine();
		if (!confirm.equals("confirm"))
		{
			System.out.println("You did not enter \"confirm\"! Exiting operation.");
			return;
		}
		try
		{
			new BufferedReader(new InputStreamReader(
					new ProcessBuilder("heroku", "pg:reset", herokuCLIOperations.dbAlias, "-a",
									   herokuCLIOperations.appName, "--" + confirm,
									   herokuCLIOperations.appName).redirectErrorStream(true)
																   .start()
																   .getInputStream())).lines()
																					  .forEach(System.out::println);
			new BufferedReader(new InputStreamReader(
					new ProcessBuilder("heroku", "pg:push", herokuCLIOperations.localDbName,
									   herokuCLIOperations.dbAlias, "-a",
									   herokuCLIOperations.appName).redirectErrorStream(true)
																   .start()
																   .getInputStream())).lines()
																					  .forEach(System.out::println);
			System.out.println(
					"Successfully uploaded your database " + herokuCLIOperations.localDbName + " to the master!");
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public static void herokuPgReset()
	{

	}

	public static void herokuPgQuery()
	{

	}
}
