package CLI_Module.HerokuUtils.PostgresOperations;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import CLI_Module.HerokuBean;
import org.postgresql.ds.PGSimpleDataSource;

public class PGOperations
{

	private static List<String> listOfLocalDbs = new ArrayList<>();
	public static void herokuPgInfo(HerokuBean herokuBean)
	{
		if (herokuBean.getPgInfo() != null)
		{
			herokuBean.getPgInfo()
					  .forEach(System.out::println);
			return;
		}
		try
		{
			var process = new ProcessBuilder("heroku", "pg:info", "-a",
											 herokuBean.getAppName()).redirectErrorStream(true)
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

	public static void herokuPgCredentials(HerokuBean herokuBean)
	{
		if (herokuBean.getPgCredentials() != null)
		{
			herokuBean.getPgCredentials()
					  .forEach(System.out::println);
			return;
		}
		try
		{
			listOfDatabases();
			var process = new ProcessBuilder("heroku", "pg:credentials:url", herokuBean.getDbAlias(), "-a",
											 herokuBean.getAppName()).redirectErrorStream(true)
																		 .start();
			new BufferedReader(new InputStreamReader(process.getInputStream())).lines()
																			   .forEach(System.out::println);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void herokuPgPull(HerokuBean herokuBean)
	{

		System.out.println("Enter the name of a local database that you want heroku to create and download data into");
		herokuBean.setLocalDbName(new Scanner(System.in).nextLine());
		try
		{
			if (listOfLocalDbs.contains(herokuBean.getLocalDbName()))
			{
				throw new HerokuPostgresException("Database entered should not exist!");
			}

			var process = new ProcessBuilder("heroku", "pg:pull", herokuBean.getDbAlias(),
											 herokuBean.getLocalDbName(), "-a",
											 herokuBean.getAppName()).redirectErrorStream(true)
																		 .start();
			new BufferedReader(new InputStreamReader(process.getInputStream())).lines()
																			   .forEach(System.out::println);
			System.out.println(
					"Successfully downloaded the remote database from " + herokuBean.getLocalDbName() +
					" to your local database!");
		}
		catch (HerokuPostgresException e)
		{
			System.out.println(e.getMessage());
			herokuPgPull(herokuBean);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			herokuPgPull(herokuBean);
		}
	}

	public static void herokuPgPush(HerokuBean herokuBean)
	{

		try
		{
			listOfDatabases();
			System.out.println("Enter the name of your local database that your want to upload to heroku: ");
			herokuBean.setLocalDbName(new Scanner(System.in).nextLine());
			if (!listOfLocalDbs.contains(herokuBean.getLocalDbName()))
			{
				throw new HerokuPostgresException("Incorrect or non existent local database name.");
			}
		}
		catch (HerokuPostgresException e)
		{
			System.out.println(e.getMessage());
			herokuPgPush(herokuBean);
		}
		catch (Exception e)
		{
			System.out.println("An error occurred. Here's the stacktrace: \n----" + e.getMessage());
			
			herokuPgPush(herokuBean);
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
					new ProcessBuilder("heroku", "pg:reset", herokuBean.getDbAlias(), "-a",
									   herokuBean.getAppName(), "--" + confirm,
									   herokuBean.getAppName()).redirectErrorStream(true)
																   .start()
																   .getInputStream())).lines()
																					  .forEach(System.out::println);
			new BufferedReader(new InputStreamReader(
					new ProcessBuilder("heroku", "pg:push", herokuBean.getLocalDbName(),
									   herokuBean.getDbAlias(), "-a",
									   herokuBean.getAppName()).redirectErrorStream(true)
																   .start()
																   .getInputStream())).lines()
																					  .forEach(System.out::println);
			System.out.println(
					"Successfully uploaded your database " + herokuBean.getLocalDbName() + " to the master!");
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

	private static void listOfDatabases()
			throws SQLException
	{

		var dataSource = new PGSimpleDataSource();
		dataSource.setUser("postgres");
		dataSource.setPassword("");
		dataSource.setPortNumber(5432);
		var resultSet = dataSource.getConnection()
								  .createStatement()
								  .executeQuery("SELECT datname FROM pg_database WHERE datistemplate = false");
		System.out.println("\nHere are the postgres databases under the local user's account:");
		while (resultSet.next())
		{
			listOfLocalDbs.add(resultSet.getString(1));
			System.out.println(resultSet.getString(1));
		}

	}
}
