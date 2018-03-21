package main.java.HerokuUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class HerokuCLIOperations
{
	public static void herokuApps()
	{
		try
		{
			var process = new ProcessBuilder("heroku", "apps").redirectErrorStream(true)
															  .start();
			new BufferedReader(new InputStreamReader(process.getInputStream())).lines()
																			   .forEach(System.out::println);
			System.out.println("Which app would you like postgres information on?!");
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public static void herokuPgInfo(String appName)
	{
		try
		{
			var process = new ProcessBuilder("heroku", "pg:info", "-a", appName).redirectErrorStream(true)
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
