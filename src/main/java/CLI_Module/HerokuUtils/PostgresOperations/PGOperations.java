package CLI_Module.HerokuUtils.PostgresOperations;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

import CLI_Module.HerokuBean;
import org.postgresql.ds.PGSimpleDataSource;

public class PGOperations
{

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

			var process = new ProcessBuilder("heroku", "pg:credentials:url", herokuBean.getDbAlias(), "-a",
											 herokuBean.getAppName()).redirectErrorStream(true)
																		 .start();
			new BufferedReader(new InputStreamReader(process.getInputStream())).lines()
																			   .forEach(System.out::println);
		}
		catch (IOException e)
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
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public static void herokuPgPush(HerokuBean herokuBean)
	{

		System.out.println("Enter the name of your local database that your want to upload to the remote");
		herokuBean.setLocalDbName(new Scanner(System.in).nextLine());
		try
		{
			var dataSource = new PGSimpleDataSource();
			dataSource.setUser("postgres");
			dataSource.setPassword("");
			dataSource.setPortNumber(5432);
			dataSource.setDatabaseName(herokuBean.getLocalDbName());
			dataSource.getConnection();
		}
		catch (Exception e)
		{
			System.out.println("An error occurred. Perhaps you entered an incorrect local database name.");

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
}
