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

	public static boolean checkToSeeIfHerokuIsInstalled()
	{
		try
		{
			new ProcessBuilder("heroku", "version").start();
			return true;
		}
		catch (IOException e)
		{

			e.printStackTrace();

			return false;
		}

	}

	public static void askIfUserWantsToInstallHeroku()
	{
		System.out.println("It appears that heroku is not installed on this machine.");
		if (System.getProperty("os.name")
				  .contains("win"))
		{
			System.out.println(
					"\n1. Please go to https://devcenter.heroku.com/articles/heroku-cli#download-and-install " +
					"to download and install the heroku CLI client on your machine." +
					"\n2. Enter \"heroku login\" once the installation is complete to enter your credentials." +
					"\n3. Run this app again once 1&2 are done!");
			System.exit(0);
		}
		System.out.println("Would you like to install heroku? Enter y for yes or n for no: ");
		var inputValidationFlag = true;
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
		try
		{
			System.out.println("\nDownloading heroku...");
			Process herokuCliDownloadProcess;

			//This step checks the OS and architecture and gets the download from the appropriate link
			if (osIAmDealingWith.contains("Mac"))
			{
				herokuCliDownloadProcess = new ProcessBuilder("curl",
															  "https://cli-assets.heroku.com/heroku-cli/channels/stable/heroku-cli-darwin-x64.tar.gz",
															  "-o", "heroku.tar.gz").redirectErrorStream(true)
																					.start();

				new BufferedReader(new InputStreamReader(herokuCliDownloadProcess.getInputStream())).lines()
																									.forEach(
																											System.out::println);
			}
			else if (osIAmDealingWith.contains("nux"))
			{
				//64-bit or 32-bit linux architecture check
				if (System.getProperty("os.arch")
						  .equalsIgnoreCase("x86_64"))
				{
					herokuCliDownloadProcess = new ProcessBuilder("curl",
																  "https://cli-assets.heroku.com/heroku-cli/channels/stable/heroku-cli-linux-x64.tar.gz",
																  "-o", "heroku.tar.gz").redirectErrorStream(true)
																						.start();
				}
				else
				{
					herokuCliDownloadProcess = new ProcessBuilder("curl",
																  "https://cli-assets.heroku.com/heroku-cli/channels/stable/heroku-cli-linux-x86.tar.gz",
																  "-o", "heroku.tar.gz").redirectErrorStream(true)
																						.start();
				}

				new BufferedReader(new InputStreamReader(herokuCliDownloadProcess.getInputStream())).lines()
																									.forEach(
																											System.out::println);
			}

			System.out.println("\n\nUnzipping the download...");
			var tarUnzipProcess = new ProcessBuilder("tar", "-xvzf", "heroku.tar.gz").redirectErrorStream(true)
																					 .start();

			//Unzipping and getting the unzipped directory's name
			var unzippedDir = new BufferedReader(new InputStreamReader(tarUnzipProcess.getInputStream())).lines()
																										 .collect(
																												 Collectors.toList())
																										 .get(0)
																										 .replace(
																												 "/",
																												 "")
																										 .replace(
																												 "x ",
																												 "");

			System.out.println("Unzipping download and moving unzipped directory to installation location...");


			//This checks for the existence of the folder and sub-folders and creates them atomically
			Files.createDirectories(Paths.get("/usr/local/bin/"));
			Files.createDirectories(Paths.get("/usr/local/lib/"));
			Files.move(Paths.get(unzippedDir), Paths.get("/usr/local/lib/heroku/"));
			Files.delete(Paths.get("heroku.tar.gz"));

			System.out.println("Done moving");

			Files.createSymbolicLink(Paths.get("/usr/local/bin/heroku"), Paths.get("/usr/local/lib/heroku/bin/heroku"));

			System.out.println("Created the symbolic link!");
			System.out.println("Heroku is now installed on your system!");
			System.out.println(
					"\nPlease type \"heroku login\" in a separate terminal window to enter your credentials!");
			System.out.println("\n***APPLICATION WILL NOW EXIT***");
		}
		catch (Exception e)
		{
			System.err.println("Unable to complete installation");
			e.printStackTrace();
		}

	}
}
