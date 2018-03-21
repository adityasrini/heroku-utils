package main.java.HerokuUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.stream.Collectors;

public class InstallUtils
{

	//BAD HEROKU COMMAND SUPPLIED FOR TESTING
	public static boolean checkToSeeIfHerokuIsInstalled()
	{
		try
		{
			new ProcessBuilder("heroku", "version").start();
			return true;
		}
		catch (IOException e)
		{
			
				//TODO: log this to a file
			
			return false;
		}

	}

	public static void askIfUserWantsToInstallHeroku()
	{
		System.out.println("It appears that heroku is not installed on this machine.");
		System.out.println("Would you like to install heroku? Enter y for yes or n for no: ");
		boolean inputValidationFlag = true;
		while (inputValidationFlag)
		{
			var exitCode = new Scanner(System.in).nextLine()
												 .toLowerCase();

			switch (exitCode)
			{
				case "y":
					System.out.println("Installing Heroku...");
					inputValidationFlag = false;
					installHeroku();
					break;
				case "n":
					inputValidationFlag = false;
					System.out.println("Goodbye!");
					System.exit(0);
					break;
				default:
					System.out.println("Please enter y or n");
			}
		}

	}

	private static void installHeroku()
	{
		var osIAmDealingWith = System.getProperty("os.name");
		if (osIAmDealingWith.contains("Mac"))
		{
			try
			{
				System.out.println("\nDownloading heroku...");
				var herokuCliDownloadProcess = new ProcessBuilder("curl",
																  "https://cli-assets.heroku.com/heroku-cli/channels/stable/heroku-cli-darwin-x64.tar.gz",
																  "-o", "heroku.tar.gz").redirectErrorStream(true)
																						.start();

				new BufferedReader(new InputStreamReader(herokuCliDownloadProcess.getInputStream())).lines()
																									.forEach(
																											System.out::println);
				System.out.println("\n\nUnzipping the download...");
				var tarUnzipProcess = new ProcessBuilder("tar", "-xvzf", "heroku.tar.gz").redirectErrorStream(true)
																						 .start();
				var stringList = new BufferedReader(new InputStreamReader(tarUnzipProcess.getInputStream())).lines()
																											.collect(
																													Collectors.toList());
				var unzippedDir = stringList.get(0)
											.replace("/", "")
											.replace("x ", "");
				System.out.println(
						"Creating the required folders and moving unzipped directory to installation location...");


				//This checks for the existence of the folder and sub-folders and creates them atomically
				Files.createDirectories(Paths.get("/usr/local/bin/"));
				Files.createDirectories(Paths.get("/usr/local/lib/"));
				Files.move(Paths.get(unzippedDir), Paths.get("/usr/local/lib/heroku/"));
				Files.delete(Paths.get("heroku.tar.gz"));
				
				System.out.println("Done moving");

				Files.createSymbolicLink(Paths.get("/usr/local/bin/heroku"),Paths.get("/usr/local/lib/heroku/bin/heroku"));

				System.out.println("Created the symbolic link!");
			}
			catch (Exception e)
			{
				System.err.println("Unable to complete installation");
				e.printStackTrace();
			}
		}

	}
}
